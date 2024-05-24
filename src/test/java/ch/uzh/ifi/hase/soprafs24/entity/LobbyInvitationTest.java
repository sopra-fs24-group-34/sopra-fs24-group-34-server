package ch.uzh.ifi.hase.soprafs24.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LobbyInvitationTest {
    @Test
    public void testSetCreatorUsernameSuccess() {
        // Create a LobbyInvitation instance
        LobbyInvitation invitation = new LobbyInvitation();

        // Set creatorUsername
        invitation.setCreatorUsername("JohnDoe");

        // Verify creatorUsername
        assertEquals("JohnDoe", invitation.getCreatorUsername());
    }

    @Test
    public void testSetLobbyIdSuccess() {
        // Create a LobbyInvitation instance
        LobbyInvitation invitation = new LobbyInvitation();

        // Set lobbyId
        invitation.setLobbyId(1L);

        // Verify lobbyId
        assertEquals(1L, invitation.getLobbyId());
    }

    @Test
    public void testSetCreatorIconSuccess() {
        // Create a LobbyInvitation instance
        LobbyInvitation invitation = new LobbyInvitation();

        // Set creatorIcon
        invitation.setCreatorIcon("icon123");

        // Verify creatorIcon
        assertEquals("icon123", invitation.getCreatorIcon());
    }

    @Test
    public void testSetCreatorIdSuccess() {
        // Create a LobbyInvitation instance
        LobbyInvitation invitation = new LobbyInvitation();

        // Set creatorId
        invitation.setCreatorId(2L);

        // Verify creatorId
        assertEquals(2L, invitation.getCreatorId());
    }
}

