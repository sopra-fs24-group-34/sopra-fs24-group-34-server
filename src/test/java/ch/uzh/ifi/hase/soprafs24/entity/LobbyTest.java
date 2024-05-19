package ch.uzh.ifi.hase.soprafs24.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LobbyTest {

    @Test
    public void testSetLobbyIdSuccess() {
        // Create a Lobby instance
        Lobby lobby = new Lobby();

        // Set lobbyId
        lobby.setLobbyid(1L);

        // Verify lobbyId
        assertEquals(1L, lobby.getLobbyid());
    }

    @Test
    public void testSetCreatorUserIdSuccess() {
        // Create a Lobby instance
        Lobby lobby = new Lobby();

        // Set creatorUserId
        lobby.setCreator_userid(2L);

        // Verify creatorUserId
        assertEquals(2L, lobby.getCreator_userid());
    }

    @Test
    public void testSetInvitedUserIdSuccess() {
        // Create a Lobby instance
        Lobby lobby = new Lobby();

        // Set invitedUserId
        lobby.setInvited_userid(3L);

        // Verify invitedUserId
        assertEquals(3L, lobby.getInvited_userid());
    }

    @Test
    public void testSetLobbyTokenSuccess() {
        // Create a Lobby instance
        Lobby lobby = new Lobby();

        // Set lobbyToken
        String token = "token123";
        lobby.setToken(token);

        // Verify lobbyToken
        assertEquals(token, lobby.getlobbyToken());
    }

    @Test
    public void testSetGameSuccess() {
        // Create a Lobby instance
        Lobby lobby = new Lobby();

        // Create a Game instance
        Game game = new Game();

        // Set game
        lobby.setGame(game);

        // Verify game
        assertEquals(game, lobby.getGame());
    }
}

