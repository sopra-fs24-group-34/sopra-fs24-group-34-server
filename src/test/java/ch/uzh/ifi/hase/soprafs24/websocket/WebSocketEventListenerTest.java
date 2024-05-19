package ch.uzh.ifi.hase.soprafs24.websocket;

import ch.uzh.ifi.hase.soprafs24.websocket.WebSocketEventListener;
import ch.uzh.ifi.hase.soprafs24.websocket.WebSocketSessionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

class WebSocketEventListenerTest {

    private WebSocketEventListener webSocketEventListener;
    private WebSocketSessionService webSocketSessionService;

    @BeforeEach
    void setUp() {
        webSocketSessionService = Mockito.mock(WebSocketSessionService.class);
        webSocketEventListener = new WebSocketEventListener();
        webSocketEventListener.webSocketSessionService = webSocketSessionService;
    }

    @Test
    void handleWebSocketConnectListener_success() {
        SessionConnectedEvent event = mock(SessionConnectedEvent.class);
        webSocketEventListener.handleWebSocketConnectListener(event);
        // No specific behavior to verify, just ensuring no exceptions are thrown
    }

    @Test
    void handleWebSocketDisconnectListener_success() {
        String sessionId = "sessionId";
        SessionDisconnectEvent event = mock(SessionDisconnectEvent.class);
        when(event.getSessionId()).thenReturn(sessionId);

        webSocketEventListener.handleWebSocketDisconnectListener(event);

        verify(event, times(1)).getSessionId();
        // System.out.println verification is not needed as we don't test sysout in unit tests
    }

    @Test
    void handleWebSocketSubscribeListener_success() {
        String sessionId = "sessionId";
        String destination = "/topic/someTopic";

        SimpMessageHeaderAccessor accessor = SimpMessageHeaderAccessor.create();
        accessor.setSessionId(sessionId);
        accessor.setDestination(destination);

        Map<String, Object> headers = new HashMap<>(accessor.toMap());
        Message<byte[]> message = mock(Message.class);
        when(message.getHeaders()).thenReturn(new MessageHeaders(headers));

        SessionSubscribeEvent event = new SessionSubscribeEvent(this, message);

        webSocketEventListener.handleWebSocketSubscribeListener(event);

        verify(webSocketSessionService, times(1)).handleSubscription(destination, sessionId);
    }
}