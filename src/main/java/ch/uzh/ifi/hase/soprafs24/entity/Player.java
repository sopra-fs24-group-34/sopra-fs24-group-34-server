package ch.uzh.ifi.hase.soprafs24.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class Player implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long playerId;

    @Column(nullable = true)
    private Long chosencharacter;

    @Column(nullable = false)
    private int strikes;

    @Column(nullable = true)
    private Long userId;


    public Long getPlayerId() {
      return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    public Long getChosencharacter() {
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

  public Long getUserId() {
        return userId;
    }

  public void setUserId(Long userId) {
        this.userId = userId;
    }

}