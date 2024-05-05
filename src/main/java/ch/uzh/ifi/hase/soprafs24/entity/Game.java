package ch.uzh.ifi.hase.soprafs24.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import ch.uzh.ifi.hase.soprafs24.entity.Chat;

@Entity
public class Game implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long gameId;

    @Column(nullable = true)
    private Long maxguesses; // smailalijagic: handle guesses for each player in frontend

    @Column(nullable = true)
    private Long creatorId; // smailalijagic: creator

    @Column(nullable = true)
    private Long invitedplayerId; // smailalijagic: invited user

    @Column(nullable = true)
    private Long guessingtime; // smailalijagic:

    //@OneToOne(mappedBy = "game", cascade = CascadeType.ALL)
    private Chat chat = new Chat();

    @ManyToMany
    @JoinTable(
            name = "game_image",
            joinColumns = @JoinColumn(name = "game_id"), // column referencing Game
            inverseJoinColumns = @JoinColumn(name = "image_id") // column referencing Image
    )
    private List<Image> gameImages; // images associated to a game

    //@OneToOne(cascade = CascadeType.ALL)
    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) { this.chat = chat; }

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

    public void setGameImages(List<Image> gameImages) { this.gameImages = gameImages;}

}