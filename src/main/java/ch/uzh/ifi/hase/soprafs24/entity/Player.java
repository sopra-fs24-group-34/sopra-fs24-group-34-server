package ch.uzh.ifi.hase.soprafs24.entity;

import javax.persistence.*;
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

  @OneToOne
  @JoinColumn(name = "id")
  private User user;


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

  public User getUser() {
        return user;
    }

  public void setUser(User user) {
        this.user = user;
    }

}