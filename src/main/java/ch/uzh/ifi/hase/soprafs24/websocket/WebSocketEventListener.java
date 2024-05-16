package ch.uzh.ifi.hase.soprafs24.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import java.util.Objects;


@Service
public class WebSocketEventListener {

    private final WebSocketSessionService sessionService = WebSocketSessionService.getInstance();

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        //System.out.println("New WebSocket Connection has been formed");
        //event.getSource();
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        String sessionId = event.getSessionId();
        System.out.println("Session Disconnected: " + sessionId + " | whole event: " + event);

    }

    @EventListener
    public void handleWebSocketSubscribeListener(SessionSubscribeEvent event) {
        String sessionId = Objects.requireNonNull(event.getMessage().getHeaders().get("simpSessionId")).toString();
        String destination = Objects.requireNonNull(event.getMessage().getHeaders().get("simpDestination")).toString();
        System.out.println("Subscription formed. SessionId: " + sessionId + " | Destination: " + destination);
        Long destinationId = Long.valueOf(destination.split("/")[2]);
        sessionService.mapActiveSessionToLobbyOrGame(destinationId, sessionId);
        sessionService.printSessionsMap();
        sessionService.printActiveSessions();
    }
}