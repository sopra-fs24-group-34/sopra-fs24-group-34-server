package ch.uzh.ifi.hase.soprafs24.rest.dto;

import java.util.List;

public class GamePostDTO {

    private Long creator_userid;
    private Long invited_userid;


    public Long getCreator_userid(){
        return creator_userid;
    }

    public void setCreator_userid(Long creator_userid){
        this.creator_userid = creator_userid;
    }

    public Long getInvited_userid() {
        return invited_userid;
    }

    public void setInvited_userid(Long invited_userid) {
        this.invited_userid = invited_userid;
    }
}