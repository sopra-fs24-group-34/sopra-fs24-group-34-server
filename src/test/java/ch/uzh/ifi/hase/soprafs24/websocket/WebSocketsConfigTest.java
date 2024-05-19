//package ch.uzh.ifi.hase.soprafs24.websocket;
//
//import ch.uzh.ifi.hase.soprafs24.websocket.WebSocketChannelInterceptor;
//import ch.uzh.ifi.hase.soprafs24.websocket.WebSocketCustomHandler;
//import ch.uzh.ifi.hase.soprafs24.websocket.WebSocketsConfig;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.messaging.simp.config.ChannelRegistration;
//import org.springframework.messaging.simp.config.MessageBrokerRegistry;
//import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
//import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
//import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
//import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
//import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;
//
//import static org.mockito.Mockito.*;
//
//@SpringJUnitConfig(WebSocketsConfigTest.Config.class)
//class WebSocketsConfigTest {
//
//    @Autowired
//    private WebSocketsConfig webSocketsConfig;
//
//    private StompEndpointRegistry stompEndpointRegistry;
//    private MessageBrokerRegistry messageBrokerRegistry;
//    private WebSocketTransportRegistration webSocketTransportRegistration;
//    private ChannelRegistration channelRegistration;
//
//    @BeforeEach
//    void setUp() {
//        stompEndpointRegistry = Mockito.mock(StompEndpointRegistry.class);
//        messageBrokerRegistry = Mockito.mock(MessageBrokerRegistry.class);
//        webSocketTransportRegistration = Mockito.mock(WebSocketTransportRegistration.class);
//        channelRegistration = Mockito.mock(ChannelRegistration.class);
//    }
//
////    @Test
////    void registerStompEndpoints() {
////        webSocketsConfig.registerStompEndpoints(stompEndpointRegistry);
////
////        verify(stompEndpointRegistry, times(1)).addEndpoint("/ws");
////        verify(stompEndpointRegistry.addEndpoint("/ws"), times(1))
////                .setAllowedOriginPatterns("ws://localhost:*", "wss://localhost:*", "http://localhost:*", "https://localhost:*",
////                        "ws://sopra-fs24-group-34-*", "wss://sopra-fs24-group-34-*",
////                        "http://sopra-fs24-group-34-*", "https://sopra-fs24-group-34-*");
////        verify(stompEndpointRegistry.addEndpoint("/ws"), times(1)).withSockJS();
////    }
//
//    @Test
//    void configureMessageBroker() {
//        webSocketsConfig.configureMessageBroker(messageBrokerRegistry);
//
//        verify(messageBrokerRegistry, times(1)).enableSimpleBroker("/games", "/lobbies");
//        verify(messageBrokerRegistry, times(1)).setApplicationDestinationPrefixes("/app");
//    }
//
//    @Test
//    void configureWebSocketTransport() {
//        webSocketsConfig.configureWebSocketTransport(webSocketTransportRegistration);
//
//        verify(webSocketTransportRegistration, times(1)).addDecoratorFactory(any());
//    }
//
//    @Test
//    void configureClientInboundChannel() {
//        webSocketsConfig.configureClientInboundChannel(channelRegistration);
//
//        verify(channelRegistration, times(1)).interceptors(any(WebSocketChannelInterceptor.class));
//    }
//
//    @Configuration
//    @EnableWebSocketMessageBroker
//    static class Config implements WebSocketMessageBrokerConfigurer {
//
//        @Bean
//        public WebSocketsConfig webSocketsConfig() {
//            return new WebSocketsConfig();
//        }
//    }
//}