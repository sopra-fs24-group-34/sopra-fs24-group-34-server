package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.entity.Chat;
import ch.uzh.ifi.hase.soprafs24.entity.Game;
import ch.uzh.ifi.hase.soprafs24.rest.dto.MessageGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.MessagePostDTO;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs24.service.ChatServiceWebSockets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ChatControllerWebSockets {
    private final ChatServiceWebSockets chatServiceWebSockets;
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public ChatControllerWebSockets(ChatServiceWebSockets chatServiceWebSockets, SimpMessagingTemplate messagingTemplate) {
        this.chatServiceWebSockets = chatServiceWebSockets;
        this.messagingTemplate = messagingTemplate;
    }

    @PostMapping("/game/{gameId}/chat/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void addMessage(@RequestBody String message, @PathVariable("gameId") String gameId, @PathVariable("userId") String userId) {
        Long gameIdLong = Long.valueOf(gameId);
        Long userIdLong = Long.valueOf(userId);

        MessagePostDTO messagePostDTO = new MessagePostDTO();
        messagePostDTO.setMessage(message);

        Chat chat = DTOMapper.INSTANCE.convertMessagePostDTOtoEntity(messagePostDTO);

        MessageGetDTO messageGetDTO = chatServiceWebSockets.addMessage(chat, userIdLong, gameIdLong);
        String destination = "/topic/gameRound/" + gameIdLong; // smailalijagic: search chat in here
        messagingTemplate.convertAndSend(destination, messageGetDTO); // smailalijagic: message is sent
    }

    @GetMapping("/game/{gameId}/chat")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public MessageGetDTO getAllMessages(@PathVariable("gameId") String id) {
        // smailalijagic: get gameId
        Long gameid = Long.valueOf(id); // smailalijagic: added
        // smailalijagic: get Game --> get chat
        Game game = chatServiceWebSockets.getGameByGameId(gameid);
        // smailalijagic: get Chat
        Chat chat = game.getChat();
        // smailalijagic: return api repr of chat
        return DTOMapper.INSTANCE.convertEntityToMessageGetDTO(chat);
    }


    @GetMapping("/chats")
    public List<Chat> getAllChats() {
        return chatServiceWebSockets.getAllChats();
    }

    @GetMapping("chats/{chatId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Chat getChat(@PathVariable("chatId") String id) {
        Long chatid = Long.valueOf(id);
        return chatServiceWebSockets.getChat(chatid);
    }
}
