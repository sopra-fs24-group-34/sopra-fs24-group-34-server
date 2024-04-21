package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.entity.Guess;
import ch.uzh.ifi.hase.soprafs24.entity.Player;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.entity.Game;
import ch.uzh.ifi.hase.soprafs24.repository.GameRepository;
import ch.uzh.ifi.hase.soprafs24.repository.ImageRepository;
import ch.uzh.ifi.hase.soprafs24.repository.PlayerRepository;
import ch.uzh.ifi.hase.soprafs24.rest.dto.GamePostDTO;
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


    public Game creategame(Long lobbyid, Game game) {
        // till: check if both players exist
        gameUserService.checkIfUserExists(game.getCreatorId());
        gameUserService.checkIfUserExists(game.getInvitedPlayerId());
        // till: check if both players are online
        gameUserService.checkIfUserOnline(game.getCreatorId());
        gameUserService.checkIfUserOnline(game.getInvitedPlayerId());
        // till: checks if the user is actually the creator of the lobby
        gameUserService.checkForCorrectLobby(lobbyid, game.getCreatorId());


        // till: create 2 players
        Player player1 = new Player();
        Player player2 = new Player();
        //save the changes
        gameUserService.saveplayerchanges(player1);
        gameUserService.saveplayerchanges(player2);

        // Set the game's players
        game.setCreatorId(player1.getPlayerId());
        game.setInvitedPlayerId(player2.getPlayerId());

        // save changes to game
        gameRepository.save(game);
        gameRepository.flush();

        return game;


    }

    public Player selectimage(Guess guess) {
        //till: check if game exists
        checkIfGameExists(guess.getGameId());
        // check: if IMage exists
        checkIfImageExists(guess.getImageId());
        //check if player is in game but throws error always

        // till: get the player, to set the chosen character
        Player player = gameUserService.getUser(guess.getPlayerId());

        if (player.getChosencharacter() == null) {
            player.setChosencharacter(guess.getImageId());
            gameUserService.saveplayerchanges(player);
        }
        else {
            throw new IllegalStateException("The player has already chosen a character.");
        }
        return player;
    }

    public Boolean guessimage(Guess guess){
        //till: check if game exists
        checkIfGameExists(guess.getGameId());
        //till: check if Imageid exists
        checkIfImageExists(guess.getImageId());
        //till: check if player is in the game
        Game game = gameRepository.findByGameId(guess.getGameId());
        gameUserService.checkIfPlayerinGame(game, guess.getPlayerId());

        //get the chosencharacter of the Opponent
        Long oppChosenCharacter = gameUserService.getChosenCharacterofOpponent(game, guess.getPlayerId());

        //check if guess is correct, and check strikes
        if (oppChosenCharacter.equals(guess.getImageId())){
            return true;
        } else if (gameUserService.increaseandcheckStrikes(guess.getPlayerId())){
            return false;
        } else {
            System.out.println("3rd strike! The game is over");
            throw new IllegalStateException(); // Game over, end of game needs to be handled
        }

    }

    public Game getGame(Long gameid) {
        return gameRepository.findByGameId(gameid);
    }



    //
    // Check functions
    //


    public Boolean checkIfGameExists(Long gameId) {
        try {
            assert gameRepository.findByGameId(gameId) != null;
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
            System.out.println(e.getMessage());
            return false;
        }
    }

    public class ImageNotFoundException extends Exception {

        public ImageNotFoundException(String message) {
            super(message);


        }
    }
}