package ch.uzh.ifi.hase.soprafs24.entity;

import javax.persistence.Embeddable;

@Embeddable
public class LobbyInvitation {

    private String creatorUsername;

    private String creatorIcon;

    private Long lobbyId;

    private Long creatorId;

    public String getCreatorUsername() {
        return creatorUsername;
    }

    public void setCreatorUsername(String creatorUsername) {
        this.creatorUsername = creatorUsername;
    }

    public void setLobbyId(Long lobbyId) {
        this.lobbyId = lobbyId;
    }

    public Long getLobbyId() {
        return lobbyId;
    }

    public String getCreatorIcon() {
        return creatorIcon;
    }

    public void setCreatorIcon(String creatorIcon) {
        this.creatorIcon = creatorIcon;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }
}
