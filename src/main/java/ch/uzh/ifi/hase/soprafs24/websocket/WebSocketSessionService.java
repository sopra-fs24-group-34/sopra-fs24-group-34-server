package ch.uzh.ifi.hase.soprafs24.websocket;

import ch.uzh.ifi.hase.soprafs24.entity.Game;

import ch.uzh.ifi.hase.soprafs24.service.GameService;
import ch.uzh.ifi.hase.soprafs24.service.LobbyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.socket.WebSocketSession;


import java.util.*;

@Service
public class WebSocketSessionService {

    //nedim-j: ensure that there is only one instance of SessionService
    private static WebSocketSessionService instance;
    private static LobbyService lobbyService;
    private static GameService gameService;
    private static WebSocketMessenger webSocketMessenger;


    private static final int timeoutThreshold = 10; //nedim-j: how long a users session can be disconnected until lobby/game is closed
    private static final int lobbyTimeoutThreshold = 3;
    private static final int timeoutPollDelay = 1;
    private static final int closeSessionsDelay = 2;

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

    public WebSocketSessionService() {
        // Private constructor to prevent instantiation
    }

    public static WebSocketSessionService getInstance() {
        if (instance == null) {
            instance = new WebSocketSessionService();
        }
        return instance;
    }

    //nedim-j: session handling
    private final Map<Long, List<WebSocketSession>> sessionsMap = new HashMap<>(); // Map to store WebSocket sessions to corresponding lobby/game
    private final Map<String, WebSocketSession> activeSessions = new HashMap<>();
    private final Map<Long, Long> disconnectedSessions = new HashMap<>(); //userid, lobbyid

    public void handleSubscription(String destination, String sessionId) {
        String[] destinationSplit = destination.split("/");
        Long destinationId = Long.valueOf(destinationSplit[2]);
        if(destinationSplit[1].equals("lobbies")) {
            mapActiveSessionToLobby(destinationId, sessionId);
        } else if(destinationSplit[1].equals("games") && (destinationSplit.length < 4)/*!destinationSplit[3].equals("chat")*/){
            mapReconnectingSessionToLobby(sessionId, destination, destinationId);
        }
        printSessionsMap();
        printActiveSessions();
        //nedim-j: handling gameIds not necessary i think. there's no games without a lobby and games are already assigned to a lobby entity.
    }

    public void addActiveSession(WebSocketSession session) {
        String sessionId = session.getId();
        activeSessions.put(sessionId, session);
    }

    public void addUserIdToActiveSession(String sessionId, String userId) {
        WebSocketSession session = activeSessions.get(sessionId);
        session.getAttributes().put("userId", Long.valueOf(userId));
    }

