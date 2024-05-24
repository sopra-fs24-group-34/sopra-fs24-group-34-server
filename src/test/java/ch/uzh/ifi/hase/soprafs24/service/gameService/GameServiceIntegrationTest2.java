package ch.uzh.ifi.hase.soprafs24.service.gameService;

import ch.uzh.ifi.hase.soprafs24.entity.Game;
import ch.uzh.ifi.hase.soprafs24.entity.Image;
import ch.uzh.ifi.hase.soprafs24.entity.Lobby;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.*;
import ch.uzh.ifi.hase.soprafs24.service.GameService;
import ch.uzh.ifi.hase.soprafs24.service.GameUserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class GameServiceIntegrationTest2 {
    @Mock
    private GameRepository gamerepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PlayerRepository playerrepository;
    @Mock
    private LobbyRepository lobbyRepository;
    @Mock
    private GameUserService gameUserService;

    @Mock
    private ImageRepository imagerepository;

    @InjectMocks
    private GameService gameservice;

    private User creator;

    private User invited;

    private Lobby lobby;

    private Game createdgame;


    @BeforeEach
    public void setup() {
        creator = new User();
        creator.setId(1L);
        invited = new User();
        invited.setId(2L);

        userRepository.save(creator);
        userRepository.save(invited);
        userRepository.flush();

        lobby = new Lobby();
        lobby.setCreatorUserId(creator.getId());
        lobby.setInvitedUserId(invited.getId());
        lobby.setLobbyid(3L);

        lobbyRepository.save(lobby);
        lobbyRepository.flush();

        createdgame = new Game();
        createdgame.setGameId(4L);
        createdgame.setCreatorPlayerId(1L);
        createdgame.setInvitedPlayerId(2L);

        gamerepository.save(createdgame);
        gamerepository.flush();
    }

    @AfterEach
    void teardown() {
        userRepository.deleteAll();
        lobbyRepository.deleteAll();
        gamerepository.deleteAll();
    }

    @Test
    void getGames_validInput(){
        List<Game> games = new ArrayList<>();
        games.add(createdgame);
        when(gamerepository.findAll()).thenReturn(games);

        List<Game> result = gameservice.getGames();

        assertEquals(result.size(), 1);
        assertEquals(result.get(0), createdgame);
    }

    @Test
    void getGame() {
        when(gamerepository.findByGameId(anyLong())).thenReturn(createdgame);

        Game result = gameservice.getGame(4L);

        assertEquals(result, createdgame);
    }

    @Test
    void checkIfImageExists_validInputs(){
        Image image = new Image();
        image.setId(1L);

        when(imagerepository.findImageById(1L)).thenReturn(image);

        Boolean result = gameservice.checkIfImageExists(1L);

        assertTrue(result);
    }
}
