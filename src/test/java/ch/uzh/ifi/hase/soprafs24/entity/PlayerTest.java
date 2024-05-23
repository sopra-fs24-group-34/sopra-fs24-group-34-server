package ch.uzh.ifi.hase.soprafs24.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {
    @Test
    public void testSetPlayerIdSuccess() {
        // Create a Player instance
        Player player = new Player();

        // Set playerId
        player.setPlayerId(1L);

        // Verify playerId
        assertEquals(1L, player.getPlayerId());
    }

    @Test
    public void testSetChosenCharacterSuccess() {
        // Create a Player instance
        Player player = new Player();

        // Set chosenCharacter
        player.setChosencharacter(2L);

        // Verify chosenCharacter
        assertEquals(2L, player.getChosencharacter());
    }

    @Test
    public void testSetStrikesSuccess() {
        // Create a Player instance
        Player player = new Player();

        // Set strikes
        player.setStrikes(3);

        // Verify strikes
        assertEquals(3, player.getStrikes());
    }

    @Test
    public void testSetUserIdSuccess() {
        // Create a Player instance
        Player player = new Player();

        // Set userId
        player.setUserId(4L);

        // Verify userId
        assertEquals(4L, player.getUserId());
    }
}
