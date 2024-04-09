package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.entity.Lobby;
import ch.uzh.ifi.hase.soprafs24.rest.dto.LobbyPostDTO;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs24.service.LobbyService;
import net.bytebuddy.asm.Advice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class LobbyController {

    private final LobbyService LobbyService;

    public LobbyController(LobbyService lobbyService) {
        this.LobbyService = lobbyService;
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }


  @PostMapping("/lobbies/create/{userId}")
  @ResponseStatus(HttpStatus.CREATED)
  @ResponseBody
  public Lobby createLobby(@PathVariable("userId") Long userId) {

     return LobbyService.createlobby(userId);
  }

  @PutMapping("/lobbies/settings/{lobbyId}")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public void updateLobby(@PathVariable("lobbyId") String lobbyId) {
    // smailalijagic: change lobby settings
  }

  @DeleteMapping("/lobbies/{lobbyId}/delete")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public void deleteLobby(@PathVariable("lobbyId") String lobbyid) {
    // smailalijagic: when a game is started lobby should be automatically deleted
  }





}
