package ch.uzh.ifi.hase.soprafs24.service.GameUserServiceTest;
import ch.uzh.ifi.hase.soprafs24.constant.GameStatus;
import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.*;
import ch.uzh.ifi.hase.soprafs24.repository.GameRepository;
import ch.uzh.ifi.hase.soprafs24.repository.PlayerRepository;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs24.rest.dto.RoundDTO;
import ch.uzh.ifi.hase.soprafs24.service.GameUserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class GameUserServiceTest {

    @Mock
    private GameRepository gamerepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PlayerRepository playerrepository;

    @InjectMocks
    private GameUserService gameUserService;


    private Player player;

    private User user;


    @BeforeEach
    void setup(){
        player = new Player();
        player.setPlayerId(1L);
        player.setStrikes(0);
        playerrepository.save(player);
        playerrepository.flush();
    }

    @AfterEach
    void teardown() {
        userRepository.deleteAll();
    }

    @Test
    void getPlayer_validInputs() {
        when(playerrepository.findByPlayerId(anyLong())).thenReturn(player);

        Player result = gameUserService.getPlayer(1L);

        assertEquals(result, player);
    }

    @Test
    void getUser_validInputs() {
        user = new User();
        user.setId(1L);


        when(userRepository.findUserById(anyLong())).thenReturn(user);

        User result = gameUserService.getUser(1L);

        assertEquals(result, user);
    }

    /**
    @Test
    void createPlayer_validInputs() {
        List<Player> list = new ArrayList<>();

        Player newplayer = gameUserService.createPlayer(1L);
        list.add(newplayer);

        assertEquals(list.size(), 1);
    }*/

    @Test
    void getOpponentId_validInputs(){
        Player player2 = new Player();
        player2.setPlayerId(2L);
        Game game = new Game();
        game.setCreatorPlayerId(1L);
        game.setInvitedPlayerId(2L);

        Long result = gameUserService.getOpponentId(game, player.getPlayerId());

        assertEquals(result, 2L);
    }

    @Test
    void getChosenCharacterofOpponent_validInputs() {
        Image image = new Image();
        image.setId(1L);
        Player opp = new Player();
        opp.setPlayerId(2L);
        opp.setChosencharacter(1L);
        Game game = new Game();
        game.setCreatorPlayerId(1L);
        game.setInvitedPlayerId(2L);

        when(playerrepository.findByPlayerId(2L)).thenReturn(opp);

        Long result = gameUserService.getChosenCharacterOfOpponent(game, 1L);

        assertEquals(result, 1L);
    }


    // Test for save User and save player changes?

    @Test
    void getStrikes_validInput(){
        when(gameUserService.getPlayer(1L)).thenReturn(player);

        Integer number = gameUserService.getStrikes(1L);

        assertEquals(number,0);
    }

    @Test
    void increaseStrikesByOne_validInputs(){
        when(playerrepository.findByPlayerId(1L)).thenReturn(player);

        assertEquals(player.getStrikes(), 0);

        gameUserService.increaseStrikesByOne(1L);

        assertEquals(player.getStrikes(), 1);
    }

    @Test
    void determineStatus_wrongInputs() {
        Player invited = new Player();
        invited.setPlayerId(2L);
        Game game = new Game();
        game.setGameId(1L);
        game.setCreatorPlayerId(player.getPlayerId());
        game.setInvitedPlayerId(invited.getPlayerId());
        gamerepository.save(game);
        gamerepository.flush();

        when(gamerepository.findByGameId(1L)).thenReturn(game);
        when(playerrepository.findByPlayerId(1L)).thenReturn(player);
        when(playerrepository.findByPlayerId(2L)).thenReturn(invited);

        GameStatus result = gameUserService.determineGameStatus(1L);

        assertEquals(result, GameStatus.CHOOSING);
    }

    @Test
    void createResponse_validInputs() {
        RoundDTO roundDTO = new RoundDTO(1, 1L);
        Response response = new Response();
        response.setGuess(true);
        response.setPlayerId(1L);
        response.setStrikes(0);
        response.setRoundStatus(GameStatus.GUESSING);
        response.setRoundDTO(roundDTO);


        Response result = gameUserService.createResponse(true, player.getPlayerId(), 0, GameStatus.GUESSING, roundDTO);

        assertEquals(result.getGuess(), response.getGuess());
        assertEquals(result.getStrikes(), response.getStrikes());
        assertEquals(result.getPlayerId(), response.getPlayerId());
        assertEquals(result.getRoundStatus(), response.getRoundStatus());
        assertEquals(result.getRoundDTO(), response.getRoundDTO());
    }

    @Test
    void increaseWinTotal_validInputs() {
        User user = new User();
        user.setId(1L);
        player.setUserId(user.getId());
        when(gameUserService.getPlayer(1L)).thenReturn(player);
        when(gameUserService.getUser(1L)).thenReturn(user);

        assertNull(user.getTotalwins());

        gameUserService.increaseWinTotal(player.getPlayerId());

        assertNotNull(user.getTotalwins());
        assertEquals(user.getTotalwins(), 1L);
    }

    @Test
    void increaseGamesPlayed_validInputs() {
        User user = new User();
        user.setId(1L);
        player.setUserId(user.getId());
        when(gameUserService.getPlayer(1L)).thenReturn(player);
        when(gameUserService.getUser(1L)).thenReturn(user);

        assertNull(user.getTotalplayed());

        gameUserService.increaseGamesPlayed(player.getPlayerId());

        assertNotNull(user.getTotalplayed());
        assertEquals(user.getTotalplayed(), 1L);
    }

    @Test
    void createGameHistory_validInputs() {
        User user = new User();
        user.setTotalwins(0L);
        user.setTotalplayed(2L);

        GameHistory gameHistory = new GameHistory();
        gameHistory.setTotalgamesplayed(2L);
        gameHistory.setTotalwins(0L);
        gameHistory.setWinPercentage(0L);

        GameHistory result = gameUserService.createGameHistory(user);

        assertEquals(result.getTotalwins(), 0L);
        assertEquals(result.getTotalgamesplayed(), 2L);
        assertEquals(result.getWinPercentage(), 0L);

    }

    @Test
    void checkIfUserOnline_isOnline() {
        User user = new User();
        user.setId(1L);
        user.setStatus(UserStatus.ONLINE);

        when(userRepository.findUserById(1L)).thenReturn(user);

        Boolean result = gameUserService.checkIfUserOnline(1L);

        assertTrue(result);
    }

    @Test
    void checkIfUserExists_UserExists() {
        User user = new User();
        user.setId(1L);
        user.setStatus(UserStatus.ONLINE);

        when(userRepository.findUserById(1L)).thenReturn(user);

        Boolean result = gameUserService.checkIfUserExists(1L);

        assertTrue(result);
    }

}