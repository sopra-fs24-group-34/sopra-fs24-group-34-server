package ch.uzh.ifi.hase.soprafs24.entity;

import ch.uzh.ifi.hase.soprafs24.constant.GameStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ResponseTest {
    @Test
    public void testSetGuessSuccess() {
        // Create a Response instance
        Response response = new Response();

        // Set guess
        response.setGuess(true);

        // Verify guess
        assertTrue(response.getGuess());
    }

    @Test
    public void testSetPlayerIdSuccess() {
        // Create a Response instance
        Response response = new Response();

        // Set playerId
        response.setPlayerId(1L);

        // Verify playerId
        assertEquals(1L, response.getPlayerId());
    }

    @Test
    public void testSetStrikesSuccess() {
        // Create a Response instance
        Response response = new Response();

        // Set strikes
        response.setStrikes(3);

        // Verify strikes
        assertEquals(3, response.getStrikes());
    }

    @Test
    public void testSetRoundStatusSuccess() {
        // Create a Response instance
        Response response = new Response();

        // Set roundStatus
        response.setRoundStatus(GameStatus.GUESSING);

        // Verify roundStatus
        assertEquals(GameStatus.GUESSING, response.getRoundStatus());
    }
}

