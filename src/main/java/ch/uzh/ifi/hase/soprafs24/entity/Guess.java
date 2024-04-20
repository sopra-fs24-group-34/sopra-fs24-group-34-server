package ch.uzh.ifi.hase.soprafs24.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class Guess {

    @Id
    @GeneratedValue
    private Long id;

    // Other attributes and methods



    @Column(name = "game_id")
    private Long gameId;

    @Column(name = "player_id")
    private Long playerId;

    @Column(name = "image_id")
    private Long imageId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameid) {
        this.gameId = gameid;
    }

    public Long getPlayerId(){
        return playerId;
    }

    public void setPlayerId(Long playerid) {
            this.playerId = playerid;
        }

    public Long getImageId() {
        return imageId;
    }

    public void setImageId(Long imageid) {
            this.imageId = imageid;
        }


}

