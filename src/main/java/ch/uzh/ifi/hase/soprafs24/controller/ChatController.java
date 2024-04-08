package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.entity.Chat;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.rest.dto.MessageGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.MessagePostDTO;
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

  @PostMapping("/game/{gameId}") // smailalijagic: use gameId to check if a game exists
  @ResponseStatus(HttpStatus.CREATED)
  @ResponseBody
  public String addMessage(@RequestBody MessagePostDTO messagePostDTO, @PathVariable("gameId") String id) {
    Long gameid = Long.valueOf(id); // smailalijagic: added
    // assert GameService.checkIfGameExists(gameId); // smailalijagic: how to check this???
    Chat message = DTOMapper.INSTANCE.convertMessagePostDTOtoEntity(messagePostDTO);
    chatService.addMessage(message.toString());
    return message.toString(); // smailalijagic: needed?
  }

  @GetMapping("/game/{gameId}")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public List<MessageGetDTO> getAllMessages(@PathVariable("gameId") Long id) {
    Long gameid = Long.valueOf(id); // smailalijagic: added
    // assert GameService.checkIfGameExists(gameId); // smailalijagic: how to check this???
    // fetch all users in the internal representation
    List<Chat> messages = chatService.getMessages();
    List<MessageGetDTO> messageGetDTOs = new ArrayList<>();

    // convert each user to the API representation
    for (Chat message : messages) {
      messageGetDTOs.add(DTOMapper.INSTANCE.convertEntityToMessageGetDTO(message));
    }

    return messageGetDTOs;
  }
}

