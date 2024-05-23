package ch.uzh.ifi.hase.soprafs24.repository;

import ch.uzh.ifi.hase.soprafs24.entity.Lobby;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LobbyRepositoryTest {

    @Mock
    private LobbyRepository lobbyRepository;

    @Test
    void findByLobbyid_ReturnsLobby_WhenLobbyExists() {
        // Given
        Long lobbyId = 1L;
        Lobby lobby = new Lobby();
        lobby.setLobbyid(lobbyId);
        when(lobbyRepository.findByLobbyid(lobbyId)).thenReturn(lobby);

        // When
        Lobby found = lobbyRepository.findByLobbyid(lobbyId);

        // Then
        assertEquals(lobbyId, found.getLobbyid());
    }

    @Test
    void findByLobbyid_ReturnsNull_WhenLobbyDoesNotExist() {
        // Given
        Long lobbyId = 1L;
        when(lobbyRepository.findByLobbyid(lobbyId)).thenReturn(null);

        // When
        Lobby found = lobbyRepository.findByLobbyid(lobbyId);

        // Then
        assertEquals(null, found);
    }
}
