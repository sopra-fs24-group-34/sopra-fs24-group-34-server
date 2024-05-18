package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.entity.Chat;
import ch.uzh.ifi.hase.soprafs24.entity.Game;
import ch.uzh.ifi.hase.soprafs24.rest.dto.MessageGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs24.service.ChatServiceWebSockets;
import ch.uzh.ifi.hase.soprafs24.websocket.WebSocketHandler;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class ChatControllerWebSocketsTest {

    @MockBean
    private ChatServiceWebSockets chatServiceWebSockets;

    @MockBean
    private SimpMessagingTemplate messagingTemplate;

    @MockBean
    private WebSocketHandler webSocketHandler;

    @Autowired
    private ChatControllerWebSockets chatControllerWebSockets;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(chatControllerWebSockets).build();
    }

    @Test
    public void testAddMessageSuccess() throws Exception {
        String message = "Hello, world!";
        Long gameId = 1L;
        Long userId = 2L;

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("message", message);
        jsonObject.addProperty("gameId", gameId.toString());
        jsonObject.addProperty("userId", userId.toString());
        String jsonMessage = jsonObject.toString();

        Game game = new Game();
        Chat chat = new Chat();
        chat.setLastmessage(message);

        MessageGetDTO messageGetDTO = DTOMapper.INSTANCE.convertEntityToMessageGetDTO(chat);

        when(chatServiceWebSockets.getGameByGameId(gameId)).thenReturn(game);
        when(chatServiceWebSockets.addMessage(message, userId, gameId)).thenReturn(chat);

        chatControllerWebSockets.addMessage(jsonMessage);

        verify(webSocketHandler, times(1)).sendMessage(eq("/games/" + gameId + "/chat"), eq("chat-message"), any(MessageGetDTO.class));
        verify(chatServiceWebSockets, times(1)).updateGameChat(game, chat);
    }

    @Test
    public void testAddMessageFailure() throws Exception {
        String invalidJsonMessage = "{invalidJson}";

        chatControllerWebSockets.addMessage(invalidJsonMessage);

        verify(webSocketHandler, never()).sendMessage(anyString(), anyString(), any());
        verify(chatServiceWebSockets, never()).updateGameChat(any(), any());
    }

    @Test
    public void testGetAllMessagesSuccess() throws Exception {
        Long gameId = 1L;
        List<String> messages = Arrays.asList("Hello", "World");

        when(chatServiceWebSockets.getAllMessages(gameId)).thenReturn(messages);

        mockMvc.perform(get("/games/{gameId}/chat", gameId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("[\"Hello\",\"World\"]"));

        verify(chatServiceWebSockets, times(1)).getAllMessages(gameId);
    }

    @Test
    public void testGetAllMessagesFailure() throws Exception {
        Long gameId = 1L;

        when(chatServiceWebSockets.getAllMessages(gameId)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/games/{gameId}/chat", gameId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("[]"));

        verify(chatServiceWebSockets, times(1)).getAllMessages(gameId);
    }
}