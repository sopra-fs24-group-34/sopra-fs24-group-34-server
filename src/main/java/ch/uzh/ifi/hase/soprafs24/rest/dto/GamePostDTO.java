package ch.uzh.ifi.hase.soprafs24.rest.dto;

public class GamePostDTO {

    private Long creator_userid;
    private Long invited_userid;
    private int maxStrikes;
    private int timePerRound;

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

    public void setMaxStrikes(int maxStrikes) {
        this.maxStrikes = maxStrikes;
    }

    public int getMaxStrikes() {
        return maxStrikes;
    }

    public void setTimePerRound(int timePerRound) {
        this.timePerRound = timePerRound;
    }

    public int getTimePerRound() {
        return timePerRound;
    }

}