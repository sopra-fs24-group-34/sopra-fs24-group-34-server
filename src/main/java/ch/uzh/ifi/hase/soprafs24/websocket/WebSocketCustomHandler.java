package ch.uzh.ifi.hase.soprafs24.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
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
        System.out.println("CONNECTION ESTABLISHED: " + session);
        sessionService.addActiveSession(session);
        super.afterConnectionEstablished(session);
    }
}
