package ch.uzh.ifi.hase.soprafs24.websocket;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

@Component
public class WebSocketChannelInterceptor implements ChannelInterceptor {

    WebSocketSessionService webSocketSessionService = WebSocketSessionService.getInstance();

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (accessor != null && accessor.getCommand() == StompCommand.CONNECT) {
            String userId = accessor.getFirstNativeHeader("userId");
            String sessionId = accessor.getSessionId();

            webSocketSessionService.addUserIdToActiveSession(sessionId, userId);
        }

        return message;
    }


}
