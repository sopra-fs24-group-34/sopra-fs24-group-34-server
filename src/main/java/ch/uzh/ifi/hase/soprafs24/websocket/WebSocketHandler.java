package ch.uzh.ifi.hase.soprafs24.websocket;

import ch.uzh.ifi.hase.soprafs24.entity.Lobby;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import com.google.gson.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Service
public class WebSocketHandler extends TextWebSocketHandler {

    private final Gson gson = new Gson();
    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketHandler(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void handleLobbyJoin(Lobby lobby, User user) {
        Long lobbyId = lobby.getLobbyid();
        UserGetDTO u = DTOMapper.INSTANCE.convertEntityToUserGetDTO(user);
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