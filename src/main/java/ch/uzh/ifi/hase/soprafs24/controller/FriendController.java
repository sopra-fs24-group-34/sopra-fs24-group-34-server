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
    public void sendFriendRequest(@PathVariable Long userId, @RequestBody FriendRequestPostDTO friendRequestPostDTO) {
        User sender = userService.getUser(userId);
        User receiver = userService.getUserByUsername(friendRequestPostDTO.getReceiverUserName());
        FriendRequest friendRequest = DTOMapper.INSTANCE.convertFriendRequestPostDTOtoEntity(friendRequestPostDTO);
        friendRequest.setReceiverId(receiver.getId());
        friendRequest.setSenderUserName(sender.getUsername());
        friendService.createFriendRequest(friendRequest);
    }


    // Handle friend request (accept or decline)
    @PostMapping("/users/{userId}/friends/answer")
    @ResponseStatus(HttpStatus.OK)
    public void answerFriendRequest(@PathVariable Long userId, @RequestBody FriendRequestPutDTO friendRequestPutDTO){
        FriendRequest friendrequest = DTOMapper.INSTANCE.convertFriendRequestPutDTOtoEntity(friendRequestPutDTO);
        friendService.answerFriendRequest(friendrequest, friendRequestPutDTO.isAnswer());

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
    public void deleteFriend(@PathVariable Long userId) {

    }

    // Get all friends. (Only return Id, username and usericon)
    @GetMapping("/users/{userId}/friends")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<FriendGetDTO> getAllUsers(@PathVariable Long userId) {
        User user = userService.getUser(userId);
        return friendService.getFriends(user);
    }
}
