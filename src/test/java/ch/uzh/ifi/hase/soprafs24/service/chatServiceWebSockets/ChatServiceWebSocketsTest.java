package ch.uzh.ifi.hase.soprafs24.service.chatServiceWebSockets;

import ch.uzh.ifi.hase.soprafs24.entity.Chat;
import ch.uzh.ifi.hase.soprafs24.entity.Game;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.GameRepository;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs24.service.ChatServiceWebSockets;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class ChatServiceWebSocketsTest {

    @Mock
    private GameRepository gameRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ChatServiceWebSockets chatServiceWebSockets;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addMessage_ValidInput_Success() {
        // smailalijagic: Given
        String message = "Test message";
        Long userId = 1L;
        Long gameId = 1L;

        Game game = new Game();
        game.setGameId(gameId);
        Chat chat = new Chat();
        game.setChat(chat);

        User user = new User();
        user.setId(userId);
        user.setUsername("TestUser");

        when(gameRepository.findByGameId(gameId)).thenReturn(game);
        when(userRepository.findUserById(userId)).thenReturn(user);

        // smailalijagic: When
        Chat result = chatServiceWebSockets.addMessage(message, userId, gameId);

        // smailalijagic: Then
        assertNotNull(result);
        assertEquals("TestUser: Test message", result.getMessages().get(0));
    }

    @Test
    void addMessage_MessageTooLong_Truncated() {
        // smailalijagic: Given
        String message = "A".repeat(251);
        String truncatedMessage = "TestUser: " + "A".repeat(250);
        Long userId = 1L;
        Long gameId = 1L;

        Game game = new Game();
        game.setGameId(gameId);
        Chat chat = new Chat();
        game.setChat(chat);

        User user = new User();
        user.setId(userId);
        user.setUsername("TestUser");

        when(gameRepository.findByGameId(gameId)).thenReturn(game);
        when(userRepository.findUserById(userId)).thenReturn(user);

        // smailalijagic: When
        Chat result = chatServiceWebSockets.addMessage(message, userId, gameId);

        // smailalijagic: Then
        assertNotNull(result);
        assertEquals(truncatedMessage, result.getMessages().get(0));
    }

    @Test
    void getAllMessages_ValidInput_Success() {
        // smailalijagic: Given
        Long gameId = 1L;
        Game game = new Game();
        Chat chat = new Chat();
        chat.setMessages("Message 1");
        chat.setMessages("Message 2");
        game.setChat(chat);

        List<String> messages = chat.getMessages();

        when(gameRepository.findByGameId(gameId)).thenReturn(game);

        // smailalijagic: When
        List<String> result = chatServiceWebSockets.getAllMessages(gameId);

        // smailalijagic: Then
        assertNotNull(result);
        assertEquals(messages, result);
    }

    @Test
    void getGameByGameId_GameNotFound_ExceptionThrown() {
        // smailalijagic: Given
        Long gameId = 999L;

        when(gameRepository.findByGameId(gameId)).thenReturn(null);

        // smailalijagic: When / Then
        assertThrows(ResponseStatusException.class, () -> chatServiceWebSockets.getGameByGameId(gameId));
    }
}
