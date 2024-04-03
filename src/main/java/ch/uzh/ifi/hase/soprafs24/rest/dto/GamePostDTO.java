package ch.uzh.ifi.hase.soprafs24.rest.dto;

public class GamePostDTO {

  private Long gameid;

  private String getGameid() {
    return "game_" + gameid;
  }

  private void setGameid(Long gameid) {
    this.gameid = gameid;
  }


}
