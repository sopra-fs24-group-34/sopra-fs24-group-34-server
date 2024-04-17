package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.Game;
import ch.uzh.ifi.hase.soprafs24.entity.Lobby;
import ch.uzh.ifi.hase.soprafs24.entity.Player;
import ch.uzh.ifi.hase.soprafs24.entity.User;
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


    public Player getUser(Long playerid){
        return playerRepository.findPlayerById(playerid);
    }


    public Player createplayer(Long userid){
        Player player = new Player();
        player.setPlayerid(userid);

        //saving PLayer to repository
        player = playerRepository.save(player);
        playerRepository.flush();

        return player;

    }


    public Long getChosenCharacterofOpponent(Game game, Long playerid) {
        // till: get players from the game
        Long creatorid = game.getCreatorid();


        // create Long for opponentplayerid
        Long opponentid;
        // Find the opponent player (the player who is not the one making the guess)
        if (playerid.equals(creatorid)){
            opponentid = game.getInvitedPlayerid();
        } else {
            opponentid = game.getCreatorid();
        }

        // get player to get chosencharacter
        Player opponent = playerRepository.findPlayerById(opponentid);

        return opponent.getchosencharacter();


    }


    public Boolean increaseandcheckStrikes(Long playerid){
        Player player = getUser(playerid);
        if (player.getStrikes() == 2){
            return false;
        } else {
            player.setStrikes(player.getStrikes() + 1);
            return true;
        }
    }



    //
    // check Functions
    //


    public Boolean checkIfUserExists(Long userid){
            User user = userrepository.findUserById(userid);

            return user != null; //user = null-> user does not exist

    }

    public Boolean checkIfUserOnline(Long userid){
        User user = userrepository.findUserById(userid);
        return user.getStatus() == UserStatus.ONLINE;

    }

    public Boolean checkForCorrectLobby(Long lobbyid, Long userid) {
        // gets the Lobby
        Lobby lobby = lobbyRepository.findByLobbyid(lobbyid);
        // checks if the userid is the same as the one saved in the lobby
        if (lobby.getUser() == userid){
            return true;
        } else {
            return false;
        }
    }

    public Boolean checkIfPlayerinGame(Game game, Long playerid){
        // till: get players from the game
        playerRepository.findPlayerById(playerid);

        // check if the player is in the game players list, returns true when in game and false when not
        return game.getCreatorid().equals(playerid) || game.getInvitedPlayerid().equals(playerid);






    }
}
