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
  private Long gameid;

  private Long creatorid;

  private Long invitedplayerid;

  public String getGameid() {
    return "game_" + gameid;
  }

  public void setGameid(Long gameid) {
    this.gameid = gameid;
  }

  public Long getCreatorid(){
      return creatorid;
  }

  public void setCreatorid(Long creatorid){
      this.creatorid = creatorid;
  }

  public Long getInvitedPlayerid(){
        return invitedplayerid;
    }

  public void setInvitedPlayerid(Long invitedPlayerid){
        this.invitedplayerid = invitedPlayerid;
    }

}
