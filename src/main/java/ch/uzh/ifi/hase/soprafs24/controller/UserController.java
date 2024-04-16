package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.AuthenticationResponseDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserPutDTO;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs24.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

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
  public List<UserGetDTO> getAllUsers() {
    // fetch all users in the internal representation
    List<User> users = userService.getUsers();
    List<UserGetDTO> userGetDTOs = new ArrayList<>();

    // convert each user to the API representation
    for (User user : users) {
      userGetDTOs.add(DTOMapper.INSTANCE.convertEntityToUserGetDTO(user));
    }
    return userGetDTOs;
  }

  //nedim-j: copied from M1, please adjust if needed
    @GetMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public UserGetDTO getUser(@PathVariable(name="userId") String userIdAsStr) {

        Long userId;
        if(userIdAsStr == null) {
            userId = null;
        } else {
            userId = Long.parseLong(userIdAsStr, 10);
        }

        User userById = new User();
        try {
            userById = userService.getUserById(userId);
        } catch(NoSuchElementException e) {
            userById = null;
        }

        if(userById != null) {
            return DTOMapper.INSTANCE.convertEntityToUserGetDTO(userById);
        }
        else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "User with userId " + userId + " does not exist");
        }
    }

  @PostMapping("/register")
  @ResponseStatus(HttpStatus.CREATED)
  @ResponseBody
  public AuthenticationResponseDTO createUser(@RequestBody UserPostDTO userPostDTO) {
    // convert API user to internal representation
    User userInput = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);

    return userService.createUser(userInput);
  }

  @PostMapping("/login")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public AuthenticationResponseDTO loginUser(@RequestBody UserPostDTO userPostDTO) {
    // convert API user to internal representation
    User loginUser = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);

    return userService.loginUser(loginUser);
  }

  @PutMapping("/users/{userId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @ResponseBody
  public UserGetDTO updateUser(@PathVariable ("userId") String id, @RequestBody UserPutDTO userPutDTO) {
    // smailalijagic: rename function to updateUser
    Long userId = Long.valueOf(id); // smailalijagic: added
    User userInput = DTOMapper.INSTANCE.convertUserPutDTOtoEntity(userPutDTO);

    User updatedUser = userService.updateUser(userInput, userId); // smailalijagic: rename method to updateUser

    return DTOMapper.INSTANCE.convertEntityToUserGetDTO(updatedUser);

  }





}
