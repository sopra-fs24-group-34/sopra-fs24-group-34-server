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
        game.setMaxStrikes(5);

        // Verify maxGuesses
        assertEquals(5, game.getMaxStrikes());
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
        game.setTimePerRound(30);

        // Verify guessingTime
        assertEquals(30, game.getTimePerRound());
    }
}
