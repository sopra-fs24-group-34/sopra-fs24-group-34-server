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

//    @PostMapping("/game/{gameId}/chat/{userId}")
//    @ResponseStatus(HttpStatus.OK)
//    public void addMessage(@RequestBody String message, @PathVariable("gameId") String gameId, @PathVariable("userId") String userId) {
//        Long gameIdLong = Long.valueOf(gameId);
//        Long userIdLong = Long.valueOf(userId);
//
//        MessagePostDTO messagePostDTO = new MessagePostDTO();
//        messagePostDTO.setMessage(message);
//
//        Chat chat = DTOMapper.INSTANCE.convertMessagePostDTOtoEntity(messagePostDTO);
//
//        MessageGetDTO messageGetDTO = chatServiceWebSockets.addMessage(chat, userIdLong, gameIdLong);
//        String destination = "/topic/game/" + gameIdLong + "/chat/" + chat.getId(); // smailalijagic: search chat in here
//        messagingTemplate.convertAndSend(destination, messageGetDTO); // smailalijagic: message is sent
//    }


    @MessageMapping("/sendMessage")
    public void addMessage(String stringJsonRequest) {
        try {
            //nedim-j: search/create an own decorator for string parsing maybe
            JsonParser parser = new JsonParser();
            JsonObject jsonObject = parser.parse(stringJsonRequest).getAsJsonObject();

            String message = jsonObject.get("message").getAsString();
            Long gameId = Long.parseLong(jsonObject.get("gameId").getAsString());
            Long userId = Long.parseLong(jsonObject.get("userId").getAsString());

            //MessagePostDTO messagePostDTO = new MessagePostDTO();
            //messagePostDTO.setMessage(message);

        //Chat chat = DTOMapper.INSTANCE.convertMessagePostDTOtoEntity(messagePostDTO);

            MessageGetDTO messageGetDTO = chatServiceWebSockets.addMessage(message, userId, gameId);

            String destination = "/games/" + gameId + "/chat"; // smailalijagic: search chat in here
            //messagingTemplate.convertAndSend(destination, messageGetDTO); // smailalijagic: last message is sent
            webSocketHandler.sendMessage(destination, "chat-message", messageGetDTO);
        } catch(Exception e) {
            System.out.println("Something went wrong with chat: "+e);
        }
    }

    @GetMapping("/game/{gameId}/chat")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<ChatTuple> getAllMessages(@PathVariable("gameId") Long gameId) {
        // smailalijagic: get Game --> get chat
        Game game = chatServiceWebSockets.getGameByGameId(gameId);
        // smailalijagic: get Chat
        Chat chat = game.getChat();
        // smailalijagic: return all messages
        return chat.getMessages();
    }


//    @GetMapping("/chats")
//    public List<Chat> getAllChats() {
//        return chatServiceWebSockets.getAllChats();
//    }
//
//    @GetMapping("chats/{chatId}")
//    @ResponseStatus(HttpStatus.OK)
//    @ResponseBody
//    public Chat getChat(@PathVariable("chatId") String id) {
//        Long chatid = Long.valueOf(id);
//        return chatServiceWebSockets.getChat(chatid);
//    }
}
