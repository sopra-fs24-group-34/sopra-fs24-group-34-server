package ch.uzh.ifi.hase.soprafs24.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.List;

@Entity
public class Game {
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue
  private Long gameId;

  private Long creatorId;

  private Long invitedPlayerId;

  public Long getGameId() {
    return gameId;
  }

  public void setGameId(Long gameId) {
    this.gameId = gameId;
  }

  public Long getCreatorId(){
      return creatorId;
  }

  public void setCreatorId(Long creatorid){
      this.creatorId = creatorid;
  }

  public Long getInvitedPlayerId(){
        return invitedPlayerId;
    }

  public void setInvitedPlayerId(Long invitedPlayerid){
        this.invitedPlayerId = invitedPlayerid;
    }

}
