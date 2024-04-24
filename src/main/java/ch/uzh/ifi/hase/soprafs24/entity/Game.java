package ch.uzh.ifi.hase.soprafs24.entity;


import javax.persistence.*;
import java.util.List;

@Entity
public class Game {
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue
  private Long gameId;

  private Long maxguesses; // smailalijagic: handle guesses for each player in frontend

  private Long creatorId; // smailalijagic: creator

  private Long invitedplayerId; // smailalijagic: invited user

  private Long guessingtime; // smailalijagic:
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true) //orphanremoval -> instant deletion from db if delted from game
    @JoinColumn(name = "game_id")
    private List<Image> gameImages; // dario: images associated to a game


    @OneToOne(cascade = CascadeType.ALL)
  private Chat chat = new Chat();

  public Chat getChat() {
    return chat;
  }

  public Long getGameId() {
    return gameId;
  }

  public void setGameId(Long gameId) {
    this.gameId = gameId;
  }

  public Long getMaxGuesses() {
    return maxguesses;
  }

  public void setMaxGuesses(Long maxguesses) {
    this.maxguesses = maxguesses;
  }

  public Long getGuessingtime() {
    return guessingtime;
  }

  public void setGuessingtime(Long guessingtime) {
    this.guessingtime = guessingtime;
  }

  public Long getCreatorId() {
    return creatorId;
  }

  public void setCreatorId(Long creatorId) {
    this.creatorId = creatorId;
  }

  public Long getInvitedPlayerId() {
    return invitedplayerId;
  }

  public void setInvitedPlayerId(Long invitedplayerId) {
    this.invitedplayerId = invitedplayerId;
  }

  public List<Image> getGameImages() {
        return gameImages;
    }
    public void setGameImages(List<Image> gameImages) {
        this.gameImages = gameImages;
    }
}
