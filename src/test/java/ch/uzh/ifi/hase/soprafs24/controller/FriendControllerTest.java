package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.entity.Friend;
import ch.uzh.ifi.hase.soprafs24.entity.FriendRequest;
import ch.uzh.ifi.hase.soprafs24.entity.LobbyInvitation;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.rest.dto.FriendGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.FriendRequestPostDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.FriendRequestPutDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.LobbyInvitationPutDTO;
import ch.uzh.ifi.hase.soprafs24.service.FriendService;
import ch.uzh.ifi.hase.soprafs24.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class FriendControllerTest {

    @Mock
    private FriendService friendService;

    @Mock
    private UserService userService;

    @InjectMocks
    private FriendController friendController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(friendController).build();
    }

    @Test
    void testSendFriendRequest() throws Exception {
        FriendRequestPostDTO friendRequestPostDTO = new FriendRequestPostDTO();
        // Set necessary properties for friendRequestPostDTO

        mockMvc.perform(post("/users/friends/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(friendRequestPostDTO)))
                .andExpect(status().isCreated());

        verify(friendService, times(1)).createFriendRequest(any(FriendRequestPostDTO.class));
    }

    @Test
    void testAnswerFriendRequest() throws Exception {
        FriendRequestPutDTO friendRequestPutDTO = new FriendRequestPutDTO();
        // Set necessary properties for friendRequestPutDTO

        when(friendService.answerFriendRequest(any(FriendRequestPutDTO.class))).thenReturn(true);

        mockMvc.perform(put("/users/friends/answer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(friendRequestPutDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        verify(friendService, times(1)).answerFriendRequest(any(FriendRequestPutDTO.class));
    }

//    @Test
//    void testLobbyInvite() throws Exception {
//        LobbyInvitationPostDTO lobbyInvitationPostDTO = new LobbyInvitationPostDTO();
//        // Set necessary properties for lobbyInvitationPostDTO
//
//        mockMvc.perform(post("/lobbies/invite")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(new ObjectMapper().writeValueAsString(lobbyInvitationPostDTO)))
//                .andExpect(status().isCreated());
//
//        verify(friendService, times(1)).inviteFriendtoLobby(anyLong(), anyString(), anyLong());
//    }

    @Test
    void testHandleGameInvitation() throws Exception {
        LobbyInvitationPutDTO lobbyInvitationPutDTO = new LobbyInvitationPutDTO();
        // Set necessary properties for lobbyInvitationPutDTO

        mockMvc.perform(put("/lobbies/invitation/answer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(lobbyInvitationPutDTO)))
                .andExpect(status().isOk());

        verify(friendService, times(1)).answerLobbyInvitation(any(LobbyInvitationPutDTO.class));
    }

    @Test
    void testDeleteFriend() throws Exception {
        Long userId = 1L;
        Long friendId = 2L;

        User user = new User();
        when(userService.getUser(userId)).thenReturn(user);

        mockMvc.perform(delete("/users/{userId}/friends/delete/{friendId}", userId, friendId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(friendService, times(1)).deleteFriend(user, friendId);
    }

    @Test
    void testGetAllFriends() throws Exception {
        Long userId = 1L;
        User user = new User();
        List<Friend> friends = Arrays.asList(new Friend(), new Friend());

        when(userService.getUser(userId)).thenReturn(user);
        when(friendService.getFriends(user)).thenReturn(friends);

        mockMvc.perform(get("/users/{userId}/friends", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(friends)));

        verify(friendService, times(1)).getFriends(user);
    }

    @Test
    void testGetFriendRequests() throws Exception {
        Long userId = 1L;
        User receiver = new User();
        List<FriendRequest> friendRequests = Arrays.asList(new FriendRequest(), new FriendRequest());
        List<FriendGetDTO> friendGetDTOs = new ArrayList<>();

        when(userService.getUser(userId)).thenReturn(receiver);
        when(friendService.getFriendRequests(receiver)).thenReturn(friendRequests);
        when(friendService.convertFriendRequestToFriend(any(FriendRequest.class))).thenReturn(new FriendGetDTO());

        mockMvc.perform(get("/users/{userId}/friends/requests", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(friendService, times(1)).getFriendRequests(receiver);
    }

    @Test
    void testGetLobbyInvitations() throws Exception {
        Long userId = 1L;
        User receiver = new User();
        List<LobbyInvitation> lobbyInvitations = Arrays.asList(new LobbyInvitation(), new LobbyInvitation());

        when(userService.getUser(userId)).thenReturn(receiver);
        when(friendService.getLobbyInviations(receiver)).thenReturn(lobbyInvitations);

        mockMvc.perform(get("/users/{userId}/lobbies/invitations", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(lobbyInvitations)));

        verify(friendService, times(1)).getLobbyInviations(receiver);
    }
}

