package ch.uzh.ifi.hase.soprafs24.websocket;

import ch.uzh.ifi.hase.soprafs24.websocket.WebSocketMessenger;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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

    @Test
    public void testSendMessage_withObjectData() {
        String destination = "/topic/test";
        String eventType = "test-event";
        TestData data = new TestData("testData");

        webSocketMessenger.sendMessage(destination, eventType, data);

        ArgumentCaptor<String> destinationCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
        verify(messagingTemplate).convertAndSend(destinationCaptor.capture(), messageCaptor.capture());

        assertEquals(destination, destinationCaptor.getValue());

        JsonObject messageJson = JsonParser.parseString(messageCaptor.getValue()).getAsJsonObject();
        assertEquals(eventType, messageJson.get("event-type").getAsString());

        JsonObject dataJson = messageJson.get("data").getAsJsonObject();
        assertEquals("testData", dataJson.get("data").getAsString());
    }

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

    private static class TestData {
        private final String data;

        public TestData(String data) {
            this.data = data;
        }

        public String getData() {
            return data;
        }
    }
}