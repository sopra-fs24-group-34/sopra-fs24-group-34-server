package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.entity.Friend;
import ch.uzh.ifi.hase.soprafs24.entity.FriendRequest;
import ch.uzh.ifi.hase.soprafs24.entity.LobbyInvitation;
import ch.uzh.ifi.hase.soprafs24.rest.dto.*;
import ch.uzh.ifi.hase.soprafs24.service.FriendService;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.service.UserService;
import ch.uzh.ifi.hase.soprafs24.websocket.WebSocketMessenger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
public class FriendController {

    private final FriendService friendService;
    private final UserService userService;

    private WebSocketMessenger webSocketMessenger;

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
        friendService.createFriendRequest(friendRequestPostDTO);
    }



    // Handle friend request (accept or decline)
    @PutMapping("/users/friends/answer")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public boolean answerFriendRequest(@RequestBody FriendRequestPutDTO friendRequestPutDTO){
        return friendService.answerFriendRequest(friendRequestPutDTO);

    }

    //Invite friend into lobby
    @PostMapping("/lobbies/invite")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public void lobbyInvite(@RequestBody LobbyInvitationPostDTO lobbyInvitationPostDTO) {
        Long userId = lobbyInvitationPostDTO.getCreatorId();
        String invitedUsername = lobbyInvitationPostDTO.getInvitedUserName();
        Long lobbyId = lobbyInvitationPostDTO.getLobbyId();

        friendService.inviteFriendtoLobby(userId, invitedUsername, lobbyId);
    }

    // Handle lobby invitation
    @PutMapping("/lobbies/invitation/answer")
    @ResponseStatus(HttpStatus.OK)
    public void handleGameInvitation(@RequestBody LobbyInvitationPutDTO lobbyInvitationPutDTO){
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
        User user = userService.getUser(userId);
        return friendService.getFriends(user);
    }

    @GetMapping("/users/{userId}/friends/online")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<Friend> getAllOnlineFriends(@PathVariable Long userId) {
        User user = userService.getUser(userId);
        return friendService.getOnlineFriends(user);
    }



    // Get all friend requests
    @GetMapping("/users/{userId}/friends/requests")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<FriendGetDTO> getFriendRequests(@PathVariable Long userId) {
        User receiver = userService.getUser(userId);
        List<FriendRequest> friendRequests = friendService.getFriendRequests(receiver);
        List<FriendGetDTO> friendGetDTOs = new ArrayList<>();
        for (FriendRequest friendRequest : friendRequests) {
            FriendGetDTO friendGetDTO = friendService.convertFriendRequestToFriend(friendRequest);
            friendGetDTOs.add(friendGetDTO);
        }
        return friendGetDTOs;
    }

    @GetMapping("/users/{userId}/lobbies/invitations")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<LobbyInvitation> getLobbyInvitations(@PathVariable Long userId){
        User receiver = userService.getUser(userId);
        List<LobbyInvitation> lobbyInvitations = friendService.getLobbyInviations(receiver);
        return lobbyInvitations;
    }

}
