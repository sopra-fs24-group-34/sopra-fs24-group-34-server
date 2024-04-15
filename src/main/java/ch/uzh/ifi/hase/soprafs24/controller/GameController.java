package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.entity.Lobby;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

public class GameController {
  @PostMapping("/game/{gameId}/start")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public void createGame(@PathVariable("gameId") String gameid, User user1, User user2, Lobby lobby) {
    // smailalijagic:
    // 1. correct Lobby
    // 2. User1 online?
    // 3. User2 online?
    // 4. remove lobby
    // 5. load game --> game logic (follows)
  }

  @PutMapping("/game/{gameId}/select")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @ResponseBody
  public Boolean playGame(@PathVariable("gameId") String gameid, User user1, User user2) {
    // smailalijagic:
    // correct guess --> return true --> deleteGame(...)
    // false guess --> return false and decrease total guesses by 1
    return true;
  }

  @PostMapping("/game/{gameId}/pick")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public Boolean pickChar(@PathVariable("gameId") String gameId, String charURL) {
    // smailalijagic:
    // 1. pick char
    // 2. return true
    return true;
  }

  @DeleteMapping("/game/{gameId}/delete")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public void deleteGame(@PathVariable("gameId") String gameid, User user1, User user2) {
    // smailalijagic:
    // delete game from database
    // delete chat from database
    // update stats
    // load new page
  }


}
