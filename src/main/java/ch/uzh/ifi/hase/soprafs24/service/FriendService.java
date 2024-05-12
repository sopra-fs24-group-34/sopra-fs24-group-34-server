package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.FriendRequest;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.FriendRequestRepository;
import ch.uzh.ifi.hase.soprafs24.repository.LobbyRepository;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs24.rest.dto.FriendGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.FriendRequestPostDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.FriendRequestPutDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.LobbyInvitationPostDTO;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

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

        assert sender != receiver: "You cannot send a friend request to yourself";

        friendRequest.setSenderUserName(sender.getUsername());
        friendRequest.setReceiverUserName(receiver.getUsername());
        friendRequest.setSenderId(sender.getId());
        friendRequest.setReceiverId(receiver.getId());

        assert !sender.getFriendRequests().contains(friendRequest) : "This friend request already exists";
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

        if (friendRequestRepository.findBySenderIdAndReceiverId(sender.getId(), receiver.getId()) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no such friend request");
        }
        FriendRequest friendRequest = friendRequestRepository.findBySenderIdAndReceiverId(sender.getId(), receiver.getId());

        for (FriendRequest request : sender.getFriendRequests()) System.out.println(request.getSenderId() + " " + request.getReceiverId());
        for (FriendRequest request : receiver.getFriendRequests()) System.out.println(request.getSenderId() + " " + request.getReceiverId());

        if (!sender.getFriendRequests().contains(friendRequest) && receiver.getFriendRequests().contains(friendRequest)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no such friend request in the database");
        }

        sender.removeFriendRequest(friendRequest);
        receiver.removeFriendRequest(friendRequest);
        friendRequestRepository.delete(friendRequest);

        if (friendRequestPutDTO.isAnswer()) {
            sender.addFriend(receiver);
            receiver.addFriend(sender);
            return true;
            }
        return false;
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

    public List<FriendGetDTO> getFriends(User user) {
        List<User> friends = user.getFriendsList();
        List<FriendGetDTO> friendGetDTOs = new ArrayList<>();
        for (User friend : friends) friendGetDTOs.add(DTOMapper.INSTANCE.convertEntityToFriendGetDTO(friend));
        return friendGetDTOs;
    }

    public FriendGetDTO convertFriendRequestToFriend(FriendRequest friendRequest) {
        FriendGetDTO friendGetDTO = new FriendGetDTO();
        User user = userRepository.findUserById(friendRequest.getSenderId());
        friendGetDTO.setFriendIcon(user.getUsericon());
        friendGetDTO.setFriendUsername(user.getUsername());
        friendGetDTO.setFriendId(user.getId());
        return friendGetDTO;
    }

    public void inviteFriendtoLobby(Long userId, Long invitedUserId) {
        User user = userRepository.findUserById(userId);
        User invitedUser = userRepository.findUserById(invitedUserId);

        if (invitedUser.getStatus() != UserStatus.ONLINE) {
            System.out.println("The invited Friend cannot be invited to a Lobby right now.");
        }
        if (user.getStatus() != UserStatus.ONLINE){
            System.out.println("The user cannot send a lobby invitation right now.");
        }
        List<String> invitations = invitedUser.getLobbyInvitations();
        invitations.add(user.getUsername());
        invitedUser.setLobbyInvitations(invitations);
        userRepository.save(invitedUser);
        userRepository.flush();

    }
}
