package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.entity.Lobby;
import net.bytebuddy.asm.Advice;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

public class LobbyController {
  @PostMapping("/lobbies/create")
  @ResponseStatus(HttpStatus.CREATED)
  @ResponseBody
  public Lobby createLobby() {
    // smailalijagic: created lobby -> needs to return lobby which will be added to usergamelobbylist
    return null;
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
