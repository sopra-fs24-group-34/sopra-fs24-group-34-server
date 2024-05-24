package ch.uzh.ifi.hase.soprafs24.service.gameService;

import ch.uzh.ifi.hase.soprafs24.constant.GameStatus;
import ch.uzh.ifi.hase.soprafs24.entity.*;
import ch.uzh.ifi.hase.soprafs24.repository.GameRepository;
import ch.uzh.ifi.hase.soprafs24.repository.ImageRepository;
import ch.uzh.ifi.hase.soprafs24.repository.LobbyRepository;
import ch.uzh.ifi.hase.soprafs24.rest.dto.AuthenticationDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.GamePostDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.RoundDTO;
import ch.uzh.ifi.hase.soprafs24.service.GameService;
import ch.uzh.ifi.hase.soprafs24.service.GameUserService;
import ch.uzh.ifi.hase.soprafs24.service.LobbyService;
import ch.uzh.ifi.hase.soprafs24.service.UnsplashService;
import ch.uzh.ifi.hase.soprafs24.websocket.WebSocketMessenger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class GameServiceTest {

    private GameService gameService;
    private GameRepository gameRepository;
    private ImageRepository imageRepository;
    private GameUserService gameUserService;
    private LobbyRepository lobbyRepository;
    private UnsplashService unsplashService;
    private LobbyService lobbyService;
    private WebSocketMessenger webSocketMessenger;

    @BeforeEach
    public void setup() {
        gameRepository = mock(GameRepository.class);
        imageRepository = mock(ImageRepository.class);
        gameUserService = mock(GameUserService.class);
        lobbyRepository = mock(LobbyRepository.class);
        unsplashService = mock(UnsplashService.class);
        lobbyService = mock(LobbyService.class);
        webSocketMessenger = mock(WebSocketMessenger.class);
        gameService = new GameService(gameRepository, imageRepository, gameUserService, lobbyRepository, unsplashService, lobbyService, webSocketMessenger);
    }

    @Test
    public void testGetGames() {
        List<Game> games = new ArrayList<>();
        when(gameRepository.findAll()).thenReturn(games);
        List<Game> returnedGames = gameService.getGames();
        assertEquals(games, returnedGames);
    }

    @Test
    public void testGetGameSuccess() {
        Game game = new Game();
        when(gameRepository.findByGameId(anyLong())).thenReturn(game);
        Game returnedGame = gameService.getGame(1L);
        assertEquals(game, returnedGame);
    }

    @Test
    public void testBothPlayersChosen() {
        Game game = new Game();
        Player player1 = new Player();
        Player player2 = new Player();
        player1.setChosencharacter(1L);
        player2.setChosencharacter(1L);

        when(gameRepository.findByGameId(anyLong())).thenReturn(game);
        when(gameUserService.getPlayer(game.getCreatorPlayerId())).thenReturn(player1);
        when(gameUserService.getPlayer(game.getInvitedPlayerId())).thenReturn(player2);

        assertTrue(gameService.bothPlayersChosen(1L));
    }

    @Test
    public void testUpdateTurn() {
        Game game = new Game();
        game.setCurrentTurnPlayerId(1L);
        game.setCreatorPlayerId(1L);
        game.setInvitedPlayerId(2L);

        when(gameRepository.findByGameId(anyLong())).thenReturn(game);

        RoundDTO roundDTO = gameService.updateTurn(1L, GameStatus.GUESSING);

        assertEquals(2L, roundDTO.getCurrentTurnPlayerId());
        verify(gameRepository).save(game);
    }


    @Test
    public void testGetGames2() {
        List<Game> games = new ArrayList<>();
        when(gameRepository.findAll()).thenReturn(games);
        List<Game> returnedGames = gameService.getGames();
        assertEquals(games, returnedGames);
    }

    @Test
    public void testGetGameSuccess2() {
        Game game = new Game();
        when(gameRepository.findByGameId(anyLong())).thenReturn(game);
        Game returnedGame = gameService.getGame(1L);
        assertEquals(game, returnedGame);
    }

    @Test
    public void testChooseImageFailure() {
        Guess guess = new Guess();
        guess.setGameId(1L);
        guess.setPlayerId(1L);
        guess.setImageId(1L);

        Player player = new Player();
        player.setChosencharacter(1L);

        when(imageRepository.findImageById(anyLong())).thenReturn(new Image());
        when(gameRepository.findByGameId(anyLong())).thenReturn(new Game());
        when(gameUserService.getPlayer(anyLong())).thenReturn(player);

        assertThrows(ResponseStatusException.class, () -> gameService.chooseImage(guess));
    }

    @Test
    public void testBothPlayersChosen2() {
        Game game = new Game();
        Player player1 = new Player();
        Player player2 = new Player();
        player1.setChosencharacter(1L);
        player2.setChosencharacter(1L);

        when(gameRepository.findByGameId(anyLong())).thenReturn(game);
        when(gameUserService.getPlayer(game.getCreatorPlayerId())).thenReturn(player1);
        when(gameUserService.getPlayer(game.getInvitedPlayerId())).thenReturn(player2);

        assertTrue(gameService.bothPlayersChosen(1L));
    }

    @Test
    public void testUpdateTurnSuccess() {
        Game game = new Game();
        game.setCurrentTurnPlayerId(1L);
        game.setCreatorPlayerId(1L);
        game.setInvitedPlayerId(2L);

        when(gameRepository.findByGameId(anyLong())).thenReturn(game);

        RoundDTO roundDTO = gameService.updateTurn(1L, GameStatus.GUESSING);

        assertEquals(2L, roundDTO.getCurrentTurnPlayerId());
        verify(gameRepository).save(game);
    }

    @Test
    public void testCreateGameFailure() {
        Lobby lobby = new Lobby();
        GamePostDTO gamePostDTO = new GamePostDTO();
        AuthenticationDTO authenticationDTO = new AuthenticationDTO();

        when(lobbyRepository.findByLobbyid(anyLong())).thenReturn(lobby);
        when(lobbyService.isLobbyOwner(anyLong(), any(AuthenticationDTO.class))).thenReturn(false);

        assertThrows(ResponseStatusException.class, () -> gameService.createGame(1L, gamePostDTO, authenticationDTO));
    }

    @Test
    void testGetGameState_GameExists() {
        // Given
        Long gameId = 1L;
        Game game = new Game();
        game.setGameId(gameId);
        game.setCurrentRound(2);
        game.setCurrentTurnPlayerId(3L);
        game.setGameStatus(GameStatus.GUESSING);

        when(gameRepository.findByGameId(anyLong())).thenReturn(game);

        // When
        RoundDTO roundDTO = gameService.getGameState(gameId);

        // Then
        assertNotNull(roundDTO);
        assertEquals(2, roundDTO.getRoundNumber());
        assertEquals(3L, roundDTO.getCurrentTurnPlayerId());
        assertEquals(GameStatus.GUESSING.toString(), roundDTO.getEvent());
    }

}
