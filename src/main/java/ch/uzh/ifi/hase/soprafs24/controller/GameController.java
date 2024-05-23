package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.entity.*;
import ch.uzh.ifi.hase.soprafs24.rest.dto.*;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs24.service.GameService;
import ch.uzh.ifi.hase.soprafs24.service.GameUserService;
import ch.uzh.ifi.hase.soprafs24.service.LobbyService;
import ch.uzh.ifi.hase.soprafs24.websocket.WebSocketMessenger;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import com.google.gson.Gson;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class GameController {
  private final GameService gameService;
  private final GameUserService gameUserService;
  private final WebSocketMessenger webSocketMessenger;
  private final LobbyService lobbyService;
  private final Gson gson = new Gson();

  GameController(GameService gameService, GameUserService gameUserService, WebSocketMessenger webSocketMessenger, LobbyService lobbyService) {
    this.gameService = gameService;
    this.gameUserService = gameUserService;
    this.webSocketMessenger = webSocketMessenger;
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

  @MessageMapping("/createGame")
  public Game createGame(String stringJsonRequest) {
    Map<String, Object> requestMap = gson.fromJson(stringJsonRequest, Map.class);
    Long lobbyId = gson.fromJson(gson.toJson(requestMap.get("lobbyId")), Long.class);
    GamePostDTO gamePostDTO = gson.fromJson(gson.toJson(requestMap.get("gamePostDTO")), GamePostDTO.class);
    AuthenticationDTO authenticationDTO = gson.fromJson(gson.toJson(requestMap.get("authenticationDTO")), AuthenticationDTO.class);

    Game createdGame = gameService.createGame(lobbyId, gamePostDTO, authenticationDTO);
    GameGetDTO gameGetDTO = DTOMapper.INSTANCE.convertEntityToGameGetDTO(createdGame);

    webSocketMessenger.sendMessage("/lobbies/"+lobbyId, "game-created", gameGetDTO);

    return createdGame;
  }

  @MessageMapping("/startGame")
  public void startGame(String stringJsonRequest) {
    Map<String, Object> requestMap = gson.fromJson(stringJsonRequest, Map.class);
    Long lobbyId = gson.fromJson(gson.toJson(requestMap.get("lobbyId")), Long.class);
    Long gameId = gson.fromJson(gson.toJson(requestMap.get("gameId")), Long.class);
    AuthenticationDTO authenticationDTO = gson.fromJson(gson.toJson(requestMap.get("authenticationDTO")), AuthenticationDTO.class);

    if(lobbyService.isLobbyOwner(lobbyId, authenticationDTO)) {
        Game createdGame = gameService.getGame(gameId);
        GameGetDTO gameGetDTO = DTOMapper.INSTANCE.convertEntityToGameGetDTO(createdGame);
        webSocketMessenger.sendMessage("/lobbies/" + lobbyId, "game-started", gameGetDTO);
    } else {
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not the owner of the lobby");
    }
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
    RoundDTO roundDTO = gameService.chooseImage(guess);
    if (gameService.bothPlayersChosen(guess.getGameId())) {
        webSocketMessenger.sendMessage("/games/"+guess.getGameId(), "round0", roundDTO);
    }
  }

  @MessageMapping("/guessImage")
  public void guessImage(String stringJsonRequest){
    Map<String, Object> requestMap = gson.fromJson(stringJsonRequest, Map.class);
    GuessPostDTO guessPostDTO = gson.fromJson(gson.toJson(requestMap.get("guessPostDTO")), GuessPostDTO.class);
    //nedim-j: use for round-basis, authentication:
    //AuthenticationDTO authenticationDTO = gson.fromJson(gson.toJson(requestMap.get("authenticationDTO")), AuthenticationDTO.class);

    Guess guess = DTOMapper.INSTANCE.convertGuessPostDTOtoEntity(guessPostDTO);
    Response response = gameService.guessImage(guess);

    Game game = gameService.getGame(guessPostDTO.getGameid());

    RoundDTO roundDTO = gameService.updateTurn(guess.getGameId());

    webSocketMessenger.sendMessage("/games/" + guess.getGameId(), roundDTO.getEvent(), roundDTO);

    webSocketMessenger.sendMessage("/games/"+guess.getGameId(),"guess-result", response);

  }

    @MessageMapping("/switchTurn")
    public void switchTurn(String stringJsonRequest) {
        Map<String, Object> requestMap = gson.fromJson(stringJsonRequest, Map.class);
        Long gameId = gson.fromJson(gson.toJson(requestMap.get("gameId")), Long.class);

        RoundDTO roundDTO = gameService.updateTurn(gameId);

        webSocketMessenger.sendMessage("/games/" + gameId, roundDTO.getEvent(), roundDTO);

    }




        // Endpoints regarding game-specific images
    @GetMapping("/games/{gameId}/images")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<ImageDTO> getGameImages(@PathVariable Long gameId) {

        return gameService.getGameImages(gameId);
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