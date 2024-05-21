package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.*;
import ch.uzh.ifi.hase.soprafs24.repository.FriendRequestRepository;
import ch.uzh.ifi.hase.soprafs24.repository.LobbyRepository;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs24.rest.dto.*;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.*;

@Service
@Transactional
public class FriendService {

    private final UserRepository userRepository;
    private final LobbyRepository lobbyRepository;
    private final FriendRequestRepository friendRequestRepository;

    @Autowired
    public FriendService(@Qualifier("userRepository") UserRepository userRepository, @Qualifier("lobbyRepository") LobbyRepository lobbyRepository,
                         @Qualifier("friendRequestRepository") FriendRequestRepository friendRequestRepository){
        this.userRepository = userRepository;
        this.lobbyRepository = lobbyRepository;
        this.friendRequestRepository = friendRequestRepository;
    }

    public void createFriendRequest(FriendRequestPostDTO friendRequestPostDTO){
        FriendRequest friendRequest = new FriendRequest();
        User sender = userRepository.findUserById(friendRequestPostDTO.getSenderId());
        User receiver = userRepository.findByUsername(friendRequestPostDTO.getReceiverUserName());

        if (receiver == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The user does not exist");
        }
        assert sender != receiver: "You cannot send a friend request to yourself";

        friendRequest.setSenderUserName(sender.getUsername());
        friendRequest.setReceiverUserName(receiver.getUsername());
        friendRequest.setSenderId(sender.getId());
        friendRequest.setReceiverId(receiver.getId());

        assert !sender.getFriendRequests().contains(friendRequest) : "This friend request already exists";
        //if send request was already sent it catches them and there is not send a second one
        List<FriendRequest> friendRequests = sender.getFriendRequests();
        for (FriendRequest friendRequest1 : friendRequests){
            if (friendRequest1.getReceiverId() == receiver.getId()){
                throw new ResponseStatusException(HttpStatus.CONFLICT, "You already sent a friend request to this User.");
            }
        }
        List<Friend> friends = sender.getFriendsList();
        for (Friend friend : friends){
            if (friend.getFriendId().equals(receiver.getId())){
                throw new ResponseStatusException(HttpStatus.CONFLICT, "The friend is already in your friendlist. Maybe you need to refresh the page");
            }
        }

        sender.addFriendRequest(friendRequest);
        receiver.addFriendRequest(friendRequest);
        friendRequestRepository.save(friendRequest);
        friendRequestRepository.flush();
    }

    public List<FriendRequest> getFriendRequests(User user) {
        return user.getFriendRequests();
    }

    public boolean answerFriendRequest(FriendRequestPutDTO friendRequestPutDTO){
        User sender = userRepository.findUserById(friendRequestPutDTO.getSenderId());
        User receiver = userRepository.findUserById(friendRequestPutDTO.getReceiverId());
        System.out.println("Works 1");
        if (friendRequestRepository.findBySenderIdAndReceiverId(sender.getId(), receiver.getId()) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no such friend request");
        }
        FriendRequest friendRequest = friendRequestRepository.findBySenderIdAndReceiverId(sender.getId(), receiver.getId());
        System.out.println("Works 2");
        for (FriendRequest request : sender.getFriendRequests()) System.out.println(request.getSenderId() + " " + request.getReceiverId());
        for (FriendRequest request : receiver.getFriendRequests()) System.out.println(request.getSenderId() + " " + request.getReceiverId());
        System.out.println("Works 3");
        if (!sender.getFriendRequests().contains(friendRequest) && receiver.getFriendRequests().contains(friendRequest)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no such friend request in the database");
        }
        System.out.println("Works 4");
        sender.removeFriendRequest(friendRequest);
        receiver.removeFriendRequest(friendRequest);
        friendRequestRepository.delete(friendRequest);
        if (friendRequestPutDTO.isAnswer()) {
            sender.addFriend(createFriend(receiver)); //till: create Friend and add id, icon, username as values for a friend
            receiver.addFriend(createFriend(sender)); //same
            return true;
            }
        return false;
        }

    public Friend createFriend(User user) {
        Friend friend = new Friend();
        friend.setFriendId(user.getId());
        friend.setFriendIcon(user.getProfilePicture());
        friend.setFriendUsername(user.getUsername());
        return friend;
    }

