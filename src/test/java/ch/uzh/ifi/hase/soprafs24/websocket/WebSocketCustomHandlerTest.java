package ch.uzh.ifi.hase.soprafs24.websocket;

import ch.uzh.ifi.hase.soprafs24.websocket.WebSocketCustomHandler;
import ch.uzh.ifi.hase.soprafs24.websocket.WebSocketSessionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;

import static org.mockito.Mockito.*;

class WebSocketCustomHandlerTest {

    private WebSocketCustomHandler webSocketCustomHandler;
    private WebSocketSessionService webSocketSessionService;
    private WebSocketHandler webSocketHandler;
    private WebSocketSession webSocketSession;

    @BeforeEach
    void setUp() {
        webSocketSessionService = Mockito.mock(WebSocketSessionService.class);
        webSocketHandler = Mockito.mock(WebSocketHandler.class);
        webSocketCustomHandler = new WebSocketCustomHandler(webSocketHandler);
        webSocketCustomHandler.webSocketSessionService = webSocketSessionService;
        webSocketSession = Mockito.mock(WebSocketSession.class);
    }

    @Test
    void afterConnectionEstablished_success() throws Exception {
        webSocketCustomHandler.afterConnectionEstablished(webSocketSession);

        verify(webSocketSessionService, times(1)).addActiveSession(webSocketSession);
        verify(webSocketHandler, times(1)).afterConnectionEstablished(webSocketSession);
    }

    @Test
    void afterConnectionClosed_success() throws Exception {
        CloseStatus closeStatus = CloseStatus.NORMAL;

        webSocketCustomHandler.afterConnectionClosed(webSocketSession, closeStatus);

        verify(webSocketSessionService, times(1)).handleDisconnectedSession(webSocketSession);
        verify(webSocketHandler, times(1)).afterConnectionClosed(webSocketSession, closeStatus);
    }
}