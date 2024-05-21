package ch.uzh.ifi.hase.soprafs24.service;

//import ch.uzh.ifi.hase.soprafs24.constant.RoundStatus;

import ch.uzh.ifi.hase.soprafs24.constant.GameStatus;
import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.*;
import ch.uzh.ifi.hase.soprafs24.repository.*;
import ch.uzh.ifi.hase.soprafs24.repository.GameRepository;
import ch.uzh.ifi.hase.soprafs24.rest.dto.AuthenticationDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.RoundDTO;

import ch.uzh.ifi.hase.soprafs24.rest.dto.ImageDTO;
import ch.uzh.ifi.hase.soprafs24.websocket.WebSocketMessenger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.logging.Logger;

@Service
@Transactional
public class GameService {
  private final GameRepository gameRepository;
  private final ImageRepository imageRepository;
  private final GameUserService gameUserService;
  private final LobbyRepository lobbyRepository;
  private final UnsplashService unsplashService;
  private static final Logger logger = Logger.getLogger(UnsplashService.class.getName());
  private final LobbyService lobbyService;
  private final AuthenticationService authenticationService;
  private final WebSocketMessenger webSocketMessenger;


    @Autowired
  public GameService(@Qualifier("gameRepository") GameRepository gameRepository, @Qualifier("imageRepository") ImageRepository imageRepository,
                     GameUserService gameUserService, @Qualifier("lobbyRepository") LobbyRepository lobbyRepository,
                     UnsplashService unsplashService, LobbyService lobbyService, AuthenticationService authenticationService,
                     WebSocketMessenger webSocketMessenger) {
    this.gameRepository = gameRepository;
    this.imageRepository = imageRepository;
    this.gameUserService = gameUserService;
    this.lobbyRepository = lobbyRepository;
    this.unsplashService = unsplashService;
    this.lobbyService = lobbyService;
    this.authenticationService = authenticationService;
    this.webSocketMessenger = webSocketMessenger;
    }

  public List<Game> getGames() {
    return this.gameRepository.findAll();
  }

  public Game getGame(Long gameid) {
    return this.gameRepository.findByGameId(gameid);
  }

  public RoundDTO chooseImage(Guess guess) {
    checkIfImageExists(guess.getImageId());
    Game game = gameRepository.findByGameId(guess.getGameId());
    Player player = gameUserService.getPlayer(guess.getPlayerId());

    if (player.getChosencharacter() == null) {
      player.setChosencharacter(guess.getImageId());
      gameUserService.savePlayerChanges(player);
    } else {
      //throw new IllegalStateException("The player has already chosen a character.");
        throw new ResponseStatusException(HttpStatus.CONFLICT, "The player has already chosen a character");
    }
      if (bothPlayersChosen(guess.getGameId())) {
          game.setCurrentTurnPlayerId(game.getInvitedPlayerId());
          game.setCurrentRound(1);
          gameRepository.save(game);
      }

      RoundDTO roundDTO = new RoundDTO(game.getCurrentRound(), game.getCurrentTurnPlayerId());

      // response is probably unnecessary
    return roundDTO;}

    public boolean bothPlayersChosen(Long gameId) {
        Game game = gameRepository.findByGameId(gameId);
        Player player1 = gameUserService.getPlayer(game.getCreatorPlayerId());
        Player player2 = gameUserService.getPlayer(game.getInvitedPlayerId());
        return player1.getChosencharacter() != null && player2.getChosencharacter() != null;
    }

    public RoundDTO updateTurn(Long gameId) {

        Game game = gameRepository.findByGameId(gameId);
        Long currentTurnPlayerId = game.getCurrentTurnPlayerId();
        Long nextTurnPlayerId = currentTurnPlayerId.equals(game.getCreatorPlayerId()) ? game.getInvitedPlayerId() : game.getCreatorPlayerId();
        game.setCurrentTurnPlayerId(nextTurnPlayerId);

        if (nextTurnPlayerId.equals(game.getInvitedPlayerId())) {
            game.setCurrentRound(game.getCurrentRound() + 1);
        }
        gameRepository.save(game);
        gameRepository.flush();

        return new RoundDTO(game.getCurrentRound(), game.getCurrentTurnPlayerId());
    }


