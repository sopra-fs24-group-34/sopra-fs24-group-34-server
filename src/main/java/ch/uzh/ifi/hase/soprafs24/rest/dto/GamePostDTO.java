package ch.uzh.ifi.hase.soprafs24.rest.dto;

import java.util.List;

public class GamePostDTO {

  private Long gameid;

  private List<Long> playerid;


  private Long getGameid() {
    return gameid;
  }

  private void setGameid(Long gameid) {
    this.gameid = gameid;
  }

  private List<Long> getPlayerid() {
        return playerid;
    }

  private void setPlayerid(Long gameid) {
        this.playerid = playerid;
    }

}
