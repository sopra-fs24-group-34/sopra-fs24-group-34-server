package ch.uzh.ifi.hase.soprafs24.rest.dto;

public class LobbyInvitationPostDTO {

    private Long creatorId;

    private String invitedUserName;

    private Long lobbyId;

    public Long getCreatorId() {
        return creatorId;
    }

    public String getInvitedUserName() {
        return invitedUserName;
    }

    public Long getLobbyId() {
        return lobbyId;
    }

    public void setLobbyId(Long lobbyId) {
        this.lobbyId = lobbyId;
    }
}
