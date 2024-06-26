package ch.uzh.ifi.hase.soprafs24.repository;

import ch.uzh.ifi.hase.soprafs24.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository("playerRepository")
public interface PlayerRepository extends JpaRepository <Player, Long>{
    Player findByPlayerId(Long playerId);
}