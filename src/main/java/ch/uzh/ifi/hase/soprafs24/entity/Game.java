package ch.uzh.ifi.hase.soprafs24.entity;

import javax.persistence.*;

@Entity
public class Game {
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue
  private Long gameid;

  private Long maxguesses; // smailalijagic: handle guesses for each player in frontend

  private Long user1_guess;

  private Long user2_guess;

  private String user1_pick; // smailalijagic: URL of char

  private String user2_pick;

  private Long guessingtime; // smailalijagic:

  @OneToOne(cascade = CascadeType.ALL)
  private Chat chat = new Chat();

  public Chat getChat() {
    return chat;
  }

  public String getGameid() {
    return "game_" + gameid;
  }

  public void setGameid(Long gameid) {
    this.gameid = gameid;
  }

  public Long getMaxGuesses() {
    return maxguesses;
  }

  public void setMaxGuesses(Long maxguesses) {
    this.maxguesses = maxguesses;
  }

  public String getUser1_pick() {
    return user1_pick;
  }

  public void setUser1_pick(String user1_pick) {
    this.user1_pick = user1_pick;
  }

  public String getUser2_pick() {
    return user2_pick;
  }

  public void setUser2_pick(String user2_pick) {
    this.user2_pick = user2_pick;
  }

  public Long getGuessingtime() {
    return guessingtime;
  }

  public void setGuessingtime(Long guessingtime) {
    this.guessingtime = guessingtime;
  }

  public Long getUser1_guess() {
    return user1_guess;
  }

  public void setUser1_guess(Long user1_guess) {
    this.user1_guess = user1_guess;
  }

  public Long getUser2_guess() {
    return user2_guess;
  }

  public void setUser2_guess(Long user2_guess) {
    this.user2_guess = user2_guess;
  }
}
