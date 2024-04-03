package ch.uzh.ifi.hase.soprafs24.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Game {
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue
  private Long gameid;

  public String getGameid() {
    return "game_" + gameid;
  }

  public void setGameid(Long gameid) {
    this.gameid = gameid;
  }


}
