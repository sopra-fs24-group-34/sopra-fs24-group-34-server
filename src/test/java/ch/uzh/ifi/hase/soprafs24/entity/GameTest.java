package ch.uzh.ifi.hase.soprafs24.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GameTest {

    @Test
    public void testSetGameIdSuccess() {
        // Create a Game instance
        Game game = new Game();

        // Set gameId
        game.setGameId(1L);

        // Verify gameId
        assertEquals(1L, game.getGameId());
    }

    @Test
    public void testSetMaxGuessesSuccess() {
        // Create a Game instance
        Game game = new Game();

        // Set maxGuesses
        game.setMaxGuesses(5L);

        // Verify maxGuesses
        assertEquals(5L, game.getMaxGuesses());
    }

    @Test
    public void testSetCreatorPlayerIdSuccess() {
        // Create a Game instance
        Game game = new Game();

        // Set creatorPlayerId
        game.setCreatorPlayerId(10L);

        // Verify creatorPlayerId
        assertEquals(10L, game.getCreatorPlayerId());
    }

    @Test
    public void testSetInvitedPlayerIdSuccess() {
        // Create a Game instance
        Game game = new Game();

        // Set invitedPlayerId
        game.setInvitedPlayerId(20L);

        // Verify invitedPlayerId
        assertEquals(20L, game.getInvitedPlayerId());
    }

    @Test
    public void testSetGuessingTimeSuccess() {
        // Create a Game instance
        Game game = new Game();

        // Set guessingTime
        game.setGuessingtime(30L);

        // Verify guessingTime
        assertEquals(30L, game.getGuessingtime());
    }
}
