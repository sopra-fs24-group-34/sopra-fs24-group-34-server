package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.rest.dto.*;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs24.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

/**
 * User Controller
 * This class is responsible for handling all REST request that are related to
 * the user.
 * The controller will receive the request and delegate the execution to the
 * UserService and finally return the result.
 */
@RestController
public class UserController {

  private final UserService userService;

  UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping("/users")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public List<UserStatsGetDTO> getAllUsers() {
    // fetch all users in the internal representation
    List<User> users = userService.getUsers();

    List<UserStatsGetDTO> userStatsGetDTOs = new ArrayList<>();

    // convert each user to the API representation
    for (User user : users) {
      userStatsGetDTOs.add(DTOMapper.INSTANCE.convertEntityToUserStatsGetDTO(user));
    }
    return userStatsGetDTOs;
  }

  // nedim-j: copied from M1, please adjust if needed
  // smailalijagic: adjusted
  @GetMapping("/users/{userId}")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public UserGetDTO getUser(@PathVariable("userId") Long userid) {
    User user = userService.getUser(userid);
    return DTOMapper.INSTANCE.convertEntityToUserGetDTO(user);
  }

  @PostMapping("/register")
  @ResponseStatus(HttpStatus.CREATED)
  @ResponseBody
  public AuthenticationDTO createUser(@RequestBody UserPostDTO userPostDTO) {
    // convert API user to internal representation
    User userInput = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);

    return userService.createUser(userInput);
  }

  @PostMapping("/login")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public AuthenticationDTO loginUser(@RequestBody UserPostDTO userPostDTO) {
    // convert API user to internal representation
    User loginUser = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);

    return userService.loginUser(loginUser);
  }

  @PostMapping("/guestuser/create")
  @ResponseStatus(HttpStatus.CREATED)
  @ResponseBody
  public AuthenticationDTO createGuestUser(@RequestBody UserPostDTO userPostDTO) {
    // smailalijagic:
    // Set default name: Guest
    // and password: 12345
    // in client
    User guestUser = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);

    return userService.createGuestUser(guestUser); // smailalijagic: isGuest == true --> name: Guest + {guestId}

  }

  @PutMapping("/users/{userId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @ResponseBody
  public UserGetDTO updateUser(@PathVariable ("userId") String id, @RequestBody UserPutDTO userPutDTO) {
    Long userId = Long.valueOf(id); // smailalijagic: added
    User userInput = DTOMapper.INSTANCE.convertUserPutDTOtoEntity(userPutDTO);

    User updatedUser = userService.updateUser(userInput, userId); // smailalijagic: rename method to updateUser

    return DTOMapper.INSTANCE.convertEntityToUserGetDTO(updatedUser);

  }

  @DeleteMapping("guestusers/{guestuserId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @ResponseBody
  public void deleteGuestUser(@PathVariable("guestuserId") Long guestuserId) {
    userService.deleteUser(guestuserId);
  }

  @DeleteMapping("users/{userId}/delete")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteUser(@PathVariable("userId") Long userId) {
    //User user = DTOMapper.INSTANCE.convertUserDeleteDTOtoEntity(userDeleteDTO);
    //if (!user.getId().equals(userId)) {
    //  throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not possible to delete different users");
    //}
    userService.deleteUser(userId);
  }
}
