package ch.uzh.ifi.hase.soprafs24.websocket;

import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.ExceptionWebSocketHandlerDecorator;

@Service
public class WebSocketCustomHandler extends ExceptionWebSocketHandlerDecorator {

    private final WebSocketSessionService sessionService = WebSocketSessionService.getInstance();

    public WebSocketCustomHandler(WebSocketHandler delegate) {
        super(delegate);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        System.out.println("-----------------------------------");
        System.out.println("CH Connection established: " + session);
        //System.out.println("CH Session attributes: " + session.getAttributes());
        //System.out.println("CH Session userId: " + session.getAttributes().get("userId"));
        //System.out.println("Session Handshake headers: " + session.get);
        sessionService.addActiveSession(session);
        super.afterConnectionEstablished(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessionService.handleDisconnectedSession(session);
        super.afterConnectionClosed(session, status);
    }
}
