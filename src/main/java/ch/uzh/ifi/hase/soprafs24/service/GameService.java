package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.constant.GameStatus;
import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.*;
import ch.uzh.ifi.hase.soprafs24.repository.*;
import ch.uzh.ifi.hase.soprafs24.repository.GameRepository;
import ch.uzh.ifi.hase.soprafs24.rest.dto.AuthenticationDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.ImageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
  private final UnsplashService unsplashService; // Inject UnsplashService
  private static final Logger logger = Logger.getLogger(UnsplashService.class.getName());
  private final LobbyService lobbyService;
  private final AuthenticationService authenticationService;


    @Autowired
  public GameService(@Qualifier("gameRepository") GameRepository gameRepository, @Qualifier("imageRepository") ImageRepository imageRepository, GameUserService gameUserService, @Qualifier("lobbyRepository") LobbyRepository lobbyRepository, UnsplashService unsplashService, LobbyService lobbyService, AuthenticationService authenticationService) {
    this.gameRepository = gameRepository;
    this.imageRepository = imageRepository;
    this.gameUserService = gameUserService;
    this.lobbyRepository = lobbyRepository;
    this.unsplashService = unsplashService;
    this.lobbyService = lobbyService;
    this.authenticationService = authenticationService;
    }

  public List<Game> getGames() {
    return this.gameRepository.findAll();
  }

  public Game getGame(Long gameid) {
    return this.gameRepository.findByGameId(gameid);
  }

  public Response chooseImage(Guess guess) {
    checkIfImageExists(guess.getImageId());
    Player player = gameUserService.getPlayer(guess.getPlayerId());

    if (player.getChosencharacter() == null) {
      player.setChosencharacter(guess.getImageId());
      gameUserService.savePlayerChanges(player);
    } else {
      //throw new IllegalStateException("The player has already chosen a character.");
        throw new ResponseStatusException(HttpStatus.CONFLICT, "The player has already chosen a character");
    }
    return gameUserService.createResponse(false, player.getPlayerId(), player.getStrikes(), gameUserService.determineStatus(guess.getGameId()));
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

    // till: Save the changes
    gameUserService.savePlayerChanges(player1);
    gameUserService.savePlayerChanges(player2);
    gameUserService.saveUserChanges(creator);
    gameUserService.saveUserChanges(inviteduser);

    game.setCreatorPlayerId(player1.getPlayerId());
    game.setInvitedPlayerId(player2.getPlayerId());

    // save changes to game
    gameRepository.save(game);
    gameRepository.flush();
    lobby.setGame(game); // smailalijagic: add game to lobby

      // Check if there are already 200 images in the database
      int imageCount = imageRepository.countAllImages();
      if (imageCount < 200) {
          // If there are less than 200 images, fetch and save more
          int count = 225 - imageCount;
          unsplashService.saveRandomPortraitImagesToDatabase(count);
      }

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
      Response r = handleWin(playerId);
      //handle opponents loss
      gameUserService.increaseGamesPlayed((gameUserService.getOpponentId(game, playerId))); //nedim-j: do we also send a websocket for the loss of opponent?
      deleteGame(game);
      return r;
    } else {
        //if (gameUserService.checkStrikes(playerId)) { //nedim-j: maybe redundant, as we checkStrikes for both players in determineStatus
            gameUserService.increaseStrikesByOne(playerId);
            int strikes = gameUserService.getStrikes(playerId);
            GameStatus gameStatus = gameUserService.determineStatus(game.getGameId());
            if(gameStatus != GameStatus.END) {
                return gameUserService.createResponse(false, playerId, strikes, gameStatus);
            }
            else {
                Response r = handleLoss(playerId);
                //handle opponents win
                handleWin(gameUserService.getOpponentId(game, playerId)); //nedim-j: do we also send a websocket for the loss of opponent?
                deleteGame(game);
                return r;
            }
        }
    }


  public Response handleWin(Long playerId) {
    //nedim-j: handle stats increase etc.
    gameUserService.increaseGamesPlayed(playerId);
    gameUserService.increaseWinTotal(playerId);
    int strikes = gameUserService.getStrikes(playerId);
    return gameUserService.createResponse(true, playerId, strikes, GameStatus.END);
  }

  public Response handleLoss(Long playerId) {
    //nedim-j: handle stats increase etc.
    gameUserService.increaseGamesPlayed(playerId);
     //nedim-j: mb increaseLoss? round-based games could be drawn as well
    int strikes = gameUserService.getStrikes(playerId);
    return gameUserService.createResponse(false, playerId, strikes, GameStatus.END);
}

  private void deleteGame(Game game) {

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

      //delete the game
      gameRepository.delete(game);

       */
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


    private Game getGameById(Long gameId) {
        return gameRepository.findById(gameId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found with id: " + gameId));
    }


    private List<ImageDTO> fetchNewImages(int count) {

        return unsplashService.getImageUrlsFromDatabase(count);
    }

    private List<ImageDTO> filterDuplicates(Game game,int count) {
        List<Long> currentImageIds = game.getGameImages().stream()
                .map(Image::getId)
                .toList();

        List<ImageDTO> nonDuplicateImages = new ArrayList<>();
        int iterations = 0;
        int maxIterations = 10;

        // continue fetching new images until we find non-duplicates (or reach the maximum number of iterations)
        while (nonDuplicateImages.isEmpty() && iterations < maxIterations) {
            List<ImageDTO> newImageDTOs = fetchNewImages(10); // fetch another 5 images
            Collections.shuffle(newImageDTOs);
            nonDuplicateImages = newImageDTOs.stream()
                    .filter(imageDTO -> !currentImageIds.contains(imageDTO.getId()))
                    .limit(count) // limit additional images
                    .collect(Collectors.toList());

            iterations++;
        }

        // if nonDuplicateImages is still empty, throw an error
        if (nonDuplicateImages.isEmpty()) {
            throw new IllegalStateException("Unable to find non-duplicate images after " + maxIterations + " iterations.");
        }

        return nonDuplicateImages;
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

    public List<ImageDTO> getGameImages(Long gameId) {
        try {
            // retrieve the game entity (db)
            Game game = getGameById(gameId);

            // access the list of game-specific images from the game entity
            List<Image> gameImages = game.getGameImages();

            // convert Image objects to ImageDTO objects
            List<ImageDTO> gameImageDTOs = gameImages.stream()
                    .map(image -> {
                        ImageDTO imageDTO = new ImageDTO();
                        imageDTO.setId(image.getId());
                        imageDTO.setUrl(image.getUrl());
                        return imageDTO;
                    })
                    .collect(Collectors.toList());

            return gameImageDTOs;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Error while fetching images for your game");
        }
    }

    public void saveGameImages(Long gameId, int count) {
        try {
            // retrieve the game entity from (db)
            Game game = getGameById(gameId);

            // fetch new images
            List<ImageDTO> newImageDTOs = fetchNewImages(count);

            // filter out duplicates and ensure enough unique images are fetched
            //List<ImageDTO> filteredNewImageDTOs = filterDuplicates(game, newImageDTOs, count);

            // create Image entities based on the filtered new ImageDTOs
            List<Image> newImages = createImageEntities(newImageDTOs);

            // add the new images to the game's image list and save the game
            game.getGameImages().addAll(newImages);
            gameRepository.save(game);;

        } catch (Exception e) {
            logger.severe("Error while saving images for game: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Error while saving images for your game", e);
        }
    }

    public void replaceGameImages(Long gameId) {
        try {
            // retrieve the game entity from (db)
            Game game = getGameById(gameId);

            // filter out duplicates
            List<ImageDTO> filteredNewImageDTOs = filterDuplicates(game, 1);

            // create Image entities
            List<Image> newImages = createImageEntities(filteredNewImageDTOs);

            // add the new images to the game's image list and save the game
            game.getGameImages().addAll(newImages);
            gameRepository.save(game);
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
            replaceGameImages(gameId);

            // fetch and add a new image to ensure the game always has at least one image
            //return replaceGameImages(gameId);

        } catch (Exception e) {
            logger.severe("Error while deleting image from game: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Error while deleting image from your game", e);
        }
    }

    @Scheduled(fixedRate = 15000) // Check every 15 seconds
    public void checkGameTimeout() {
        //List<Game> activeGames = getActiveGames();

        for (Game game : gameRepository.findAll()) {
            if (isPlayerInactive(game.getCreatorPlayerId())) {
                gameUserService.increaseGamesPlayed(game.getCreatorPlayerId());
                handleWin(game.getInvitedPlayerId());
                deleteGame(game);
            } else if (isPlayerInactive(game.getInvitedPlayerId())) {
                gameUserService.increaseGamesPlayed(game.getInvitedPlayerId());
                handleWin(game.getCreatorPlayerId());
                deleteGame(game);
            }
        }
    }

    private boolean isPlayerInactive(Long playerId) {
        // Check if player is inactive based on last activity time
        // Return true if inactive, false otherwise
        return true;
    }

}


