package ch.uzh.ifi.hase.soprafs24.rest.dto;

import java.util.List;

public class GamePostDTO {
  private Long gameid;
  private Long creatorid;
  private Long invitedplayerid;

  public Long getGameid() {
    return gameid;
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

  public Long getInvitedplayerid() {
    return invitedplayerid;
  }

  public void setInvitedplayeridid(Long invitedplayerid) {
    this.invitedplayerid = invitedplayerid;
  }
}