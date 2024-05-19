package ch.uzh.ifi.hase.soprafs24.websocket;

import ch.uzh.ifi.hase.soprafs24.entity.Game;
import ch.uzh.ifi.hase.soprafs24.service.GameService;
import ch.uzh.ifi.hase.soprafs24.service.LobbyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WebSocketSessionServiceTest {

    @Mock
    private LobbyService lobbyService;
    @Mock
    private GameService gameService;
    @Mock
    private WebSocketMessenger webSocketMessenger;
    @Mock
    private WebSocketSession webSocketSession;

    @InjectMocks
    private WebSocketSessionService webSocketSessionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        webSocketSessionService = WebSocketSessionService.getInstance();
        webSocketSessionService.setLobbyService(lobbyService);
        webSocketSessionService.setGameService(gameService);
        webSocketSessionService.setWebSocketMessenger(webSocketMessenger);
    }

//    @Test
//    void handleSubscription_lobby() {
//        String destination = "/lobbies/123";
//        String sessionId = "session1";
//
//        webSocketSessionService.handleSubscription(destination, sessionId);
//
//        // Verify that the mapActiveSessionToLobby method is called
//        verify(webSocketSessionService, times(1)).mapActiveSessionToLobby(123L, sessionId);
//    }

//    @Test
//    void handleSubscription_game() {
//        String destination = "/games/123";
//        String sessionId = "session1";
//
//        webSocketSessionService.handleSubscription(destination, sessionId);
//
//        // Verify that the mapReconnectingSessionToLobby method is called
//        verify(webSocketSessionService, times(1)).mapReconnectingSessionToLobby(sessionId, destination, 123L);
//    }

//    @Test
//    void addActiveSession() {
//        when(webSocketSession.getId()).thenReturn("session1");
//
//        webSocketSessionService.addActiveSession(webSocketSession);
//
//        assertTrue(webSocketSessionService.getActiveSessions().containsKey("session1"));
//    }

//    @Test
//    void addUserIdToActiveSession() {
//        when(webSocketSession.getId()).thenReturn("session1");
//        webSocketSessionService.addActiveSession(webSocketSession);
//
//        webSocketSessionService.addUserIdToActiveSession("session1", "123");
//
//        assertEquals(123L, webSocketSessionService.getActiveSessions().get("session1").getAttributes().get("userId"));
//    }

//    @Test
//    void mapReconnectingSessionToLobby() {
//        when(webSocketSession.getId()).thenReturn("session1");
//        Map<String, Object> attributes = new HashMap<>();
//        attributes.put("userId", "123");
//        when(webSocketSession.getAttributes()).thenReturn(attributes);
//        webSocketSessionService.addActiveSession(webSocketSession);
//        webSocketSessionService.getDisconnectedSessions().put(123L, 456L);
//
//        webSocketSessionService.mapReconnectingSessionToLobby("session1", "/games/789", 789L);
//
//        verify(webSocketMessenger, times(1)).sendMessage("/games/789", "update-game-state", gameService.getGame(789L));
//    }

    @Test
    void mapActiveSessionToLobby() {
        when(webSocketSession.getId()).thenReturn("session1");
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("userId", "123");
        when(webSocketSession.getAttributes()).thenReturn(attributes);
        webSocketSessionService.addActiveSession(webSocketSession);

        webSocketSessionService.mapActiveSessionToLobby(456L, "session1");

        assertTrue(webSocketSessionService.getSessionsMap().get(456L).contains(webSocketSession));
        verify(lobbyService, times(1)).translateAddUserToLobby(456L, 123L);
    }

//    @Test
//    void handleDisconnectedSession() {
//        when(webSocketSession.getId()).thenReturn("session1");
//        Map<String, Object> attributes = new HashMap<>();
//        attributes.put("userId", "123");
//        attributes.put("lobbyId", "456");
//        when(webSocketSession.getAttributes()).thenReturn(attributes);
//        webSocketSessionService.addActiveSession(webSocketSession);
//        webSocketSessionService.mapActiveSessionToLobby(456L, "session1");
//
//        webSocketSessionService.handleDisconnectedSession(webSocketSession);
//
//        assertTrue(webSocketSessionService.getDisconnectedSessions().containsKey(123L));
//    }

//    @Test
//    void closeSessionsOfLobbyId() throws IOException {
//        when(webSocketSession.getId()).thenReturn("session1");
//        webSocketSessionService.getSessionsMap().put(456L, new ArrayList<>(Collections.singletonList(webSocketSession)));
//
//        webSocketSessionService.closeSessionsOfLobbyId(456L);
//
//        verify(webSocketSession, times(1)).close();
//        assertFalse(webSocketSessionService.getSessionsMap().containsKey(456L));
//    }
}