  public Game createGame(Long lobbyId, Game game, AuthenticationDTO authenticationDTO) {
    Lobby lobby = lobbyRepository.findByLobbyid(lobbyId); // smailalijagic: get lobby object

    // till: check if both players exist
    //gameUserService.checkIfUserExists(game.getCreatorId());
    //gameUserService.checkIfUserExists(game.getInvitedPlayerId());
    // till: check if both players are online
    //nedim-j: should keep it commented out atm, because status not done yet, also make it checkIfUserInlobbyReady
    //gameUserService.checkIfUserOnline(game.getCreatorId());
    //gameUserService.checkIfUserOnline(game.getInvitedPlayerId());

    //nedim-j: check if user making request is the host
    if(!lobbyService.isLobbyOwner(lobbyId, authenticationDTO)) {
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are not the host");
    }

    // till: Create Player instances and set Users to keep connection
    Player player1 = new Player();
    User creator = gameUserService.getUser(game.getCreatorPlayerId());
    player1.setUserId(creator.getId());
    creator.setStatus(UserStatus.PLAYING);

    Player player2 = new Player();
    User inviteduser = gameUserService.getUser(game.getInvitedPlayerId());
    player2.setUserId(inviteduser.getId());
    inviteduser.setStatus(UserStatus.PLAYING);

    // till: change userStatus
    creator.setStatus(UserStatus.PLAYING);
    inviteduser.setStatus(UserStatus.PLAYING);
    gameUserService.saveuserchanges(creator);
    gameUserService.saveuserchanges(inviteduser);


    // till: Save the changes
    gameUserService.savePlayerChanges(player1);
    gameUserService.savePlayerChanges(player2);
    gameUserService.saveUserChanges(creator);
    gameUserService.saveUserChanges(inviteduser);

    game.setCreatorPlayerId(player1.getPlayerId());
    game.setInvitedPlayerId(player2.getPlayerId());
    game.setCurrentRound(0);
    game.setCurrentTurnPlayerId(null);

      // Check if there are already 200 images in the database
      databaseImageCheck();

      // save changes to game
      gameRepository.save(game);
      gameRepository.flush();

      saveGameImages(game.getGameId());

      lobby.setGame(game); // smailalijagic: add game to lobby
      return game;
  }

  public Response guessImage(Guess guess){
    //till: check if game exists
    // checkIfGameExists(guess.getGameId());
    //till: check if Imageid exists
    // checkIfImageExists(guess.getImageId());
    //till: check if player is in the game
    Game game = new Game();
    try {
        game = gameRepository.findByGameId(guess.getGameId());
    } catch(Exception e) {
        System.out.println("Game is null in GameService.guessImage");
    }
    Long playerId = guess.getPlayerId();
    //gameUserService.checkIfPlayerinGame(game, playerId);

    //get the chosencharacter of the Opponent
    Long oppChosenCharacter = gameUserService.getChosenCharacterOfOpponent(game, playerId);

    if (oppChosenCharacter.equals(guess.getImageId())){
      Response r = handleWin(playerId,game.getGameId());
      //handle opponents loss
      gameUserService.increaseGamesPlayed((gameUserService.getOpponentId(game, playerId))); //nedim-j: do we also send a websocket for the loss of opponent?
      deleteGame(game);
      return r;
    } else {
        //if (gameUserService.checkStrikes(playerId)) { //nedim-j: maybe redundant, as we checkStrikes for both players in determineStatus
            gameUserService.increaseStrikesByOne(playerId);
            int strikes = gameUserService.getStrikes(playerId);
            GameStatus gameStatus = gameUserService.determineStatus(game.getGameId());

            RoundDTO roundDTO = updateTurn(game.getGameId());
            if(gameStatus != GameStatus.END) {
                return gameUserService.createResponse(false, playerId, strikes, gameStatus, roundDTO);
            }
            else {
                Response r = handleLoss(playerId, game.getGameId());
                //handle opponents win
                handleWin(gameUserService.getOpponentId(game, playerId), game.getGameId()); //nedim-j: do we also send a websocket for the loss of opponent?
                deleteGame(game);
                return r;
            }
        }
    }

