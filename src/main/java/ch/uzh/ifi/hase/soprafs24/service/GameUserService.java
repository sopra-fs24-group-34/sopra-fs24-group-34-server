package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.constant.GameStatus;
import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.*;
import ch.uzh.ifi.hase.soprafs24.repository.*;
import ch.uzh.ifi.hase.soprafs24.rest.dto.RoundDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs24.repository.PlayerRepository;
import ch.uzh.ifi.hase.soprafs24.repository.GameRepository;

import javax.transaction.Transactional;
import java.util.List;


@Service     //Service to handle User and PLayer methods that need USer and PLayer repository, done to reduce coupling
@Transactional
public class GameUserService {
    private final PlayerRepository playerRepository;
    private final UserRepository userRepository;
    private final LobbyRepository lobbyRepository;
    private final GameRepository gameRepository;

  @Autowired
  public GameUserService(@Qualifier("playerRepository") PlayerRepository playerRepository, @Qualifier("userRepository") UserRepository userRepository, @Qualifier("lobbyRepository") LobbyRepository lobbyRepository, @Qualifier("gameRepository") GameRepository gameRepository) {
    this.userRepository = userRepository;
    this.playerRepository = playerRepository;
    this.lobbyRepository = lobbyRepository;
    this.gameRepository = gameRepository;
  }

  public Player getPlayer(Long playerId){
    return playerRepository.findByPlayerId(playerId);
  }

  public User getUser(Long userId){
      return userRepository.findUserById(userId);
  }

  public Long getOpponentId(Game game, Long playerId) {
      // till: get players from the game
      Long creatorId = game.getCreatorPlayerId();

      // create Long for opponentplayerid
      Long opponentId;
      // Find the opponent player (the player who is not the one making the guess)
      if (playerId.equals(creatorId)){
          opponentId = game.getInvitedPlayerId();
      } else {
          opponentId = game.getCreatorPlayerId();
      }
      return opponentId;
  }


  public Long getChosenCharacterOfOpponent(Game game, Long playerId) {

      Long opponentId = getOpponentId(game, playerId);
      // get player to get chosencharacter
      Player opponent = playerRepository.findByPlayerId(opponentId);

      return opponent.getChosencharacter();
  }

  public void savePlayerChanges(Player player){
      playerRepository.save(player);
      playerRepository.flush();
  }
  public void saveuserchanges(User user){
      userRepository.save(user);
      userRepository.flush();
  }


  public void saveUserChanges(User user){
      userRepository.save(user);
      userRepository.flush();
}

    public Boolean checkStrikes(Long playerid) {
      //nedim-j: returns true, if player has less than max strikes and can play on
        Player player = getPlayer(playerid);
        //nedim-j: for M4 need some variable maxGuesses instead of 2
        int maxGuesses = 3;
        if (player.getStrikes() >= maxGuesses){
            return false;
        }
        return true;
    }

    public int getStrikes(Long playerId) {
      return getPlayer(playerId).getStrikes();
    }

  public void increaseStrikesByOne(Long playerId) {
      //increases the number of strikes a player has by one
      Player player = playerRepository.findByPlayerId(playerId);
      player.setStrikes(player.getStrikes() + 1);
      playerRepository.save(player);
      playerRepository.flush();
    }

    public GameStatus determineStatus(Long gameId) {
        Game game = new Game();
        try {
            game = gameRepository.findByGameId(gameId);
        } catch (Exception e) {
            System.out.println("Game is null");
            return GameStatus.CHOOSING;
        }

        Player creator = playerRepository.findByPlayerId(game.getCreatorPlayerId());
        Player invited = playerRepository.findByPlayerId(game.getInvitedPlayerId());
        if((creator.getChosencharacter() == null) || (invited.getChosencharacter() == null)) {
            return GameStatus.CHOOSING;
        } else if(!checkStrikes(creator.getPlayerId()) || !checkStrikes(invited.getPlayerId())) {
            return GameStatus.END;
        }
        return GameStatus.GUESSING;
    }

  public Response createResponse(Boolean guess, Long playerId, int strikes, GameStatus gameStatus, RoundDTO roundDTO) {
      // creates a response that is send back to the frontend
      //Player player = playerRepository.findByPlayerId(playerId);
      Response response = new Response();
      response.setGuess(guess);
      response.setPlayerId(playerId);
      response.setStrikes(strikes);
      response.setRoundStatus(gameStatus);
      response.setRoundDTO(roundDTO);
      return response;
  }

  public void increaseWinTotal(Long playerId) {
      //till: get the Player where the user is saved and there access the totalwins attribute
      Player player = getPlayer(playerId);
      User user = getUser(player.getUserId());
      try {
          user.setTotalwins(user.getTotalwins() + 1);
      } catch (NullPointerException e){
          user.setTotalwins(1L);
      }
      userRepository.save(user);
      userRepository.flush();
  }

  public void increaseGamesPlayed(Long playerId){
      //till: get the Player where the user is saved and there access the totalgames attribute
      Player player = getPlayer(playerId);
      User user = getUser(player.getUserId());
      try {
          user.setTotalplayed(user.getTotalplayed() + 1);
      } catch (NullPointerException e){
          try {
              user.setTotalplayed(1L);
          } catch(Exception f) {
              System.out.println("User is null in GameUserService.increaseGamesPlayed");
          }
      }
      userRepository.save(user);
      userRepository.flush();
  }

  public void updategamelobbylist(User user) {
      // deleting the game from game lobby list and setting it to null
      try {
          List<Lobby> newUserGamelobbylist = user.getUsergamelobbylist();

          if (user.getUsergamelobbylist() != null) {
              for (Lobby lobby : newUserGamelobbylist) {
                  lobby.setGame(null);
              }

              //Update the user
              user.setUsergamelobbylist(newUserGamelobbylist);

              userRepository.save(user);
              userRepository.flush();
          }
      } catch (Exception e) {
          System.out.println("User is null in GameUserService.updategamelobbylist");
      }

  }


  public GameHistory createGameHistory(User user) {
        GameHistory gameHistory = new GameHistory();
        gameHistory.setTotalgamesplayed(user.getTotalplayed());
        gameHistory.setTotalwins(user.getTotalwins());
        gameHistory.setWinPercentage(user.getTotalwins()/user.getTotalplayed());
        return gameHistory;
  }


    //
    // check Functions
    //
  public Boolean checkIfUserOnline(Long userid){
    User user = userRepository.findUserById(userid);
    return user.getStatus() == UserStatus.ONLINE;
  }


  public Boolean checkIfUserExists(Long userid){
        User user = userRepository.findUserById(userid);
        return user != null; //user = null-> user does not exist
    }

  public Boolean checkIfPlayerinGame(Game game, Long playerid){
        // till: get players from the game
        playerRepository.findByPlayerId(playerid);

        // check if the player is in the game players list, returns true when in game and false when not
        return game.getCreatorPlayerId().equals(playerid) || game.getInvitedPlayerId().equals(playerid);
    }


}