package ch.uzh.ifi.hase.soprafs24.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
public class Game implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long gameId;

    @Column(nullable = true)
    private Long creatorPlayerId; // smailalijagic: creator

    @Column(nullable = true)
    private Long invitedPlayerId; // smailalijagic: invited user

    //@OneToOne(mappedBy = "game", cascade = CascadeType.ALL)
    @Column(length = 7000) // smailalijagic: 7000bytes --> 1750-7000 (=1000 words) chars max. possible = 65'535 bytes --> 16'383-65'535 chars
    private Chat chat = new Chat();

    @Column(nullable = true)
    private int maxStrikes; // smailalijagic: handle guesses for each player in frontend

    @Column(nullable = true)
    private int timePerRound; // smailalijagic:

    @Column
    private int currentRound;

    @Column
    private Long currentTurnPlayerId;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "game_image",
            joinColumns = @JoinColumn(name = "game_id"), // column referencing Game
            inverseJoinColumns = @JoinColumn(name = "image_id") // column referencing Image
    )
    private List<Image> gameImages; // images associated to a game

    @Column
    private boolean charactersAccepted;

    public void setCharactersAccepted(boolean charactersAccepted) {this.charactersAccepted = charactersAccepted;}

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

    public int getMaxStrikes() {
        return maxStrikes;
    }

    public void setMaxStrikes(int maxguesses) {
        this.maxStrikes = maxguesses;
    }

    public int getTimePerRound() {
        return timePerRound;
    }

    public void setTimePerRound(int timePerRound) {
        this.timePerRound = timePerRound;
    }

    public Long getCreatorPlayerId() {
        return creatorPlayerId;
    }

    public void setCreatorPlayerId(Long creatorPlayerId) {
        this.creatorPlayerId = creatorPlayerId;
    }

    public Long getInvitedPlayerId() {
        return invitedPlayerId;
    }

    public void setInvitedPlayerId(Long invitedplayerId) {
        this.invitedPlayerId = invitedplayerId;
    }

    public Long getCurrentTurnPlayerId() {
        return currentTurnPlayerId;
    }

    public void setCurrentTurnPlayerId(Long currentTurnPlayerId) {
        this.currentTurnPlayerId = currentTurnPlayerId;
    }

    public int getCurrentRound() {
        return currentRound;
    }

    public void setCurrentRound(int currentRound) {
        this.currentRound = currentRound;
    }

    public List<Image> getGameImages() {
        return gameImages;
    }

    public void setGameImages(List<Image> gameImages) { this.gameImages = gameImages;}

}