  public RoundDTO getGameState(Long gameId) {
        Game game = getGame(gameId);
        return new RoundDTO(game.getCurrentRound(), game.getCurrentTurnPlayerId());
  }

  public Response handleWin(Long playerId, Long gameId) {
    //nedim-j: handle stats increase etc.

    gameUserService.increaseGamesPlayed(playerId);
    gameUserService.increaseWinTotal(playerId);
    int strikes = gameUserService.getStrikes(playerId);
    RoundDTO roundDTO = updateTurn(gameId);
    return gameUserService.createResponse(true, playerId, strikes, GameStatus.END, roundDTO);
  }

  public Response handleLoss(Long playerId, Long gameId) {
    //nedim-j: handle stats increase etc.
    gameUserService.increaseGamesPlayed(playerId);
     //nedim-j: mb increaseLoss? round-based games could be drawn as well
    int strikes = gameUserService.getStrikes(playerId);

    RoundDTO roundDTO = updateTurn(gameId);
    return gameUserService.createResponse(false, playerId, strikes, GameStatus.END, roundDTO);
}

  public void deleteGame(Game game) {

      //Get the users
      Player creator = gameUserService.getPlayer(game.getCreatorPlayerId());
      User host = gameUserService.getUser(creator.getUserId());
      Player invitedPlayer = gameUserService.getPlayer(game.getInvitedPlayerId());
      User invitedUser = gameUserService.getUser(invitedPlayer.getUserId());

      host.setStatus(UserStatus.ONLINE);
      invitedUser.setStatus(UserStatus.ONLINE);
      gameUserService.saveUserChanges(host);
      gameUserService.saveUserChanges(invitedUser);

      /*
      //set the game in the Usergamelobbylist to null
      gameUserService.updategamelobbylist(user);
      gameUserService.updategamelobbylist(invitedUser);
      // increase the games played
      gameUserService.increaseGamesPlayed(game.getCreatorId());
      gameUserService.increaseGamesPlayed(game.getInvitedPlayerId());

      user.setStatus(UserStatus.ONLINE);  // till: probably needs to be changed when players go back to the lobby
      invitedUser.setStatus(UserStatus.ONLINE);
      gameUserService.saveuserchanges(user); // saves Status change
      gameUserService.saveuserchanges(invitedUser);

      //delete the game
      gameRepository.delete(game);

       */
  }

