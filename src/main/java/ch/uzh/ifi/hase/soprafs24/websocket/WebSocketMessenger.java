package ch.uzh.ifi.hase.soprafs24.websocket;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class WebSocketMessenger {

    private final Gson gson = new Gson();
    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketMessenger(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendMessage(String destination, String event_type, Object data) {
        JsonObject messageJson = new JsonObject();
        messageJson.addProperty("event-type", event_type);
        messageJson.add("data", gson.toJsonTree(data));
        String message = gson.toJson(messageJson);
        messagingTemplate.convertAndSend(destination, message);
    }

    public void sendMessage(String destination, String event_type, String data) {
        JsonObject messageJson = new JsonObject();
        messageJson.addProperty("event-type", event_type);
        messageJson.addProperty("data", data);
        String message = gson.toJson(messageJson);
        messagingTemplate.convertAndSend(destination, message);
    }
}