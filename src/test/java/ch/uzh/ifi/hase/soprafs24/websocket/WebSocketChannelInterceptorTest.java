package ch.uzh.ifi.hase.soprafs24.websocket;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.GenericMessage;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class WebSocketChannelInterceptorTest {

    private WebSocketChannelInterceptor webSocketChannelInterceptor;
    private WebSocketSessionService webSocketSessionService;
    private Message<?> message;
    private MessageChannel messageChannel;

    @BeforeEach
    void setUp() {
        webSocketSessionService = Mockito.mock(WebSocketSessionService.class);
        webSocketChannelInterceptor = new WebSocketChannelInterceptor();
        webSocketChannelInterceptor.webSocketSessionService = webSocketSessionService;

        messageChannel = mock(MessageChannel.class);
    }

    @Test
    void preSend_whenNonConnectCommand_noAction() {
        StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.DISCONNECT);
        accessor.setSessionId("sessionId");
        accessor.setNativeHeader("userId", "userId");

        Map<String, Object> headers = new HashMap<>(accessor.toMap());
        message = new GenericMessage<>(new byte[0], headers);

        webSocketChannelInterceptor.preSend(message, messageChannel);

        verify(webSocketSessionService, never()).addUserIdToActiveSession(anyString(), anyString());
    }

    @Test
    void preSend_whenNullAccessor_noAction() {
        message = new GenericMessage<>(new byte[0]);

        Message<?> result = webSocketChannelInterceptor.preSend(message, messageChannel);

        assertEquals(message, result);
        verify(webSocketSessionService, never()).addUserIdToActiveSession(anyString(), anyString());
    }
}