package ch.uzh.ifi.hase.soprafs24.websocket;

import ch.uzh.ifi.hase.soprafs24.service.GameService;
import ch.uzh.ifi.hase.soprafs24.service.LobbyService;
import ch.uzh.ifi.hase.soprafs24.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.socket.WebSocketSession;


import java.io.IOException;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class WebSocketSessionService {

    //nedim-j: ensure that there is only one instance of SessionService
    private static WebSocketSessionService instance;
    private static UserService userService;
    private static LobbyService lobbyService;
    private static GameService gameService;
    private static WebSocketMessenger webSocketMessenger;


    private static final int timeoutThreshold = 10; //nedim-j: how long a users session can be disconnected until lobby/game is closed
    private static final int lobbyTimeoutThreshold = 3;
    private static final int timeoutPollDelay = 1;
    private static final int closeSessionsDelay = 2;

    @Autowired
    public void setUserService(UserService userService) { WebSocketSessionService.userService = userService; }

    @Autowired
    public void setLobbyService(LobbyService lobbyService) {
        WebSocketSessionService.lobbyService = lobbyService;
    }

    @Autowired
    public void setGameService(GameService gameService) {
        WebSocketSessionService.gameService = gameService;
    }

    @Autowired
    public void setWebSocketMessenger(WebSocketMessenger webSocketMessenger) {
        WebSocketSessionService.webSocketMessenger = webSocketMessenger;
    }

    private WebSocketSessionService() {
        // Private constructor to prevent instantiation
    }

    public static WebSocketSessionService getInstance() {
        if (instance == null) {
            instance = new WebSocketSessionService();
            instance.startSessionCleanupTask();
        }
        return instance;
    }

    //nedim-j: session handling
    private final Map<Long, List<WebSocketSession>> sessionsMap = new HashMap<>(); // Map to store WebSocket sessions to corresponding lobby/game
    private final Map<String, WebSocketSession> activeSessions = new HashMap<>();
    private final Map<Long, Long> disconnectedUserLobby = new HashMap<>(); //userid, lobbyid

    //nedim-j: session cleanup
    private final Map<String, Long> activeSessionTimestamps = new HashMap<>();
    private final Map<Long, Long> disconnectedUserLobbyTimestamps = new HashMap<>();
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);;


    //testing purposes
    public Map<Long, List<WebSocketSession>> getSessionsMap() {
        return sessionsMap;
    }
    Map<String, WebSocketSession> getActiveSessions() {
        return activeSessions; // smailalijagic: do not delete, needed for testing
    }
    Map<Long, Long> getDisconnectedUserLobby() {
        return disconnectedUserLobby; // smailalijagic: do not delete, needed for testing
    }


    public void handleSubscription(String destination, String sessionId) {
        String[] destinationSplit = destination.split("/");
        Long destinationId = Long.valueOf(destinationSplit[2]);
        if(destinationSplit[1].equals("lobbies")) {
            mapActiveSessionToLobby(destinationId, sessionId);
        } else if(destinationSplit[1].equals("games") && (destinationSplit.length < 4)/*!destinationSplit[3].equals("chat")*/){
            mapReconnectingSessionToLobby(sessionId, destination, destinationId);
        }
        printEverything();
    }

    public void addActiveSession(WebSocketSession session) {
        String sessionId = session.getId();
        activeSessions.put(sessionId, session);
        activeSessionTimestamps.put(sessionId, System.currentTimeMillis());
    }

    public void addUserIdToActiveSession(String sessionId, String userId) {
        WebSocketSession session = activeSessions.get(sessionId);
        session.getAttributes().put("userId", Long.valueOf(userId));
    }

    public void mapReconnectingSessionToLobby(String sessionId, String destination, Long destinationId) {

        if (activeSessions.containsKey(sessionId)) {

            WebSocketSession session = activeSessions.get(sessionId);
            Long userId = Long.valueOf(session.getAttributes().get("userId").toString());
            Long lobbyId = disconnectedUserLobby.get(userId);
            session.getAttributes().put("lobbyId", lobbyId);

            List<WebSocketSession> sessionsList = sessionsMap.get(lobbyId);

            if (sessionsList != null) {
                sessionsList.add(session);
            }

            sessionsMap.put(lobbyId, sessionsList);

            System.out.println("User " + userId + " reconnected! | Reconnecting Lobby ID: " + lobbyId);
            activeSessionTimestamps.remove(sessionId);
            activeSessions.remove(sessionId);
            disconnectedUserLobbyTimestamps.remove(userId);
            disconnectedUserLobby.remove(userId);

            try {
                // Pause the execution of the loop for the specified interval
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            webSocketMessenger.sendMessage("/games/"+destinationId, "update-game-state", gameService.getGameState(destinationId));
        }
    }

    public void mapActiveSessionToLobby(Long lobbyId, String sessionId) {
        // Step 1: Check if the session exists in activeSessions
        if (activeSessions.containsKey(sessionId)) {
            // Step 2: Retrieve the session
            WebSocketSession session = activeSessions.get(sessionId);
            session.getAttributes().put("lobbyId", lobbyId);

            // Step 3: Check if there is already a list of sessions for the given lobbyId
            List<WebSocketSession> sessionsList = sessionsMap.get(lobbyId);

            if (sessionsList == null) {
                // Step 5: If no list exists, create a new list and add the session to it
                sessionsList = new ArrayList<>();
                sessionsList.add(session);
                sessionsMap.put(lobbyId, sessionsList);
            } else {
                // Step 4: If a list exists, append the session to the list
                sessionsList.add(session);
            }

            Long userId = Long.valueOf(session.getAttributes().get("userId").toString());
            lobbyService.translateAddUserToLobby(lobbyId, userId);
            // Step 6: Remove the session from activeSessions
            activeSessions.remove(sessionId);
            activeSessionTimestamps.remove(sessionId);
        }
    }

    public void handleDisconnectedSession(WebSocketSession session) {
        String sessionId = session.getId();
        Long userId = Long.valueOf(session.getAttributes().get("userId").toString());
        Long lobbyId = Long.valueOf(session.getAttributes().get("lobbyId").toString());
        disconnectedUserLobby.put(userId, lobbyId);
        disconnectedUserLobbyTimestamps.put(userId, System.currentTimeMillis());

        System.out.println("User timed out! | Session ID: " + sessionId + " | User ID: " + userId + " | Lobby ID: " + lobbyId);

        List<WebSocketSession> sessions = sessionsMap.get(lobbyId);
        sessions.remove(session);
        sessionsMap.put(lobbyId, sessions);

        Long gameId = null;
        try {
            gameId = lobbyService.getGameIdFromLobbyId(lobbyId);
        } catch(Exception ignored) {
        }

        if(gameId != null || sessions.isEmpty()) {
            timerCloseSessions(lobbyId, gameId);
        } else {
            timerRemoveUserFromLobby(lobbyId, userId);
        }

        printEverything();
    }


    public void timerRemoveUserFromLobby(Long lobbyId, Long userId) {

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                // Your function to execute
                System.out.println("User left by closing session!");
                try {
                    lobbyService.removeUserFromLobby(lobbyId, userId);
                    userService.deleteUserIfGuest(userId);
                } catch(Exception e) {
                    System.out.println(e);
                }
            }
        }, lobbyTimeoutThreshold * 1000L); // Convert seconds to milliseconds

        //nedim-j: add logic which checks that the same user rejoined
        for(int timePassed = 0; timePassed <= lobbyTimeoutThreshold; timePassed += timeoutPollDelay) {
            if (sessionsMap.containsKey(lobbyId) && sessionsMap.get(lobbyId).size() >= 2) {
                timer.cancel(); // Cancel the timer
                break;
            }
            try {
                // Pause the execution of the loop for the specified interval
                Thread.sleep(timeoutPollDelay * 1000); // Convert seconds to milliseconds
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void timerCloseSessions(Long lobbyId, Long gameId) {

        webSocketMessenger.sendMessage("/games/"+gameId, "user-timeout", timeoutThreshold);
        System.out.println("User timed out!");

        Timer timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                // Your function to execute
                System.out.println("User disconnected!");
                webSocketMessenger.sendMessage("/games/"+gameId, "user-disconnected", timeoutThreshold);

                try {
                    Thread.sleep(closeSessionsDelay * 1000); // Convert seconds to milliseconds
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                
                closeSessionsOfLobbyId(lobbyId);
            }
        }, timeoutThreshold * 1000L); // Convert seconds to milliseconds


        for(int timePassed = 0; timePassed <= timeoutThreshold; timePassed += timeoutPollDelay) {
            if (sessionsMap.containsKey(lobbyId) && sessionsMap.get(lobbyId).size() >= 2) {
                timer.cancel(); // Cancel the timer
                System.out.println("User rejoined!");
                webSocketMessenger.sendMessage("/games/"+gameId, "user-rejoined", "");
                break;
            }
            try {
                // Pause the execution of the loop for the specified interval
                Thread.sleep(timeoutPollDelay * 1000); // Convert seconds to milliseconds
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void closeSessionsOfLobbyId(Long lobbyId) {
        List<WebSocketSession> sessions = sessionsMap.get(lobbyId);

        for (WebSocketSession session : sessions) {
            Long userId = Long.valueOf(session.getAttributes().get("userId").toString());
            userService.deleteUserIfGuest(userId);
            try {
                if (session.isOpen()) {
                    session.close();
                }
            }
            catch (Exception e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Could not close session " +
                        session + " | " + e);
            }
        }

        sessionsMap.remove(lobbyId);
        System.out.println("Closed sessions for ID: " + lobbyId);
        //nedim-j: delete lobby

        lobbyService.deleteLobby(lobbyService.getLobby(lobbyId));

        printEverything();
    }

    public void startSessionCleanupTask() {
        scheduler.scheduleAtFixedRate(() -> {
            long currentTime = System.currentTimeMillis();

            // Cleanup activeSessions
            Iterator<Map.Entry<String, Long>> activeSessionIterator = activeSessionTimestamps.entrySet().iterator();
            while (activeSessionIterator.hasNext()) {
                Map.Entry<String, Long> entry = activeSessionIterator.next();
                String sessionId = entry.getKey();
                long timestamp = entry.getValue();

                if (currentTime - timestamp > 10_000) { // 10 seconds
                    try {
                        WebSocketSession session = activeSessions.get(sessionId);
                        if (session != null && session.isOpen()) {
                            session.close();
                        }
                        activeSessions.remove(sessionId);
                        System.out.println("Removed active session " + sessionId);
                    } catch(Exception e) {
                        System.err.println("Exception occurred during SESSION cleanup task: " + e.getMessage());
                        e.printStackTrace();
                    }
                    activeSessionIterator.remove();
                }
            }

            // Cleanup disconnectedUserLobby
            Iterator<Map.Entry<Long, Long>> disconnectedUserLobbyIterator = disconnectedUserLobbyTimestamps.entrySet().iterator();
            while (disconnectedUserLobbyIterator.hasNext()) {
                Map.Entry<Long, Long> entry = disconnectedUserLobbyIterator.next();
                Long userId = entry.getKey();
                long timestamp = entry.getValue();

                if (currentTime - timestamp > 10_000) { // 10 seconds
                    try {
                        disconnectedUserLobby.remove(userId);
                        userService.deleteUserIfGuest(userId);
                        System.out.println("Removed disconnected user " + userId);
                        disconnectedUserLobbyIterator.remove();
                    } catch(Exception e) {
                        System.err.println("Exception occurred during USER cleanup task: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
        }, 0, 5, TimeUnit.SECONDS);
    }


    //nedim-j: for debugging
    public void printSessionsMap() {
        System.out.println("Sessions Map:");
        for (Map.Entry<Long, List<WebSocketSession>> entry : sessionsMap.entrySet()) {
            Long lobbyId = entry.getKey();
            List<WebSocketSession> sessionsList = entry.getValue();

            System.out.println("  Lobby ID: " + lobbyId + ":");
            for (WebSocketSession session : sessionsList) {
                if(session != null) {
                    System.out.println("    SessionID: " + session.getId() +
                                    " | UserID: " + session.getAttributes().get("userId")
                            //+ " | LobbyId: " + session.getAttributes().get("lobbyId")
                    );
                }
            }
            System.out.println();
        }
    }

    public void printActiveSessions() {
        System.out.println("Sessions without Lobby/Game:");
        if (!activeSessions.isEmpty()) {
            for (Map.Entry<String, WebSocketSession> entry : activeSessions.entrySet()) {
                if(entry != null) {
                    String sessionId = entry.getKey();
                    WebSocketSession session = entry.getValue();

                    System.out.println("  Session ID: " + sessionId);
                    // You can print more details about the session if needed
                }
            }
        } else {
            System.out.println("No active sessions.");
        }
        System.out.println();
    }

    public void printDisconnectedSessions() {
        System.out.println("Disconnected sessions to remove:");
        if (!disconnectedUserLobby.isEmpty()) {
            for (Map.Entry<Long, Long> entry : disconnectedUserLobby.entrySet()) {
                if(entry != null) {
                    Long userId = entry.getKey();
                    Long lobbyId = entry.getValue();

                    System.out.println("  User ID: " + userId + " | Lobby ID: " + lobbyId);
                    // You can print more details about the session if needed
                }
            }
        } else {
            System.out.println("No disconnected sessions.");
        }
        System.out.println();
    }

    public void printEverything() {
        System.out.println("-----");
        printSessionsMap();
        printActiveSessions();
        printDisconnectedSessions();
        System.out.println("-----");
    }

    public void printSessionAttributes(String sessionId) {
        WebSocketSession session = activeSessions.get(sessionId);
        System.out.println("Session attributes: " +session.getAttributes());
    }
}
