package ch.uzh.ifi.hase.soprafs24.service.friendService;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.*;
import ch.uzh.ifi.hase.soprafs24.repository.*;
import ch.uzh.ifi.hase.soprafs24.rest.dto.*;
import ch.uzh.ifi.hase.soprafs24.service.FriendService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FriendServiceTest {

    @Mock
    private LobbyRepository lobbyRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private FriendRequestRepository friendRequestRepository;

    @InjectMocks
    private FriendService friendService;


    private User creator;

    private User invitedUser;

    private FriendRequest friendRequest;

    @BeforeEach
    public void setup(){
        creator = new User();
        creator.setId(1L);
        creator.setUsername("creator");
        creator.setProfilePicture("picture");
        invitedUser = new User();
        invitedUser.setId(2L);
        invitedUser.setUsername("invitedUser");

        friendRequest = new FriendRequest();
        friendRequest.setSenderId(1L);
        friendRequest.setReceiverUserName("invitedUser");
        friendRequest.setReceiverId(2L);
        friendRequest.setSenderUserName("creator");

    }

    @Test
    public void createFriendRequest_validInputs(){
        FriendRequestPostDTO friendRequestPostDTO = new FriendRequestPostDTO();
        friendRequestPostDTO.setSenderId(1L);
        friendRequestPostDTO.setReceiverUserName("invitedUser");

        when(userRepository.findUserById(1L)).thenReturn(creator);
        when(userRepository.findByUsername("invitedUser")).thenReturn(invitedUser);


        // Before function
        assert creator.getFriendRequests().isEmpty();
        assert invitedUser.getFriendRequests().isEmpty();

        friendService.createFriendRequest(friendRequestPostDTO);
        FriendRequest result = creator.getFriendRequests().get(0);


        // After function
        assertEquals(result.getReceiverId(), 2L);
        assertEquals(result.getReceiverUserName(), "invitedUser");
        assertEquals(result.getSenderId(), 1L);
        assertEquals(result.getSenderUserName(), "creator");
    }

    @Test
    public void createFriendRequest_alreadySentFriendRequest_ThrowsException() {
        FriendRequestPostDTO friendRequestPostDTO = new FriendRequestPostDTO();
        friendRequestPostDTO.setSenderId(1L);
        friendRequestPostDTO.setReceiverUserName("invitedUser");
        creator.addFriendRequest(friendRequest);

        when(userRepository.findUserById(1L)).thenReturn(creator);
        when(userRepository.findByUsername("invitedUser")).thenReturn(invitedUser);

        assertThrows(ResponseStatusException.class, () -> friendService.createFriendRequest(friendRequestPostDTO));
    }

    @Test
    public void createFriendRequest_alreadyReceivedFriendRequestFromUser_ThrowsException() {
        FriendRequestPostDTO friendRequestPostDTO = new FriendRequestPostDTO();
        friendRequestPostDTO.setSenderId(1L);
        friendRequestPostDTO.setReceiverUserName("invitedUser");
        FriendRequest friendRequest1 = new FriendRequest();
        friendRequest1.setReceiverId(1L);
        friendRequest1.setSenderId(2L);
        creator.addFriendRequest(friendRequest1);

        when(userRepository.findUserById(1L)).thenReturn(creator);
        when(userRepository.findByUsername("invitedUser")).thenReturn(invitedUser);

        assertThrows(ResponseStatusException.class, () -> friendService.createFriendRequest(friendRequestPostDTO));
    }

    @Test
    public void createFriendRequest_userAlreadyYourFriend_ThrowsException() {
        FriendRequestPostDTO friendRequestPostDTO = new FriendRequestPostDTO();
        friendRequestPostDTO.setSenderId(1L);
        friendRequestPostDTO.setReceiverUserName("invitedUser");
        Friend friend = new Friend();
        friend.setFriendId(2L);
        creator.addFriend(friend);

        when(userRepository.findUserById(1L)).thenReturn(creator);
        when(userRepository.findByUsername("invitedUser")).thenReturn(invitedUser);

        assertThrows(ResponseStatusException.class, () -> friendService.createFriendRequest(friendRequestPostDTO));
    }

    @Test
    public void getFriendRequests_validInputs() {
        creator.addFriendRequest(friendRequest);

        List<FriendRequest> list = friendService.getFriendRequests(creator);

        assertEquals(list, creator.getFriendRequests());
    }

    @Test
    public void answerFriendRequests_validInputs() {
        FriendRequestPutDTO friendRequestPutDTO = new FriendRequestPutDTO();
        friendRequestPutDTO.setSenderId(1L);
        friendRequestPutDTO.setAnswer(true);
        friendRequestPutDTO.setReceiverId(2L);

        when(userRepository.findUserById(1L)).thenReturn(creator);
        when(userRepository.findUserById(2L)).thenReturn(invitedUser);
        when(friendRequestRepository.findBySenderIdAndReceiverId(1L, 2L)).thenReturn(friendRequest);

        //Before
        assert creator.getFriendsList().isEmpty();
        assert invitedUser.getFriendsList().isEmpty();

        friendService.answerFriendRequest(friendRequestPutDTO);

        assertEquals(creator.getFriendsList().get(0).getFriendUsername(), "invitedUser");
        assertEquals(invitedUser.getFriendsList().get(0).getFriendUsername(), "creator");
    }

    @Test
    public void createFriend_validInputs() {

        Friend result = friendService.createFriend(creator);

        assertEquals(result.getFriendUsername(), "creator");
        assertEquals(result.getFriendId(), 1L);
        assertEquals(result.getFriendIcon(), "picture");
    }
    
    @Test
    public void checkFriendRequests_validInputs() {
        //creator does not have a friend request
        assertFalse(friendService.checkFriendRequest(friendRequest, creator.getFriendRequests()));

        //add the Friend request
        creator.addFriendRequest(friendRequest);

        //creator now has a friend request
        assertTrue(friendService.checkFriendRequest(friendRequest, creator.getFriendRequests()));

    }

    @Test
    public void getOnlineFriends_validInputs() {
        //till: One User is online, one's offline, only the online User should be added to the list
        User user = new User();
        user.setId(3L);
        user.setStatus(UserStatus.ONLINE);
        invitedUser.setStatus(UserStatus.OFFLINE);
        Friend friend = new Friend();
        friend.setFriendId(3L);
        Friend invitedFriend = new Friend();
        invitedFriend.setFriendId(2L);

        when(userRepository.findUserById(3L)).thenReturn(user);
        when(userRepository.findUserById(2L)).thenReturn(invitedUser);

        assert friendService.getFriends(creator).isEmpty();

        creator.addFriend(friend);
        creator.addFriend(invitedFriend);

        List<Friend> result = friendService.getOnlineFriends(creator);
        assert result.contains(friend);
        assert !result.contains(invitedFriend);

    }

    @Test
    public void getFriends_validInputs() {
        Friend friend = new Friend();
        friend.setFriendId(1L);

        assert friendService.getFriends(creator).isEmpty();

        creator.addFriend(friend);

        List<Friend> result = friendService.getFriends(creator);

        assert result.contains(friend);
    }

    @Test
    public void convertFriendRequestToFriend_validInputs() {

        when(userRepository.findUserById(1L)).thenReturn(creator);

        FriendGetDTO friendGetDTO = friendService.convertFriendRequestToFriend(friendRequest);

        assertEquals(friendGetDTO.getFriendId(), 1L);
        assertEquals(friendGetDTO.getFriendIcon(), "picture");
        assertEquals(friendGetDTO.getFriendUsername(), "creator");
    }

    @Test
    public void inviteFriendToLobby_validInputs() {
        Lobby lobby = new Lobby();
        lobby.setLobbyid(3L);
        lobby.setCreatorUserId(1L);

        creator.setStatus(UserStatus.INLOBBY_PREPARING);
        invitedUser.setStatus(UserStatus.ONLINE);

        when(userRepository.findUserById(1L)).thenReturn(creator);
        when(userRepository.findByUsername("invitedUser")).thenReturn(invitedUser);
        when(lobbyRepository.findByLobbyid(3L)).thenReturn(lobby);

        //Before
        assert creator.getLobbyInvitations().isEmpty();
        assert invitedUser.getLobbyInvitations().isEmpty();

        friendService.inviteFriendtoLobby(1L, "invitedUser", 3L);

        assertEquals(invitedUser.getLobbyInvitations().get(0).getCreatorUsername(), "creator");
        assertEquals(invitedUser.getLobbyInvitations().get(0).getLobbyId(), 3L);
    }

    //smailalijagic: fails because Status is never changed and lobby seems to be empty?
    @Test
    public void answerLobbyInvitation_validInputs() {
        creator.setStatus(UserStatus.INLOBBY_PREPARING);
        Lobby lobby = new Lobby();
        lobby.setLobbyid(3L);
        lobby.setCreatorUserId(1L);
        LobbyInvitationPutDTO lobbyInvitationPutDTO = new LobbyInvitationPutDTO();
        lobbyInvitationPutDTO.setAnswer(true);
        lobbyInvitationPutDTO.setLobbyId(3L);
        lobbyInvitationPutDTO.setInvitedUserId(2L);
        lobbyInvitationPutDTO.setCreatorId(1L);
        LobbyInvitation lobbyInvitation = new LobbyInvitation();
        lobbyInvitation.setCreatorUsername("creator");
        lobbyInvitation.setLobbyId(3L);
        lobbyInvitation.setCreatorId(1L);
        invitedUser.addLobbyInvitation(lobbyInvitation);

        when(userRepository.findUserById(1L)).thenReturn(creator);
        when(userRepository.findUserById(2L)).thenReturn(invitedUser);

        friendService.answerLobbyInvitation(lobbyInvitationPutDTO);

        assert (!invitedUser.getLobbyInvitations().contains(lobbyInvitation));
    }

    @Test
    public void answerLobbyInvitation_UserNotInLobby_throwsException() {
        creator.setStatus(UserStatus.ONLINE);
        Lobby lobby = new Lobby();
        lobby.setLobbyid(3L);
        lobby.setCreatorUserId(1L);
        LobbyInvitationPutDTO lobbyInvitationPutDTO = new LobbyInvitationPutDTO();
        lobbyInvitationPutDTO.setAnswer(true);
        lobbyInvitationPutDTO.setLobbyId(3L);
        lobbyInvitationPutDTO.setInvitedUserId(2L);
        lobbyInvitationPutDTO.setCreatorId(1L);
        LobbyInvitation lobbyInvitation = new LobbyInvitation();
        lobbyInvitation.setCreatorUsername("creator");
        lobbyInvitation.setLobbyId(3L);
        lobbyInvitation.setCreatorId(1L);
        invitedUser.addLobbyInvitation(lobbyInvitation);

        when(userRepository.findUserById(1L)).thenReturn(creator);
        when(userRepository.findUserById(2L)).thenReturn(invitedUser);

        assertThrows(ResponseStatusException.class, () -> friendService.answerLobbyInvitation(lobbyInvitationPutDTO));
    }

    @Test
    public void deleteFriend_validInputs() {
        Friend creatorFriend = new Friend();
        creatorFriend.setFriendId(1L);
        creatorFriend.setFriendUsername("creator");
        Friend invitedUserFriend = new Friend();
        invitedUserFriend.setFriendId(2L);
        invitedUserFriend.setFriendUsername("invitedUser");

        creator.addFriend(invitedUserFriend);
        invitedUser.addFriend(creatorFriend);

        when(userRepository.findUserById(2L)).thenReturn(invitedUser);

        //Before
        assertEquals(creator.getFriendsList().get(0), invitedUserFriend);
        assertEquals(invitedUser.getFriendsList().get(0), creatorFriend);

        friendService.deleteFriend(creator, 2L);

        //After
        assert creator.getFriendsList().isEmpty();
        assert invitedUser.getFriendsList().isEmpty();
    }

    @Test
    public void getLobbyInvitations() {
        LobbyInvitation lobbyInvitation = new LobbyInvitation();
        creator.addLobbyInvitation(lobbyInvitation);

        List<LobbyInvitation> result = friendService.getLobbyInviations(creator);

        assert result.contains(lobbyInvitation);
    }

}
