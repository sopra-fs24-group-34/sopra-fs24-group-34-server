package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.constant.GameStatus;
import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.*;
import ch.uzh.ifi.hase.soprafs24.repository.*;
import ch.uzh.ifi.hase.soprafs24.rest.dto.*;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs24.websocket.WebSocketMessenger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class GameServiceTest {

    @Mock
    private GameRepository gameRepository;

    @Mock
    private ImageRepository imageRepository;

    @Mock
    private GameUserService gameUserService;

    @Mock
    private LobbyRepository lobbyRepository;

    @Mock
    private UnsplashService unsplashService;

    @Mock
    private LobbyService lobbyService;

    @InjectMocks
    private GameService gameService;

    private Game testGame;
    private Player testPlayer1;
    private Player testPlayer2;
    private Guess testGuess;
    private GamePostDTO gamePostDTO;
    private AuthenticationDTO authDTO;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        testGame = new Game();
        testGame.setGameId(1L);
        testGame.setCreatorPlayerId(1L);
        testGame.setInvitedPlayerId(2L);
        testGame.setCurrentRound(0);

        testPlayer1 = new Player();
        testPlayer1.setPlayerId(1L);
        testPlayer1.setUserId(1L);

        testPlayer2 = new Player();
        testPlayer2.setPlayerId(2L);
        testPlayer2.setUserId(2L);

        testGuess = new Guess();
        testGuess.setGameId(1L);
        testGuess.setPlayerId(1L);
        testGuess.setImageId(1L);

        gamePostDTO = new GamePostDTO();
        gamePostDTO.setCreator_userid(1L);
        gamePostDTO.setInvited_userid(2L);

        authDTO = new AuthenticationDTO();
        authDTO.setId(1L);
        //authDTO.setUsername("host");
    }

    @Test
    public void testGetGames() {
        when(gameRepository.findAll()).thenReturn(Collections.singletonList(testGame));

        List<Game> games = gameService.getGames();

        assertEquals(1, games.size());
        assertEquals(testGame, games.get(0));
    }

    @Test
    public void testGetGame() {
        when(gameRepository.findByGameId(anyLong())).thenReturn(testGame);

        Game game = gameService.getGame(1L);

        assertEquals(testGame, game);
    }

    @Test
    public void testChooseImage() {
        when(gameRepository.findByGameId(anyLong())).thenReturn(testGame);
        when(gameUserService.getPlayer(anyLong())).thenReturn(testPlayer1);
        when(imageRepository.findImageById(anyLong())).thenReturn(new Image());

        RoundDTO roundDTO = gameService.chooseImage(testGuess);

        assertNotNull(roundDTO);
        assertEquals(1, roundDTO.getRoundNumber());
        assertEquals(2L, roundDTO.getCurrentTurnPlayerId());
    }

    @Test
    public void testChooseImage_AlreadyChosen() {
        testPlayer1.setChosencharacter(1L);

        when(gameRepository.findByGameId(anyLong())).thenReturn(testGame);
        when(gameUserService.getPlayer(anyLong())).thenReturn(testPlayer1);
        when(imageRepository.findImageById(anyLong())).thenReturn(new Image());

        assertThrows(ResponseStatusException.class, () -> gameService.chooseImage(testGuess));
    }

    @Test
    public void testBothPlayersChosen() {
        testPlayer1.setChosencharacter(1L);
        testPlayer2.setChosencharacter(2L);

        when(gameRepository.findByGameId(anyLong())).thenReturn(testGame);
        when(gameUserService.getPlayer(anyLong())).thenReturn(testPlayer1);
        when(gameUserService.getPlayer(anyLong())).thenReturn(testPlayer2);

        boolean result = gameService.bothPlayersChosen(1L);

        assertTrue(result);
    }

//    @Test
//    public void testUpdateTurn() {
//        when(gameRepository.findByGameId(anyLong())).thenReturn(testGame);
//
//        RoundDTO roundDTO = gameService.updateTurn(1L);
//
//        assertNotNull(roundDTO);
//        assertEquals(0, roundDTO.getRoundNumber());
//        assertEquals(2L, roundDTO.getCurrentTurnPlayerId());
//    }

