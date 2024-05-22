package ch.uzh.ifi.hase.soprafs24.rest.dto;

public class LobbyPutDTO {
  private Long id;

  private Long creator_userid;

  private Long invited_userid;

  private String lobbyToken;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getCreator_userid() {
    return creator_userid;
  }

  public void setCreator_userid(Long creator_userid) {
    this.creator_userid = creator_userid;
  }

  public Long getInvited_userid() {
    return invited_userid;
  }

  public void setInvited_userid(Long invited_userid) {
    this.invited_userid = invited_userid;
  }

//  public String getLobbyToken() {
//    return lobbyToken;
//  }

  public void setLobbyToken(String lobbyToken) {
    this.lobbyToken = lobbyToken;
  }

}
