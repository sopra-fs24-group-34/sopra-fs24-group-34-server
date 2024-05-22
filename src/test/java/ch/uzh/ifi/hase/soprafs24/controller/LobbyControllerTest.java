package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.Lobby;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.rest.dto.AuthenticationDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.LobbyGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs24.service.GameUserService;
import ch.uzh.ifi.hase.soprafs24.service.LobbyService;
import ch.uzh.ifi.hase.soprafs24.websocket.WebSocketMessenger;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class LobbyControllerTest {

    private MockMvc mockMvc;

    @Mock
    private LobbyService lobbyService;

    @Mock
    private WebSocketMessenger webSocketMessenger;

    @Mock
    private GameUserService gameUserService;

    @InjectMocks
    private LobbyController lobbyController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(lobbyController).build();
    }

//    @Test
//    public void testGetAllLobbies() throws Exception {
//        List<Lobby> lobbies = new ArrayList<>();
//        lobbies.add(new Lobby());
//        lobbies.add(new Lobby());
//
//        when(lobbyService.getLobbies()).thenReturn(lobbies);
//        when(DTOMapper.INSTANCE.convertEntityToLobbyGetDTO(any(Lobby.class))).thenReturn(new LobbyGetDTO());
//
//        mockMvc.perform(get("/lobbies"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.size()", is(2)));
//
//        verify(lobbyService, times(1)).getLobbies();
//    }

//    @Test
//    public void testGetLobby() throws Exception {
//        Lobby lobby = new Lobby();
//        when(lobbyService.getLobby(anyLong())).thenReturn(lobby);
//
//        mockMvc.perform(get("/lobbies/{lobbyId}", 1L))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id", is(lobby.getLobbyid())));
//
//        verify(lobbyService, times(1)).getLobby(1L);
//    }

    @Test
    public void testCreateLobby() throws Exception {
        when(lobbyService.createLobby(anyLong())).thenReturn(1L);

        mockMvc.perform(post("/lobbies/create/{userId}", 1L))
                .andExpect(status().isCreated())
                .andExpect(content().string("1"));

        verify(lobbyService, times(1)).createLobby(1L);
    }

    @Test
    public void testUpdateLobby() throws Exception {
        mockMvc.perform(put("/lobbies/settings/{lobbyId}", "1"))
                .andExpect(status().isOk());

        verify(lobbyService, times(1)).getLobby(1L);
    }

    @Test
    public void testGetLobbySettings() throws Exception {
        mockMvc.perform(get("/lobbies/settings/{lobbyId}", "1"))
                .andExpect(status().isOk());

        // You can add more verifications as needed
    }

    @Test
    public void testJoinLobby() throws Exception {
        Lobby lobby = new Lobby();
        User user = new User();
        user.setStatus(UserStatus.INLOBBY_PREPARING);

        when(lobbyService.checkIfLobbyExists(anyLong())).thenReturn(true);
        when(lobbyService.getLobby(anyLong())).thenReturn(lobby);
        when(lobbyService.getUser(anyLong())).thenReturn(user);

        mockMvc.perform(put("/lobbies/join/{lobbyId}/{userId}", "1", "1"))
                .andExpect(status().isOk());

        verify(lobbyService, times(1)).addUserToLobby(any(Lobby.class), any(User.class));
        verify(gameUserService, times(1)).saveUserChanges(any(User.class));
    }

    @Test
    void testGetLobby2() throws Exception {
        Long lobbyId = 1L;
        Lobby lobby = new Lobby();
        when(lobbyService.getLobby(lobbyId)).thenReturn(lobby);

        mockMvc.perform(get("/lobbies/{lobbyId}", lobbyId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(lobby)));

        verify(lobbyService, times(1)).getLobby(lobbyId);
    }

    @Test
    void testCreateLobby2() throws Exception {
        Long userId = 1L;
        Long lobbyId = 1L;
        when(lobbyService.createLobby(userId)).thenReturn(lobbyId);

        mockMvc.perform(post("/lobbies/create/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().string(lobbyId.toString()));

        verify(lobbyService, times(1)).createLobby(userId);
    }

    @Test
    void testUpdateLobby2() throws Exception {
        Long lobbyId = 1L;
        Lobby updatedLobby = new Lobby();
        when(lobbyService.getLobby(lobbyId)).thenReturn(updatedLobby);

        mockMvc.perform(put("/lobbies/settings/{lobbyId}", lobbyId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(lobbyService, times(1)).getLobby(lobbyId);
    }

    @Test
    void testUpdateStatus() {
        String jsonRequest = "{\"readyStatus\": \"READY\", \"lobbyId\": 1, \"userId\": 1}";
        Long lobbyId = 1L;
        Long userId = 1L;
        UserGetDTO userGetDTO = new UserGetDTO();

        when(lobbyService.updateReadyStatus(userId, "READY")).thenReturn(userGetDTO);

        lobbyController.updateStatus(jsonRequest);

        verify(lobbyService, times(1)).updateReadyStatus(userId, "READY");
        verify(webSocketMessenger, times(1)).sendMessage("/lobbies/" + lobbyId, "user-statusUpdate", userGetDTO);
    }

//    @Test
//    void testCloseLobby() {
//        String jsonRequest = "{\"lobbyId\": 1, \"authenticationDTO\": {}}";
//        Long lobbyId = 1L;
//        AuthenticationDTO authenticationDTO = new AuthenticationDTO();
//
//        lobbyController.closeLobby(jsonRequest);
//
//        verify(lobbyService, times(1)).closeLobby(lobbyId, authenticationDTO);
//        verify(webSocketMessenger, times(1)).sendMessage("/lobbies/" + lobbyId, "lobby-closed", "Lobby has been closed");
//    }
}