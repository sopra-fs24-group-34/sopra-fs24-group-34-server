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
        registry.addEndpoint("/ws").setAllowedOrigins("http://localhost:3000").withSockJS();
        registry.addEndpoint("/websocket-demo").setAllowedOrigins("http://localhost:3000").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // smailalijagic: Configuring message broker to enable broadcasting to "/topic"
        registry.enableSimpleBroker("/topic", "/games", "/lobbies", "/chat");
        registry.setApplicationDestinationPrefixes("/app");
    }

    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/websocket-demo").allowedOrigins("http://localhost:3000").allowedMethods("*").allowCredentials(true);
    }

    /*
    //nedim-j: don't know if we can do this dynamically. "/lobbies" (above) seems to be enough though, pls verify
    public void createLobbyRegistry (Long lobbyId) {
        String lobbyEndpoint = "/lobbies/" + lobbyId;
        MessageBrokerRegistry registry = new MessageBrokerRegistry();
        registry.enableSimpleBroker(lobbyEndpoint);
    }
     */

}
