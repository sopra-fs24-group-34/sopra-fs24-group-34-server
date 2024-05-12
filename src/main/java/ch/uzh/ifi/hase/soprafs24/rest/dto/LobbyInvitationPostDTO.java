package ch.uzh.ifi.hase.soprafs24.rest.dto;

public class LobbyInvitationPostDTO {

    private Long creatorId;

    private Long invitedUserId;

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public void setInvitedUserId(Long invitedUserId) {
        this.invitedUserId = invitedUserId;
    }

    public Long getInvitedUserId() {
        return invitedUserId;
    }
}
