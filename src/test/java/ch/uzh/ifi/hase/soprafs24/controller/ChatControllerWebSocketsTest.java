package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.entity.Chat;
import ch.uzh.ifi.hase.soprafs24.entity.Game;
import ch.uzh.ifi.hase.soprafs24.rest.dto.MessageGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs24.service.ChatServiceWebSockets;
import ch.uzh.ifi.hase.soprafs24.websocket.WebSocketMessenger;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ChatControllerWebSocketsTest {

    @Mock
    private ChatServiceWebSockets chatServiceWebSockets;

    @Mock
    private WebSocketMessenger webSocketMessenger;

    @InjectMocks
    private ChatControllerWebSockets chatController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addMessage() {
        // Given
        String stringJsonRequest = "{\"message\":\"Hello\",\"gameId\":\"1\",\"userId\":\"2\"}";
        JsonObject jsonObject = JsonParser.parseString(stringJsonRequest).getAsJsonObject();
        long gameId = jsonObject.get("gameId").getAsLong();
        long userId = jsonObject.get("userId").getAsLong();
        String message = jsonObject.get("message").getAsString();

        Game game = new Game();
        game.setGameId(gameId);

        Chat chat = new Chat();
        chat.setMessages(message);

        MessageGetDTO messageGetDTO = DTOMapper.INSTANCE.convertEntityToMessageGetDTO(chat);

        ArgumentCaptor<String> destinationCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> messageTypeCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<MessageGetDTO> messageGetDTOCaptor = ArgumentCaptor.forClass(MessageGetDTO.class);

        // When
        when(chatServiceWebSockets.getGameByGameId(gameId)).thenReturn(game);
        when(chatServiceWebSockets.addMessage(message, userId, gameId)).thenReturn(chat);

        chatController.addMessage(stringJsonRequest);

        // Then
        verify(webSocketMessenger).sendMessage(
                destinationCaptor.capture(),
                messageTypeCaptor.capture(),
                messageGetDTOCaptor.capture()
        );

        assertTrue(destinationCaptor.getValue().startsWith("/games/" + gameId + "/chat"));
        assertTrue(messageTypeCaptor.getValue().equals("chat-message"));
        assertTrue(messageGetDTOCaptor.getValue().getMessage().equals(message));
    }

    @Test
    void getAllMessages() {
        // Given
        long gameId = 1L;
        List<String> messages = Arrays.asList("Message 1", "Message 2");

        // When
        when(chatServiceWebSockets.getAllMessages(gameId)).thenReturn(messages);

        // Then
        List<String> result = chatController.getAllMessages(gameId);
        assertEquals(messages, result);
    }
}
