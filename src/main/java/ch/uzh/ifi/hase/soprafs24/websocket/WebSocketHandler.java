package ch.uzh.ifi.hase.soprafs24.websocket;

import ch.uzh.ifi.hase.soprafs24.entity.*;
import ch.uzh.ifi.hase.soprafs24.repository.GameRepository;
import ch.uzh.ifi.hase.soprafs24.repository.LobbyRepository;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs24.service.LobbyService;
import com.google.gson.*;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.PingMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.handler.WebSocketSessionDecorator;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class WebSocketHandler extends TextWebSocketHandler {

    private final Gson gson = new Gson();
    private final SimpMessagingTemplate messagingTemplate;
    private final Map<Long, Long> lastActivityMap = new HashMap<>(); // Map to store last activity time of players
    //private final Map<Long, WebSocketSession> sessionsMap = new HashMap<>(); // Map to store lobbyId associated with WebSocket sessions
    private Map<String, WebSocketSession> activeSessions = new ConcurrentHashMap<>();
    private final LobbyRepository lobbyRepository;
    private final GameRepository gameRepository;

    public WebSocketHandler(SimpMessagingTemplate messagingTemplate, LobbyRepository lobbyRepository, GameRepository gameRepository) {
        this.messagingTemplate = messagingTemplate;
        this.lobbyRepository = lobbyRepository;
        this.gameRepository = gameRepository;
    }

    public void sendMessage(String destination, String event_type, Object data) {
        JsonObject messageJson = new JsonObject();
        messageJson.addProperty("event-type", event_type);
        messageJson.add("data", gson.toJsonTree(data));
        String message = gson.toJson(messageJson);
        messagingTemplate.convertAndSend(destination, message);
    }

    public void sendMessage(String destination, String event_type, String data) {
        JsonObject messageJson = new JsonObject();
        messageJson.addProperty("event-type", event_type);
        messageJson.addProperty("data", data);
        String message = gson.toJson(messageJson);
        messagingTemplate.convertAndSend(destination, message);
    }

    public Long getIdFromSession(WebSocketSession session) {
        return (Long) session.getAttributes().get("id");
    }

    /*
    public WebSocketSession getSessionFromIdInMap(Long id) {
        for (Map.Entry<Long, WebSocketSession> entry : sessionsMap.entrySet()) {
            if (entry.getKey().equals(id)) {
                return entry.getValue();
            }
        }
        return null; // Session not found for the given ID
    }
     */

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("Connection established with session id: " + session.getId());
        // Determine the type of session (game or lobby)
        // Extract playerId or userId from session attributes or message payload
        activeSessions.put(session.getId(), session);
        /*
        Long id = getIdFromSession(session);
        if (id != null) {
            if (gameRepository.findByGameId(id) != null) {
                // If the id belongs to a game, retrieve the player ids from the game
                Game game = gameRepository.findByGameId(id);
                handleGameSession(session, game);
            } else if (lobbyRepository.findByLobbyid(id) != null) {
                // If the id belongs to a lobby, retrieve the user id from the lobby
                Lobby lobby = lobbyRepository.findByLobbyid(id);
                handleLobbySession(session, lobby);
            } else {
                // Handle the case where the id doesn't belong to a game or lobby
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Lobby or game does not exist");
            }
        } else {
            // Handle the case where the session doesn't have an id associated with it
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No ID associated with WS-session");
        }

         */
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // Remove player's or user's entry from last activity map when connection is closed
        // Optional: Perform cleanup or handling of closed connections
        activeSessions.remove(session.getId());
        /*
        String gameId = extractGameId(session);

        // Get the set of sessions with the same game ID
        Set<WebSocketSession> sessions = sessionsByGameId.get(gameId);

        // Close connections for other users
        for (Map.Entry<String, WebSocketSession> entry : activeSessions.entrySet()) {
            WebSocketSession otherSession = entry.getValue();
            if (!otherSession.getId().equals(session.getId())) {
                otherSession.close();
            }
        }
        sessions.remove(session);

         */
    }

    private void handleSession() {

    }

    /*
    @Override
    protected void handlePongMessage(WebSocketSession session, PingMessage message) throws Exception {
        // Update last activity time for the player or user
        Long playerId;
        Long userId;

        if (playerId != null) {
            lastActivityMap.put(playerId, System.currentTimeMillis());
        } else if (userId != null) {
            // Update last activity time for the user
            // Implement as needed
        }
    }

    // Method to send ping message to a player's WebSocket session
    private void sendPingMessage(Long playerId) {
        WebSocketSession session;
        try {
            session.sendMessage(new PingMessage());
        } catch (IOException e) {
            // Handle exception
        }
    }
    */

    // Method to handle game sessions
    public void handleGameSession(WebSocketSession session, Game game) {
        // Initialize last activity time for the player
        //lastActivityMap.put(playerId, System.currentTimeMillis());
    }

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

}