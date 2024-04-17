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
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ChatController {
  private final ChatService chatService;

  public ChatController(ChatService chatService) {
    this.chatService = chatService;
  }

  @PostMapping("/game/{gameId}/chat/{userId}") // smailalijagic: use gameId to check if a game exists
  @ResponseStatus(HttpStatus.CREATED)
  @ResponseBody
  public void addMessage(@RequestBody MessagePostDTO messagePostDTO, @PathVariable("gameId") String game_id, @PathVariable("userId") String user_id) {
    // smailalijagic: get gameId
    Long gameid = Long.valueOf(game_id); // smailalijagic: added
    // smailalijagic: get message
    Chat chat = DTOMapper.INSTANCE.convertMessagePostDTOtoEntity(messagePostDTO); // smailalijagic: convert api representation to entity
    // smailalijagic: get userId
    Long userid = Long.valueOf(user_id);
    // smailalijagic: update chat
    chatService.addMessage(chat, userid, gameid); // smailalijagic: add message and userid to chat that belongs to game with gameid XYZ
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

