package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs24.rest.dto.FriendRequestDTO;
import ch.uzh.ifi.hase.soprafs24.service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class FriendController {

    private final FriendService friendService;

    @Autowired
    FriendController(FriendService friendService, UserRepository userRepository) {
        this.friendService = friendService;
    }


    // add friend request
    @PostMapping("/users/{userId}/friends/add")
    @ResponseStatus(HttpStatus.CREATED)
    public void addFriend(@PathVariable Long userId, @RequestBody FriendRequestDTO friendRequestDTO) {
    }


    // Handle friend request
    @PostMapping("/users/{userId}/friends/answer")
    @ResponseStatus(HttpStatus.OK)
    public void answerFriendRequest(@PathVariable Long userId, @RequestBody FriendRequestDTO friendRequestDTO){

    }

    //Invite friend into game
    @PostMapping("/game/invite/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    public void gameInvitation(@PathVariable Long userId, @RequestBody FriendRequestDTO gameInvitationDTO) {

    }

    // Handle game invitation
    @PostMapping("/game/{userId}/invitationresponse")
    @ResponseStatus(HttpStatus.OK)
    public void handleGameInvitation(@PathVariable Long userId, @RequestBody FriendRequestDTO friendRequestDTO){

    }

    // Delete friend
    @PutMapping("/users/{userId}/friends/delete")
    @ResponseStatus(HttpStatus.OK)
    public void deleteFriend(@PathVariable Long userId, @RequestParam("FriendId") Long friendId) {

    }

    /*
    get all friends
     */
    @GetMapping("/users/{userId}/friends")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void getAllUsers(@PathVariable Long userId, @RequestHeader("Authorization") String authorization) {

    }
}
