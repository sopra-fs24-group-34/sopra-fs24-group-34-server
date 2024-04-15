package ch.uzh.ifi.hase.soprafs24.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Player {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long playerid;


    @Column(nullable = true)
    private Long chosencharacter;

    @Column(nullable = false)
    private int strikes;


    public Long getPlayerid() {
        return playerid;
    }

    public void setPlayerid(Long playerid) {
        this.playerid = playerid;
    }

    public Long getchosencharacter() {
        return chosencharacter;
    }

    public void setChosencharacter(Long chosencharacter) {
        this.chosencharacter = chosencharacter;
    }

    public int getStrikes() {
        return strikes;
    }

    public void setStrikes(int strikes) {
        this.strikes = strikes;
    }

}
