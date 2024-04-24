package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.entity.ChatTuple;
import ch.uzh.ifi.hase.soprafs24.entity.Chat;
import ch.uzh.ifi.hase.soprafs24.entity.Game;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.rest.dto.MessageGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.MessagePostDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs24.service.ChatService;
import ch.uzh.ifi.hase.soprafs24.service.GameService;
import com.pusher.rest.Pusher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ChatController {
  private final ChatService chatService;
  private final Pusher pusher;

  @Autowired
  public ChatController(ChatService chatService, Pusher pusher) {
    this.chatService = chatService;
    this.pusher = pusher;
  }

  @PostMapping("/game/{gameId}/chat/{userId}") // smailalijagic: use gameId to check if a game exists
  @ResponseStatus(HttpStatus.CREATED)
  @ResponseBody
  public MessageGetDTO addMessage(@RequestBody String message, @PathVariable("gameId") String game_id, @PathVariable("userId") String user_id) {
    Long gameid = Long.valueOf(game_id); // smailalijagic: get gameId
    //nedim-j: changed this and Requestbody to string, feel free to adjust
    MessagePostDTO m = new MessagePostDTO();
    m.setMessage(message);

    Chat chat = DTOMapper.INSTANCE.convertMessagePostDTOtoEntity(m); // smailalijagic: convert api representation to entity
    Long userid = Long.valueOf(user_id); // smailalijagic: get userId
    chatService.addMessage(chat, userid, gameid); // smailalijagic: add message and userid to chat that belongs to game with gameid XYZ

    // smailalijagic: trigger a Pusher event to notify clients about the new chat message
    pusher.trigger("chat_channel", "new_message", chat.getLastmessage());

    return DTOMapper.INSTANCE.convertEntityToMessageGetDTO(chat); // smailalijagic: return api representation of chat

  }

  @GetMapping("chats")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public List<Chat> getChats() {
    return chatService.getChats();
  }

  @GetMapping("chats/{chatId}")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public Chat getChat(@PathVariable("chatId") String id) {
    Long chatid = Long.valueOf(id);
    return chatService.getChat(chatid);
  }

  @GetMapping("/game/{gameId}/chat")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public MessageGetDTO getAllMessages(@PathVariable("gameId") String id) {
    // smailalijagic: get gameId
    Long gameid = Long.valueOf(id); // smailalijagic: added
    // smailalijagic: get Game --> get chat
    Game game = chatService.getGame(gameid);
    // smailalijagic: get Chat
    Chat chat = game.getChat();
    // smailalijagic: return api repr of chat
    return DTOMapper.INSTANCE.convertEntityToMessageGetDTO(chat);
  }
}

