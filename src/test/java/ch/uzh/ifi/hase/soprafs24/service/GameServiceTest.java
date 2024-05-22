package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.constant.GameStatus;
import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.*;
import ch.uzh.ifi.hase.soprafs24.repository.*;
import ch.uzh.ifi.hase.soprafs24.rest.dto.*;
import ch.uzh.ifi.hase.soprafs24.websocket.WebSocketMessenger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class GameServiceTest {

    private GameService gameService;
    private GameRepository gameRepository;
    private ImageRepository imageRepository;
    private GameUserService gameUserService;
    private LobbyRepository lobbyRepository;
    private UnsplashService unsplashService;
    private LobbyService lobbyService;

    @BeforeEach
    public void setup() {
        gameRepository = mock(GameRepository.class);
        imageRepository = mock(ImageRepository.class);
        gameUserService = mock(GameUserService.class);
        lobbyRepository = mock(LobbyRepository.class);
        unsplashService = mock(UnsplashService.class);
        lobbyService = mock(LobbyService.class);
        gameService = new GameService(gameRepository, imageRepository, gameUserService, lobbyRepository, unsplashService, lobbyService);
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

        RoundDTO roundDTO = gameService.updateTurn(1L);

        assertEquals(2L, roundDTO.getCurrentTurnPlayerId());
        verify(gameRepository).save(game);
    }

    @Test
    public void testGetGameState() {
        Game game = new Game();
        game.setCurrentRound(1);
        game.setCurrentTurnPlayerId(1L);

        when(gameRepository.findByGameId(anyLong())).thenReturn(game);

        RoundDTO roundDTO = gameService.getGameState(1L);

        assertNotNull(roundDTO);
        assertEquals(1, roundDTO.getRoundNumber());
        assertEquals(1L, roundDTO.getCurrentTurnPlayerId());
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

//    @Test
//    public void testGetGameFailure() {
//        when(gameRepository.findByGameId(anyLong())).thenReturn(null);
//        assertThrows(ResponseStatusException.class, () -> gameService.getGame(1L));
//    }

//    @Test
//    public void testChooseImageSuccess() {
//        Guess guess = new Guess();
//        guess.setGameId(1L);
//        guess.setPlayerId(1L);
//        guess.setImageId(1L);
//
//        Game game = new Game();
//        Player player = new Player();
//
//        when(imageRepository.findImageById(anyLong())).thenReturn(new Image());
//        when(gameRepository.findByGameId(anyLong())).thenReturn(game);
//        when(gameUserService.getPlayer(anyLong())).thenReturn(player);
//
//        RoundDTO roundDTO = gameService.chooseImage(guess);
//
//        assertNotNull(roundDTO);
//        verify(gameUserService).savePlayerChanges(player);
//    }

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

        RoundDTO roundDTO = gameService.updateTurn(1L);

        assertEquals(2L, roundDTO.getCurrentTurnPlayerId());
        verify(gameRepository).save(game);
    }

//    @Test
//    public void testCreateGameSuccess() {
//        Lobby lobby = new Lobby();
//        GamePostDTO gamePostDTO = new GamePostDTO();
//        AuthenticationDTO authenticationDTO = new AuthenticationDTO();
//        Game game = new Game();
//        User user = new User();
//        Player player = new Player();
//        player.setPlayerId(1L);
//
//        when(lobbyRepository.findByLobbyid(anyLong())).thenReturn(lobby);
//        when(lobbyService.isLobbyOwner(anyLong(), any(AuthenticationDTO.class))).thenReturn(true);
//        when(gameUserService.getUser(anyLong())).thenReturn(user);
//        when(gameUserService.getPlayer(anyLong())).thenReturn(player);
//
//        Game createdGame = gameService.createGame(1L, gamePostDTO, authenticationDTO);
//
//        assertNotNull(createdGame);
//        verify(gameRepository).save(any(Game.class));
//    }

    @Test
    public void testCreateGameFailure() {
        Lobby lobby = new Lobby();
        GamePostDTO gamePostDTO = new GamePostDTO();
        AuthenticationDTO authenticationDTO = new AuthenticationDTO();

        when(lobbyRepository.findByLobbyid(anyLong())).thenReturn(lobby);
        when(lobbyService.isLobbyOwner(anyLong(), any(AuthenticationDTO.class))).thenReturn(false);

        assertThrows(ResponseStatusException.class, () -> gameService.createGame(1L, gamePostDTO, authenticationDTO));
    }

//    @Test
//    public void testGuessImageSuccess() {
//        Guess guess = new Guess();
//        guess.setGameId(1L);
//        guess.setPlayerId(1L);
//        guess.setImageId(1L);
//
//        Game game = new Game();
//        when(gameRepository.findByGameId(anyLong())).thenReturn(game);
//        when(gameUserService.getChosenCharacterOfOpponent(any(Game.class), anyLong())).thenReturn(1L);
//        when(gameUserService.getOpponentId(any(Game.class), anyLong())).thenReturn(2L);
//
//        Response response = gameService.guessImage(guess);
//
//        assertNotNull(response);
//    }

//    @Test
//    public void testGuessImageFailure() {
//        Guess guess = new Guess();
//        guess.setGameId(1L);
//        guess.setPlayerId(1L);
//        guess.setImageId(2L);
//
//        Game game = new Game();
//        when(gameRepository.findByGameId(anyLong())).thenReturn(game);
//        when(gameUserService.getChosenCharacterOfOpponent(any(Game.class), anyLong())).thenReturn(1L);
//        when(gameUserService.getOpponentId(any(Game.class), anyLong())).thenReturn(2L);
//
//        Response response = gameService.guessImage(guess);
//
//        assertNotNull(response);
//        verify(gameUserService).increaseStrikesByOne(anyLong());
//    }

    @Test
    public void testGetGameState2() {
        Game game = new Game();
        game.setCurrentRound(1);
        game.setCurrentTurnPlayerId(1L);

        when(gameRepository.findByGameId(anyLong())).thenReturn(game);

        RoundDTO roundDTO = gameService.getGameState(1L);

        assertNotNull(roundDTO);
        assertEquals(1, roundDTO.getRoundNumber());
        assertEquals(1L, roundDTO.getCurrentTurnPlayerId());
    }

//    @Test
//    public void testHandleWin() {
//        Player player = new Player();
//        player.setPlayerId(1L);
//
//        when(gameUserService.getStrikes(anyLong())).thenReturn(0);
//
//        Response response = gameService.handleWin(1L, 1L);
//
//        assertNotNull(response);
//        verify(gameUserService).increaseGamesPlayed(anyLong());
//        verify(gameUserService).increaseWinTotal(anyLong());
//    }

//    @Test
//    public void testHandleLoss() {
//        Player player = new Player();
//        player.setPlayerId(1L);
//
//        when(gameUserService.getStrikes(anyLong())).thenReturn(0);
//
//        Response response = gameService.handleLoss(1L, 1L);
//
//        assertNotNull(response);
//        verify(gameUserService).increaseGamesPlayed(anyLong());
//    }

//    @Test
//    public void testDeleteGame() {
//        Game game = new Game();
//        Player creator = new Player();
//        Player invitedPlayer = new Player();
//        User host = new User();
//        User invitedUser = new User();
//
//        when(gameUserService.getPlayer(game.getCreatorPlayerId())).thenReturn(creator);
//        when(gameUserService.getUser(creator.getUserId())).thenReturn(host);
//        when(gameUserService.getPlayer(game.getInvitedPlayerId())).thenReturn(invitedPlayer);
//        when(gameUserService.getUser(invitedPlayer.getUserId())).thenReturn(invitedUser);
//
//        gameService.deleteGame(game);
//
//        verify(gameUserService).saveUserChanges(host);
//        verify(gameUserService).saveUserChanges(invitedUser);
//        //verify(gameUserService).deletePlayer(creator);
//        //verify(gameUserService).deletePlayer(invitedPlayer);
//        verify(gameRepository).delete(game);
//    }



}


