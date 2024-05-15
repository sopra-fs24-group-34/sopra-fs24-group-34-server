package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.entity.Chat;
import ch.uzh.ifi.hase.soprafs24.entity.ChatTuple;
import ch.uzh.ifi.hase.soprafs24.entity.Game;
import ch.uzh.ifi.hase.soprafs24.rest.dto.MessageGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.MessagePostDTO;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs24.service.ChatServiceWebSockets;
import ch.uzh.ifi.hase.soprafs24.websocket.WebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import com.google.gson.*;

import java.util.List;

@RestController
public class ChatControllerWebSockets {
    private final ChatServiceWebSockets chatServiceWebSockets;
    private final SimpMessagingTemplate messagingTemplate;
    private final Gson gson = new Gson();
    private WebSocketHandler webSocketHandler;

    @Autowired
    public ChatControllerWebSockets(ChatServiceWebSockets chatServiceWebSockets, SimpMessagingTemplate messagingTemplate, WebSocketHandler webSocketHandler) {
        this.chatServiceWebSockets = chatServiceWebSockets;
        this.messagingTemplate = messagingTemplate;
        this.webSocketHandler = webSocketHandler;
    }

    @MessageMapping("/sendMessage")
    public void addMessage(String stringJsonRequest) {
        try {
            //nedim-j: search/create an own decorator for string parsing maybe
            JsonParser parser = new JsonParser();
            JsonObject jsonObject = parser.parse(stringJsonRequest).getAsJsonObject();

            String message = jsonObject.get("message").getAsString();
            Long gameId = Long.parseLong(jsonObject.get("gameId").getAsString());
            Long userId = Long.parseLong(jsonObject.get("userId").getAsString());

            Game game = chatServiceWebSockets.getGameByGameId(gameId);

            Chat chat = chatServiceWebSockets.addMessage(message, userId, gameId);

            MessageGetDTO messageGetDTO = DTOMapper.INSTANCE.convertEntityToMessageGetDTO(chat);

            //MessageGetDTO messageGetDTO = chatServiceWebSockets.addMessage(message, userId, gameId);

            String destination = "/games/" + gameId + "/chat"; // smailalijagic: search chat in here
            //messagingTemplate.convertAndSend(destination, messageGetDTO); // smailalijagic: last message is sent
            webSocketHandler.sendMessage(destination, "chat-message", messageGetDTO);

            chatServiceWebSockets.updateGameChat(game, chat);

        } catch(Exception e) {
            System.out.println("Something went wrong with chat: "+e);
        }
    }

    @GetMapping("/games/{gameId}/chat")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<String> getAllMessages(@PathVariable("gameId") Long gameId) {
        return chatServiceWebSockets.getAllMessages(gameId);
    }
}
