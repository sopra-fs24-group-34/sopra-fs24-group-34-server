package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.Lobby;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.rest.dto.AuthenticationDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.LobbyGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs24.service.GameUserService;
import ch.uzh.ifi.hase.soprafs24.service.LobbyService;
import ch.uzh.ifi.hase.soprafs24.websocket.WebSocketMessenger;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
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
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class LobbyControllerTest {

    private MockMvc mockMvc;

    @Mock
    private LobbyService lobbyService;

    @Mock
    private WebSocketMessenger webSocketMessenger;

    @Mock
    private GameUserService gameUserService;

    @InjectMocks
    private LobbyController lobbyController;

    private Gson gson;

    @BeforeEach
    void setup() {
        gson = new Gson();
        mockMvc = MockMvcBuilders.standaloneSetup(lobbyController).build();
    }

    @Test
    void testGetAllLobbies() throws Exception {
        List<Lobby> lobbies = new ArrayList<>();
        when(lobbyService.getLobbies()).thenReturn(lobbies);

        mockMvc.perform(get("/lobbies"))
                .andExpect(status().isOk())
                .andExpect(content().json(gson.toJson(new ArrayList<LobbyGetDTO>())));

        verify(lobbyService, times(1)).getLobbies();
    }

    @Test
    void testGetLobby() throws Exception {
        Lobby lobby = new Lobby();
        when(lobbyService.getLobby(anyLong())).thenReturn(lobby);

        mockMvc.perform(get("/lobbies/{lobbyId}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().json(gson.toJson(lobby)));

        verify(lobbyService, times(1)).getLobby(1L);
    }

    @Test
    void testCreateLobby() throws Exception {
        Long userId = 1L;
        Long lobbyId = 1L;
        when(lobbyService.createLobby(anyLong())).thenReturn(lobbyId);

        mockMvc.perform(post("/lobbies/create/{userId}", userId))
                .andExpect(status().isCreated())
                .andExpect(content().json(lobbyId.toString()));

        verify(lobbyService, times(1)).createLobby(userId);
    }

    @Test
    void testUpdateLobby() throws Exception {
        Lobby lobby = new Lobby();
        when(lobbyService.getLobby(anyLong())).thenReturn(lobby);

        mockMvc.perform(put("/lobbies/settings/{lobbyId}", "1"))
                .andExpect(status().isOk());

        verify(lobbyService, times(1)).getLobby(1L);
    }

    @Test
    void testJoinLobby() throws Exception {
        Long lobbyId = 1L;
        Long userId = 1L;
        User user = new User();
        Lobby lobby = new Lobby();

        when(lobbyService.checkIfLobbyExists(anyLong())).thenReturn(true);
        when(lobbyService.getLobby(anyLong())).thenReturn(lobby);
        when(lobbyService.getUser(anyLong())).thenReturn(user);

        mockMvc.perform(put("/lobbies/join/{lobbyId}/{userId}", lobbyId.toString(), userId.toString()))
                .andExpect(status().isOk());

        verify(lobbyService, times(1)).checkIfLobbyExists(lobbyId);
        verify(lobbyService, times(1)).getLobby(lobbyId);
        verify(lobbyService, times(1)).getUser(userId);
        verify(lobbyService, times(1)).addUserToLobby(lobby, user);
        verify(gameUserService, times(1)).saveUserChanges(user);
        verify(webSocketMessenger, times(1)).sendMessage(anyString(), anyString(), any(UserGetDTO.class));
    }

    @Test
    void testUpdateStatus() {
        String requestJson = "{\"readyStatus\": \"READY\", \"lobbyId\": 1, \"userId\": 1}";
        UserGetDTO userGetDTO = new UserGetDTO();

        when(lobbyService.updateReadyStatus(anyLong(), anyString())).thenReturn(userGetDTO);

        lobbyController.updateStatus(requestJson);

        verify(lobbyService, times(1)).updateReadyStatus(1L, "READY");
        verify(webSocketMessenger, times(1)).sendMessage(anyString(), anyString(), any(UserGetDTO.class));
    }

    @Test
    void testCloseLobby() {
        String requestJson = "{\"lobbyId\": 1, \"authenticationDTO\": {}}";

        lobbyController.closeLobby(requestJson);

        verify(lobbyService, times(1)).closeLobby(anyLong(), any(AuthenticationDTO.class));
        verify(webSocketMessenger, times(1)).sendMessage(anyString(), anyString(), anyString());
    }

    @Test
    public void testCreateLobby2() throws Exception {
        when(lobbyService.createLobby(anyLong())).thenReturn(1L);

        mockMvc.perform(post("/lobbies/create/{userId}", 1L))
                .andExpect(status().isCreated())
                .andExpect(content().string("1"));

        verify(lobbyService, times(1)).createLobby(1L);
    }

    @Test
    public void testUpdateLobby2() throws Exception {
        mockMvc.perform(put("/lobbies/settings/{lobbyId}", "1"))
                .andExpect(status().isOk());

        verify(lobbyService, times(1)).getLobby(1L);
    }

    @Test
    public void testGetLobbySettings() throws Exception {
        mockMvc.perform(get("/lobbies/settings/{lobbyId}", "1"))
                .andExpect(status().isOk());
    }

    @Test
    public void testJoinLobby2() throws Exception {
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
    void testCreateLobby3() throws Exception {
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
    void testUpdateLobby3() throws Exception {
        Long lobbyId = 1L;
        Lobby updatedLobby = new Lobby();
        when(lobbyService.getLobby(lobbyId)).thenReturn(updatedLobby);

        mockMvc.perform(put("/lobbies/settings/{lobbyId}", lobbyId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(lobbyService, times(1)).getLobby(lobbyId);
    }

    @Test
    void testUpdateStatus2() {
        String jsonRequest = "{\"readyStatus\": \"READY\", \"lobbyId\": 1, \"userId\": 1}";
        Long lobbyId = 1L;
        Long userId = 1L;
        UserGetDTO userGetDTO = new UserGetDTO();

        when(lobbyService.updateReadyStatus(userId, "READY")).thenReturn(userGetDTO);

        lobbyController.updateStatus(jsonRequest);

        verify(lobbyService, times(1)).updateReadyStatus(userId, "READY");
        verify(webSocketMessenger, times(1)).sendMessage("/lobbies/" + lobbyId, "user-statusUpdate", userGetDTO);
    }

}
