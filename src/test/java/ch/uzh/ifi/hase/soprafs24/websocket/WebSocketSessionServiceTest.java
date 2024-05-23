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

//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }

    @Test
    void getInstance_ShouldReturnSameInstance() {
        WebSocketSessionService instance1 = WebSocketSessionService.getInstance();
        WebSocketSessionService instance2 = WebSocketSessionService.getInstance();
        assertSame(instance1, instance2);
    }

//    @Test
//    void addActiveSession_ShouldAddSession() {
//        when(session.getId()).thenReturn("sessionId1");
//        webSocketSessionService.addActiveSession(session);
//        assertTrue(webSocketSessionService.getSessionsMap().isEmpty());
//    }

    @Test
    void addUserIdToActiveSession_ShouldAddUserId() {
        when(session.getId()).thenReturn("sessionId1");
        webSocketSessionService.addActiveSession(session);
        when(session.getAttributes()).thenReturn(new HashMap<>());
        webSocketSessionService.addUserIdToActiveSession("sessionId1", "1");
        assertEquals(1L, session.getAttributes().get("userId"));
    }

//    @Test
//    void handleSubscription_ShouldMapSessionToLobby() {
//        when(session.getId()).thenReturn("sessionId1");
//        webSocketSessionService.addActiveSession(session);
//        when(session.getAttributes()).thenReturn(new HashMap<>());
//
//        webSocketSessionService.handleSubscription("/lobbies/1", "sessionId1");
//        assertFalse(webSocketSessionService.getSessionsMap().isEmpty());
//    }

//    @Test
//    void mapReconnectingSessionToLobby_ShouldReconnectSession() {
//        when(session.getId()).thenReturn("sessionId1");
//        webSocketSessionService.addActiveSession(session);
//        when(session.getAttributes()).thenReturn(new HashMap<>());
//        webSocketSessionService.addUserIdToActiveSession("sessionId1", "1");
//
//        Map<Long, Long> disconnectedSessions = new HashMap<>();
//        disconnectedSessions.put(1L, 1L);
//        webSocketSessionService.mapReconnectingSessionToLobby("sessionId1", "/games/1", 1L);
//
//        verify(webSocketMessenger, times(1)).sendMessage(eq("/games/1"), eq("update-game-state"), any());
//    }

//    @Test
//    void handleDisconnectedSession_ShouldHandleDisconnection() {
//        when(session.getId()).thenReturn("sessionId1");
//        when(session.getAttributes()).thenReturn(new HashMap<>(Map.of("userId", 1L, "lobbyId", 1L)));
//        webSocketSessionService.handleDisconnectedSession(session);
//        verify(lobbyService, times(1)).getGameIdFromLobbyId(1L);
//    }

//    @Test
//    void closeSessionsOfLobbyId_ShouldCloseSessions() throws IOException {
//        when(session.getAttributes()).thenReturn(new HashMap<>(Map.of("userId", 1L, "lobbyId", 1L)));
//        List<WebSocketSession> sessions = new ArrayList<>();
//        sessions.add(session);
//
//        Map<Long, List<WebSocketSession>> sessionsMap = webSocketSessionService.getSessionsMap();
//        sessionsMap.put(1L, sessions);
//
//        webSocketSessionService.closeSessionsOfLobbyId(1L);
//
//        verify(session, times(1)).close();
//        assertTrue(sessionsMap.isEmpty());
//    }

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

    // new tests below

    @Test
    void getInstance_ShouldReturnSameInstance2() {
        WebSocketSessionService instance1 = WebSocketSessionService.getInstance();
        WebSocketSessionService instance2 = WebSocketSessionService.getInstance();
        assertSame(instance1, instance2);
    }

//    @Test
//    void addActiveSession_ShouldAddSession2() {
//        when(session.getId()).thenReturn("sessionId1");
//        webSocketSessionService.addActiveSession(session);
//        assertTrue(webSocketSessionService.getSessionsMap().isEmpty());
//        assertEquals(session, webSocketSessionService.getActiveSessions().get("sessionId1"));
//    }

    @Test
    void addUserIdToActiveSession_ShouldAddUserId2() {
        when(session.getId()).thenReturn("sessionId1");
        webSocketSessionService.addActiveSession(session);
        when(session.getAttributes()).thenReturn(new HashMap<>());
        webSocketSessionService.addUserIdToActiveSession("sessionId1", "1");
        assertEquals(1L, session.getAttributes().get("userId"));
    }

