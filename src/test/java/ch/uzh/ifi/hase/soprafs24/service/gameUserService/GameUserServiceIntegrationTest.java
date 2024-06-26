package ch.uzh.ifi.hase.soprafs24.service.gameUserService;

import ch.uzh.ifi.hase.soprafs24.constant.GameStatus;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class GameUserServiceIntegrationTest {

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
    void createResponse_validInputs() {
        Response response = new Response();
        response.setGuess(true);
        response.setPlayerId(1L);
        response.setStrikes(0);
        response.setRoundStatus(GameStatus.GUESSING);

        RoundDTO roundDTO = new RoundDTO(1, 1L, "");
        //roundDTO.setRoundNumber(1);
        //roundDTO.setCurrentTurnPlayerId(1L);

        Response result = gameUserService.createResponse(true, player.getPlayerId(), 0, GameStatus.GUESSING);

        assertEquals(result.getGuess(), response.getGuess());
        assertEquals(result.getStrikes(), response.getStrikes());
        assertEquals(result.getPlayerId(), response.getPlayerId());
        assertEquals(result.getRoundStatus(), response.getRoundStatus());
    }
}
