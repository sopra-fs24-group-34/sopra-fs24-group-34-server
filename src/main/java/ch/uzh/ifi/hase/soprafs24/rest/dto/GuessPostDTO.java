package ch.uzh.ifi.hase.soprafs24.rest.dto;

public class GuessPostDTO {
  private Long gameid;
  private Long playerid;
  private Long imageid;

  public Long getGameid() {
    return gameid;
  }

  public void setGameid(Long gameid) {
    this.gameid = gameid;
  }

  public Long getPlayerid() {
    return playerid;
  }

  public void setPlayerid(Long playerid) {
    this.playerid = playerid;
  }

  public Long getImageid(){
    return imageid;
  }

  public void setImageid(Long imageid) {
    this.imageid = imageid;
  }
}