    public boolean checkFriendRequest(FriendRequest sentFriendRequest, List<FriendRequest> friendRequestsList){
        if (friendRequestsList == null) {
            return false; // List is empty, so no ChildObject with the attributes exists
        }
        for (FriendRequest friendRequest : friendRequestsList) {
            if (friendRequest.getSenderId() == sentFriendRequest.getSenderId()
                    && friendRequest.getSenderUserName().equals(sentFriendRequest.getSenderUserName())) {
                return true; // Found a FriendRequest from the User
            }
        }
        return false;
    }

    public List<Friend> getFriends(User user) {
        List<Friend> friends = user.getFriendsList();
        /**List<FriendGetDTO> friendGetDTOs = new ArrayList<>();
        for (UserGetDTO friend : friends) friendGetDTOs.add(DTOMapper.INSTANCE.convertEntityToFriendGetDTO(friend));*/
        return friends;
    }

    public FriendGetDTO convertFriendRequestToFriend(FriendRequest friendRequest) {
        FriendGetDTO friendGetDTO = new FriendGetDTO();
        User user = userRepository.findUserById(friendRequest.getSenderId());
        friendGetDTO.setFriendIcon(user.getProfilePicture());
        friendGetDTO.setFriendUsername(user.getUsername());
        friendGetDTO.setFriendId(user.getId());
        return friendGetDTO;
    }

    public void inviteFriendtoLobby(Long creatorId, String invitedUserName, Long lobbyId) {
        User creator = userRepository.findUserById(creatorId);
        User invitedUser = userRepository.findByUsername(invitedUserName);

        if (invitedUser.getStatus() != UserStatus.ONLINE) {
            System.out.println("The invited Friend cannot be invited to a Lobby right now.");
        }
        if (creator.getStatus() != UserStatus.INLOBBY_PREPARING){
            System.out.println("The user cannot send a lobby invitation right now.");
        }
        //till: prevents sending multiple lobby Invitations to the same friend
        List<LobbyInvitation> lobbyInvitations = invitedUser.getLobbyInvitations();
        for (LobbyInvitation lobbyInvitation : lobbyInvitations){
            if (lobbyInvitation.getCreatorId().equals(creator.getId())){
                return;
            }
        }
        LobbyInvitation invitation = new LobbyInvitation();
        invitation.setCreatorUsername(creator.getUsername());
        invitation.setLobbyId(lobbyId);
        invitation.setCreatorId(creatorId);
        invitation.setCreatorIcon(creator.getProfilePicture());
        invitedUser.addLobbyInvitation(invitation);
    }

    public void answerLobbyInvitation(LobbyInvitationPutDTO lobbyInvitationPutDTO) {
        User creator = userRepository.findUserById(lobbyInvitationPutDTO.getCreatorId());
        User invitedUser = userRepository.findUserById(lobbyInvitationPutDTO.getInvitedUserId());
        System.out.println(creator.getStatus());
        if (creator.getStatus() != UserStatus.INLOBBY_PREPARING) {
            System.out.println("The User who invited you is not in the Lobby anymore");
        }
        List<LobbyInvitation> lobbyInvitations = new ArrayList<>(invitedUser.getLobbyInvitations());
        for (LobbyInvitation lobbyInvitation : invitedUser.getLobbyInvitations()){
            if (lobbyInvitation.getCreatorId() == creator.getId()){
                lobbyInvitations.remove(lobbyInvitation);
            }
        }
        invitedUser.setLobbyInvitations(lobbyInvitations);
        userRepository.save(invitedUser);
        userRepository.flush();
    }

    public void deleteFriend(User user, Long friendId) {
        List<Friend> userFriendsList = new ArrayList<>(user.getFriendsList());
        System.out.println("Userfriendlist: " + userFriendsList);
        for (Friend friend : userFriendsList) {
            System.out.println(friend.getFriendId() + " " + friendId);
            if (friend.getFriendId() == friendId) {
                userFriendsList.remove(friend);
                user.setFriendsList(userFriendsList);
                System.out.println(userFriendsList);
                break;
            }
        }
        User deletedFriend = userRepository.findUserById(friendId);
        List<Friend> deletedFriendFriendsList = deletedFriend.getFriendsList();
        for (Friend friend : deletedFriend.getFriendsList()){
            if (friend.getFriendId() == user.getId()) {
                deletedFriendFriendsList.remove(friend);
                deletedFriend.setFriendsList(deletedFriendFriendsList);
                System.out.println(deletedFriendFriendsList);
                break;
            }
        }
        userRepository.save(user);
        userRepository.save(deletedFriend);
        userRepository.flush();
    }

    public List<LobbyInvitation> getLobbyInviations(User receiver) {
        return receiver.getLobbyInvitations();
    }
}
