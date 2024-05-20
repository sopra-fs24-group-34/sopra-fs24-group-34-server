package ch.uzh.ifi.hase.soprafs24.websocket;

import ch.uzh.ifi.hase.soprafs24.websocket.WebSocketMessenger;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class WebSocketMessengerTest {

    private WebSocketMessenger webSocketMessenger;
    private SimpMessagingTemplate messagingTemplate;

    @BeforeEach
    void setUp() {
        messagingTemplate = Mockito.mock(SimpMessagingTemplate.class);
        webSocketMessenger = new WebSocketMessenger(messagingTemplate);
    }

//    @Test
//    void sendMessage_withObjectData_success() {
//        String destination = "/topic/test";
//        String eventType = "test-event";
//        Object data = new TestData("test", 123);
//
//        webSocketMessenger.sendMessage(destination, eventType, data);
//
//        ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
//        verify(messagingTemplate, times(1)).convertAndSend(eq(destination), messageCaptor.capture());
//
//        String capturedMessage = messageCaptor.getValue();
//        JsonObject expectedJson = new JsonObject();
//        expectedJson.addProperty("event-type", eventType);
//        expectedJson.add("data", webSocketMessenger.getGson().toJsonTree(data));
//
//        assertEquals(expectedJson.toString(), capturedMessage);
//    }

    @Test
    void sendMessage_withStringData_success() {
        String destination = "/topic/test";
        String eventType = "test-event";
        String data = "test data";

        webSocketMessenger.sendMessage(destination, eventType, data);

        ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
        verify(messagingTemplate, times(1)).convertAndSend(eq(destination), messageCaptor.capture());

        String capturedMessage = messageCaptor.getValue();
        JsonObject expectedJson = new JsonObject();
        expectedJson.addProperty("event-type", eventType);
        expectedJson.addProperty("data", data);

        assertEquals(expectedJson.toString(), capturedMessage);
    }

    static class TestData {
        private String name;
        private int value;

        public TestData(String name, int value) {
            this.name = name;
            this.value = value;
        }

        // Getters and setters (if needed)
    }
}