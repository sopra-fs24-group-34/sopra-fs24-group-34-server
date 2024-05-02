package ch.uzh.ifi.hase.soprafs24;

// WebSocketsConfig.java

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketsConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // smailalijagic: Registering WebSocket endpoint
        registry.addEndpoint("/ws").setAllowedOriginPatterns("*").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // smailalijagic: Configuring message broker to enable broadcasting to "/topic"
        registry.enableSimpleBroker("/topic", "/game", "/chat", "/lobby");
        registry.setApplicationDestinationPrefixes("/app");
    }

    // smailalijagic: commented for the moment
//    @Bean
//    public TaskScheduler HBScheduler() {
//        // smailalijagic: verifies that connection is still working
//        return new ThreadPoolTaskScheduler();
//    }

}
