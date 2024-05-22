package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.entity.*;
import ch.uzh.ifi.hase.soprafs24.rest.dto.*;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs24.service.GameService;
import ch.uzh.ifi.hase.soprafs24.service.GameUserService;
import ch.uzh.ifi.hase.soprafs24.service.LobbyService;
import ch.uzh.ifi.hase.soprafs24.websocket.WebSocketMessenger;
import com.google.gson.Gson;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
//@WebMvcTest(GameController.class)
public class GameControllerTest {

    @Mock
    private GameService gameService;

    @Mock
    private GameUserService gameUserService;

    @Mock
    private WebSocketMessenger webSocketMessenger;

    @Mock
    private LobbyService lobbyService;

    @InjectMocks
    private GameController gameController;

    //@Autowired
    private MockMvc mockMvc;

    private Gson gson = new Gson();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(gameController).build();
    }

    @Test
    void testGetGames() throws Exception {
        List<Game> games = Arrays.asList(new Game(), new Game());
        when(gameService.getGames()).thenReturn(games);

        mockMvc.perform(get("/games")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(games)));

        verify(gameService, times(1)).getGames();
    }

    @Test
    void testGetGame() throws Exception {
        Long gameId = 1L;
        Game game = new Game();
        when(gameService.getGame(gameId)).thenReturn(game);

        mockMvc.perform(get("/games/{gameId}", gameId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(game)));

        verify(gameService, times(1)).getGame(gameId);
    }

//    @Test
//    void testCreateGame() {
//        String jsonRequest = "{\"lobbyId\": 1, \"gamePostDTO\": {}, \"authenticationDTO\": {}}";
//        Map<String, Object> requestMap = gson.fromJson(jsonRequest, Map.class);
//        Long lobbyId = 1L;
//        GamePostDTO gamePostDTO = new GamePostDTO();
//        AuthenticationDTO authenticationDTO = new AuthenticationDTO();
//        Game createdGame = new Game();
//        when(gameService.createGame(anyLong(), any(GamePostDTO.class), any(AuthenticationDTO.class))).thenReturn(createdGame);
//
//        gameController.createGame(jsonRequest);
//
//        verify(gameService, times(1)).createGame(lobbyId, gamePostDTO, authenticationDTO);
//        verify(webSocketMessenger, times(1)).sendMessage("/lobbies/" + lobbyId, "game-started", createdGame);
//    }

    @Test
    void testGetPlayer() throws Exception {
        Long playerId = 1L;
        Player player = new Player();
        when(gameUserService.getPlayer(playerId)).thenReturn(player);

        mockMvc.perform(get("/player/{playerid}", playerId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(player)));

        verify(gameUserService, times(1)).getPlayer(playerId);
    }

//    @Test
//    void testChooseImage() {
//        String jsonRequest = "{\"guessPostDTO\": {}}";
//        GuessPostDTO guessPostDTO = new GuessPostDTO();
//        Guess guess = new Guess();
//        RoundDTO roundDTO = new RoundDTO(1, 1L);
//        when(DTOMapper.INSTANCE.convertGuessPostDTOtoEntity(any(GuessPostDTO.class))).thenReturn(guess);
//        when(gameService.chooseImage(any(Guess.class))).thenReturn(roundDTO);
//        when(gameService.bothPlayersChosen(anyLong())).thenReturn(true);
//
//        gameController.chooseImage(jsonRequest);
//
//        verify(gameService, times(1)).chooseImage(guess);
//        verify(webSocketMessenger, times(1)).sendMessage("/games/" + guess.getGameId(), "round0", roundDTO);
//    }

//    @Test
//    void testGuessImage() {
//        String jsonRequest = "{\"guessPostDTO\": {}}";
//        GuessPostDTO guessPostDTO = new GuessPostDTO();
//        Guess guess = new Guess();
//        Response response = new Response();
//        when(DTOMapper.INSTANCE.convertGuessPostDTOtoEntity(any(GuessPostDTO.class))).thenReturn(guess);
//        when(gameService.guessImage(any(Guess.class))).thenReturn(response);
//
//        gameController.guessImage(jsonRequest);
//
//        verify(gameService, times(1)).guessImage(guess);
//        verify(webSocketMessenger, times(1)).sendMessage("/games/" + guess.getGameId(), "round-update", response);
//    }

    @Test
    void testGetGameImages() throws Exception {
        Long gameId = 1L;
        List<ImageDTO> images = Arrays.asList(new ImageDTO(), new ImageDTO());
        when(gameService.getGameImages(gameId)).thenReturn(images);

        mockMvc.perform(get("/games/{gameId}/images", gameId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(images)));

        verify(gameService, times(1)).getGameImages(gameId);
    }

    @Test
    void testDeleteGameImage() throws Exception {
        Long gameId = 1L;
        Long imageId = 1L;

        mockMvc.perform(delete("/games/{gameId}/images/{imageId}", gameId, imageId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(gameService, times(1)).deleteGameImage(gameId, imageId);
    }

//    @Test
//    void testGetGameHistory() throws Exception {
//        Long gameId = 1L;
//        Long userId = 1L;
//        GameHistory gameHistory = new GameHistory();
//        when(gameService.getGameHistory(gameId, userId)).thenReturn(gameHistory);
//
//        mockMvc.perform(get("/games/{gameId}/history/{userId}", gameId, userId)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(content().json(new ObjectMapper().writeValueAsString(gameHistory)));
//
//        verify(gameService, times(1)).getGameHistory(gameId, userId);
//    }
}

