package ch.uzh.ifi.hase.soprafs24.service;

//import ch.uzh.ifi.hase.soprafs24.constant.RoundStatus;

import ch.uzh.ifi.hase.soprafs24.constant.GameStatus;
import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.*;
import ch.uzh.ifi.hase.soprafs24.repository.GameRepository;
import ch.uzh.ifi.hase.soprafs24.repository.ImageRepository;
import ch.uzh.ifi.hase.soprafs24.repository.LobbyRepository;
import ch.uzh.ifi.hase.soprafs24.rest.dto.AuthenticationDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.GamePostDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.ImageDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.RoundDTO;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs24.websocket.WebSocketMessenger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

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
  private final WebSocketMessenger webSocketMessenger;


    @Autowired
  public GameService(@Qualifier("gameRepository") GameRepository gameRepository, @Qualifier("imageRepository") ImageRepository imageRepository,
                     GameUserService gameUserService, @Qualifier("lobbyRepository") LobbyRepository lobbyRepository,
                     UnsplashService unsplashService, LobbyService lobbyService, WebSocketMessenger webSocketMessenger) {
    this.gameRepository = gameRepository;
    this.imageRepository = imageRepository;
    this.gameUserService = gameUserService;
    this.lobbyRepository = lobbyRepository;
    this.unsplashService = unsplashService;
    this.lobbyService = lobbyService;
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
          game.setGameStatus(GameStatus.GUESSING);
          gameRepository.save(game);
          gameRepository.flush();
      }

      RoundDTO roundDTO = new RoundDTO(game.getCurrentRound(), game.getCurrentTurnPlayerId(), "");

      // response is probably unnecessary
    return roundDTO;}

    public boolean bothPlayersChosen(Long gameId) {
        Game game = gameRepository.findByGameId(gameId);
        Player player1 = gameUserService.getPlayer(game.getCreatorPlayerId());
        Player player2 = gameUserService.getPlayer(game.getInvitedPlayerId());
        return player1.getChosencharacter() != null && player2.getChosencharacter() != null;
    }

    public RoundDTO updateTurn(Long gameId, GameStatus gameStatus) {

        Game game = gameRepository.findByGameId(gameId);

        Long currentTurnPlayerId = game.getCurrentTurnPlayerId();
        Long nextTurnPlayerId = currentTurnPlayerId.equals(game.getCreatorPlayerId()) ? game.getInvitedPlayerId() : game.getCreatorPlayerId();
        game.setCurrentTurnPlayerId(nextTurnPlayerId);

        String event;
        if (nextTurnPlayerId.equals(game.getInvitedPlayerId())) {
            event = "round-update";
            game.setCurrentRound(game.getCurrentRound() + 1);
        } else {event = "turn-update";}

        RoundDTO roundDTO = new RoundDTO(game.getCurrentRound(), game.getCurrentTurnPlayerId(), event);
        // Check if the game status is END
        if (gameStatus == GameStatus.END || gameStatus == GameStatus.TIE) {
            // Delete the game
            deleteGame(game);
        } else {
            // Save the changes to the game
            gameRepository.save(game);
            gameRepository.flush();
        }

        return roundDTO;
    }


  public Game createGame(Long lobbyId, GamePostDTO gamePostDTO, AuthenticationDTO authenticationDTO) {
    Lobby lobby = lobbyRepository.findByLobbyid(lobbyId); // smailalijagic: get lobby object
    Game game = DTOMapper.INSTANCE.convertGamePostDTOtoEntity(gamePostDTO);

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

    // till: Save the changes
    gameUserService.savePlayerChanges(player1);
    gameUserService.savePlayerChanges(player2);
    gameUserService.saveUserChanges(creator);
    gameUserService.saveUserChanges(inviteduser);

    game.setCreatorPlayerId(player1.getPlayerId());
    game.setInvitedPlayerId(player2.getPlayerId());
    game.setCurrentRound(0);
    game.setCurrentTurnPlayerId(null);
    game.setGameStatus(GameStatus.CHOOSING);

    try {
    databaseImageCheck();}
    catch (ResponseStatusException e) {throw e;}

      // save changes to game
      gameRepository.save(game);
      gameRepository.flush();

      saveGameImages(game.getGameId());

      lobby.setGame(game); // smailalijagic: add game to lobby
      return game;
  }

    public Response guessImage(Guess guess) {
        Long playerId = guess.getPlayerId();
        System.out.println("Guess received from player " + playerId + " for game " + guess.getGameId());
        Game game = gameRepository.findByGameId(guess.getGameId());



        // Get the chosen character of the opponent
        Long oppChosenCharacter = gameUserService.getChosenCharacterOfOpponent(game, playerId);
        Response r;

        // If the guess is correct
        if (oppChosenCharacter.equals(guess.getImageId())) {
            // If the invited player has guessed correctly
            if (Objects.equals(playerId, game.getInvitedPlayerId())) {
                game.setGuestGuess(true);
                // If both players have guessed correctly, it's a tie
                if (game.getCreatorGuess() && game.getGuestGuess()) {
                    return handleTie(game);
                }
                // Otherwise, the game status is set to LASTCHANCE
                else {
                    int strikes = gameUserService.getStrikes(playerId);
                    game.setGameStatus(GameStatus.LASTCHANCE);
                    gameRepository.save(game);
                    gameRepository.flush();
                    return gameUserService.createResponse(true, playerId, strikes, GameStatus.LASTCHANCE);
                }
            }
            // If the creator player has guessed correctly
            else if (Objects.equals(playerId, game.getCreatorPlayerId())) {
                game.setCreatorGuess(true);
                // If both players have guessed correctly, it's a tie
                if (game.getCreatorGuess() && game.getGuestGuess()) {
                    game.setGameStatus(GameStatus.TIE);
                    gameRepository.save(game);
                    gameRepository.flush();
                    return handleTie(game);
                }
            }
            // If the game is not a tie, handle the win
            r = handleWin(playerId, game.getGameId());
            gameUserService.increaseGamesPlayed(gameUserService.getOpponentId(game, playerId));
            //deleteGame(game);
            return r;
        }
        // If the guess is incorrect
// If the guess is incorrect
        else {
            gameUserService.increaseStrikesByOne(playerId);
            int strikes = gameUserService.getStrikes(playerId);

            boolean isInvitedUser = guess.getPlayerId().equals(game.getInvitedPlayerId());
            //GameStatus gameStatus = gameUserService.determineGameStatus(game.getGameId(), false, isInvitedUser);

            GameStatus gameStatus = game.getGameStatus();

            // If the game status was LASTCHANCE before the guess was made
            if (gameStatus == GameStatus.LASTCHANCE) {
                // Handle the win for the other player
                r = handleLoss(playerId, game.getGameId());
                handleWin(gameUserService.getOpponentId(game, playerId), game.getGameId());
                // Delete the game
                //deleteGame(game);
                // Return a response indicating the game has ended
                return r;
            }
            // If a players has reached the maximum number of guesses
            else if (gameUserService.getStrikes(playerId) == game.getMaxStrikes()){
                handleWin(gameUserService.getOpponentId(game, playerId), game.getGameId());
                return handleLoss(playerId, game.getGameId());
            }
            // If the game is not over, return the current game status
            else if (gameStatus != GameStatus.END) {
                return gameUserService.createResponse(false, playerId, strikes, gameStatus);
            }
            // If the game is over, handle the loss
            else {
                r = handleLoss(playerId, game.getGameId());
                handleWin(gameUserService.getOpponentId(game, playerId), game.getGameId());
                //deleteGame(game);
                return r;
            }
        }
    }

  public RoundDTO getGameState(Long gameId) {
        Game game = getGame(gameId);
        RoundDTO roundDTO = new RoundDTO(game.getCurrentRound(), game.getCurrentTurnPlayerId(), "");
        return roundDTO;
  }

  public Response handleWin(Long playerId, Long gameId) {
    //nedim-j: handle stats increase etc.
    gameUserService.increaseGamesPlayed(playerId);
    gameUserService.increaseWinTotal(playerId);
    int strikes = gameUserService.getStrikes(playerId);
    return gameUserService.createResponse(true, playerId, strikes, GameStatus.END);
  }

  public Response handleTie(Game game) {
    // Get the player IDs
    Long creatorPlayerId = game.getCreatorPlayerId();
    Long invitedPlayerId = game.getInvitedPlayerId();

    // Handle stats increase for both players
    gameUserService.increaseGamesPlayed(creatorPlayerId);
    gameUserService.increaseGamesPlayed(invitedPlayerId);

    // Create a response
    int strikes = gameUserService.getStrikes(creatorPlayerId);
    Response response = gameUserService.createResponse(true, creatorPlayerId, strikes, GameStatus.TIE);

    // Delete the game
    //deleteGame(game);

    return response;
  }

  public Response handleLoss(Long playerId, Long gameId) {
    //nedim-j: handle stats increase etc.
    gameUserService.increaseGamesPlayed(playerId);
     //nedim-j: mb increaseLoss? round-based games could be drawn as well
    int strikes = gameUserService.getStrikes(playerId);

    return gameUserService.createResponse(false, playerId, strikes, GameStatus.END);
}

  public void deleteGame(Game game) {
      System.out.println("Game deleted with ID: " + game.getGameId());

      //Get the users
      Player creator = gameUserService.getPlayer(game.getCreatorPlayerId());
      User host = gameUserService.getUser(creator.getUserId());
      Player invitedPlayer = gameUserService.getPlayer(game.getInvitedPlayerId());
      User invitedUser = gameUserService.getUser(invitedPlayer.getUserId());

      host.setStatus(UserStatus.ONLINE);
      invitedUser.setStatus(UserStatus.ONLINE);
      gameUserService.saveUserChanges(host);
      gameUserService.saveUserChanges(invitedUser);

      // Find the lobby that references the game
      Lobby lobby = lobbyRepository.findByGame(game);

      // Remove the reference to the game from the lobby
      if (lobby != null) {
          lobby.setGame(null);
          lobbyRepository.save(lobby);
      }

      // Now you can safely delete the game
      gameRepository.deleteByGameId(game.getGameId());

      webSocketMessenger.sendMessage("/lobbies/"+game.getGameId(), "game-deleted", "");
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

  public void databaseImageCheck() {
      int imageCount = imageRepository.countAllImages();
      logger.severe(String.valueOf(imageCount));

      int desiredImageNr = 100; // don't go higher or it will not work because of limited images on unsplash (max.120)
      /*if (imageCount == 0){
          desiredImageNr = 110;
      } */

      if (imageCount < desiredImageNr) {
          // if there are less than 120 images, fetch and save more
          int count = desiredImageNr - imageCount;
          /*
          if (count < 15){
              count = 15;
          }
                     */
          int i = 0;
          int keysAmount = 3; // change to amount of keys!!
          //the following is to try every api key and then throw an error in case
          while (i < keysAmount) {
              try {
                  unsplashService.saveRandomPortraitImagesToDatabase(count);
                  break;
              }
              catch (ResponseStatusException e) {
                  i++;
                  imageCount = imageRepository.countAllImages();
                  count = desiredImageNr - imageCount;
                  if (i == keysAmount) {
                      String additionalMessage = "Failed to fetch images from Unsplash after 4 attempts - rate exceeded: WAIT AN HOUR AND RETRY ";
                      logger.severe(additionalMessage);
                      throw new ResponseStatusException(HttpStatus.FAILED_DEPENDENCY, additionalMessage);
                  }
              }
          }
      }
  }


    public Game getGameById(Long gameId) {
        return gameRepository.findById(gameId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found with id: " + gameId));
    }


    private List<ImageDTO> fetchNewImages(int count, Optional<List<ImageDTO>> gameImages) {

        return unsplashService.getImageUrlsFromDatabase(count, gameImages);
    }


    public List<Image> createImageEntities(List<ImageDTO> imageDTOs) {
        return imageDTOs.stream()
                .map(imageDTO -> {
                    Image image = new Image();
                    image.setId(imageDTO.getId());
                    image.setUrl(imageDTO.getUrl());
                    return image;
                })
                .collect(Collectors.toList());
    }

    public List<ImageDTO> createImageDTO(List<Image> imageDTOs) {
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
    @Transactional
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

            boolean removed = currentImages.removeIf(image -> image.getId().equals(imageId));

            if (!removed) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Image not found in the game");
            }
            // set the updated list of images to the game
            game.setGameImages(currentImages);
            
            // save the game entity to update the images
            gameRepository.save(game);
            gameRepository.flush();

            // Delete the image from the database
            //imageRepository.deleteById(imageId);

            replaceGameImages(gameId);
            //databaseImageCheck();


        } catch (Exception e) {
            logger.severe("Error while deleting image from game: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Error while deleting image from your game", e);
        }
    }
}