  public GameHistory getGameHistory(Long gameId, Long userId) {
      Game game = gameRepository.findByGameId(gameId);
      assert(game.getCreatorPlayerId() == userId || game.getInvitedPlayerId() == userId);
      User user = gameUserService.getUser(userId);
      GameHistory userGameHistory = gameUserService.createGameHistory(user);
      return userGameHistory;
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

  private void databaseImageCheck() {
      int imageCount = imageRepository.countAllImages();
      if (imageCount < 200) {
          // If there are less than 200 images, fetch and save more
          int count = 225 - imageCount;
          unsplashService.saveRandomPortraitImagesToDatabase(count);
      }
  }


    private Game getGameById(Long gameId) {
        return gameRepository.findById(gameId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found with id: " + gameId));
    }


    private List<ImageDTO> fetchNewImages(int count, Optional<List<ImageDTO>> gameImages) {

        return unsplashService.getImageUrlsFromDatabase(count, gameImages);
    }


    private List<Image> createImageEntities(List<ImageDTO> imageDTOs) {
        return imageDTOs.stream()
                .map(imageDTO -> {
                    Image image = new Image();
                    image.setId(imageDTO.getId());
                    image.setUrl(imageDTO.getUrl());
                    return image;
                })
                .collect(Collectors.toList());
    }

    private List<ImageDTO> createImageDTO(List<Image> imageDTOs) {
        return imageDTOs.stream()
                .map(image -> {
                    ImageDTO imageDTO = new ImageDTO();
                    imageDTO.setId(image.getId());
                    imageDTO.setUrl(image.getUrl());
                    return imageDTO;
                })
                .collect(Collectors.toList());
    }

    public List<ImageDTO> getGameImages(Long gameId) {
        try {
            // retrieve the game entity (db)
            Game game = getGameById(gameId);

            // access the list of game-specific images from the game entity
            List<Image> gameImages = game.getGameImages();

            // convert Image objects to ImageDTO objects
            return createImageDTO(gameImages);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Error while fetching images for your game");
        }
    }

    public void saveGameImages(Long gameId) {
        try {
            // retrieve the game entity from (db)
            Game game = getGameById(gameId);

            Optional<List<ImageDTO>> gameImagesOptional = Optional.empty();

            // fetch new images
            List<ImageDTO> newImageDTOs = fetchNewImages(20, gameImagesOptional);

            // create Image entities based on the filtered new ImageDTOs
            List<Image> newImages = createImageEntities(newImageDTOs);

            // add the new images to the game's image list and save the game
            game.setGameImages(newImages);
            gameRepository.save(game);
            gameRepository.flush();

        } catch (Exception e) {
            logger.severe("Error while saving images for game: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Error while saving images for your game", e);
        }
    }

    public void replaceGameImages(Long gameId) {
        try {
            // retrieve the game entity from (db)
            Game game = getGameById(gameId);


            List<Image> gameImages = game.getGameImages();
            List<ImageDTO> gameImagesDTO = createImageDTO(gameImages);
            Optional<List<ImageDTO>> gameImagesOptional = Optional.of(gameImagesDTO);

            List<ImageDTO> newImageDTOs = fetchNewImages(1, gameImagesOptional);

            // create Image entities
            List<Image> newImages = createImageEntities(newImageDTOs);

            // add the new images to the game's image list and save the game
            game.getGameImages().addAll(newImages);
            gameRepository.save(game);
            gameRepository.flush();

            // return filteredNewImageDTOs;

        } catch (Exception e) {
            logger.severe("Error while replacing image for game: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Error while replacing image from your game", e);
        }
    }

    public void deleteGameImage(Long gameId, Long imageId) {
        try {
            // retrieve the game entity from (db)
            Game game = getGameById(gameId);

            // check if image exists
            checkIfImageExists(imageId);

            // delete the specific image chosen by the user from the database
            // imageRepository.deleteById(imageId);

            // update the list of images of the game
            List<Image> currentImages = game.getGameImages();

            currentImages.removeIf(image -> image.getId().equals(imageId));

            // set the updated list of images to the game
            game.setGameImages(currentImages);
            
            // save the game entity to update the images
            gameRepository.save(game);
            gameRepository.flush();

            // Delete the image from the database
            imageRepository.deleteById(imageId);

            replaceGameImages(gameId);
            databaseImageCheck();

            // fetch and add a new image to ensure the game always has at least one image
            //return replaceGameImages(gameId);

        } catch (Exception e) {
            logger.severe("Error while deleting image from game: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Error while deleting image from your game", e);
        }
    }

    /*
    @Scheduled(fixedRate = 15000) // Check every 15 seconds
    public void checkGameTimeout() {
        //List<Game> activeGames = getActiveGames();
        System.out.println("Scheduled activity check ");
        for (Game game : gameRepository.findAll()) {
            if (!WebSocketMessenger.isPlayerActive(game.getCreatorPlayerId())) {
                gameUserService.increaseGamesPlayed(game.getCreatorPlayerId());
                handleWin(game.getInvitedPlayerId());
                deleteGame(game);
            } else if (!WebSocketMessenger.isPlayerActive(game.getInvitedPlayerId())) {
                gameUserService.increaseGamesPlayed(game.getInvitedPlayerId());
                handleWin(game.getCreatorPlayerId());
                deleteGame(game);
            }
        }
    }

     */



}


