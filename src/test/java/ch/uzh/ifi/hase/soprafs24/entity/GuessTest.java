package ch.uzh.ifi.hase.soprafs24.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GuessTest {

    @Test
    public void testSetIdSuccess() {
        // Create a Guess instance
        Guess guess = new Guess();

        // Set id
        guess.setId(1L);

        // Verify id
        assertEquals(1L, guess.getId());
    }

    @Test
    public void testSetGameIdSuccess() {
        // Create a Guess instance
        Guess guess = new Guess();

        // Set gameId
        guess.setGameId(2L);

        // Verify gameId
        assertEquals(2L, guess.getGameId());
    }

    @Test
    public void testSetPlayerIdSuccess() {
        // Create a Guess instance
        Guess guess = new Guess();

        // Set playerId
        guess.setPlayerId(3L);

        // Verify playerId
        assertEquals(3L, guess.getPlayerId());
    }

    @Test
    public void testSetImageIdSuccess() {
        // Create a Guess instance
        Guess guess = new Guess();

        // Set imageId
        guess.setImageId(4L);

        // Verify imageId
        assertEquals(4L, guess.getImageId());
    }
}
