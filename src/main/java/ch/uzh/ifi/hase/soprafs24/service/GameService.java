package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.entity.Guess;
import ch.uzh.ifi.hase.soprafs24.entity.Player;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.entity.Game;
import ch.uzh.ifi.hase.soprafs24.repository.GameRepository;
import ch.uzh.ifi.hase.soprafs24.repository.ImageRepository;
import ch.uzh.ifi.hase.soprafs24.repository.PlayerRepository;
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




  @Autowired
  public GameService(@Qualifier("gameRepository") GameRepository gameRepository, @Qualifier("imageRepository") ImageRepository imageRepository, GameUserService gameUserService) {
    this.gameRepository = gameRepository;
    this.imageRepository = imageRepository;
    this.gameUserService = gameUserService;
  }


  //
  // Not done yet
  //
  public void creategame(Long lobbyid, Long user1id, Long user2id) {
    // till: check if both players exist
    gameUserService.checkIfUserExists(user1id);
    gameUserService.checkIfUserExists(user2id);
    // till: check if both players are online
    gameUserService.checkIfUserOnline(user1id);
    gameUserService.checkIfUserOnline(user2id);
    // till: checks if the user is actually the creator of the lobby
    gameUserService.checkForCorrectLobby(lobbyid, user1id);


    // till: create a new game
    Game game = new Game();
    // till: create 2 players
    Player player1 = new Player();
    Player player2 = new Player();


    // create List of players and set it

    game.setCreatorid(player1.getPlayerid());









  }



  public void chooseimage(Long imageId, Long playerId) {
    checkIfImageExists(imageId);

    Player player = gameUserService.getUser(playerId);    //player gets searched to set the chosen character of that player
    if (player.getchosencharacter() == null){
        player.setChosencharacter(imageId);
    }
    else{
        throw new IllegalStateException("The player has already chosen a character.");  //if player has already chosen character Exception is thrown
    }
    }

  public Boolean guessimage(Guess guess) {
      Guess.GuessKey id = guess.getId(); // Get the embedded key
      Long gameId = id.getGameId(); // Get the gameId
      Long playerId = id.getPlayerId(); // Get the playerId
      Long imageId = id.getImageId(); // Get the imageId

      // till: check if game exists
      checkIfGameExists(gameId);
      // till: check if imageId exists
      checkIfImageExists(imageId);
      // till: check if Player is in the game
      Game game = gameRepository.findByGameid(gameId);
      gameUserService.checkIfPlayerinGame(game, playerId);

      // get the chosen character of the Opponent
      Long oppChosenCharacter = gameUserService.getChosenCharacterofOpponent(game, playerId);

      if (oppChosenCharacter.equals(imageId)){
          return true;
      } else if (gameUserService.increaseandcheckStrikes(playerId)) {
          return false;
      } else {
          throw new IllegalStateException();
      }

  }



  public Boolean checkIfGameExists(Long gameId) {
      try {
          assert gameRepository.findByGameid(gameId) != null;
          return true;
      }
      catch (Exception e) {
          return false;
      }
  }


  public Boolean checkIfImageExists(Long imageId) {
      try {
          if (imageRepository.findByImageId(imageId) != null) {
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



