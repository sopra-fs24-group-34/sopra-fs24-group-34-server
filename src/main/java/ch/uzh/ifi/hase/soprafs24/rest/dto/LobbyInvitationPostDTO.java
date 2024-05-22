package ch.uzh.ifi.hase.soprafs24.rest.dto;

public class LobbyInvitationPostDTO {

    private Long creatorId;

    private String invitedUserName;

    private Long lobbyId;

    public Long getCreatorId() {
        return creatorId;
    }

//    public void setCreatorId(Long creatorId) {
//        this.creatorId = creatorId;
//    }

//    public void setInvitedUserName(String invitedUserName) {
//        this.invitedUserName = invitedUserName;
//    }

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
