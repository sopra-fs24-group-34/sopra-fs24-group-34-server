package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.*;
import ch.uzh.ifi.hase.soprafs24.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs24.repository.PlayerRepository;

import javax.transaction.Transactional;
import java.util.List;


@Service     //Service to handle User and PLayer methods that need USer and PLayer repository, done to reduce coupling
@Transactional
public class GameUserService {
    private final PlayerRepository playerRepository;
    private final UserRepository userrepository;
    private final LobbyRepository lobbyRepository;

  @Autowired
  public GameUserService(@Qualifier("playerRepository") PlayerRepository playerRepository, @Qualifier("userRepository") UserRepository userrepository, @Qualifier("lobbyRepository") LobbyRepository lobbyRepository) {
    this.userrepository = userrepository;
    this.playerRepository = playerRepository;
    this.lobbyRepository = lobbyRepository;
  }

  public Player getPlayer(Long playerId){
    return playerRepository.findByPlayerId(playerId);
  }

  public User getUser(Long userId){
      return userrepository.findUserById(userId);
  }

  public Player createplayer(Long userid){
    Player player = new Player();

    //saving PLayer to repository
    player = playerRepository.save(player);
    playerRepository.flush();

    return player;
  }

  public Long getChosenCharacterofOpponent(Game game, Long playerid) {
      // till: get players from the game
      Long creatorid = game.getCreatorId();

      // create Long for opponentplayerid
      Long opponentid;
      // Find the opponent player (the player who is not the one making the guess)
      if (playerid.equals(creatorid)){
          opponentid = game.getInvitedPlayerId();
      } else {
          opponentid = game.getCreatorId();
      }

      // get player to get chosencharacter
      Player opponent = playerRepository.findByPlayerId(opponentid);

      return opponent.getChosencharacter();
  }

  public void saveplayerchanges(Player player){
      playerRepository.save(player);
      playerRepository.flush();
  }

    public Boolean checkStrikes(Long playerid) {
        Player player = getPlayer(playerid);
        //nedim-j: for M4 need some variable maxGuesses instead of 2
        if (player.getStrikes() == 2){
            return false;
        }
        return true;
    }

  public void increaseStrikesByOne(Long playerId) {
      //increases the number of strikes a player has by one
      Player player = playerRepository.findByPlayerId(playerId);
      player.setStrikes(player.getStrikes() + 1);
      playerRepository.save(player);
      playerRepository.flush();
    }

  public Response createResponse(Boolean guess, Long playerId) {
      // creates a response that is send back to the frontend
      Player player = playerRepository.findByPlayerId(playerId);
      Response response = new Response();
      response.setGuess(guess);
      response.setStrikes((long) player.getStrikes());
      return response;
  }

  public void increaseWinTotal(Long playerId) {
      //till: get the Player where the user is saved and there access the totalwins attribute
      Player player = getPlayer(playerId);
      User user = player.getUser();
      try {
          user.setTotalwins(user.getTotalwins() + 1);
      } catch (NullPointerException e){
          user.setTotalwins(1L);
      }
      userrepository.save(user);
      userrepository.flush();
  }

  public void increaseGamesPlayed(Long playerId){
      //till: get the Player where the user is saved and there access the totalgames attribute
      Player player = getPlayer(playerId);
      User user = player.getUser();
      user.setTotalplayed(user.getTotalplayed() + 1);
      userrepository.save(user);
      userrepository.flush();
  }

  public void updategamelobbylist(User user) {
      // deleting the game from game lobby list and setting it to null
      List<Lobby> newUserGamelobbylist = user.getUsergamelobbylist();

      if (user.getUsergamelobbylist() != null) {
          for (Lobby lobby : newUserGamelobbylist) {
              lobby.setGame(null);
          }

      //Update the user
      user.setUsergamelobbylist(newUserGamelobbylist);

      userrepository.save(user);
      userrepository.flush();
      }
  }

    //
    // check Functions
    //
  public Boolean checkIfUserOnline(Long userid){
    User user = userrepository.findUserById(userid);
    return user.getStatus() == UserStatus.ONLINE;
  }

  public Boolean checkForCorrectLobby(Long lobbyid, Long userid) {
    // gets the Lobby
    Lobby lobby = lobbyRepository.findByLobbyid(lobbyid);
    // checks if the userid is the same as the one saved in the lobby
    if (lobby.getCreator_userid().equals(userid)){
      return true;
    } else {
      return false;
        }
    }

  public Boolean checkIfUserExists(Long userid){
        User user = userrepository.findUserById(userid);
        return user != null; //user = null-> user does not exist
    }

  public Boolean checkIfPlayerinGame(Game game, Long playerid){
        // till: get players from the game
        playerRepository.findByPlayerId(playerid);

        // check if the player is in the game players list, returns true when in game and false when not
        return game.getCreatorId().equals(playerid) || game.getInvitedPlayerId().equals(playerid);
    }
}