//    @Test
//    void handleSubscription_ShouldMapSessionToLobby2() {
//        when(session.getId()).thenReturn("sessionId1");
//        webSocketSessionService.addActiveSession(session);
//        when(session.getAttributes()).thenReturn(new HashMap<>());
//
//        webSocketSessionService.handleSubscription("/lobbies/1", "sessionId1");
//        assertFalse(webSocketSessionService.getSessionsMap().isEmpty());
//    }

//    @Test
//    void mapReconnectingSessionToLobby_ShouldReconnectSession2() {
//        when(session.getId()).thenReturn("sessionId1");
//        webSocketSessionService.addActiveSession(session);
//        when(session.getAttributes()).thenReturn(new HashMap<>());
//        webSocketSessionService.addUserIdToActiveSession("sessionId1", "1");
//
//        Map<Long, Long> disconnectedSessions = new HashMap<>();
//        disconnectedSessions.put(1L, 1L);
//        webSocketSessionService.getDisconnectedSessions().putAll(disconnectedSessions);
//
//        webSocketSessionService.mapReconnectingSessionToLobby("sessionId1", "/games/1", 1L);
//
//        verify(webSocketMessenger, times(1)).sendMessage(eq("/games/1"), eq("update-game-state"), any());
//        assertFalse(webSocketSessionService.getActiveSessions().containsKey("sessionId1"));
//    }

    @Test
    void handleDisconnectedSession_ShouldHandleDisconnection2() {
        when(session.getId()).thenReturn("sessionId1");
        when(session.getAttributes()).thenReturn(new HashMap<>(Map.of("userId", 1L, "lobbyId", 1L)));
        List<WebSocketSession> sessions = new ArrayList<>();
        sessions.add(session);

        webSocketSessionService.getSessionsMap().put(1L, sessions);

        webSocketSessionService.handleDisconnectedSession(session);

        verify(lobbyService, times(1)).getGameIdFromLobbyId(1L);
        assertTrue(webSocketSessionService.getDisconnectedSessions().containsKey(1L));
    }

//    @Test
//    void closeSessionsOfLobbyId_ShouldCloseSessions2() throws IOException {
//        when(session.getAttributes()).thenReturn(new HashMap<>(Map.of("userId", 1L, "lobbyId", 1L)));
//        List<WebSocketSession> sessions = new ArrayList<>();
//        sessions.add(session);
//
//        Map<Long, List<WebSocketSession>> sessionsMap = webSocketSessionService.getSessionsMap();
//        sessionsMap.put(1L, sessions);
//
//        webSocketSessionService.closeSessionsOfLobbyId(1L);
//
//        verify(session, times(1)).close();
//        assertTrue(sessionsMap.isEmpty());
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

//    @Test
//    void timerRemoveUserFromLobby_ShouldRemoveUserAfterTimeout2() throws InterruptedException {
//        webSocketSessionService.timerRemoveUserFromLobby(1L, 1L);
//        Thread.sleep(4000);
//
//        verify(lobbyService, times(1)).removeUserFromLobby(1L, 1L);
//        verify(userService, times(1)).deleteUserIfGuest(1L);
//    }

//    @Test
//    void timerCloseSessions_ShouldCloseSessionsAfterTimeout2() throws InterruptedException {
//        webSocketSessionService.timerCloseSessions(1L, 1L);
//        Thread.sleep(11000);
//
//        verify(webSocketMessenger, times(2)).sendMessage(anyString(), anyString(), any());
//        assertTrue(webSocketSessionService.getSessionsMap().isEmpty());
//    }

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

