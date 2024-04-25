package ch.uzh.ifi.hase.soprafs24.rest.dto;

public class ResponsePostDTO {

    private Boolean guess;
    private String strikes;

    public Boolean getGuess() {
        return guess;
    }

    public void setGuess(Boolean guess) {
        this.guess = guess;
    }

    public String getStrikes() {
        return strikes;
    }

    public void setStrikes(String strikes) {
        this.strikes = strikes;
    }

}
