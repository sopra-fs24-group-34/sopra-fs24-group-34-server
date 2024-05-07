package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.User;
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
//

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * User Service
 * This class is the "worker" and responsible for all functionality related to
 * the user
 * (e.g., it creates, modifies, deletes, finds). The result will be passed back
 * to the caller.
 */
@Service
@Transactional
public class UserService {
  private final Logger log = LoggerFactory.getLogger(UserService.class);

  private final UserRepository userRepository;

  @Autowired
  public UserService(@Qualifier("userRepository") UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public List<User> getUsers() {
    return this.userRepository.findAll();
  }

  public User getUser(Long userId) {
    try {
      if (this.userRepository.findUserById(userId) == null) {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with userId " + userId + " does not exist");
      }
        return this.userRepository.findUserById(userId);
    } catch (Exception e) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with userId " + userId + " does not exist");
    }
  }

  public User getUserByUsername(String username) {
    try {
      if (this.userRepository.findByUsername(username) == null) {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist");
      }
      return this.userRepository.findByUsername(username);
    } catch (Exception e) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist");
    }
  }

  public AuthenticationResponseDTO createUser(User newUser) {
    checkIfUserExists(newUser);
    if (newUser.getUsername() == null || newUser.getPassword() == null) {
      throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Password or username was not set");
    }
    newUser.setStatus(UserStatus.ONLINE);
    newUser.setToken(UUID.randomUUID().toString());

    // saves the given entity but data is only persisted in the database once
    // flush() is called
    newUser = userRepository.save(newUser);
    userRepository.flush();

    log.debug("Created Information for User: {}", newUser);
    return new AuthenticationResponseDTO(newUser.getId(), newUser.getToken());
  }

  public AuthenticationResponseDTO createGuestUser(User newGuestUser) {
    checkIfUserExists(newGuestUser);
    newGuestUser.setStatus(UserStatus.INLOBBY); // smailalijagic: created user waits per default in lobby
    newGuestUser.setToken(UUID.randomUUID().toString());

    // saves the given entity but data is only persisted in the database once
    // flush() is called
    newGuestUser = userRepository.save(newGuestUser);
    userRepository.flush(); // smailalijagic: this sets the ID

    newGuestUser.setUsername(newGuestUser.getUsername() + newGuestUser.getId()); // smailalijagic: username = Guest1

    newGuestUser = userRepository.save(newGuestUser); // smailalijagic: update guestuser
    userRepository.flush();

    log.debug("Created Information for User: {}", newGuestUser);
    return new AuthenticationResponseDTO(newGuestUser.getId(), newGuestUser.getToken());
  }


  /**
   * This is a helper method that will check the uniqueness criteria of the
   * username and the name
   * defined in the User entity. The method will do nothing if the input is unique
   * and throw an error otherwise.
   *
   * @param userToBeCreated
   * @throws org.springframework.web.server.ResponseStatusException
   * @see User
   */
  private Boolean checkIfUserExists(User userToBeCreated) {
    User userByUsername = userRepository.findByUsername(userToBeCreated.getUsername());

    String baseErrorMessage = "The %s provided %s not unique. Therefore, the user could not be created!";
    if (userByUsername != null) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, String.format(baseErrorMessage, "username", "is"));
    }
    return false; // smailalijagic: user = null --> does not exist yet
  }

  public AuthenticationResponseDTO loginUser(User loginUser) {
    User existingUser = userRepository.findByUsernameAndPassword(loginUser.getUsername(), loginUser.getPassword());

    if (existingUser != null) {
      // User is authenticated
      existingUser.setStatus(UserStatus.ONLINE);
      return new AuthenticationResponseDTO(existingUser.getId(), existingUser.getToken());
    } else {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
    }
  }

  public User updateUser(User updatedUser, Long userId) {
    //checkIfUserExistsUpdate(updatedUser, userId); // smailalijagic: commented for the moment, but probably can be deleted
    User exsistingUser = userRepository.findUserById(userId); // smailalijagic: null or User...

    // smailalijagic: check that new username is not empty && check that new username is not already used -> unique username
    if (!Objects.equals(updatedUser.getUsername(), "") && !checkIfUserExists(updatedUser)) {
      exsistingUser.setUsername(updatedUser.getUsername()); // smailalijagic: update username
    }
    if (!Objects.equals(updatedUser.getPassword(), "")) {
      exsistingUser.setPassword(updatedUser.getPassword()); // smailalijagic: update password
    }
    exsistingUser.setFriendsList(updatedUser.getFriendsList()); // smailalijagic: update friendlist
    exsistingUser.setUsergamelobbylist(updatedUser.getUsergamelobbylist()); // smailalijagic: update with all active gamelobbies
    exsistingUser.setUsericon(updatedUser.getUsericon()); // smailalijagic: update usericon

    updatedUser = userRepository.save(exsistingUser);
    userRepository.flush();
    
    return updatedUser;
  }

  public void deleteUser(Long id) {
    this.userRepository.deleteById(id);
  }

}
