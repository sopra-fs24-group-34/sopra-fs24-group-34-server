package ch.uzh.ifi.hase.soprafs24.rest.dto;

public class LobbyGetDTO {
  private Long id;

  private Long creatorUserId;

  private Long invitedUserId;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getCreatorUserId() {
    return creatorUserId;
  }

  public void setCreatorUserId(Long creatorUserId) {
    this.creatorUserId = creatorUserId;
  }

  public Long getInvitedUserId() {
    return invitedUserId;
  }

  public void setInvitedUserId(Long invitedUserId) {
    this.invitedUserId = invitedUserId;
  }

}
