package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.entity.Player;
import ch.uzh.ifi.hase.soprafs24.repository.GameRepository;
import ch.uzh.ifi.hase.soprafs24.repository.ImageRepository;
import ch.uzh.ifi.hase.soprafs24.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;


import javax.transaction.Transactional;

@Service
@Transactional
public class GameService {
  private final GameRepository gameRepository;

  private final ImageRepository imageRepository;

  private final PlayerRepository playerRepository;



  @Autowired
  public GameService(@Qualifier("gameRepository") GameRepository gameRepository, @Qualifier("imageRepository") ImageRepository imageRepository, @Qualifier("playerRepository") PlayerRepository playerRepository) {
    this.gameRepository = gameRepository;
    this.imageRepository = imageRepository;
    this.playerRepository = playerRepository;
  }

  public void chooseimage(Long imageId, Long playerId) {
    checkIfGameExists(imageId);
    Player player = playerRepository.findPlayerById(playerId);    //player gets searched to set the chosen character of that player
    if (player.getchosencharacter() == null){
        player.setChosencharacter(imageId);
    }
    else{
        throw new IllegalStateException("The player has already chosen a character.");  //if player has already chosen character Exception is thrown
    }
    }


  public Boolean checkIfImageExists(Long imageId) {
      try{
          assert imageRepository.findByImageId(imageId) != null;   //checks if Image is in the imageRepository
          return true;
      } catch (Exception e){
          return false;
      }

  }

  public Boolean checkIfGameExists(Long gameId) {
    try {
      assert gameRepository.findByGameid(gameId) != null;
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  public void guessimage(Long imageId) {






    }
}
