package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.entity.Friend;
import ch.uzh.ifi.hase.soprafs24.entity.FriendRequest;
import ch.uzh.ifi.hase.soprafs24.entity.LobbyInvitation;
import ch.uzh.ifi.hase.soprafs24.rest.dto.*;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs24.service.FriendService;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.service.UserService;
import ch.uzh.ifi.hase.soprafs24.websocket.WebSocketHandler;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
public class FriendController {

    private final FriendService friendService;
    private final UserService userService;

    private WebSocketHandler webSocketHandler;

    @Autowired
    FriendController(FriendService friendService, UserService userService) {
        this.friendService = friendService;
        this.userService = userService;
    }


    // add friend request
    @PostMapping("/users/friends/add")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public void sendFriendRequest(@RequestBody FriendRequestPostDTO friendRequestPostDTO) {
        System.out.println("DO FRIEND REQUEST POST");
        friendService.createFriendRequest(friendRequestPostDTO);
    }



    // Handle friend request (accept or decline)
    @PutMapping("/users/friends/answer")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public boolean answerFriendRequest(@RequestBody FriendRequestPutDTO friendRequestPutDTO){
        System.out.println("DO FRIEND ANSWER POST");
        return friendService.answerFriendRequest(friendRequestPutDTO);

    }

    //Invite friend into lobby
    @PostMapping("/lobbies/invite")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public void lobbyInvite(@RequestBody LobbyInvitationPostDTO lobbyInvitationPostDTO) {
        System.out.println("DO LOBBY INVITE POST");
        Long userId = lobbyInvitationPostDTO.getCreatorId();
        String invitedUsername = lobbyInvitationPostDTO.getInvitedUserName();
        Long lobbyId = lobbyInvitationPostDTO.getLobbyId();

        System.out.println("1: " + invitedUsername);

        friendService.inviteFriendtoLobby(userId, invitedUsername, lobbyId);
    }

    // Handle lobby invitation
    @PutMapping("/lobbies/invitation/answer")
    @ResponseStatus(HttpStatus.OK)
    public void handleGameInvitation(@RequestBody LobbyInvitationPutDTO lobbyInvitationPutDTO){
        System.out.println("DO LOBBY ANSWER POST");
        friendService.answerLobbyInvitation(lobbyInvitationPutDTO);

    }

    // Delete friend
    @DeleteMapping("/users/{userId}/friends/delete/{friendId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void deleteFriend(@PathVariable Long userId, @PathVariable Long friendId) {
        User user = userService.getUser(userId);
        friendService.deleteFriend(user, friendId);
    }

    // Get all friends. (Only return Id, username and usericon)
    @GetMapping("/users/{userId}/friends")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<Friend> getAllFriends(@PathVariable Long userId) {
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

    @GetMapping("/users/{userId}/lobbies/invitations")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<LobbyInvitation> getLobbyInvitations(@PathVariable Long userId){
        System.out.println("DO LOBBY INVITATIONS GETTER");
        User receiver = userService.getUser(userId);
        List<LobbyInvitation> lobbyInvitations = friendService.getLobbyInviations(receiver);
        return lobbyInvitations;
    }

}
