package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.entity.*;
import ch.uzh.ifi.hase.soprafs24.rest.dto.*;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs24.service.GameService;
import ch.uzh.ifi.hase.soprafs24.service.GameUserService;
import ch.uzh.ifi.hase.soprafs24.service.LobbyService;
import ch.uzh.ifi.hase.soprafs24.websocket.WebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class GameController {
  private final GameService gameService;
  private final GameUserService gameUserService;
  private final WebSocketHandler webSocketHandler;

  @Autowired
  GameController(GameService gameService, GameUserService gameUserService, WebSocketHandler webSocketHandler) {
    this.gameService = gameService;
    this.gameUserService = gameUserService;
    this.webSocketHandler = webSocketHandler;
  }

  @GetMapping("/games")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public List<Game> getGames() {
    return gameService.getGames();
  }

  @GetMapping("/games/{gameId}")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public Game getGame(@PathVariable("gameId") String id) {
    Long gameid = Long.valueOf(id);
    return gameService.getGame(gameid);
  }

  @PostMapping("/game/{lobbyid}/start")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public Game createGame(@PathVariable("lobbyid") Long lobbyid, @RequestBody GamePostDTO gamePostDTO) {
    // smailalijagic:
    // 1. correct Lobby, till: gameid is not created yet, compare with lobbyid
    // 2. User1 online?
    // 3. User2 online?
    // 4. remove lobby
    // 5. load game --> game logic (follows)
    Game game = DTOMapper.INSTANCE.convertGamePostDTOtoEntity(gamePostDTO);
    //nedim-j: made new game to return in pusher, feel free to adjust
    Game createdGame = gameService.creategame(lobbyid, game);

    webSocketHandler.sendMessage("/lobbies/"+lobbyid, "game-started", createdGame);

    return createdGame;
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
    Player player = gameUserService.getPlayer(playerid);
    return player;
  }

  @PutMapping("/game/character/choose")
  @ResponseStatus(HttpStatus.ACCEPTED)
  @ResponseBody
  public void chooseImage(@RequestBody GuessPostDTO guessPostDTO){
    // till:
    // 1. ImageID exists?
    // 2. chosencharacter still null?
    Guess guess = DTOMapper.INSTANCE.convertGuessPostDTOtoEntity(guessPostDTO);
    Response response = gameService.chooseImage(guess);

    webSocketHandler.sendMessage("/games/"+guess.getGameId(), "round-update", response);

    /*
    Guess guess = DTOMapper.INSTANCE.convertGuessPostDTOtoEntity(guessPostDTO);
    Game game = gameService.getGame(guess.getGameId());
    Long creatorId = game.getCreatorId();
    Long invitedId = game.getInvitedPlayerId();
    if(!gameService.playerHasSelected(guess.getPlayerId())) {
        gameService.selectimage(guess);
        //if(gameService.playerHasSelected(creatorId) && gameService.playerHasSelected(invitedId)) {

            String message = "Player " + guess.getPlayerId() + " has chosen character " + guess.getImageId();
            pusher.trigger(channelName, "round-update", message);
        //}
    }
    else {
        throw new RuntimeException("You have already chosen a character");
    }
     */
  }

  @PostMapping("/game/character/guess")
  @ResponseStatus(HttpStatus.ACCEPTED)
  @ResponseBody
  public Response guessImage(@RequestBody GuessPostDTO guessPostDTO){
    Guess guess = DTOMapper.INSTANCE.convertGuessPostDTOtoEntity(guessPostDTO);
    Response response = gameService.guesssimage(guess);

    webSocketHandler.sendMessage("/games/"+guess.getGameId(), "round-update", response);
    return response;
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



    // Endpoints regarding game-specific images
    @GetMapping("/games/{gameId}/images")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<ImageDTO> getGameImages(@PathVariable Long gameId) {

        return gameService.getGameImages(gameId);
    }
    @PostMapping("/games/{gameId}/images")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public void saveGameImages(@PathVariable Long gameId) {
        gameService.saveGameImages(gameId, 20);
    }
    @DeleteMapping("/games/{gameId}/images/{imageId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void deleteGameImage(@PathVariable Long gameId, @PathVariable Long imageId) {
      gameService.deleteGameImage(gameId, imageId);
    }

    @GetMapping("/games/{gameId}/history/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public GameHistory getGameHistory(@PathVariable Long gameId, Long userId){
      return gameService.getGameHistory(gameId, userId);

    }


}