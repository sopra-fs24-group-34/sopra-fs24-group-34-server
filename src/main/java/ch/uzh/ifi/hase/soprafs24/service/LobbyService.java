package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.entity.Lobby;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.LobbyRepository;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Transactional
public class LobbyService {

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

  public Boolean checkIfUserExists(User userToBeCreated) {
    // smailalijagic: changed to boolean
    User userByUsername = userRepository.findByUsername(userToBeCreated.getUsername());

    return userByUsername != null;// smailalijagic: user = null --> does not exist yet
  }

  public List<Lobby> getLobbies() {
    return this.lobbyRepository.findAll();
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

}