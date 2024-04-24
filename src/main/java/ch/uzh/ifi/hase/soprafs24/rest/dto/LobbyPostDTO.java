package ch.uzh.ifi.hase.soprafs24.rest.dto;

import java.util.List;

public class LobbyPostDTO {
    private Long id;

    private Long creator_userid;

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
}