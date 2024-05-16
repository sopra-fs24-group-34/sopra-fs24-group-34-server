package ch.uzh.ifi.hase.soprafs24.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.ExceptionWebSocketHandlerDecorator;

public class WebSocketCustomHandler extends ExceptionWebSocketHandlerDecorator {

    @Autowired
    private WebSocketSessionService sessionService = new WebSocketSessionService();

    public WebSocketCustomHandler(WebSocketHandler delegate) {
        super(delegate);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        System.out.println("CONNECTION ESTABLISHED: " + session);
        sessionService.addActiveSession(session);
        super.afterConnectionEstablished(session);
    }
}
