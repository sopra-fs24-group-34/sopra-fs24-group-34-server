package ch.uzh.ifi.hase.soprafs24.rest.dto;

public class GamePostDTO {

    private Long creatorUserId;
    private Long invitedUserId;
    private int maxStrikes;

    public Long getCreatorUserId(){
        return creatorUserId;
    }

    public void setCreatorUserId(Long creatorUserId){
        this.creatorUserId = creatorUserId;
    }

    public Long getInvitedUserId() {
        return invitedUserId;
    }

    public void setInvitedUserId(Long invitedUserId) {
        this.invitedUserId = invitedUserId;
    }

    public void setMaxStrikes(int maxStrikes) {
        this.maxStrikes = maxStrikes;
    }

    public int getMaxStrikes() {
        return maxStrikes;
    }

}