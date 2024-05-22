package ch.uzh.ifi.hase.soprafs24.rest.dto;

public class LobbyPostDTO {
    private Long id;

    private Long creatorUserId;

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
}