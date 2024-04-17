package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.Lobby;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.LobbyRepository;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
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



  private User createUser() {
    User user = new User();
    user.setUsername("Guest");
    user.setPassword("12345"); // smailalijagic: default password
    user.setStatus(UserStatus.ONLINE);
    user.setToken(UUID.randomUUID().toString());
    // saves the given entity but data is only persisted in the database once
    // flush() is called
    user = userRepository.save(user);
    userRepository.flush();
    // getUserId() --> 1
    log.debug("Created Information for User: {}", user);
    return user;
  }

  public User createGuestUser() {
    User temp_user = createUser();
    Long temp_user_id = temp_user.getId();
    String username = temp_user.getUsername() + temp_user_id; // smailalijagic: default name: Guestnull --> Guest1, Guest2
    User guestUser = userRepository.findUserById(temp_user_id);
    guestUser.setUsername(username);
    temp_user = userRepository.save(guestUser);
    userRepository.flush();
    return temp_user;
    // Guest1
    // Id: 1
    // password: 12345
  }

  public List<Lobby> getLobbies() {
    return this.lobbyRepository.findAll();
  }

  public Lobby getLobby(Long lobbyId) {
    return this.lobbyRepository.findByLobbyid(lobbyId);
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

  public Long createlobby(Long userId){
      try {
          Lobby newlobby = new Lobby();
          User creator = userRepository.findUserById(userId);
          newlobby.setToken(UUID.randomUUID().toString());
          newlobby.setUser(userId);

          newlobby = lobbyRepository.save(newlobby);
          lobbyRepository.flush();

          List<Lobby> lobbyList = creator.getUsergamelobbylist();
          lobbyList.add(newlobby);
          creator.setUsergamelobbylist(lobbyList);

          log.debug("Created Information for Lobby: {}", newlobby);
          return newlobby.getLobbyid();
      } catch (Exception e) {
          throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lobby could not be created");
      }
  }

  public void deleteLobby(Lobby lobby) {
      try {
          lobbyRepository.delete(lobby);
      } catch (Exception e) {
          throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find the lobby");
      }
  }

  public Boolean checkIfUserExists(User userToBeCreated) {
      // smailalijagic: changed to boolean
      User userByUsername = userRepository.findByUsername(userToBeCreated.getUsername());

      return userByUsername != null;// smailalijagic: user = null --> does not exist yet
  }

  public Boolean checkIfLobbyExists(Long lobbyid) {
      // smailalijagic: changed to boolean
      Lobby lobbyById = lobbyRepository.findByLobbyid(lobbyid);

      return lobbyById != null;// smailalijagic: lobby = null --> does not exist yet
  }
}
