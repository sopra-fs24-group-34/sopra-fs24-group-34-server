package ch.uzh.ifi.hase.soprafs24.rest.dto;

public class GameGetDTO {

    private Long gameId;
    private Long creatorPlayerId;
    private Long invitedPlayerId;
    private int maxStrikes;

    public Long getGameId(){
        return gameId;
    }
    public void setGameId(Long gameId){
        this.gameId = gameId;
    }

    public Long getCreatorPlayerId(){
        return creatorPlayerId;
    }

    public void setCreatorPlayerId(Long creatorPlayerId){ this.creatorPlayerId = creatorPlayerId; }

    public Long getInvitedPlayerId() {
        return invitedPlayerId;
    }

    public void setInvitedPlayerId(Long invitedUserid) { this.invitedPlayerId = invitedUserid; }

    public void setMaxStrikes(int maxStrikes) {
        this.maxStrikes = maxStrikes;
    }

    public int getMaxStrikes() {
        return maxStrikes;
    }

}