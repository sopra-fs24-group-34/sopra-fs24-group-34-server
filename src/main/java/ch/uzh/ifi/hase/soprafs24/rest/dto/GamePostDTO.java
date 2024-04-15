package ch.uzh.ifi.hase.soprafs24.rest.dto;

public class GamePostDTO {

  private Long gameid;

  private Long playerid;

  private Long imageid;

  private String getGameid() {
    return "game_" + gameid;
  }

  private void setGameid(Long gameid) {
    this.gameid = gameid;
  }

  private String getPlayerid() {
        return "player_" + playerid;
    }

  private void setPlayerid(Long gameid) {
        this.playerid = playerid;
    }

  private String getImageid() {
        return "image_" + imageid;
    }

  private void setImageid(Long gameid) {
        this.imageid = imageid;
    }

}
