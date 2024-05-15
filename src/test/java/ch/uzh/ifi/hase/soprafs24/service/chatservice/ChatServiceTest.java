//package ch.uzh.ifi.hase.soprafs24.service.chatservice;
//
//import ch.uzh.ifi.hase.soprafs24.entity.Chat;
//import ch.uzh.ifi.hase.soprafs24.entity.ChatTuple;
//import ch.uzh.ifi.hase.soprafs24.entity.Game;
//import ch.uzh.ifi.hase.soprafs24.entity.User;
//import ch.uzh.ifi.hase.soprafs24.repository.ChatRepository;
//import ch.uzh.ifi.hase.soprafs24.repository.GameRepository;
//import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
//import ch.uzh.ifi.hase.soprafs24.service.ChatService;
//import ch.uzh.ifi.hase.soprafs24.service.UserService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
//import org.springframework.http.HttpStatus;
//import org.springframework.web.server.ResponseStatusException;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//public class ChatServiceTest {
//
//    @Mock
//    private ChatRepository chatRepository;
//
//    @Mock
//    private GameRepository gameRepository;
//
//    @Mock
//    private UserRepository userRepository;
//
//    @InjectMocks
//    private ChatService chatService;
//
//    @InjectMocks
//    private UserService userService;
//
//    private Chat chat;
//    private User user;
//    private Game game;
//
//    @BeforeEach
//    public void setup() {
//        user = new User();
//        user.setId(1L);
//        userRepository.save(user);
//        userRepository.flush();
//        game = new Game();
//        game.setGameId(1L);
//        gameRepository.save(game);
//        gameRepository.flush();
//        chat = game.getChat();
//    }
//
//    @Test
//    public void getChats_ReturnsListOfChats() {
//        // Given
//        List<Chat> chats = new ArrayList<>();
//        chats.add(chat);
//        when(chatRepository.findAll()).thenReturn(chats);
//
//        // When
//        List<Chat> result = chatService.getChats();
//
//        // Then
//        assertEquals(1, result.size());
//        assertEquals(chat, result.get(0));
//    }
//
//    @Test
//    public void getChat_ValidChatId_ReturnsChat() {
//        // Given
//        when(chatRepository.findChatById(anyLong())).thenReturn(chat);
//
//        // When
//        Chat result = chatService.getChat(1L);
//
//        // Then
//        assertEquals(chat, result);
//    }
//
//    @Test
//    public void addMessage_CheckLastMessageEqualsAddedMessage() {
//        // Given
//        ChatTuple chatTuple1 = new ChatTuple();
//        chatTuple1.setMessage("Is it male?");
//        chatTuple1.setUserid(user.getId());
//        chat.addMessage(chatTuple1.getMessage(), chatTuple1.getUserid());
//
//        chatRepository.save(chat);
//        chatRepository.flush();
//
//        //when(gameRepository.findByGameId(game.getGameId())).thenReturn(game);
//        //when(chatRepository.findChatById(chat.getId())).thenReturn(chat);
//
//        // When
//        //chatService.addMessage(new_message, user.getId(), game.getGameId());
//
//        // Then
//        assertEquals("Is it male?", chat.getLastmessage());
//    }
//
//    @Test
//    public void addMessage_MessageExceedsMaxLength_TruncatesMessage() {
//        // Given
//        when(gameRepository.findByGameId(anyLong())).thenReturn(game);
//        when(chatRepository.save(any(Chat.class))).thenAnswer(invocation -> invocation.getArgument(0));
//        String longMessage = "a".repeat(Chat.MAX_MESSAGE_LENGTH + 10);
//
//        Chat new_chat = new Chat();
//        ChatTuple chatTuple1 = new ChatTuple();
//        chatTuple1.setMessage(longMessage);
//        chatTuple1.setUserid(user.getId());
//        new_chat.addMessage(chatTuple1.getMessage(), chatTuple1.getUserid());
//
//        chatRepository.save(new_chat);
//        chatRepository.flush();
//
//        chatService.addMessage(new_chat, user.getId(), game.getGameId());
//
//        // Then
//        assertEquals(Chat.MAX_MESSAGE_LENGTH, chat.getLastmessage().length());
//    }
//
//    @Test
//    public void getGame_ValidGameId_ReturnsGame() {
//        // Given
//        when(gameRepository.findByGameId(anyLong())).thenReturn(game);
//
//        // When
//        Game result = chatService.getGame(1L);
//
//        // Then
//        assertEquals(game, result);
//    }
//
//    @Test
//    public void getGame_GameNotFound_ThrowsResponseStatusException() {
//        // Given
//        //when(gameRepository.findByGameId(anyLong())).thenReturn(null);
//
//        // When / Then
//        //assertThrows(ResponseStatusException.class, () -> chatService.getGame(1L));
//        assertThrows(ResponseStatusException.class, () -> chatService.getGame(4L));
//    }
//
//    @Test
//    public void checkIfUserExists_UserExists_ReturnsTrue() {
//        // Given
//        User existingUser = new User();
//        existingUser.setUsername("existingUser123");
//        existingUser.setPassword("existingUser");
//
//        //userService.createUser(existingUser);
//
//        when(userRepository.findByUsername("existingUser123")).thenReturn(existingUser);
//
//        // When
//        boolean result = chatService.checkIfUserExists(existingUser);
//
//        // Then
//        assertTrue(result);
//    }
//
//    @Test
//    public void checkIfUserExists_UserDoesNotExist_ReturnsFalse() {
//        // Given
//        User notexistingUser = new User();
//        notexistingUser.setUsername("NotExistingUser");
//        notexistingUser.setPassword("existingUser");
//        userService.deleteGuestUser(notexistingUser.getId());
//
//        when(userRepository.findByUsername("NotExistingUser")).thenReturn(null);
//
//        // When
//        boolean result = chatService.checkIfUserExists(notexistingUser);
//
//        // Then
//        assertFalse(result);
//    }
//}