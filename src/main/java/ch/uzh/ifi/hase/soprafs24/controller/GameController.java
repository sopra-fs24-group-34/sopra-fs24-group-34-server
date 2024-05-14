package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.entity.*;
import ch.uzh.ifi.hase.soprafs24.rest.dto.*;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs24.service.GameService;
import ch.uzh.ifi.hase.soprafs24.service.GameUserService;
import ch.uzh.ifi.hase.soprafs24.service.LobbyService;
import ch.uzh.ifi.hase.soprafs24.websocket.WebSocketHandler;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import com.google.gson.Gson;

@RestController
public class GameController {
  private final GameService gameService;
  private final GameUserService gameUserService;
  private final WebSocketHandler webSocketHandler;
  private final LobbyService lobbyService;
  private final Gson gson = new Gson();

  GameController(GameService gameService, GameUserService gameUserService, WebSocketHandler webSocketHandler, LobbyService lobbyService) {
    this.gameService = gameService;
    this.gameUserService = gameUserService;
    this.webSocketHandler = webSocketHandler;
        this.lobbyService = lobbyService;
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

  @MessageMapping("/startGame")
  public Game createGame(String stringJsonRequest) {
    // smailalijagic:
    // 1. correct Lobby, till: gameid is not created yet, compare with lobbyid
    // 2. User1 online?
    // 3. User2 online?
    // 4. remove lobby
    // 5. load game --> game logic (follows)
    Map<String, Object> requestMap = gson.fromJson(stringJsonRequest, Map.class);
    Long lobbyId = gson.fromJson(gson.toJson(requestMap.get("lobbyId")), Long.class);
    GamePostDTO gamePostDTO = gson.fromJson(gson.toJson(requestMap.get("gamePostDTO")), GamePostDTO.class);
    AuthenticationDTO authenticationDTO = gson.fromJson(gson.toJson(requestMap.get("authenticationDTO")), AuthenticationDTO.class);

    Game game = DTOMapper.INSTANCE.convertGamePostDTOtoEntity(gamePostDTO);

    Game createdGame = gameService.createGame(lobbyId, game, authenticationDTO);

    webSocketHandler.sendMessage("/lobbies/"+lobbyId, "game-started", createdGame);

    return createdGame;
  }

  @GetMapping("/player/{playerid}")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public Player getplayer(@PathVariable("playerid") Long playerid){
    // method to get a player to check with Postman
    Player player = gameUserService.getPlayer(playerid);
    return player;
  }

  @MessageMapping("/chooseImage")
  public void chooseImage(String stringJsonRequest){
    // till:
    // 1. ImageID exists?
    // 2. chosencharacter still null?

    Map<String, Object> requestMap = gson.fromJson(stringJsonRequest, Map.class);
    GuessPostDTO guessPostDTO = gson.fromJson(gson.toJson(requestMap.get("guessPostDTO")), GuessPostDTO.class);
    //nedim-j: use for round-basis, authentication:
    //AuthenticationDTO authenticationDTO = gson.fromJson(gson.toJson(requestMap.get("authenticationDTO")), AuthenticationDTO.class);

    Guess guess = DTOMapper.INSTANCE.convertGuessPostDTOtoEntity(guessPostDTO);
    Response response = gameService.chooseImage(guess);

    webSocketHandler.sendMessage("/games/"+guess.getGameId(), "round-update", response);

  }

  @MessageMapping("/guessImage")
  public Response guessImage(String stringJsonRequest){
    Map<String, Object> requestMap = gson.fromJson(stringJsonRequest, Map.class);
    GuessPostDTO guessPostDTO = gson.fromJson(gson.toJson(requestMap.get("guessPostDTO")), GuessPostDTO.class);
    //nedim-j: use for round-basis, authentication:
    //AuthenticationDTO authenticationDTO = gson.fromJson(gson.toJson(requestMap.get("authenticationDTO")), AuthenticationDTO.class);

    Guess guess = DTOMapper.INSTANCE.convertGuessPostDTOtoEntity(guessPostDTO);
    Response response = gameService.guessImage(guess);

    webSocketHandler.sendMessage("/games/"+guess.getGameId(), "round-update", response);
    return response;
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