package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.entity.Guess;
import ch.uzh.ifi.hase.soprafs24.entity.Lobby;
import ch.uzh.ifi.hase.soprafs24.entity.Player;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.rest.dto.GuessPostDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserPutDTO;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs24.service.GameService;
import ch.uzh.ifi.hase.soprafs24.service.GameUserService;
import ch.uzh.ifi.hase.soprafs24.service.LobbyService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
public class GameController {

    private final GameService gameService;

    private final GameUserService gameUserService;

    GameController(GameService gameService, GameUserService gameUserService) {
        this.gameService = gameService;
        this.gameUserService = gameUserService;
    }




  @PostMapping("/game/{lobbyId}/start")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public void createGame(@PathVariable("lobbyid") Long lobbyid, @RequestBody Long user1id, Long user2id) {
      // smailalijagic:
      // 1. correct Lobby, till: gameid is not created yet, compare with lobbyid
      // 2. User1 online?
      // 3. User2 online?
      // 4. remove lobby
      // 5. load game --> game logic (follows)
      gameService.creategame(lobbyid, user1id, user2id);


  }

  @PostMapping("/player/{playerid}")
  @ResponseStatus(HttpStatus.CREATED)
  @ResponseBody
  public Player createplayer(@PathVariable ("playerid") Long playerid) {
      // method to create a player to check with Postman

      Player player = gameUserService.createplayer(playerid);

      return player;

    }

  @GetMapping("/player/{playerid}")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public Player getplayer(@PathVariable("playerid") Long playerid){
      // method to get a player to check with Postman
      Player player = gameUserService.getUser(playerid);

      return player;

  }





  @PostMapping("/game/character/choose")
  @ResponseStatus(HttpStatus.ACCEPTED)
  @ResponseBody
  public void chooseImage(@RequestBody Long imageId, Long playerId){
      // till:
      // 1. ImageID exists?
      // 2. chosencharacter still null?

      gameService.chooseimage(imageId, playerId);

  }
    /**
    @PostMapping("/game/character/guess")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @ResponseBody
    public Boolean guessImage(@RequestBody GuessPostDTO guessPostDTO){

        Guess guess = DTOMapper.INSTANCE.convertGuessPostDTOtoEntity(guessPostDTO);

        return gameService.guessimage(guess);
    }
    */


  @PutMapping("/game/{gameId}/select")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @ResponseBody
  public Boolean playGame(@PathVariable("gameId") String gameid, User user1, User user2) {
    // smailalijagic:
    // correct guess --> return true --> deleteGame(...)
    // false guess --> return false and decrease total guesses by 1
    return true;
  }

  @DeleteMapping("/game/{gameId}/delete")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public void deleteGame(@PathVariable("gameId") String gameid, User user1, User user2) {
    // smailalijagic:
    // delete game from database
    // update stats
    // load new page
  }


}
