package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.entity.FriendRequest;
import ch.uzh.ifi.hase.soprafs24.rest.dto.FriendGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.FriendRequestPostDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.FriendRequestPutDTO;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs24.service.FriendService;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
public class FriendController {

    private final FriendService friendService;
    private final UserService userService;

    @Autowired
    FriendController(FriendService friendService, UserService userService) {
        this.friendService = friendService;
        this.userService = userService;
    }


    // add friend request
    @PostMapping("/users/{userId}/friends/add")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public void sendFriendRequest(@PathVariable Long userId, @RequestBody FriendRequestPostDTO friendRequestPostDTO) {
        System.out.println("DO FRIEND REQUEST POST");
        friendService.createFriendRequest(friendRequestPostDTO);
    }



    // Handle friend request (accept or decline)
    @PutMapping("/users/{userId}/friends/answer")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public boolean answerFriendRequest(@PathVariable Long userId, @RequestBody FriendRequestPutDTO friendRequestPutDTO){
        return friendService.answerFriendRequest(friendRequestPutDTO);

    }

    //Invite friend into game
    /*@PostMapping("/game/invite/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    public void gameInvitation(@PathVariable Long userId, @RequestBody FriendRequestDTO gameInvitationDTO) {

    }*/

    // Handle game invitation
    /*@PostMapping("/game/{userId}/invitationresponse")
    @ResponseStatus(HttpStatus.OK)
    public void handleGameInvitation(@PathVariable Long userId, @RequestBody FriendRequestDTO friendRequestDTO){

    }*/

    // Delete friend
    @DeleteMapping("/users/{userId}/friends/delete")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void deleteFriend(@PathVariable Long userId) {

    }

    // Get all friends. (Only return Id, username and usericon)
    @GetMapping("/users/{userId}/friends")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<FriendGetDTO> getAllUsers(@PathVariable Long userId) {
        System.out.println("DO FRIENDS GETTER");
        User user = userService.getUser(userId);
        return friendService.getFriends(user);
    }

    // Get all friend requests
    @GetMapping("/users/{userId}/friends/requests")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<FriendGetDTO> getFriendRequests(@PathVariable Long userId) {
        System.out.println("DO FRIEND REQUESTS GETTER");
        User receiver = userService.getUser(userId);
        List<FriendRequest> friendRequests = friendService.getFriendRequests(receiver);
        List<FriendGetDTO> friendGetDTOs = new ArrayList<>();
        for (FriendRequest friendRequest : friendRequests) {
            FriendGetDTO friendGetDTO = friendService.convertFriendRequestToFriend(friendRequest);
            System.out.println(friendGetDTO.getFriendUsername());
            System.out.println(friendGetDTO.getFriendId());
            friendGetDTOs.add(friendGetDTO);
        }
        return friendGetDTOs;
    }
}