//    @Test
//    void addActiveSession_ShouldAddSession3() {
//        when(session.getId()).thenReturn("sessionId1");
//        webSocketSessionService.addActiveSession(session);
//        assertTrue(webSocketSessionService.getSessionsMap().isEmpty());
//        assertEquals(session, webSocketSessionService.getActiveSessions().get("sessionId1"));
//    }

    @Test
    void addUserIdToActiveSession_ShouldAddUserId3() {
        when(session.getId()).thenReturn("sessionId1");
        webSocketSessionService.addActiveSession(session);
        when(session.getAttributes()).thenReturn(new HashMap<>());
        webSocketSessionService.addUserIdToActiveSession("sessionId1", "1");
        assertEquals(1L, session.getAttributes().get("userId"));
    }

//    @Test
//    void handleSubscription_ShouldMapSessionToLobby() {
//        when(session.getId()).thenReturn("sessionId1");
//        webSocketSessionService.addActiveSession(session);
//        when(session.getAttributes()).thenReturn(new HashMap<>());
//
//        webSocketSessionService.handleSubscription("/lobbies/1", "sessionId1");
//        assertFalse(webSocketSessionService.getSessionsMap().isEmpty());
//    }

//    @Test
//    void mapReconnectingSessionToLobby_ShouldReconnectSession() {
//        when(session.getId()).thenReturn("sessionId1");
//        webSocketSessionService.addActiveSession(session);
//        when(session.getAttributes()).thenReturn(new HashMap<>());
//        webSocketSessionService.addUserIdToActiveSession("sessionId1", "1");
//
//        Map<Long, Long> disconnectedSessions = new HashMap<>();
//        disconnectedSessions.put(1L, 1L);
//        webSocketSessionService.getDisconnectedSessions().putAll(disconnectedSessions);
//
//        webSocketSessionService.mapReconnectingSessionToLobby("sessionId1", "/games/1", 1L);
//
//        verify(webSocketMessenger, times(1)).sendMessage(eq("/games/1"), eq("update-game-state"), any());
//        assertFalse(webSocketSessionService.getActiveSessions().containsKey("sessionId1"));
//    }

    @Test
    void handleDisconnectedSession_ShouldHandleDisconnection() {
        when(session.getId()).thenReturn("sessionId1");
        when(session.getAttributes()).thenReturn(new HashMap<>(Map.of("userId", 1L, "lobbyId", 1L)));
        List<WebSocketSession> sessions = new ArrayList<>();
        sessions.add(session);

        webSocketSessionService.getSessionsMap().put(1L, sessions);

        webSocketSessionService.handleDisconnectedSession(session);

        verify(lobbyService, times(1)).getGameIdFromLobbyId(1L);
        assertTrue(webSocketSessionService.getDisconnectedSessions().containsKey(1L));
    }

//    @Test
//    void closeSessionsOfLobbyId_ShouldCloseSessions() throws IOException {
//        when(session.getAttributes()).thenReturn(new HashMap<>(Map.of("userId", 1L, "lobbyId", 1L)));
//        List<WebSocketSession> sessions = new ArrayList<>();
//        sessions.add(session);
//
//        Map<Long, List<WebSocketSession>> sessionsMap = webSocketSessionService.getSessionsMap();
//        sessionsMap.put(1L, sessions);
//
//        webSocketSessionService.closeSessionsOfLobbyId(1L);
//
//        verify(session, times(1)).close();
//        assertTrue(sessionsMap.isEmpty());
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

//    @Test
//    void timerRemoveUserFromLobby_ShouldRemoveUserAfterTimeout() throws InterruptedException {
//        webSocketSessionService.timerRemoveUserFromLobby(1L, 1L);
//        Thread.sleep(4000);
//
//        verify(lobbyService, times(1)).removeUserFromLobby(1L, 1L);
//        verify(userService, times(1)).deleteUserIfGuest(1L);
//    }

//    @Test
//    void timerCloseSessions_ShouldCloseSessionsAfterTimeout() throws InterruptedException {
//        webSocketSessionService.timerCloseSessions(1L, 1L);
//        Thread.sleep(11000);
//
//        verify(webSocketMessenger, times(2)).sendMessage(anyString(), anyString(), any());
//        assertTrue(webSocketSessionService.getSessionsMap().isEmpty());
//    }

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


}