//    @Test
//    public void testCreateGame() {
//        Lobby testLobby = new Lobby();
//        testLobby.setLobbyid(1L);
//
//        when(lobbyRepository.findByLobbyid(anyLong())).thenReturn(testLobby);
//        when(lobbyService.isLobbyOwner(anyLong(), any(AuthenticationDTO.class))).thenReturn(true);
//        when(gameUserService.getUser(anyLong())).thenReturn(new User());
//
//        Game game = gameService.createGame(1L, gamePostDTO, authDTO);
//
//        assertNotNull(game);
//        verify(gameRepository, times(1)).save(any(Game.class));
//    }

    @Test
    public void testCreateGame_NotHost() {
        when(lobbyService.isLobbyOwner(anyLong(), any(AuthenticationDTO.class))).thenReturn(false);

        assertThrows(ResponseStatusException.class, () -> gameService.createGame(1L, gamePostDTO, authDTO));
    }

//    @Test
//    public void testDeleteGame() {
//        when(gameRepository.findByGameId(anyLong())).thenReturn(testGame);
//        when(gameUserService.getPlayer(anyLong())).thenReturn(testPlayer1).thenReturn(testPlayer2);
//        when(gameUserService.getUser(anyLong())).thenReturn(new User()).thenReturn(new User());
//
//        gameService.deleteGame(testGame);
//
//        verify(gameRepository, times(1)).delete(any(Game.class));
//    }

//    @Test
//    public void testHandleWin() {
//        when(gameUserService.getStrikes(anyLong())).thenReturn(0);
//
//        Response response = gameService.handleWin(1L, 1L);
//
//        assertNotNull(response);
//        assertEquals(GameStatus.END, response.getRoundStatus());
//    }

//    @Test
//    public void testHandleLoss() {
//        when(gameUserService.getStrikes(anyLong())).thenReturn(0);
//
//        Response response = gameService.handleLoss(1L, 1L);
//
//        assertNotNull(response);
//        assertEquals(GameStatus.END, response.getRoundStatus());
//    }

    @Test
    public void testCheckIfGameExists() {
        when(gameRepository.findByGameId(anyLong())).thenReturn(testGame);

        boolean result = gameService.checkIfGameExists(1L);

        assertTrue(result);
    }

    @Test
    public void testCheckIfImageExists() {
        when(imageRepository.findImageById(anyLong())).thenReturn(new Image());

        boolean result = gameService.checkIfImageExists(1L);

        assertTrue(result);
    }

//    @Test
//    public void testGetGameImages() {
//        when(gameRepository.findByGameId(anyLong())).thenReturn(testGame);
//
//        List<ImageDTO> images = gameService.getGameImages(1L);
//
//        assertNotNull(images);
//    }

//    @Test
//    public void testSaveGameImages() {
//        when(gameRepository.findByGameId(anyLong())).thenReturn(testGame);
//        when(unsplashService.getImageUrlsFromDatabase(anyInt(), any())).thenReturn(Collections.singletonList(new ImageDTO()));
//
//        gameService.saveGameImages(1L);
//
//        verify(gameRepository, times(1)).save(any(Game.class));
//    }

//    @Test
//    public void testReplaceGameImages() {
//        when(gameRepository.findByGameId(anyLong())).thenReturn(testGame);
//        when(unsplashService.getImageUrlsFromDatabase(anyInt(), any())).thenReturn(Collections.singletonList(new ImageDTO()));
//
//        gameService.replaceGameImages(1L);
//
//        verify(gameRepository, times(1)).save(any(Game.class));
//    }

//    @Test
//    public void testDeleteGameImage() {
//        when(gameRepository.findByGameId(anyLong())).thenReturn(testGame);
//        when(imageRepository.findImageById(anyLong())).thenReturn(new Image());
//
//        gameService.deleteGameImage(1L, 1L);
//
//        verify(imageRepository, times(1)).deleteById(anyLong());
//        verify(gameRepository, times(1)).save(any(Game.class));
//    }

//    @Test
//    public void testDatabaseImageCheck() {
//        when(imageRepository.countAllImages()).thenReturn(100);
//        doNothing().when(unsplashService).saveRandomPortraitImagesToDatabase(anyInt());
//
//        //gameService.databaseImageCheck();
//
//        verify(unsplashService, times(1)).saveRandomPortraitImagesToDatabase(anyInt());
//    }
}
