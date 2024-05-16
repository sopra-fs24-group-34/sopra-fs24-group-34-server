package ch.uzh.ifi.hase.soprafs24.websocket;

import ch.uzh.ifi.hase.soprafs24.entity.Game;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WebSocketSessionService {

    //nedim-j: ensure that there is only one instance of SessionService
    private static WebSocketSessionService instance;

    private WebSocketSessionService() {
        // Private constructor to prevent instantiation
    }

    public static WebSocketSessionService getInstance() {
        if (instance == null) {
            instance = new WebSocketSessionService();
        }
        return instance;
    }


    private final Map<Long, List<WebSocketSession>> sessionsMap = new HashMap<>(); // Map to store WebSocket sessions to corresponding lobby/game
    private final Map<String, WebSocketSession> activeSessions = new HashMap<>();

    public void addActiveSession(WebSocketSession session) {
        String sessionId = session.getId();
        activeSessions.put(sessionId, session);
        System.out.println("Active session added: " + sessionId);
    }

    public void mapActiveSessionToLobbyOrGame(Long lobbyOrGameId, String sessionId) {
        // Step 1: Check if the session exists in activeSessions
        if (activeSessions.containsKey(sessionId)) {
            // Step 2: Retrieve the session
            WebSocketSession session = activeSessions.get(sessionId);

            // Step 3: Check if there is already a list of sessions for the given lobbyOrGameId
            List<WebSocketSession> sessionsList = sessionsMap.get(lobbyOrGameId);

            if (sessionsList == null) {
                // Step 5: If no list exists, create a new list and add the session to it
                sessionsList = new ArrayList<>();
                sessionsList.add(session);
                sessionsMap.put(lobbyOrGameId, sessionsList);
            } else {
                // Step 4: If a list exists, append the session to the list
                sessionsList.add(session);
            }

            // Step 6: Remove the session from activeSessions
            activeSessions.remove(sessionId);
        }
    }

    public Map<Long, List<WebSocketSession>> getSessionsMap() {
        return sessionsMap;
    }









    //nedim-j: for debugging
    public void printSessionsMap() {
        System.out.println("Sessions Map:");
        for (Map.Entry<Long, List<WebSocketSession>> entry : sessionsMap.entrySet()) {
            Long lobbyOrGameId = entry.getKey();
            List<WebSocketSession> sessionsList = entry.getValue();

            System.out.println("  Lobby/Game ID: " + lobbyOrGameId + ":");
            for (WebSocketSession session : sessionsList) {
                System.out.println("    Session ID: " + session.getId());
                // You can print more details about the session if needed
            }
            System.out.println();
        }
    }

    public void printActiveSessions() {
        System.out.println("Sessions without Lobby/Game:");
        if (!activeSessions.isEmpty()) {
            for (Map.Entry<String, WebSocketSession> entry : activeSessions.entrySet()) {
                String sessionId = entry.getKey();
                WebSocketSession session = entry.getValue();

                System.out.println("Session ID: " + sessionId);
                // You can print more details about the session if needed
            }
        } else {
            System.out.println("No active sessions.");
        }
        System.out.println();
    }

    // Method to handle game sessions
    public void handleGameSession(WebSocketSession session, Game game) {
        // Initialize last activity time for the player
        //lastActivityMap.put(playerId, System.currentTimeMillis());
    }

    /*
    public boolean isPlayerActive(Long playerId) {
        // Check if player is inactive based on last activity time
        // Return true if inactive, false otherwise
        Long lastActivityTime = lastActivityMap.get(playerId);
        if (lastActivityTime == null) {
            // Player's activity status unknown, assume active
            return true;
        }
        long currentTime = System.currentTimeMillis();
        // Check if last activity time is within threshold (e.g., 15 seconds)
        return (currentTime - lastActivityTime) < 15000;
    }

    public void handleLobbySession(WebSocketSession session, Lobby lobby) {
        // Initialize last activity time for the player
        //lastActivityMap.put(playerId, System.currentTimeMillis());
    }

     */
}
