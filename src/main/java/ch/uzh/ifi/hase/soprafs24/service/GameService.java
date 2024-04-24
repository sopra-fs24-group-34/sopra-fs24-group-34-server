package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.entity.*;
import ch.uzh.ifi.hase.soprafs24.repository.*;
import ch.uzh.ifi.hase.soprafs24.rest.dto.GuessPostDTO;
import ch.uzh.ifi.hase.soprafs24.service.GameUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;


import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;

@Service
@Transactional
public class GameService {
  private final GameRepository gameRepository;
  private final ImageRepository imageRepository;
  private final GameUserService gameUserService;
  private final LobbyRepository lobbyRepository;

  @Autowired
  public GameService(@Qualifier("gameRepository") GameRepository gameRepository, @Qualifier("imageRepository") ImageRepository imageRepository, GameUserService gameUserService, @Qualifier("lobbyRepository") LobbyRepository lobbyRepository) {
    this.gameRepository = gameRepository;
    this.imageRepository = imageRepository;
    this.gameUserService = gameUserService;
    this.lobbyRepository = lobbyRepository;
  }

  public List<Game> getGames() {
    return this.gameRepository.findAll();
  }

  public Game getGame(Long gameid) {
    return this.gameRepository.findByGameId(gameid);
  }

  public void selectimage(Guess guess) {
    // checkIfImageExists(guess.getImageId());

    Player player = gameUserService.getPlayer(guess.getPlayerId());

    if (player.getChosencharacter() == null) {
      player.setChosencharacter(guess.getImageId());
      gameUserService.saveplayerchanges(player);
    } else {
      throw new IllegalStateException("The player has already chosen a character.");
    }
  }

  public Boolean playerHasSelected(Long playerId) {
      return gameUserService.getPlayer(playerId).getChosencharacter() != null;
  }

  public Game creategame(Long lobbyid, Game game) {
    Lobby lobby = lobbyRepository.findByLobbyid(lobbyid); // smailalijagic: get lobby object
    // till: check if both players exist
    //gameUserService.checkIfUserExists(game.getCreatorId());
    //gameUserService.checkIfUserExists(game.getInvitedPlayerId());
    // till: check if both players are online
    //nedim-j: should keep it commented out atm, because ready/inlobby/online status not done yet
    //gameUserService.checkIfUserOnline(game.getCreatorId());
    //gameUserService.checkIfUserOnline(game.getInvitedPlayerId());
    // till: checks if the user is actually the creator of the lobby
    // gameUserService.checkForCorrectLobby(lobbyid, game.getCreatorId());

    // till: Create Player instances and set Users to keep connection
    Player player1 = new Player();
    User user = gameUserService.getUser(game.getCreatorId());
    player1.setUser(user);

    Player player2 = new Player();
    player2.setUser(gameUserService.getUser(game.getInvitedPlayerId()));

    // till: Save the changes
    gameUserService.saveplayerchanges(player1);
    gameUserService.saveplayerchanges(player2);

    game.setCreatorId(player1.getPlayerId());
    game.setInvitedPlayerId(player2.getPlayerId());

    // save changes to game
    gameRepository.save(game);

    lobby.setGame(game); // smailalijagic: add game to lobby

    return game;
  }

  public Response guesssimage(Guess guess){
    //till: check if game exists
    // checkIfGameExists(guess.getGameId());
    //till: check if Imageid exists
    // checkIfImageExists(guess.getImageId());
    //till: check if player is in the game
    Game game = gameRepository.findByGameId(Long.valueOf(guess.getGameId()));
    Long playerId = guess.getPlayerId();
    //gameUserService.checkIfPlayerinGame(game, playerId);

    //get the chosencharacter of the Opponent
    Long oppChosenCharacter = gameUserService.getChosenCharacterofOpponent(game, playerId);

    if (oppChosenCharacter.equals(guess.getImageId())){
      Response r = handleWin(playerId);
      deleteGame(game);
      return r;
    } else {
        if (gameUserService.checkStrikes(playerId)) {
            gameUserService.increaseStrikesByOne(playerId);
            return gameUserService.createResponse(false, playerId);
        }
        else {
            Response response = new Response();
            response.setGuess(false);
            //nedim-j: change from 3L to maxguesses
            response.setStrikes(3L);
            deleteGame(game);
            return response;
        }
    }
  }

  public Response handleWin(Long playerId) {
      //nedim-j: handle stats increase etc.
    gameUserService.increaseGamesPlayed(playerId);
    gameUserService.increaseWinTotal(playerId);
    return gameUserService.createResponse(true, playerId);
  }

  private void deleteGame(Game game) {
      //Get the users
      User user = gameUserService.getUser(game.getCreatorId());
      User invitedUser = gameUserService.getUser(game.getInvitedPlayerId());
      //set the game in the Usergamelobbylist to null
      gameUserService.updategamelobbylist(user);
      gameUserService.updategamelobbylist(invitedUser);

      //delete the game
      gameRepository.delete(game);
  }




  public Boolean checkIfGameExists(Long gameId) {
    try {
      assert gameRepository.findByGameId(gameId) != null;
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  public Boolean checkIfImageExists(Long imageId) {
    try {
      if (imageRepository.findImageById(imageId) != null) {
        return true;
      } else {
        throw new ImageNotFoundException("Image with ID " + imageId + " not found");
      }
    } catch (ImageNotFoundException e) {
      // Log the exception or handle it as needed
      return false;
    }
  }

  public class ImageNotFoundException extends Exception {
    public ImageNotFoundException(String message) {
      super(message);
    }
  }

}


