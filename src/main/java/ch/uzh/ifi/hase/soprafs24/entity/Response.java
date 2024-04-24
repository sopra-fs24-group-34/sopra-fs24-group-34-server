package ch.uzh.ifi.hase.soprafs24.entity;

public class Response {

    private Boolean guess;
    private Long strikes;

    public Boolean getGuess() {
        return guess;
    }

    public void setGuess(Boolean guess) {
        this.guess = guess;
    }

    public Long getStrikes() {
        return strikes;
    }

    public void setStrikes(Long strikes) {
        this.strikes = strikes;
    }
}
