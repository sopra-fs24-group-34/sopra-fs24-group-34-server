package ch.uzh.ifi.hase.soprafs24.rest.dto;

public class LobbyInvitationPutDTO {

    private Long creatorId;

    private Long invitedUserId;

    private Long lobbyId;

    private boolean answer;

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

    public Long getLobbyId() {
        return lobbyId;
    }

    public void setLobbyId(Long lobbyId) {
        this.lobbyId = lobbyId;
    }

    public boolean getAnswer() {
        return answer;
    }

    public void setAnswer(boolean answer) {
        this.answer = answer;
    }
}
