package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.entity.Chat;
import ch.uzh.ifi.hase.soprafs24.entity.Game;
import ch.uzh.ifi.hase.soprafs24.service.ChatServiceWebSockets;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Arrays;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ChatControllerWebSockets.class)
@AutoConfigureMockMvc
class ChatControllerWebSocketsTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChatServiceWebSockets chatServiceWebSockets;

    @Mock
    private Chat chat;

    @Mock
    private Game game;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // Initialize a game
        when(chatServiceWebSockets.getGameByGameId(anyLong())).thenReturn(game);
    }

    @AfterEach
    void tearDown() {
        // Nothing to tear down
    }

    @Test
    void addMessage_ValidInput_Success() throws Exception {
        // Mocking
        when(chatServiceWebSockets.addMessage("Test message", 1L, 1L)).thenReturn(chat);

        // Testing
        mockMvc.perform(post("/sendMessage")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"message\":\"Test message\",\"gameId\":\"1\",\"userId\":\"1\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void addMessage_InvalidInput_ExceptionThrown() throws Exception {
        // Mocking
        when(chatServiceWebSockets.addMessage("", 1L, 1L)).thenThrow(new Exception("Invalid message"));

        // Testing
        mockMvc.perform(post("/sendMessage")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"message\":\"\", \"gameId\":\"1\",\"userId\":\"1\"}"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void getAllMessages_ValidInput_Success() throws Exception {
        // Mocking
        when(chatServiceWebSockets.getAllMessages(1L)).thenReturn(Arrays.asList("message1", "message2"));

        // Testing
        mockMvc.perform(get("/games/1/chat")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void getAllMessages_InvalidInput_NotFound() throws Exception {
        // Mocking
        when(chatServiceWebSockets.getAllMessages(anyLong())).thenReturn(null);

        // Testing
        mockMvc.perform(get("/games/999/chat")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
