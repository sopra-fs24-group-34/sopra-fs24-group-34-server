package ch.uzh.ifi.hase.soprafs24.rest.dto;

public class RoundDTO {

    private int roundNumber;
    private Long currentTurnPlayerId;

    public RoundDTO(int roundNumber, Long currentTurnPlayerId) {
        this.roundNumber = roundNumber;
        this.currentTurnPlayerId = currentTurnPlayerId;
    }

//    public int getRoundNumber() {
//        return roundNumber;
//    }
//
//    public void setRoundNumber(int roundNumber) {
//        this.roundNumber = roundNumber;
//    }
//
//    public Long getCurrentTurnPlayerId() {
//        return currentTurnPlayerId;
//    }
//
//    public void setCurrentTurnPlayerId(Long currentTurnPlayerId) {
//        this.currentTurnPlayerId = currentTurnPlayerId;
//    }
}
