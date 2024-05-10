package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.entity.FriendRequest;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.FriendRequestRepository;
import ch.uzh.ifi.hase.soprafs24.repository.LobbyRepository;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs24.rest.dto.FriendGetDTO;
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

    public void createFriendRequest(FriendRequest friendRequest){
        User sender = userRepository.findUserById(friendRequest.getSenderId());
        User receiver = userRepository.findUserById(friendRequest.getReceiverId());
        assert !sender.getFriendRequests().contains(friendRequest) : "This friend request already exists";
        sender.addFriendRequest(friendRequest);
        friendRequestRepository.save(friendRequest);
        friendRequestRepository.flush();
    }

    public List<FriendRequest> getFriendRequests(User user) {
        return user.getFriendRequests();
    }

    public boolean answerFriendRequest(FriendRequest friendRequest, boolean answer){
        User sender = userRepository.findUserById(friendRequest.getSenderId());
        if (!sender.getFriendRequests().contains(friendRequest)) {
            throw new IllegalArgumentException("This friend request does not exist");
        }
        sender.getFriendRequests().remove(friendRequest);
        if (answer) {
            User receiver = userRepository.findUserById(friendRequest.getReceiverId());
            sender.addFriend(receiver);
            return true;
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
}
