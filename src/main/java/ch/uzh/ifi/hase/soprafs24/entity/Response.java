package ch.uzh.ifi.hase.soprafs24.entity;

import ch.uzh.ifi.hase.soprafs24.constant.GameStatus;

public class Response {

    private Boolean guess;
    private Long playerId;
    private int strikes;
    private GameStatus gameStatus;

    public Boolean getGuess() {
        return guess;
    }

    public void setGuess(Boolean guess) {
        this.guess = guess;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    public int getStrikes() {
        return strikes;
    }

    public void setStrikes(int strikes) {
        this.strikes = strikes;
    }

    public GameStatus getRoundStatus() {
        return gameStatus;
    }

    public void setRoundStatus(GameStatus gameStatus) {
        this.gameStatus = gameStatus;
    }
}
