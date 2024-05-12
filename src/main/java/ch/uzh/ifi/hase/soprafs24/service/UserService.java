package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs24.rest.dto.AuthenticationDTO;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
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

    public AuthenticationDTO createUser(User newUser) {
        if(checkIfUserExists(newUser)) {
            String baseErrorMessage = "The %s provided %s not unique. Therefore, the user could not be created!";
            throw new ResponseStatusException(HttpStatus.CONFLICT, String.format(baseErrorMessage, "username", "is"));
        }
        if (newUser.getUsername() == null || newUser.getPassword() == null) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Password or username was not set");
        }
        newUser.setStatus(UserStatus.ONLINE);
        newUser.setToken(UUID.randomUUID().toString());
        newUser.setTotalplayed(0L);
        newUser.setTotalwins(0L);
        newUser.setProfilePicture("/static/media/puck.a9a8a388abb9f9bab805.jpeg");

        // saves the given entity but data is only persisted in the database once
        // flush() is called
        newUser = userRepository.save(newUser);
        userRepository.flush();

    log.debug("Created Information for User: {}", newUser);
    return DTOMapper.INSTANCE.convertEntityToAuthenticationDTO(newUser);
  }

  public AuthenticationDTO createGuestUser(User newGuestUser) {
      if(checkIfUserExists(newGuestUser)) {
          String baseErrorMessage = "The %s provided %s not unique. Therefore, the user could not be created!";
          throw new ResponseStatusException(HttpStatus.CONFLICT, String.format(baseErrorMessage, "username", "is"));
      }
    newGuestUser.setStatus(UserStatus.INLOBBY_PREPARING); // smailalijagic: created user waits per default in lobby
    newGuestUser.setToken(UUID.randomUUID().toString());

        // saves the given entity but data is only persisted in the database once
        // flush() is called
        newGuestUser = userRepository.save(newGuestUser);
        userRepository.flush(); // smailalijagic: this sets the ID

        newGuestUser.setUsername(newGuestUser.getUsername() + newGuestUser.getId()); // smailalijagic: username = Guest1
        newGuestUser.setProfilePicture("/static/media/puck.a9a8a388abb9f9bab805.jpeg"); // dario default image set


        newGuestUser = userRepository.save(newGuestUser); // smailalijagic: update guestuser
        userRepository.flush();

    log.debug("Created Information for User: {}", newGuestUser);
    return DTOMapper.INSTANCE.convertEntityToAuthenticationDTO(newGuestUser);
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
        return userByUsername != null;
        //nedim-j: should not handle that here
        /*
        String baseErrorMessage = "The %s provided %s not unique. Therefore, the user could not be created!";
        if (userByUsername != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, String.format(baseErrorMessage, "username", "is"));
        }

         */
    }

  public AuthenticationDTO loginUser(User loginUser) {
    User existingUser = userRepository.findByUsernameAndPassword(loginUser.getUsername(), loginUser.getPassword());

    if (existingUser != null) {
      // User is authenticated
      existingUser.setStatus(UserStatus.ONLINE);
      return DTOMapper.INSTANCE.convertEntityToAuthenticationDTO(existingUser);
    } else {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
    }
  }

    public User updateUser(User updatedUser, Long userId) {
        //checkIfUserExistsUpdate(updatedUser, userId); // smailalijagic: commented for the moment, but probably can be deleted
        User existingUser = userRepository.findUserById(userId); // smailalijagic: null or User...

        // smailalijagic: check that new username is not empty && check that new username is not already used -> unique username
        if (!Objects.equals(updatedUser.getUsername(), "") && !Objects.equals(existingUser.getUsername(), updatedUser.getUsername())) {
            existingUser.setUsername(updatedUser.getUsername()); // smailalijagic: update username

            // dario: needed else error in changes
            /*
            if (checkIfUserExists(updatedUser)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Username exists");
            }
             */
            existingUser.setUsername(updatedUser.getUsername());
        }

        // dario: update password
        if (!Objects.equals(updatedUser.getPassword(), "") && !Objects.equals(existingUser.getPassword(), updatedUser.getPassword())) {
            existingUser.setPassword(updatedUser.getPassword()); // smailalijagic: update password
        }
        existingUser.setUserfriendlist(updatedUser.getUserfriendlist()); // smailalijagic: update friendlist
        existingUser.setUsergamelobbylist(updatedUser.getUsergamelobbylist()); // smailalijagic: update with all active gamelobbies
        existingUser.setProfilePicture(updatedUser.getProfilePicture());


        updatedUser = userRepository.save(existingUser);
        userRepository.flush();

        return updatedUser;
    }

    public void deleteUser(Long id) {
        User user = userRepository.findUserById(id);
        String username = user.getUsername().toUpperCase();
        // smailalijagic: guest user?
        if (username.startsWith("GUEST") && user.getStatus().equals(UserStatus.OFFLINE)) {
            this.userRepository.deleteById(id); // smailallijagic: guest can only be deleted if offline --> automatic deletion
        } else {
            // smailalijagic: user
            if (user.getStatus().equals(UserStatus.ONLINE)) {
                this.userRepository.deleteById(id); // smailalijagic: user can only be deleted if online
            }
        }
    }

}
