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

  public AuthenticationResponseDTO createUser(User newUser) {

    checkIfUserExists(newUser);
    newUser.setStatus(UserStatus.ONLINE);
    newUser.setToken(UUID.randomUUID().toString());


      // saves the given entity but data is only persisted in the database once
    // flush() is called
    newUser = userRepository.save(newUser);
    userRepository.flush();


    log.debug("Created Information for User: {}", newUser);
    return new AuthenticationResponseDTO(newUser.getId(), newUser.getToken());
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
    // smailalijagic: changed to boolean
    User userByUsername = userRepository.findByUsername(userToBeCreated.getUsername());

    String baseErrorMessage = "The %s provided %s not unique. Therefore, the user could not be created!";
    if (userByUsername != null) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, String.format(baseErrorMessage, "username", "is"));
    }
    return true;
  }
  
  public AuthenticationResponseDTO loginUser(User loginUser) {
    User existingUser = userRepository.findByUsernameAndPassword(loginUser.getUsername(), loginUser.getPassword());

    if (existingUser != null) {
      // User is authenticated
      existingUser.setStatus(UserStatus.ONLINE);
      return new AuthenticationResponseDTO(existingUser.getId(), existingUser.getToken());
    } else {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");}
  }

  public User updateUser(User updatedUser, Long userId){
    checkIfUserExistsUpdate(updatedUser, userId);
    User exsistingUser = userRepository.findUserById(userId);

    // smailalijagic: check that new username is not empty && check that new username is not already used -> unique username
    if (!Objects.equals(updatedUser.getUsername(), "") && !checkIfUserExists(updatedUser)) {
      exsistingUser.setUsername(updatedUser.getUsername()); // smailalijagic: update username
    }
    if (!Objects.equals(updatedUser.getPassword(), "")) {
      exsistingUser.setPassword(updatedUser.getPassword()); // smailalijagic: update password
    }
    exsistingUser.setUserfriendlist(updatedUser.getUserfriendlist()); // smailalijagic: update friendlist
    exsistingUser.setUsergamelobbylist(updatedUser.getUsergamelobbylist()); // smailalijagic: update with all active gamelobbies
    exsistingUser.setUsericon(updatedUser.getUsericon()); // smailalijagic: update usericon

    updatedUser = userRepository.save(exsistingUser);
    userRepository.flush();


    return updatedUser;
  }

  private void checkIfUserExistsUpdate(User userToBeUpdated, Long userId) {
    // smailalijagic: we have too many check if user exists functions --> why not have one function?
    User userByUsername = userRepository.findByUsername(userToBeUpdated.getUsername());
    Optional<User> Optionaluser = userRepository.findById(userId);
    User existingUser = Optionaluser.orElse(null);
    if (existingUser == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "This User does not exist");
    }
    String baseErrorMessage = "The %s provided %s not unique. Therefore, the user could not be created!";
    if (userByUsername != null && existingUser != userByUsername) {
      throw new ResponseStatusException(HttpStatus.CONFLICT,
              String.format(baseErrorMessage, "username", "is"));
    }
  }
}
