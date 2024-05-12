package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.FriendRequest;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.FriendRequestRepository;
import ch.uzh.ifi.hase.soprafs24.repository.LobbyRepository;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs24.rest.dto.FriendGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.LobbyInvitationPostDTO;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

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

    public Long createFriendRequest(FriendRequest friendRequest){
        User sender = userRepository.findUserById(friendRequest.getSenderId());
        User receiver = userRepository.findUserById(friendRequest.getReceiverId());
        assert !sender.getFriendRequests().contains(friendRequest) : "This friend request already exists";
        sender.addFriendRequest(friendRequest);
        friendRequestRepository.save(friendRequest);
        friendRequestRepository.flush();
        return friendRequest.getFriendRequestId();
    }

    public List<FriendRequest> getFriendRequests(User user) {
        return user.getFriendRequests();
    }

    public boolean answerFriendRequest(FriendRequest friendRequest, boolean answer){
        User sender = userRepository.findUserById(friendRequest.getSenderId());
        if (checkFriendRequest(friendRequest, sender.getFriendRequests())) {
            throw new IllegalArgumentException("This friend request does not exist");
        }
        sender.getFriendRequests().remove(friendRequest);
        if (answer) {
            User receiver = userRepository.findUserById(friendRequest.getReceiverId());
            sender.addFriend(receiver);
            // receiver.addFriend(sender); cannot add or there will be loop of data, a friend always contains his friend again
            userRepository.save(sender);
            userRepository.save(receiver);
            userRepository.flush();
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
