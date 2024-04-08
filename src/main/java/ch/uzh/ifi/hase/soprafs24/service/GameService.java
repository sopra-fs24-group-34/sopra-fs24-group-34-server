package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.entity.Game;
import ch.uzh.ifi.hase.soprafs24.repository.GameRepository;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import com.zaxxer.hikari.SQLExceptionOverride;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class GameService {
  private final GameRepository gameRepository;
  @Autowired
  public GameService(@Qualifier("gameRepository") GameRepository gameRepository) {
    this.gameRepository = gameRepository;
  }

  public Boolean checkIfGameExists(Long gameId) {
    try {
      assert gameRepository.findByGameid(gameId) != null;
      return true;
    } catch (Exception e) {
      return false;
    }
  }
}
