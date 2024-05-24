package ch.uzh.ifi.hase.soprafs24.service.gameService;

import ch.uzh.ifi.hase.soprafs24.constant.GameStatus;
import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.*;
import ch.uzh.ifi.hase.soprafs24.repository.*;
import ch.uzh.ifi.hase.soprafs24.rest.dto.AuthenticationDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.GamePostDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.ImageDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.RoundDTO;
import ch.uzh.ifi.hase.soprafs24.service.GameService;
import ch.uzh.ifi.hase.soprafs24.service.GameUserService;
import ch.uzh.ifi.hase.soprafs24.service.LobbyService;
import ch.uzh.ifi.hase.soprafs24.service.UnsplashService;
import ch.uzh.ifi.hase.soprafs24.websocket.WebSocketMessenger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class GameServiceIntegrationTest {
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
    @Mock
    private LobbyService lobbyService;
    @Mock
    private UnsplashService unsplashService;

    @Mock
    private WebSocketMessenger webSocketMessenger;

    @Spy
    @InjectMocks
    private GameService gameservice;

    private User creator;

    private User invited;

    private Lobby lobby;

    private Game createdgame;

    private GamePostDTO gamePostDTO;


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
        createdgame.setMaxStrikes(3);

        gamerepository.save(createdgame);
        gamerepository.flush();

        gamePostDTO = new GamePostDTO();
        gamePostDTO.setMaxStrikes(3);
        gamePostDTO.setCreatorUserId(1L);
        gamePostDTO.setInvitedUserId(2L);
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
    void createGame_validInput() {
        // Mock behavior of gameUserService
        Mockito.when(gameUserService.getUser(1L)).thenReturn(creator);
        Mockito.when(gameUserService.getUser(2L)).thenReturn(invited);
        // Mock the setting of a GameId
        Mockito.doAnswer(invocation -> {
            Game savedGame = invocation.getArgument(0);
            savedGame.setGameId(4L);
            return savedGame;
        }).when(gamerepository).save(Mockito.any(Game.class));

        // Players get Id matching their userId
        ArgumentMatcher<Player> playerMatcher1 = player -> player.getUserId().equals(1L);
        ArgumentMatcher<Player> playerMatcher2 = player -> player.getUserId().equals(2L);

        Mockito.doAnswer(invocation -> {
            Player player = invocation.getArgument(0);
            player.setPlayerId(1L);
            return null;
        }).when(gameUserService).savePlayerChanges(Mockito.argThat(playerMatcher1));

        Mockito.doAnswer(invocation -> {
            Player player = invocation.getArgument(0);
            player.setPlayerId(2L);
            return null;
        }).when(gameUserService).savePlayerChanges(Mockito.argThat(playerMatcher2));


        doNothing().when(gameUserService).saveUserChanges(any(User.class));
        doNothing().when(gameservice).databaseImageCheck();

        AuthenticationDTO authenticationDTO = new AuthenticationDTO();
        authenticationDTO.setId(1L);
        authenticationDTO.setToken(creator.getToken());

        Mockito.when(lobbyRepository.findByLobbyid(3L)).thenReturn(lobby);
        Mockito.when(lobbyService.isLobbyOwner(3L, authenticationDTO)).thenReturn(true);
        doNothing().when(gameservice).saveGameImages(anyLong());

        Game result = gameservice.createGame(3L, gamePostDTO, authenticationDTO);

        // Assert that the game was created with the correct properties
        assertEquals(result.getCreatorPlayerId(), 1L);
        assertEquals(result.getInvitedPlayerId(), 2L);
        assertEquals(result.getMaxStrikes(), 3);
        assertEquals(creator.getStatus(), UserStatus.PLAYING);
        assertEquals(invited.getStatus(), UserStatus.PLAYING);
    }

    @Test
    void selectImage_validInputs(){
        Image image = new Image();
        image.setId(1L);
        Player player = new Player();
        player.setPlayerId(1L);
        Player invitedPlayer = new Player();
        invitedPlayer.setPlayerId(2L);
        Guess guess = new Guess();
        guess.setGameId(4L);
        guess.setImageId(1L);
        guess.setPlayerId(1L);
        createdgame.setInvitedPlayerId(2L);

        when(gamerepository.findByGameId(4L)).thenReturn(createdgame);
        when(gameUserService.getPlayer(1L)).thenReturn(player);
        when(gameUserService.getPlayer(2L)).thenReturn(invitedPlayer);
        doAnswer(invocation -> {
            Player anyplayer = invocation.getArgument(0); // Get the Player passed to the method
            playerrepository.save(anyplayer);
            playerrepository.flush();
            anyplayer.setPlayerId(5L);
            return null; // Since the method is void, return null
        }).when(gameUserService).savePlayerChanges(any(Player.class));

        gameservice.chooseImage(guess);

        assertEquals(player.getChosencharacter(), 1L);
    }

    @Test
    void guessImage_validInput_handleWin() {
        Image image = new Image();
        image.setId(1L);
        Player player = new Player();
        player.setPlayerId(2L);
        player.setUserId(1L);
        Player opp = new Player();
        opp.setPlayerId(3L);
        opp.setUserId(2L);
        opp.setChosencharacter(1L);
        Guess guess = new Guess();
        guess.setGameId(4L);
        guess.setImageId(1L);
        guess.setPlayerId(2L);
        Response response = new Response();
        response.setGuess(true);
        response.setStrikes(0);
        response.setPlayerId(2L);
        response.setRoundStatus(GameStatus.END);
        createdgame.setCurrentRound(2);
        createdgame.setCurrentTurnPlayerId(2L);
        createdgame.setCreatorPlayerId(2L);
        createdgame.setInvitedPlayerId(3L);
        RoundDTO roundDTO = new RoundDTO(2, 2L, "");

        System.out.println(guess.getGameId());
        when(gameUserService.getChosenCharacterOfOpponent(createdgame, 2L)).thenReturn(1L);
        when(gamerepository.findByGameId(4L)).thenReturn(createdgame);
        when(gameUserService.getStrikes(2L)).thenReturn(0);
        when(gameUserService.createResponse(eq(true), eq(2L), eq(0), eq(GameStatus.END))).thenReturn(response);
        //when(gameUserService.getUser(1L)).thenReturn(creator);
        //when(gameUserService.getUser(2L)).thenReturn(invited);
        //when(gameUserService.getPlayer(2L)).thenReturn(player);
        //when(gameUserService.getPlayer(3L)).thenReturn(opp);

        Response result = gameservice.guessImage(guess);

        // this will only give back true when the Mockito functions are called with the right arguments
        assertEquals(result.getGuess(), true);
    }


    @Test
    void guessImage_wrongGuess_handleLoss() {
        Image image = new Image();
        image.setId(1L);
        Player player = new Player();
        player.setPlayerId(1L);
        player.setUserId(1L);
        player.setStrikes(2);
        Player opp = new Player();
        opp.setPlayerId(2L);
        opp.setUserId(2L);
        opp.setChosencharacter(1L);
        Guess guess = new Guess();
        guess.setGameId(4L);
        guess.setImageId(10L);
        guess.setPlayerId(1L);
        Response response = new Response();
        response.setGuess(false);
        response.setStrikes(3);
        response.setPlayerId(1L);
        response.setRoundStatus(GameStatus.END);
        createdgame.setCurrentRound(2);
        createdgame.setCurrentTurnPlayerId(2L);
        createdgame.setCreatorPlayerId(2L);
        createdgame.setInvitedPlayerId(3L);
        createdgame.setGuestGuess(false);
        createdgame.setGameStatus(GameStatus.END);
        RoundDTO roundDTO = new RoundDTO(2, 2L, "");

        System.out.println(guess.getGameId());
        when(gameUserService.getChosenCharacterOfOpponent(createdgame, 1L)).thenReturn(1L);
        when(gamerepository.findByGameId(4L)).thenReturn(createdgame);
        when(gameUserService.getStrikes(1L)).thenReturn(3);
        when(gameUserService.createResponse(eq(false), eq(1L), eq(3), eq(GameStatus.END))).thenReturn(response);
        //when(gameUserService.getUser(1L)).thenReturn(creator);
        //when(gameUserService.getUser(2L)).thenReturn(invited);
        //when(gameUserService.getPlayer(2L)).thenReturn(player);
        //when(gameUserService.getPlayer(3L)).thenReturn(opp);
        doNothing().when(gameUserService).increaseStrikesByOne(1L);
        player.setStrikes(3);

        Response result = gameservice.guessImage(guess);

        // this will only give back false when the Mockito functions are called with the right arguments
        assertEquals(result.getGuess(), false);
    }

    @Test
    void guessImage_rightGuess_handleTie() {
        Image image = new Image();
        image.setId(1L);
        Player player = new Player();
        player.setPlayerId(1L);
        player.setUserId(1L);
        Player opp = new Player();
        opp.setPlayerId(2L);
        opp.setUserId(2L);
        opp.setChosencharacter(1L);
        Guess guess = new Guess();
        guess.setGameId(4L);
        guess.setImageId(1L);
        guess.setPlayerId(1L);
        createdgame.setGuestGuess(true);
        Response response = new Response();
        response.setStrikes(0);
        response.setGuess(true);
        response.setPlayerId(1L);
        response.setRoundStatus(GameStatus.TIE);


        when(gamerepository.findByGameId(4L)).thenReturn(createdgame);
        when(gameUserService.getChosenCharacterOfOpponent(createdgame, 1L)).thenReturn(1L);
        when(gameUserService.getStrikes(1L)).thenReturn(0);
        when(gameUserService.createResponse(eq(true), eq(1L), eq(0), eq(GameStatus.TIE))).thenReturn(response);
        doNothing().when(gameUserService).increaseGamesPlayed(1L);
        doNothing().when(gameUserService).increaseGamesPlayed(2L);

        Response result = gameservice.guessImage(guess);

        // this will only give back true when the Mockito functions are called with the right arguments
        assertEquals(result.getGuess(), true);
        assertEquals(result.getRoundStatus(), GameStatus.TIE);
    }

    @Test
    void deleteGame_validInputs() {
        Player creatorPlayer = new Player();
        creatorPlayer.setPlayerId(1L);
        creatorPlayer.setUserId(1L);
        Player invitedPlayer = new Player();
        invitedPlayer.setPlayerId(2L);
        invitedPlayer.setUserId(2L);

        invited.setStatus(UserStatus.PLAYING);
        creator.setStatus(UserStatus.PLAYING);

        when(gameUserService.getPlayer(1L)).thenReturn(creatorPlayer);
        when(gameUserService.getPlayer(2L)).thenReturn(invitedPlayer);
        when(gameUserService.getUser(1L)).thenReturn(creator);
        when(gameUserService.getUser(2L)).thenReturn(invited);
        doNothing().when(webSocketMessenger).sendMessage(anyString(), anyString(), anyString());

        gameservice.deleteGame(createdgame);

        assert creator.getStatus().equals(UserStatus.ONLINE);
        assert invited.getStatus().equals(UserStatus.ONLINE);
    }

    @Test
    void getGameHistory_validInputs() {
        GameHistory gameHistory = new GameHistory();
        gameHistory.setTotalwins(1L);
        gameHistory.setTotalgamesplayed(1L);
        gameHistory.setWinPercentage(1L);

        when(gamerepository.findByGameId(4L)).thenReturn(createdgame);
        when(gameUserService.getUser(1L)).thenReturn(creator);
        when(gameUserService.createGameHistory(creator)).thenReturn(gameHistory);

        GameHistory result = gameservice.getGameHistory(createdgame.getGameId(), creator.getId());

        assertEquals(result.getWinPercentage(), 1L);
        assertEquals(result.getTotalgamesplayed(), 1L);
        assertEquals(result.getWinPercentage(), 1L);
    }

    @Test
    void checkIfImageExists_validInputs(){
        Image image = new Image();
        image.setId(1L);

        when(imagerepository.findImageById(1L)).thenReturn(image);

        Boolean result = gameservice.checkIfImageExists(1L);

        assertTrue(result);
    }

    @Test
    void checkIfGameExists_validInput() {

        when(gamerepository.findByGameId(4L)).thenReturn(createdgame);

        Boolean result = gameservice.checkIfGameExists(4L);

        assertEquals(result, true);
    }

    @Test
    void replaceGameImage_validInputs() {
        Image image = new Image();
        image.setId(1L);
        image.setUrl("picture");
        List<Image> imageList = new ArrayList<Image>();
        imageList.add(image);
        createdgame.setGameImages(imageList);

        ImageDTO imageDTO = new ImageDTO();
        imageDTO.setId(2L);
        List<ImageDTO> imageDTOList = new ArrayList<>();
        imageDTOList.add(imageDTO);

        ImageDTO imageDTO2 = new ImageDTO();
        imageDTO2.setId(1L);
        imageDTO2.setUrl("picture");
        List<ImageDTO> imageDTOList1 = new ArrayList<>();
        imageDTOList1.add(imageDTO2);
        Optional<List<ImageDTO>> optionalList = Optional.of(imageDTOList1);


        Mockito.when(gamerepository.findById(4L)).thenReturn(Optional.of(createdgame));
        Mockito.when(gameservice.createImageDTO(imageList)).thenReturn(imageDTOList1);
        Mockito.when(unsplashService.getImageUrlsFromDatabase(1, optionalList)).thenReturn(imageDTOList);

        assertEquals(createdgame.getGameImages().size(), 1);

        gameservice.replaceGameImages(4L);

        assertEquals(createdgame.getGameImages().size(), 2);
    }

    @Test
    void createImageEntities_validInput() {
        ImageDTO imageDTO = new ImageDTO();
        imageDTO.setId(1L);
        imageDTO.setUrl("first image");
        ImageDTO imageDTO1 = new ImageDTO();
        imageDTO1.setUrl("second picture");
        imageDTO1.setId(2L);
        List<ImageDTO> imageDTOList = new ArrayList<>();
        imageDTOList.add(imageDTO);
        imageDTOList.add(imageDTO1);

        List<Image> result = gameservice.createImageEntities(imageDTOList);

        assertEquals(result.size(), 2);
        assertEquals(result.get(0).getId(), 1L);
        assertEquals(result.get(0).getUrl(), "first image");
        assertEquals(result.get(1).getId(), 2L);
        assertEquals(result.get(1).getUrl(), "second picture");
    }

    @Test
    void createImageDTO_validInput() {
        Image image = new Image();
        image.setId(1L);
        image.setUrl("first image");
        Image image1 = new Image();
        image1.setUrl("second picture");
        image1.setId(2L);
        List<Image> imageList = new ArrayList<>();
        imageList.add(image);
        imageList.add(image1);

        List<ImageDTO> result = gameservice.createImageDTO(imageList);

        assertEquals(result.size(), 2);
        assertEquals(result.get(0).getId(), 1L);
        assertEquals(result.get(0).getUrl(), "first image");
        assertEquals(result.get(1).getId(), 2L);
        assertEquals(result.get(1).getUrl(), "second picture");
    }

    @Test
    void getGameImages_validInput() {
        Image image = new Image();
        image.setId(1L);
        image.setUrl("first image");
        Image image1 = new Image();
        image1.setUrl("second picture");
        image1.setId(2L);
        List<Image> imageList = new ArrayList<>();
        imageList.add(image);
        imageList.add(image1);

        Mockito.when(gamerepository.findById(4L)).thenReturn(Optional.of(createdgame));

        //List<ImageDTO> before = gameservice.getGameImages(4L);
        //assert before.isEmpty();

        createdgame.setGameImages(imageList);


        List<ImageDTO> result = gameservice.getGameImages(4L);

        assertEquals(result.size(), 2);
        assertEquals(result.get(0).getId(), 1L);
        assertEquals(result.get(0).getUrl(), "first image");
        assertEquals(result.get(1).getId(), 2L);
        assertEquals(result.get(1).getUrl(), "second picture");
    }

}
