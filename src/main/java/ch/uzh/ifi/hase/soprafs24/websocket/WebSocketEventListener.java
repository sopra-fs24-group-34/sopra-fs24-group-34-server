package ch.uzh.ifi.hase.soprafs24.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import java.util.Objects;


@Component
public class WebSocketEventListener {

    WebSocketSessionService webSocketSessionService = WebSocketSessionService.getInstance();

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

        webSocketSessionService.handleSubscription(destination, sessionId);
    }
}