package ch.uzh.ifi.hase.soprafs24.websocket;

// WebSocketsConfig.java

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketsConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // smailalijagic: Registering WebSocket endpoint
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("ws://localhost:*", "wss://localhost:*", "http://localhost:*", "https://localhost:*",
                        "ws://sopra-fs24-group-34-*", "wss://sopra-fs24-group-34-*",
                        "http://sopra-fs24-group-34-*", "https://sopra-fs24-group-34-*")
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker( "/games", "/lobbies");
        registry.setApplicationDestinationPrefixes("/app");
    }

}
