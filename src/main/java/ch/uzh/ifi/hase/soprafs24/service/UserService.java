package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
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

  public User createUser(User newUser) {
    newUser.setToken(UUID.randomUUID().toString());
    newUser.setStatus(UserStatus.OFFLINE);
    checkIfUserExists(newUser);
    // saves the given entity but data is only persisted in the database once
    // flush() is called
    newUser = userRepository.save(newUser);
    userRepository.flush();

    log.debug("Created Information for User: {}", newUser);
    return newUser;
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
  private boolean checkIfUserExists(User userToBeCreated) {
    /*
    User userByUsername = userRepository.findByUsername(userToBeCreated.getUsername());
    User userByPassword = userRepository.findByPassword(userToBeCreated.getPassword());

    String baseErrorMessage = "The %s provided %s not unique. Therefore, the user could not be created!";
    if (userByUsername != null && userByPassword != null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
          String.format(baseErrorMessage, "username and the name", "are"));
    } else if (userByUsername != null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format(baseErrorMessage, "username", "is"));
    } else if (userByPassword != null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format(baseErrorMessage, "name", "is"));
    }

    */
    User userByUsername = userRepository.findByUsername(userToBeCreated.getUsername());

    String baseErrorMessage = "The %s provided %s not unique. Therefore, the user could not be created!";
    if (userByUsername != null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
          String.format(baseErrorMessage, "username", "is"));
      //return true;
    }
    return false;
  }

  public User getUser(Long user_id) {
    List <User> AllUsers = getUsers();
    for (User user: AllUsers) {
      if ((user.getId()).equals(user_id)) {
        return user;
      }
    }
    return null;
  }

  public void updateUser(User user, Long user_id) {
    User my_user = getUser(user_id); // smailalijagic: find the user in database
    assert my_user != null; // smailalijagic: make sure user exists
    // smailalijagic: check that new username is not empty && check that new username is not already used -> unique username
    if (!Objects.equals(user.getUsername(), "") && !checkIfUserExists(user)) {
      my_user.setUsername(user.getUsername()); // smailalijagic: update username
    }
    // smailalijagic: check that password is not empty
    if (!Objects.equals(user.getPassword(), "")) {
      my_user.setPassword(user.getPassword()); // smailalijagic: update password
    }
    // smailalijagic: usericon can be null
    my_user.setUsericon(user.getUsericon()); // smailalijagic: update usericon

  }

}
