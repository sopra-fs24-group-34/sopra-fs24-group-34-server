package ch.uzh.ifi.hase.soprafs24.websocket;

import ch.uzh.ifi.hase.soprafs24.service.GameService;
import ch.uzh.ifi.hase.soprafs24.service.LobbyService;
import ch.uzh.ifi.hase.soprafs24.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class WebSocketSessionServiceTest {
    @Mock
    private UserService userService;

    @Mock
    private LobbyService lobbyService;

    @Mock
    private GameService gameService;

    @Mock
    private WebSocketMessenger webSocketMessenger;

    @InjectMocks
    private WebSocketSessionService webSocketSessionService;

    @Mock
    private WebSocketSession session;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        webSocketSessionService = WebSocketSessionService.getInstance();
        webSocketSessionService.setLobbyService(lobbyService);
        webSocketSessionService.setGameService(gameService);
        webSocketSessionService.setWebSocketMessenger(webSocketMessenger);
    }

    @Test
    void getInstance_ShouldReturnSameInstance() {
        WebSocketSessionService instance1 = WebSocketSessionService.getInstance();
        WebSocketSessionService instance2 = WebSocketSessionService.getInstance();
        assertSame(instance1, instance2);
    }

    @Test
    void addUserIdToActiveSession_ShouldAddUserId() {
        when(session.getId()).thenReturn("sessionId1");
        webSocketSessionService.addActiveSession(session);
        when(session.getAttributes()).thenReturn(new HashMap<>());
        webSocketSessionService.addUserIdToActiveSession("sessionId1", "1");
        assertEquals(1L, session.getAttributes().get("userId"));
    }

    @Test
    void mapActiveSessionToLobby() {
        when(session.getId()).thenReturn("session1");
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("userId", "123");
        when(session.getAttributes()).thenReturn(attributes);
        webSocketSessionService.addActiveSession(session);

        webSocketSessionService.mapActiveSessionToLobby(456L, "session1");

        assertTrue(webSocketSessionService.getSessionsMap().get(456L).contains(session));
        verify(lobbyService, times(1)).translateAddUserToLobby(456L, 123L);
    }

    @Test
    void getInstance_ShouldReturnSameInstance2() {
        WebSocketSessionService instance1 = WebSocketSessionService.getInstance();
        WebSocketSessionService instance2 = WebSocketSessionService.getInstance();
        assertSame(instance1, instance2);
    }

    @Test
    void addUserIdToActiveSession_ShouldAddUserId2() {
        when(session.getId()).thenReturn("sessionId1");
        webSocketSessionService.addActiveSession(session);
        when(session.getAttributes()).thenReturn(new HashMap<>());
        webSocketSessionService.addUserIdToActiveSession("sessionId1", "1");
        assertEquals(1L, session.getAttributes().get("userId"));
    }

//    @Test
//    void handleDisconnectedSession_ShouldHandleDisconnection2() {
//        when(session.getId()).thenReturn("sessionId1");
//        when(session.getAttributes()).thenReturn(new HashMap<>(Map.of("userId", 1L, "lobbyId", 1L)));
//        List<WebSocketSession> sessions = new ArrayList<>();
//        sessions.add(session);
//
//        webSocketSessionService.getSessionsMap().put(1L, sessions);
//
//        webSocketSessionService.handleDisconnectedSession(session);
//
//        verify(lobbyService, times(1)).getGameIdFromLobbyId(1L);
//        assertTrue(webSocketSessionService.getDisconnectedUserLobby().containsKey(1L));
//    }

    @Test
    void mapActiveSessionToLobby_ShouldMapSession2() {
        when(session.getId()).thenReturn("sessionId1");
        webSocketSessionService.addActiveSession(session);
        when(session.getAttributes()).thenReturn(new HashMap<>(Map.of("userId", 1L)));

        webSocketSessionService.mapActiveSessionToLobby(1L, "sessionId1");

        assertFalse(webSocketSessionService.getSessionsMap().isEmpty());
        verify(lobbyService, times(1)).translateAddUserToLobby(1L, 1L);
        assertFalse(webSocketSessionService.getActiveSessions().containsKey("sessionId1"));
    }

    @Test
    void printSessionAttributes_ShouldPrintAttributes2() {
        when(session.getId()).thenReturn("sessionId1");
        when(session.getAttributes()).thenReturn(new HashMap<>(Map.of("userId", 1L)));
        webSocketSessionService.addActiveSession(session);

        assertDoesNotThrow(() -> webSocketSessionService.printSessionAttributes("sessionId1"));
    }

    @Test
    void printSessionsMap_ShouldPrintSessionsMap2() {
        when(session.getId()).thenReturn("sessionId1");
        when(session.getAttributes()).thenReturn(new HashMap<>(Map.of("userId", 1L, "lobbyId", 1L)));
        List<WebSocketSession> sessions = new ArrayList<>();
        sessions.add(session);
        webSocketSessionService.getSessionsMap().put(1L, sessions);

        assertDoesNotThrow(webSocketSessionService::printSessionsMap);
    }

    @Test
    void printActiveSessions_ShouldPrintActiveSessions2() {
        when(session.getId()).thenReturn("sessionId1");
        webSocketSessionService.addActiveSession(session);

        assertDoesNotThrow(webSocketSessionService::printActiveSessions);
    }

    @Test
    void getInstance_ShouldReturnSameInstance3() {
        WebSocketSessionService instance1 = WebSocketSessionService.getInstance();
        WebSocketSessionService instance2 = WebSocketSessionService.getInstance();
        assertSame(instance1, instance2);
    }

    @Test
    void addUserIdToActiveSession_ShouldAddUserId3() {
        when(session.getId()).thenReturn("sessionId1");
        webSocketSessionService.addActiveSession(session);
        when(session.getAttributes()).thenReturn(new HashMap<>());
        webSocketSessionService.addUserIdToActiveSession("sessionId1", "1");
        assertEquals(1L, session.getAttributes().get("userId"));
    }

