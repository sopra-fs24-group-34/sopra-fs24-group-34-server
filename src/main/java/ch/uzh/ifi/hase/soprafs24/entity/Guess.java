package ch.uzh.ifi.hase.soprafs24.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import java.io.Serializable;

@Entity
public class Guess {

    @EmbeddedId
    private GuessKey id;

    // Other attributes and methods

    public GuessKey getId() {
        return id;
    }

    public void setId(GuessKey id) {
        this.id = id;
    }

    @Embeddable
    public static class GuessKey implements Serializable {
        @Column(name = "gameid")
        private Long gameid;

        @Column(name = "playerid")
        private Long playerid;

        @Column(name = "imageid")
        private Long imageid;

    public Long getGameId() {
        return gameid;
    }

    public Long getPlayerId(){
        return playerid;
    }

    public Long getImageId() {
        return imageid;
    }

    }
}

