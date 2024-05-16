package ch.uzh.ifi.hase.soprafs24.websocket;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.util.MultiValueMap;
import org.springframework.web.socket.WebSocketSession;

import java.util.*;

public class WebSocketChannelInterceptor implements ChannelInterceptor {

    private final WebSocketSessionService sessionService = WebSocketSessionService.getInstance();

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (accessor != null && accessor.getCommand() == StompCommand.CONNECT) {
            String userId = accessor.getFirstNativeHeader("userId");
            String sessionId = accessor.getSessionId();

            sessionService.addUserIdToActiveSession(sessionId, userId);
            //sessionService.printSessionAttributes(sessionId);
        }

        return message;
    }


}
