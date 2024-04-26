package ch.uzh.ifi.hase.soprafs24.entity;

import ch.uzh.ifi.hase.soprafs24.constant.RoundStatus;

public class Response {

    private Boolean guess;
    private Long playerId;
    private int strikes;
    private RoundStatus roundStatus;

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

    public RoundStatus getRoundStatus() {
        return roundStatus;
    }

    public void setRoundStatus(RoundStatus roundStatus) {
        this.roundStatus = roundStatus;
    }
}
