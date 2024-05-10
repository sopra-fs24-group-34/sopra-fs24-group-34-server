//package ch.uzh.ifi.hase.soprafs24.repository;
//
//import ch.uzh.ifi.hase.soprafs24.entity.Game;
//import ch.uzh.ifi.hase.soprafs24.repository.GameRepository;
//import ch.uzh.ifi.hase.soprafs24.service.GameService;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//public class GameRepositoryTest {
//
//    @Mock
//    private GameRepository gameRepository;
//
//    @InjectMocks
//    private GameService gameService;
//
//    @Test
//    void findByGameId_ReturnsGame_WhenGameExists() {
//        // Given
//        Long gameId = 1L;
//        Game game = new Game();
//        game.setGameId(gameId);
//        when(gameRepository.findByGameId(gameId)).thenReturn(game);
//
//        // When
//        Game found = gameService.getGame(gameId);
//
//        // Then
//        assertEquals(gameId, found.getGameId());
//    }
//
//    @Test
//    void findByGameId_ReturnsNull_WhenGameDoesNotExist() {
//        // Given
//        Long gameId = 1L;
//        when(gameRepository.findByGameId(gameId)).thenReturn(null);
//
//        // When
//        Game found = gameService.getGame(gameId);
//
//        // Then
//        assertEquals(null, found);
//    }
//}
