package ch.uzh.ifi.hase.soprafs24.service.gameService;

import ch.uzh.ifi.hase.soprafs24.entity.*;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


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

//    @Test
//    void createGame_validInput(){
//        // Mock behavior of gameUserService
//        Mockito.when(gameUserService.getUser(1L)).thenReturn(creator);
//        Mockito.when(gameUserService.getUser(2L)).thenReturn(invited);
//        // Define the behavior to save and flush the player
//        doAnswer(invocation -> {
//            Player player = invocation.getArgument(0); // Get the Player passed to the method
//            playerrepository.save(player);
//            playerrepository.flush();
//            player.setPlayerId(5L);
//            return null; // Since the method is void, return null
//        }).when(gameUserService).savePlayerChanges(any(Player.class));
//        when(imagerepository.countAllImages()).thenReturn(200);
//
//        Mockito.when(lobbyRepository.findByLobbyid(3L)).thenReturn(lobby);
//
//
//        Game newgame = new Game();
//        newgame.setGameId(4L);
//        newgame.setCreatorPlayerId(5L);
//        newgame.setInvitedPlayerId(5L);
//        gamerepository.save(newgame);
//        gamerepository.flush();
//
//        AuthenticationDTO authenticationDTO = DTOMapper.INSTANCE.convertEntityToAuthenticationDTO(creator);
//
//        Game result = gameservice.createGame(3L, createdgame, authenticationDTO);
//
//        assertEquals(result.getCreatorPlayerId(), newgame.getCreatorPlayerId());
//        assertEquals(result.getInvitedPlayerId(), newgame.getInvitedPlayerId());
//        System.out.println(result.getCreatorPlayerId());
//        // assertEquals(playerrepository.findByPlayerId(result.getCreatorId()).getUser(), playerrepository.findByPlayerId(newgame.getCreatorId()).getUser());
//
//    }

    // smailalijagic: this test fails!

//    @Test
//    void selectImage_validInputs(){
//        //Game game = new Game();
//        Image image = new Image();
//        image.setId(1L);
//
//        //List<Image> images = new ArrayList<>();
//        //images.add(image);
//
//        //game.setGameImages(images);
//
//        Player player = new Player();
//        player.setPlayerId(2L);
//        Guess guess = new Guess();
//        guess.setGameId(4L);
//        guess.setImageId(1L);
//        guess.setPlayerId(2L);
//
//        //game.setCreatorPlayerId(player.getPlayerId());
//
//
//        when(gameUserService.getPlayer(2L)).thenReturn(player);
//        doAnswer(invocation -> {
//            Player anyplayer = invocation.getArgument(0); // Get the Player passed to the method
//            playerrepository.save(anyplayer);
//            playerrepository.flush();
//            anyplayer.setPlayerId(5L);
//            return null; // Since the method is void, return null
//        }).when(gameUserService).savePlayerChanges(any(Player.class));
//
//
//        gameservice.chooseImage(guess);
//
//        assertEquals(player.getChosencharacter(), 1L);
//    }

//    @Test
//    void guessImage_validInput() {
//        Image image = new Image();
//        image.setId(1L);
//        Player player = new Player();
//        player.setPlayerId(2L);
//        Player opp = new Player();
//        player.setPlayerId(3L);
//        player.setChosencharacter(1L);
//        Guess guess = new Guess();
//        guess.setGameId(4L);
//        guess.setImageId(1L);
//        guess.setPlayerId(2L);
//        Response response = new Response();
//        response.setGuess(true);
//        response.setStrikes(0);
//        response.setPlayerId(2L);
//        response.setRoundStatus(GameStatus.END);
//
//        System.out.println(guess.getGameId());
//        Mockito.when(gameUserService.getChosenCharacterOfOpponent(createdgame, 2L)).thenReturn(1L);
//        Mockito.when(gamerepository.findByGameId(4L)).thenReturn(createdgame);
//        Mockito.when(gameUserService.getStrikes(2L)).thenReturn(0);
//        Mockito.when(gameUserService.createResponse(true, 2L, 0, GameStatus.END)).thenReturn(response);
//
//        Response result = gameservice.guessImage(guess);
//
//        // this will only give back true when the Mockito functions are called with the right arguments
//        assertEquals(result.getGuess(), true);
//    }

    @Test
    void checkIfImageExists_validInputs(){
        Image image = new Image();
        image.setId(1L);

        when(imagerepository.findImageById(1L)).thenReturn(image);

        Boolean result = gameservice.checkIfImageExists(1L);

        assertTrue(result);
    }
}