//package ch.uzh.ifi.hase.soprafs24.service;
//
//import ch.uzh.ifi.hase.soprafs24.constant.GameStatus;
//import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
//import ch.uzh.ifi.hase.soprafs24.entity.*;
//import ch.uzh.ifi.hase.soprafs24.repository.*;
//import ch.uzh.ifi.hase.soprafs24.rest.dto.*;
//import ch.uzh.ifi.hase.soprafs24.websocket.WebSocketMessenger;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.springframework.http.HttpStatus;
//import org.springframework.web.server.ResponseStatusException;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.Mockito.*;
//
//public class GameServiceTest {
//
//    private GameService gameService;
//    private GameRepository gameRepository;
//    private ImageRepository imageRepository;
//    private GameUserService gameUserService;
//    private LobbyRepository lobbyRepository;
//    private UnsplashService unsplashService;
//    private LobbyService lobbyService;
//
//    @BeforeEach
//    public void setup() {
//        gameRepository = mock(GameRepository.class);
//        imageRepository = mock(ImageRepository.class);
//        gameUserService = mock(GameUserService.class);
//        lobbyRepository = mock(LobbyRepository.class);
//        unsplashService = mock(UnsplashService.class);
//        lobbyService = mock(LobbyService.class);
//        gameService = new GameService(gameRepository, imageRepository, gameUserService, lobbyRepository, unsplashService, lobbyService);
//    }
//
//    @Test
//    public void testGetGames() {
//        List<Game> games = new ArrayList<>();
//        when(gameRepository.findAll()).thenReturn(games);
//        List<Game> returnedGames = gameService.getGames();
//        assertEquals(games, returnedGames);
//    }
//
//    @Test
//    public void testGetGameSuccess() {
//        Game game = new Game();
//        when(gameRepository.findByGameId(anyLong())).thenReturn(game);
//        Game returnedGame = gameService.getGame(1L);
//        assertEquals(game, returnedGame);
//    }
//
//    @Test
//    public void testGetGameFailure() {
//        when(gameRepository.findByGameId(anyLong())).thenReturn(null);
//        assertThrows(ResponseStatusException.class, () -> gameService.getGame(1L));
//    }
//
//    @Test
//    public void testChooseImageSuccess() {
//        Guess guess = new Guess();
//        guess.setGameId(1L);
//        guess.setPlayerId(1L);
//        guess.setImageId(1L);
//
//        Game game = new Game();
//        Player player = new Player();
//
//        when(imageRepository.findImageById(anyLong())).thenReturn(new Image());
//        when(gameRepository.findByGameId(anyLong())).thenReturn(game);
//        when(gameUserService.getPlayer(anyLong())).thenReturn(player);
//
//        RoundDTO roundDTO = gameService.chooseImage(guess);
//
//        assertNotNull(roundDTO);
//        verify(gameUserService).savePlayerChanges(player);
//    }
//
//    @Test
//    public void testChooseImageFailure() {
//        Guess guess = new Guess();
//        guess.setGameId(1L);
//        guess.setPlayerId(1L);
//        guess.setImageId(1L);
//
//        Player player = new Player();
//        player.setChosencharacter(1L);
//
//        when(imageRepository.findImageById(anyLong())).thenReturn(new Image());
//        when(gameRepository.findByGameId(anyLong())).thenReturn(new Game());
//        when(gameUserService.getPlayer(anyLong())).thenReturn(player);
//
//        assertThrows(ResponseStatusException.class, () -> gameService.chooseImage(guess));
//    }
//
//    @Test
//    public void testBothPlayersChosen() {
//        Game game = new Game();
//        Player player1 = new Player();
//        Player player2 = new Player();
//        player1.setChosencharacter(1L);
//        player2.setChosencharacter(1L);
//
//        when(gameRepository.findByGameId(anyLong())).thenReturn(game);
//        when(gameUserService.getPlayer(game.getCreatorPlayerId())).thenReturn(player1);
//        when(gameUserService.getPlayer(game.getInvitedPlayerId())).thenReturn(player2);
//
//        assertTrue(gameService.bothPlayersChosen(1L));
//    }
//
//    @Test
//    public void testUpdateTurnSuccess() {
//        Game game = new Game();
//        game.setCurrentTurnPlayerId(1L);
//        game.setCreatorPlayerId(1L);
//        game.setInvitedPlayerId(2L);
//
//        when(gameRepository.findByGameId(anyLong())).thenReturn(game);
//
//        RoundDTO roundDTO = gameService.updateTurn(1L);
//
//        assertEquals(2L, roundDTO.getCurrentTurnPlayerId());
//        verify(gameRepository).save(game);
//    }
//
//    @Test
//    public void testCreateGameSuccess() {
//        Lobby lobby = new Lobby();
//        GamePostDTO gamePostDTO = new GamePostDTO();
//        AuthenticationDTO authenticationDTO = new AuthenticationDTO();
//        Game game = new Game();
//        User user = new User();
//        Player player = new Player();
//        player.setPlayerId(1L);
//
//        when(lobbyRepository.findByLobbyid(anyLong())).thenReturn(lobby);
//        when(lobbyService.isLobbyOwner(anyLong(), any(AuthenticationDTO.class))).thenReturn(true);
//        when(gameUserService.getUser(anyLong())).thenReturn(user);
//        when(gameUserService.getPlayer(anyLong())).thenReturn(player);
//
//        Game createdGame = gameService.createGame(1L, gamePostDTO, authenticationDTO);
//
//        assertNotNull(createdGame);
//        verify(gameRepository).save(any(Game.class));
//    }
//
//    @Test
//    public void testCreateGameFailure() {
//        Lobby lobby = new Lobby();
//        GamePostDTO gamePostDTO = new GamePostDTO();
//        AuthenticationDTO authenticationDTO = new AuthenticationDTO();
//
//        when(lobbyRepository.findByLobbyid(anyLong())).thenReturn(lobby);
//        when(lobbyService.isLobbyOwner(anyLong(), any(AuthenticationDTO.class))).thenReturn(false);
//
//        assertThrows(ResponseStatusException.class, () -> gameService.createGame(1L, gamePostDTO, authenticationDTO));
//    }
//
//    @Test
//    public void testGuessImageSuccess() {
//        Guess guess = new Guess();
//        guess.setGameId(1L);
//        guess.setPlayerId(1L);
//        guess.setImageId(1L);
//
//        Game game = new Game();
//        when(gameRepository.findByGameId(anyLong())).thenReturn(game);
//        when(gameUserService.getChosenCharacterOfOpponent(any(Game.class), anyLong())).thenReturn(1L);
//        when(gameUserService.getOpponentId(any(Game.class), anyLong())).thenReturn(2L);
//
//        Response response = gameService.guessImage(guess);
//
//        assertNotNull(response);
//    }
//
//    @Test
//    public void testGuessImageFailure() {
//        Guess guess = new Guess();
//        guess.setGameId(1L);
//        guess.setPlayerId(1L);
//        guess.setImageId(2L);
//
//        Game game = new Game();
//        when(gameRepository.findByGameId(anyLong())).thenReturn(game);
//        when(gameUserService.getChosenCharacterOfOpponent(any(Game.class), anyLong())).thenReturn(1L);
//        when(gameUserService.getOpponentId(any(Game.class), anyLong())).thenReturn(2L);
//
//        Response response = gameService.guessImage(guess);
//
//        assertNotNull(response);
//        verify(gameUserService).increaseStrikesByOne(anyLong());
//    }
//
//    @Test
//    public void testGetGameState() {
//        Game game = new Game();
//        game.setCurrentRound(1);
//        game.setCurrentTurnPlayerId(1L);
//
//        when(gameRepository.findByGameId(anyLong())).thenReturn(game);
//
//        RoundDTO roundDTO = gameService.getGameState(1L);
//
//        assertNotNull(roundDTO);
//        assertEquals(1, roundDTO.getRoundNumber());
//        assertEquals(1L, roundDTO.getCurrentTurnPlayerId());
//    }
//
//    @Test
//    public void testHandleWin() {
//        Player player = new Player();
//        player.setPlayerId(1L);
//
//        when(gameUserService.getStrikes(anyLong())).thenReturn(0);
//
//        Response response = gameService.handleWin(1L, 1L);
//
//        assertNotNull(response);
//        verify(gameUserService).increaseGamesPlayed(anyLong());
//        verify(gameUserService).increaseWinTotal(anyLong());
//    }
//
//    @Test
//    public void testHandleLoss() {
//        Player player = new Player();
//        player.setPlayerId(1L);
//
//        when(gameUserService.getStrikes(anyLong())).thenReturn(0);
//
//        Response response = gameService.handleLoss(1L, 1L);
//
//        assertNotNull(response);
//        verify(gameUserService).increaseGamesPlayed(anyLong());
//    }
//
//    @Test
//    public void testDeleteGame() {
//        Game game = new Game();
//        Player creator = new Player();
//        Player invitedPlayer = new Player();
//        User host = new User();
//        User invitedUser = new User();
//
//        when(gameUserService.getPlayer(game.getCreatorPlayerId())).thenReturn(creator);
//        when(gameUserService.getUser(creator.getUserId())).thenReturn(host);
//        when(gameUserService.getPlayer(game.getInvitedPlayerId())).thenReturn(invitedPlayer);
//        when(gameUserService.getUser(invitedPlayer.getUserId())).thenReturn(invitedUser);
//
//        gameService.deleteGame(game);
//
//        verify(gameUserService).saveUserChanges(host);
//        verify(gameUserService).saveUserChanges(invitedUser);
//        //verify(gameUserService).deletePlayer(creator);
//        //verify(gameUserService).deletePlayer(invitedPlayer);
//        verify(gameRepository).delete(game);
//    }
//}
//
//
////package ch.uzh.ifi.hase.soprafs24.service;
////
////import ch.uzh.ifi.hase.soprafs24.constant.GameStatus;
////import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
////import ch.uzh.ifi.hase.soprafs24.entity.*;
////import ch.uzh.ifi.hase.soprafs24.repository.*;
////import ch.uzh.ifi.hase.soprafs24.rest.dto.*;
////import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
////import ch.uzh.ifi.hase.soprafs24.websocket.WebSocketMessenger;
////import org.junit.jupiter.api.BeforeEach;
////import org.junit.jupiter.api.Test;
////import org.mockito.*;
////import org.springframework.http.HttpStatus;
////import org.springframework.web.server.ResponseStatusException;
////
////import java.util.*;
////
////import static org.junit.jupiter.api.Assertions.*;
////import static org.mockito.ArgumentMatchers.*;
////import static org.mockito.Mockito.*;
////
////public class GameServiceTest {
////
////    @Mock
////    private GameRepository gameRepository;
////
////    @Mock
////    private ImageRepository imageRepository;
////
////    @Mock
////    private GameUserService gameUserService;
////
////    @Mock
////    private LobbyRepository lobbyRepository;
////
////    @Mock
////    private UnsplashService unsplashService;
////
////    @Mock
////    private LobbyService lobbyService;
////
////    @InjectMocks
////    private GameService gameService;
////
////    private Game testGame;
////    private Player testPlayer1;
////    private Player testPlayer2;
////    private Guess testGuess;
////    private GamePostDTO gamePostDTO;
////    private AuthenticationDTO authDTO;
////
////    @BeforeEach
////    public void setup() {
////        MockitoAnnotations.openMocks(this);
////
////        testGame = new Game();
////        testGame.setGameId(1L);
////        testGame.setCreatorPlayerId(1L);
////        testGame.setInvitedPlayerId(2L);
////        testGame.setCurrentRound(0);
////
////        testPlayer1 = new Player();
////        testPlayer1.setPlayerId(1L);
////        testPlayer1.setUserId(1L);
////
////        testPlayer2 = new Player();
////        testPlayer2.setPlayerId(2L);
////        testPlayer2.setUserId(2L);
////
////        testGuess = new Guess();
////        testGuess.setGameId(1L);
////        testGuess.setPlayerId(1L);
////        testGuess.setImageId(1L);
////
////        gamePostDTO = new GamePostDTO();
////        gamePostDTO.setCreatorUserId(1L);
////        gamePostDTO.setInvitedUserId(2L);
////
////        authDTO = new AuthenticationDTO();
////        authDTO.setId(1L);
////        //authDTO.setUsername("host");
////    }
////
////    @Test
////    public void testGetGames() {
////        when(gameRepository.findAll()).thenReturn(Collections.singletonList(testGame));
////
////        List<Game> games = gameService.getGames();
////
////        assertEquals(1, games.size());
////        assertEquals(testGame, games.get(0));
////    }
////
////    @Test
////    public void testGetGame() {
////        when(gameRepository.findByGameId(anyLong())).thenReturn(testGame);
////
////        Game game = gameService.getGame(1L);
////
////        assertEquals(testGame, game);
////    }
////
////    @Test
////    public void testChooseImage() {
////        when(gameRepository.findByGameId(anyLong())).thenReturn(testGame);
////        when(gameUserService.getPlayer(anyLong())).thenReturn(testPlayer1);
////        when(imageRepository.findImageById(anyLong())).thenReturn(new Image());
////
////        RoundDTO roundDTO = gameService.chooseImage(testGuess);
////
////        assertNotNull(roundDTO);
////        assertEquals(1, roundDTO.getRoundNumber());
////        assertEquals(2L, roundDTO.getCurrentTurnPlayerId());
////    }
////
////    @Test
////    public void testChooseImage_AlreadyChosen() {
////        testPlayer1.setChosencharacter(1L);
////
////        when(gameRepository.findByGameId(anyLong())).thenReturn(testGame);
////        when(gameUserService.getPlayer(anyLong())).thenReturn(testPlayer1);
////        when(imageRepository.findImageById(anyLong())).thenReturn(new Image());
////
////        assertThrows(ResponseStatusException.class, () -> gameService.chooseImage(testGuess));
////    }
////
////    @Test
////    public void testBothPlayersChosen() {
////        testPlayer1.setChosencharacter(1L);
////        testPlayer2.setChosencharacter(2L);
////
////        when(gameRepository.findByGameId(anyLong())).thenReturn(testGame);
////        when(gameUserService.getPlayer(anyLong())).thenReturn(testPlayer1);
////        when(gameUserService.getPlayer(anyLong())).thenReturn(testPlayer2);
////
////        boolean result = gameService.bothPlayersChosen(1L);
////
////        assertTrue(result);
////    }
////
//////    @Test
//////    public void testUpdateTurn() {
//////        when(gameRepository.findByGameId(anyLong())).thenReturn(testGame);
//////
//////        RoundDTO roundDTO = gameService.updateTurn(1L);
//////
//////        assertNotNull(roundDTO);
//////        assertEquals(0, roundDTO.getRoundNumber());
//////        assertEquals(2L, roundDTO.getCurrentTurnPlayerId());
//////    }
////
//////    @Test
//////    public void testCreateGame() {
//////        Lobby testLobby = new Lobby();
//////        testLobby.setLobbyid(1L);
//////
//////        when(lobbyRepository.findByLobbyid(anyLong())).thenReturn(testLobby);
//////        when(lobbyService.isLobbyOwner(anyLong(), any(AuthenticationDTO.class))).thenReturn(true);
//////        when(gameUserService.getUser(anyLong())).thenReturn(new User());
//////
//////        Game game = gameService.createGame(1L, gamePostDTO, authDTO);
//////
//////        assertNotNull(game);
//////        verify(gameRepository, times(1)).save(any(Game.class));
//////    }
////
////    @Test
////    public void testCreateGame_NotHost() {
////        when(lobbyService.isLobbyOwner(anyLong(), any(AuthenticationDTO.class))).thenReturn(false);
////
////        assertThrows(ResponseStatusException.class, () -> gameService.createGame(1L, gamePostDTO, authDTO));
////    }
////
//////    @Test
//////    public void testDeleteGame() {
//////        when(gameRepository.findByGameId(anyLong())).thenReturn(testGame);
//////        when(gameUserService.getPlayer(anyLong())).thenReturn(testPlayer1).thenReturn(testPlayer2);
//////        when(gameUserService.getUser(anyLong())).thenReturn(new User()).thenReturn(new User());
//////
//////        gameService.deleteGame(testGame);
//////
//////        verify(gameRepository, times(1)).delete(any(Game.class));
//////    }
////
//////    @Test
//////    public void testHandleWin() {
//////        when(gameUserService.getStrikes(anyLong())).thenReturn(0);
//////
//////        Response response = gameService.handleWin(1L, 1L);
//////
//////        assertNotNull(response);
//////        assertEquals(GameStatus.END, response.getRoundStatus());
//////    }
////
//////    @Test
//////    public void testHandleLoss() {
//////        when(gameUserService.getStrikes(anyLong())).thenReturn(0);
//////
//////        Response response = gameService.handleLoss(1L, 1L);
//////
//////        assertNotNull(response);
//////        assertEquals(GameStatus.END, response.getRoundStatus());
//////    }
////
////    @Test
////    public void testCheckIfGameExists() {
////        when(gameRepository.findByGameId(anyLong())).thenReturn(testGame);
////
////        boolean result = gameService.checkIfGameExists(1L);
////
////        assertTrue(result);
////    }
////
////    @Test
////    public void testCheckIfImageExists() {
////        when(imageRepository.findImageById(anyLong())).thenReturn(new Image());
////
////        boolean result = gameService.checkIfImageExists(1L);
////
////        assertTrue(result);
////    }
////
//////    @Test
//////    public void testGetGameImages() {
//////        when(gameRepository.findByGameId(anyLong())).thenReturn(testGame);
//////
//////        List<ImageDTO> images = gameService.getGameImages(1L);
//////
//////        assertNotNull(images);
//////    }
////
//////    @Test
//////    public void testSaveGameImages() {
//////        when(gameRepository.findByGameId(anyLong())).thenReturn(testGame);
//////        when(unsplashService.getImageUrlsFromDatabase(anyInt(), any())).thenReturn(Collections.singletonList(new ImageDTO()));
//////
//////        gameService.saveGameImages(1L);
//////
//////        verify(gameRepository, times(1)).save(any(Game.class));
//////    }
////
//////    @Test
//////    public void testReplaceGameImages() {
//////        when(gameRepository.findByGameId(anyLong())).thenReturn(testGame);
//////        when(unsplashService.getImageUrlsFromDatabase(anyInt(), any())).thenReturn(Collections.singletonList(new ImageDTO()));
//////
//////        gameService.replaceGameImages(1L);
//////
//////        verify(gameRepository, times(1)).save(any(Game.class));
//////    }
////
//////    @Test
//////    public void testDeleteGameImage() {
//////        when(gameRepository.findByGameId(anyLong())).thenReturn(testGame);
//////        when(imageRepository.findImageById(anyLong())).thenReturn(new Image());
//////
//////        gameService.deleteGameImage(1L, 1L);
//////
//////        verify(imageRepository, times(1)).deleteById(anyLong());
//////        verify(gameRepository, times(1)).save(any(Game.class));
//////    }
////
//////    @Test
//////    public void testDatabaseImageCheck() {
//////        when(imageRepository.countAllImages()).thenReturn(100);
//////        doNothing().when(unsplashService).saveRandomPortraitImagesToDatabase(anyInt());
//////
//////        //gameService.databaseImageCheck();
//////
//////        verify(unsplashService, times(1)).saveRandomPortraitImagesToDatabase(anyInt());
//////    }
////}