//    @Test
//    void handleDisconnectedSession_ShouldHandleDisconnection() {
//        when(session.getId()).thenReturn("sessionId1");
//        when(session.getAttributes()).thenReturn(new HashMap<>(Map.of("userId", 1L, "lobbyId", 1L)));
//        List<WebSocketSession> sessions = new ArrayList<>();
//        sessions.add(session);
//
//        webSocketSessionService.getSessionsMap().put(1L, sessions);
//
//        webSocketSessionService.handleDisconnectedSession(session);
//
//        verify(lobbyService, times(1)).getGameIdFromLobbyId(1L);
//        assertTrue(webSocketSessionService.getDisconnectedUserLobby().containsKey(1L));
//    }

    @Test
    void mapActiveSessionToLobby_ShouldMapSession() {
        when(session.getId()).thenReturn("sessionId1");
        webSocketSessionService.addActiveSession(session);
        when(session.getAttributes()).thenReturn(new HashMap<>(Map.of("userId", 1L)));

        webSocketSessionService.mapActiveSessionToLobby(1L, "sessionId1");

        assertFalse(webSocketSessionService.getSessionsMap().isEmpty());
        verify(lobbyService, times(1)).translateAddUserToLobby(1L, 1L);
        assertFalse(webSocketSessionService.getActiveSessions().containsKey("sessionId1"));
    }

    @Test
    void printSessionAttributes_ShouldPrintAttributes() {
        when(session.getId()).thenReturn("sessionId1");
        when(session.getAttributes()).thenReturn(new HashMap<>(Map.of("userId", 1L)));
        webSocketSessionService.addActiveSession(session);

        assertDoesNotThrow(() -> webSocketSessionService.printSessionAttributes("sessionId1"));
    }

    @Test
    void printSessionsMap_ShouldPrintSessionsMap() {
        when(session.getId()).thenReturn("sessionId1");
        when(session.getAttributes()).thenReturn(new HashMap<>(Map.of("userId", 1L, "lobbyId", 1L)));
        List<WebSocketSession> sessions = new ArrayList<>();
        sessions.add(session);
        webSocketSessionService.getSessionsMap().put(1L, sessions);

        assertDoesNotThrow(webSocketSessionService::printSessionsMap);
    }

    @Test
    void printActiveSessions_ShouldPrintActiveSessions() {
        when(session.getId()).thenReturn("sessionId1");
        webSocketSessionService.addActiveSession(session);

        assertDoesNotThrow(webSocketSessionService::printActiveSessions);
    }

    @Test
    public void testGetInstance() {
        WebSocketSessionService instance = WebSocketSessionService.getInstance();
        assertNotNull(instance);
    }

    @Test
    public void testAddActiveSession() {
        when(session.getId()).thenReturn("session1");
        webSocketSessionService.addActiveSession(session);
        assertTrue(webSocketSessionService.getActiveSessions().containsKey("session1"));
    }

    @Test
    public void testAddUserIdToActiveSession() {
        when(session.getId()).thenReturn("session1");
        when(session.getAttributes()).thenReturn(new HashMap<>());

        webSocketSessionService.addActiveSession(session);
        webSocketSessionService.addUserIdToActiveSession("session1", "1");

        assertEquals(1L, webSocketSessionService.getActiveSessions().get("session1").getAttributes().get("userId"));
    }

    @Test
    public void testHandleSubscription() {
        when(session.getId()).thenReturn("session1");
        when(session.getAttributes()).thenReturn(new HashMap<>());
        webSocketSessionService.addActiveSession(session);
        webSocketSessionService.addUserIdToActiveSession("session1", "1");

        webSocketSessionService.handleSubscription("/lobbies/1", "session1");

        assertTrue(webSocketSessionService.getSessionsMap().containsKey(1L));
    }

    @Test
    public void testMapActiveSessionToLobby() {
        when(session.getId()).thenReturn("session1");
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("userId", 1L);
        when(session.getAttributes()).thenReturn(attributes);

        webSocketSessionService.addActiveSession(session);
        webSocketSessionService.mapActiveSessionToLobby(1L, "session1");

        assertTrue(webSocketSessionService.getSessionsMap().containsKey(1L));
    }

//    @Test
//    public void testHandleDisconnectedSession() {
//        when(session.getId()).thenReturn("session1");
//        Map<String, Object> attributes = new HashMap<>();
//        attributes.put("userId", 1L);
//        attributes.put("lobbyId", 1L);
//        when(session.getAttributes()).thenReturn(attributes);
//
//        List<WebSocketSession> sessionsList = new ArrayList<>();
//        sessionsList.add(session);
//        webSocketSessionService.getSessionsMap().put(1L, sessionsList);
//
//        webSocketSessionService.handleDisconnectedSession(session);
//
//        assertTrue(webSocketSessionService.getDisconnectedUserLobby().containsKey(1L));
//    }

//    @Test
//    public void testTimerRemoveUserFromLobby() {
//        doNothing().when(lobbyService).removeUserFromLobby(anyLong(), anyLong());
//        doNothing().when(userService).deleteUserIfGuest(anyLong());
//
//        webSocketSessionService.timerRemoveUserFromLobby(1L, 1L);
//
//        verify(lobbyService, timeout(4000)).removeUserFromLobby(1L, 1L);
//    }

    @Test
    public void testStartSessionCleanupTask() {
        webSocketSessionService.addActiveSession(session);
        String sessionId = session.getId();
        assertNotNull(webSocketSessionService.getActiveSessions().get(sessionId));
    }


}