package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.Lobby;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.LobbyRepository;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs24.rest.dto.AuthenticationDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs24.websocket.WebSocketMessenger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service("lobbyService")
@Transactional
public class LobbyService {
  private final Logger log = LoggerFactory.getLogger(LobbyService.class);
  private final LobbyRepository lobbyRepository; // smailalijagic: needed to verify lobbies
  private final UserRepository userRepository; // smailalijagic: needed to verify user
  private final AuthenticationService authenticationService;
  private final WebSocketMessenger webSocketMessenger;

  @Autowired
  public LobbyService(@Qualifier("lobbyRepository") LobbyRepository lobbyRepository,
                      @Qualifier("userRepository") UserRepository userRepository,
                      AuthenticationService authenticationService, WebSocketMessenger webSocketMessenger) {
    this.lobbyRepository = lobbyRepository;
    this.userRepository = userRepository;
    this.authenticationService = authenticationService;
    this.webSocketMessenger = webSocketMessenger;
  }

  public List<User> getUsers() {
    return this.userRepository.findAll();
  }

  public User getUser(Long id) {
    try {
      return this.userRepository.findUserById(id);
    } catch (Exception e) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found, could not add user to lobby");
    }
  }

  public Boolean checkIfUserExists(User userToBeCreated) {
    // smailalijagic: changed to boolean
    User userByUsername = userRepository.findByUsername(userToBeCreated.getUsername());

    return userByUsername != null;// smailalijagic: user = null --> does not exist yet
  }

  public List<Lobby> getLobbies() {
    return this.lobbyRepository.findAll();
  }

  public Lobby getLobby(Long lobbyId) {
    try {
      return this.lobbyRepository.findByLobbyid(lobbyId);
    } catch (Exception e) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lobby with lobby id " + lobbyId + " could not be found!");
    }
  }

  public Boolean checkIfLobbyExists(Long lobbyid) {
      return lobbyRepository.existsByLobbyid(lobbyid);
  }

  public Boolean isLobbyOwner(Long lobbyid, AuthenticationDTO authenticationDTO) {
    Lobby lobby = lobbyRepository.findByLobbyid(lobbyid);
    User host = getUser(lobby.getCreatorUserId());
    return authenticationService.isAuthenticated(host, authenticationDTO);
  }

  public void closeLobby(Long lobbyId, AuthenticationDTO authenticationDTO) {
        if(isLobbyOwner(lobbyId, authenticationDTO)) {
            deleteLobby(lobbyRepository.findByLobbyid(lobbyId));
        }
  }

  public void deleteLobby(Lobby lobby) {
    try {
      lobbyRepository.delete(lobby);
    } catch (Exception e) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find the lobby");
    }
  }

  public Long createLobby(Long userId){
    Lobby newlobby = new Lobby();
    newlobby.setToken(UUID.randomUUID().toString());
    newlobby.setCreatorUserId(userId);

    newlobby = lobbyRepository.save(newlobby);
    lobbyRepository.flush();

    User user = userRepository.findUserById(userId);

    user.setStatus(UserStatus.INLOBBY_PREPARING);
    userRepository.save(user);
    userRepository.flush();

    log.debug("Created Information for Lobby: {}", newlobby);
    return newlobby.getLobbyid();
  }

  public UserGetDTO updateReadyStatus(Long userId, String readyStatus) {
      //d
      try {
          User user = userRepository.findUserById(userId);
          user.setStatus(UserStatus.valueOf(readyStatus));
          userRepository.save(user);
          userRepository.flush();
          return DTOMapper.INSTANCE.convertEntityToUserGetDTO(user);
      } catch(Exception e) {
          throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Illegal UserStatus");
      }
  }

  public Boolean updateLobby(Lobby lobby) {
    // smailalijagic: change settings
    return true;
  }

  public void addUserToLobby(Lobby lobby, User user) {
    Long userId = user.getId();
    lobby.setInvitedUserId(userId);
    lobby = lobbyRepository.save(lobby);
    lobbyRepository.flush();
    user.setStatus(UserStatus.INLOBBY_PREPARING);
    userRepository.save(user);
    userRepository.flush();
  }

  public void removeUserFromLobby(Long lobbyId, Long userId) {
    //Long userId = user.getId();
    try {
        Lobby lobby = lobbyRepository.findByLobbyid(lobbyId);
        UserGetDTO userGetDTO = DTOMapper.INSTANCE.convertEntityToUserGetDTO(userRepository.findUserById(userId));

        if(Objects.equals(lobby.getCreatorUserId(), userId)) {
            User host = userRepository.findUserById(userId);
            host.setStatus(UserStatus.ONLINE);
            userRepository.save(host);
            try {
                User guest = userRepository.findUserById(lobby.getInvitedUserId());
                guest.setStatus(UserStatus.ONLINE);
                userRepository.save(guest);
            } catch (Exception e) {
                log.debug("No guest in lobby");
            }
            userRepository.flush();
            lobby.setCreatorUserId(null);
            lobby.setInvitedUserId(null);
            webSocketMessenger.sendMessage("/lobbies/"+lobbyId, "lobby-closed", "");
            deleteLobby(lobby);
        } else if(Objects.equals(lobby.getInvitedUserId(), userId)) {
            lobby.setInvitedUserId(null);
            userRepository.findUserById(userId).setStatus(UserStatus.ONLINE);
            webSocketMessenger.sendMessage("/lobbies/"+lobbyId, "user-left", userGetDTO);
        }

        lobbyRepository.save(lobby);
        lobbyRepository.flush();
    } catch(Exception ignored) {
    }
}

public Long getGameIdFromLobbyId(Long lobbyId) {
      return lobbyRepository.findByLobbyid(lobbyId).getGame().getGameId();
}

public void translateAddUserToLobby(Long lobbyId, Long userId) {
      Lobby lobby = lobbyRepository.findByLobbyid(lobbyId);
      User user = userRepository.findUserById(userId);
      if(!Objects.equals(userId, lobby.getCreatorUserId()) &&
              !Objects.equals(userId, lobby.getInvitedUserId())) {
          addUserToLobby(lobby, user);
          webSocketMessenger.sendMessage("/lobbies/"+lobbyId, "user-joined", user);
      }
}




}