    public void mapReconnectingSessionToLobby(String sessionId, String destination, Long destinationId) {

        if (activeSessions.containsKey(sessionId)) {

            WebSocketSession session = activeSessions.get(sessionId);
            Long userId = Long.valueOf(session.getAttributes().get("userId").toString());
            Long lobbyId = disconnectedSessions.get(userId);
            session.getAttributes().put("lobbyId", lobbyId);

            List<WebSocketSession> sessionsList = sessionsMap.get(lobbyId);

            if (sessionsList != null) {
                sessionsList.add(session);
            }

            System.out.println("User " + userId + " reconnected!");
            activeSessions.remove(sessionId);
            disconnectedSessions.remove(userId);

            try {
                // Pause the execution of the loop for the specified interval
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
            //System.out.println("Sending message to: " + destination);
            //System.out.println("Game: " + gameService.getGameState(destinationId));
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
        }
    }

    public void handleDisconnectedSession(WebSocketSession session) {
        String sessionId = session.getId();
        Long userId = Long.valueOf(session.getAttributes().get("userId").toString());
        Long lobbyId = Long.valueOf(session.getAttributes().get("lobbyId").toString());
        if(!disconnectedSessions.containsKey(userId)) {
            disconnectedSessions.put(userId, lobbyId);
        }

        List<WebSocketSession> sessions = sessionsMap.get(lobbyId);
        sessions.remove(session);
        sessionsMap.put(lobbyId, sessions);

        Long gameId = null;
        try {
            gameId = lobbyService.getGameIdFromLobbyId(lobbyId);
        } catch(Exception ignored) {
        }

        if(gameId != null) {
            timerCloseSessions(lobbyId, gameId);
        } else {
            timerRemoveUserFromLobby(lobbyId, userId);
        }

        System.out.println("-----");
        printSessionsMap();
        printActiveSessions();
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

        //nedim-j: add logic which checks that the same user rejoined
        /*
        if (sessionsMap.containsKey(lobbyId) && sessionsMap.get(lobbyId).size() >= 2) {
            timer.cancel(); // Cancel the timer
            System.out.println("User rejoined!");
            webSocketMessenger.sendMessage("/games/"+gameId, "user-rejoined", timeoutThreshold);
        }

         */

    }


    /*
    public void timerRemoveUserFromLobby(Long lobbyId, Long userId) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                synchronized (sessionsMap) {
                    if (!sessionsMap.containsKey(lobbyId) || sessionsMap.get(lobbyId).size() < 2) {
                        System.out.println("User left by closing session!");
                        try {
                            lobbyService.removeUserFromLobby(lobbyId, userId);
                        } catch (Exception e) {
                            System.out.println(e);
                        }
                    }
                }
            }
        }, 3 * 1000);

        scheduler.schedule(() -> {
            synchronized (sessionsMap) {
                if (sessionsMap.containsKey(lobbyId) && sessionsMap.get(lobbyId).size() >= 2) {
                    timer.cancel();
                }
            }
        }, 1, TimeUnit.SECONDS);
    }

    public void timerCloseSessions(Long lobbyId, Long gameId) {
        webSocketMessenger.sendMessage("/games/" + gameId, "user-timeout", timeoutThreshold);
        System.out.println("User timed out!");

        Runnable task = () -> {
            synchronized (sessionsMap) {
                if (!sessionsMap.containsKey(lobbyId) || sessionsMap.get(lobbyId).size() < 2) {
                    System.out.println("User disconnected!");
                    webSocketMessenger.sendMessage("/games/" + gameId, "user-disconnected", timeoutThreshold);
                    closeSessionsOfLobbyId(lobbyId);
                } else {
                    System.out.println("User rejoined! Canceling timers for lobby: " + lobbyId);
                    cancelTimer();
                    webSocketMessenger.sendMessage("/games/" + gameId, "user-rejoined", timeoutThreshold);
                }
            }
        };
        currentTask = scheduler.schedule(task, timeoutThreshold, TimeUnit.SECONDS);

        scheduleCheckSessions(lobbyId, gameId);
    }

    private void cancelTimer() {
        if (currentTask != null && !currentTask.isDone()) {
            currentTask.cancel(false);
        }
    }

    private void scheduleCheckSessions(Long lobbyId, Long gameId) {
        if (currentTask != null && !currentTask.isDone()) {
            currentTask.cancel(false);
        }

        currentTask = scheduler.scheduleAtFixedRate(() -> {
            synchronized (sessionsMap) {
                List<WebSocketSession> sessions = sessionsMap.get(lobbyId);
                if (sessions != null && sessions.size() >= 2) {
                    System.out.println("User rejoined! Canceling timers for lobby: " + lobbyId);
                    currentTask.cancel(false);
                    webSocketMessenger.sendMessage("/games/" + gameId, "user-rejoined", timeoutThreshold);
                }
            }
        }, 0, 1, TimeUnit.SECONDS);
    }

     */

    /*
    @Scheduled(fixedRate = 1000)
    public void checkSessions() {
        synchronized (sessionsMap) {
            for (Map.Entry<Long, List<WebSocketSession>> entry : sessionsMap.entrySet()) {
                Long lobbyId = entry.getKey();
                List<WebSocketSession> sessions = entry.getValue();

                Long gameId = null;
                try {
                    gameId = lobbyService.getGameIdFromLobbyId(lobbyId);
                } catch(Exception ignored) {
                }

                if (gameId != null && sessions.size() >= 2) {
                    System.out.println("User rejoined! Canceling timers for lobby: " + lobbyId);
                    scheduler.shutdownNow();
                    webSocketMessenger.sendMessage("/games/" + lobbyId, "user-rejoined", timeoutThreshold);

                }
            }
        }
    }

     */


    public void closeSessionsOfLobbyId(Long lobbyId) {
        List<WebSocketSession> sessions = sessionsMap.get(lobbyId);
        for(WebSocketSession session : sessions) {
            try {
                session.close();
                sessions.remove(session);
            } catch (Exception e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Could not close session " +
                        session + " | "+ e);
            }
        }

        sessionsMap.put(lobbyId, sessions);

        if(sessionsMap.get(lobbyId) == null) {
            sessionsMap.remove(lobbyId);
            System.out.println("Closed sessions for ID: " + lobbyId);

            //nedim-j: delete lobby

        } else {
            System.out.println("Error in closing sessions for ID: " + lobbyId);
        }
    }

    public Map<Long, List<WebSocketSession>> getSessionsMap() {
        return sessionsMap;
    }


    public void printSessionAttributes(String sessionId) {
        WebSocketSession session = activeSessions.get(sessionId);
        System.out.println("Session attributes: " +session.getAttributes());
    }


    //nedim-j: for debugging
    public void printSessionsMap() {
        System.out.println("Sessions Map:");
        for (Map.Entry<Long, List<WebSocketSession>> entry : sessionsMap.entrySet()) {
            Long lobbyId = entry.getKey();
            List<WebSocketSession> sessionsList = entry.getValue();

            System.out.println("  Lobby/Game ID: " + lobbyId + ":");
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
