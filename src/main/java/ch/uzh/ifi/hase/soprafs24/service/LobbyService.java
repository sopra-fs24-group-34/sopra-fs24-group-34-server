package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.Lobby;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.LobbyRepository;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs24.rest.dto.AuthenticationResponseDTO;
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

@Service
@Transactional
public class LobbyService {

  private final Logger log = LoggerFactory.getLogger(LobbyService.class);

  private final LobbyRepository lobbyRepository; // smailalijagic: needed to verify lobbies

  private final UserRepository userRepository; // smailalijagic: needed to verify user

  @Autowired
  public LobbyService(@Qualifier("lobbyRepository") LobbyRepository lobbyRepository, @Qualifier("userRepository") UserRepository userRepository) {
    this.lobbyRepository = lobbyRepository;
    this.userRepository = userRepository;
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
    // smailalijagic: changed to boolean
    Lobby lobbyById = lobbyRepository.findByLobbyid(lobbyid);

    return lobbyById != null;// smailalijagic: lobby = null --> does not exist yet
  }

  public Boolean isLobbyOwner(User user, Long lobbyid) {
    List<Lobby> lobbyList = user.getUsergamelobbylist(); // smailalijagic: get all created lobbies
    Lobby lobbyById = lobbyRepository.findByLobbyid(lobbyid); // smailalijagic: get searched lobby

    for (Lobby lobby: lobbyList) { // smailalijagic: make sure searched lobby was created by user else return false
      if (lobby.equals(lobbyById)) {
        return true;
      }
    }

    return false;
  }

  public void deleteLobby(Lobby lobby) {
    try {
      lobbyRepository.delete(lobby);
    } catch (Exception e) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find the lobby");
    }
  }

  public Long createlobby(Long userId){
    Lobby newlobby = new Lobby();
    newlobby.setToken(UUID.randomUUID().toString());
    newlobby.setCreator_userid(userId);

    newlobby = lobbyRepository.save(newlobby);
    lobbyRepository.flush();

    User user = userRepository.findUserById(userId);

    List<Lobby> lobbyList = user.getUsergamelobbylist();
    lobbyList.add(newlobby);
    user.setUsergamelobbylist(lobbyList);

    log.debug("Created Information for Lobby: {}", newlobby);
    return newlobby.getLobbyid();
  }

  public Boolean updateLobby(Lobby lobby) {
    // smailalijagic: change settings
    return true;
  }

  public void addUserToLobby(Lobby lobby, User user) {
    Long userId = user.getId();
    lobby.setInvited_userid(userId);
    lobby = lobbyRepository.save(lobby);
    lobbyRepository.flush();
  }

}
