package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.entity.*;
import ch.uzh.ifi.hase.soprafs24.rest.dto.*;
import ch.uzh.ifi.hase.soprafs24.service.GameService;
import ch.uzh.ifi.hase.soprafs24.service.GameUserService;
import ch.uzh.ifi.hase.soprafs24.service.LobbyService;
import ch.uzh.ifi.hase.soprafs24.websocket.WebSocketMessenger;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class GameControllerTest {

    private MockMvc mockMvc;

    @Mock
    private GameService gameService;

    @Mock
    private GameUserService gameUserService;

    @Mock
    private WebSocketMessenger webSocketMessenger;

    @InjectMocks
    private GameController gameController;

    private Gson gson;

    @BeforeEach
    void setup() {
        gson = new Gson();
        mockMvc = MockMvcBuilders.standaloneSetup(gameController).build();
    }

    @Test
    void testGetGames() throws Exception {
        List<Game> games = new ArrayList<>();
        when(gameService.getGames()).thenReturn(games);

        mockMvc.perform(get("/games"))
                .andExpect(status().isOk())
                .andExpect(content().json(gson.toJson(games)));

        verify(gameService, times(1)).getGames();
    }

    @Test
    void testGetGame() throws Exception {
        Game game = new Game();
        when(gameService.getGame(anyLong())).thenReturn(game);

        mockMvc.perform(get("/games/{gameId}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().json(gson.toJson(game)));

        verify(gameService, times(1)).getGame(1L);
    }

    @Test
    void testCreateGame() {
        // Prepare the test data and mocks
        String requestJson = "{\"lobbyId\": 1, \"gamePostDTO\": {}, \"authenticationDTO\": {}}";
        Game createdGame = new Game();
        when(gameService.createGame(anyLong(), any(GamePostDTO.class), any(AuthenticationDTO.class))).thenReturn(createdGame);

        gameController.createGame(requestJson);

        verify(gameService, times(1)).createGame(anyLong(), any(GamePostDTO.class), any(AuthenticationDTO.class));
        verify(webSocketMessenger, times(1)).sendMessage(anyString(), anyString(), any(GameGetDTO.class));
    }

    @Test
    void testGetPlayer() throws Exception {
        Player player = new Player();
        when(gameUserService.getPlayer(anyLong())).thenReturn(player);

        mockMvc.perform(get("/player/{playerid}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().json(gson.toJson(player)));

        verify(gameUserService, times(1)).getPlayer(1L);
    }

//    @Test
//    void testChooseImage() {
//        // Prepare the test data and mocks
//        String requestJson = "{\"guessPostDTO\": {}}";
//        RoundDTO roundDTO = new RoundDTO(1, 1L);
//        when(gameService.chooseImage(any(Guess.class))).thenReturn(roundDTO);
//        when(gameService.bothPlayersChosen(anyLong())).thenReturn(true);
//
//        gameController.chooseImage(requestJson);
//
//        verify(gameService, times(1)).chooseImage(any(Guess.class));
//        verify(webSocketMessenger, times(1)).sendMessage(anyString(), anyString(), any(RoundDTO.class));
//    }

//    @Test
//    void testGuessImage() {
//        // Prepare the test data and mocks
//        String requestJson = "{\"guessPostDTO\": {\"gameid\": 1}}";
//        Guess guess = new Guess();
//        Response response = new Response();
//        Game game = new Game();
//
//        when(gameService.guessImage(any(Guess.class))).thenReturn(response);
//        when(gameService.getGame(anyLong())).thenReturn(game);
//
//        gameController.guessImage(requestJson);
//
//        verify(gameService, times(1)).guessImage(any(Guess.class));
//        verify(webSocketMessenger, times(1)).sendMessage(anyString(), anyString(), any(Response.class));
//    }

    @Test
    void testGetGameImages() throws Exception {
        List<ImageDTO> images = new ArrayList<>();
        when(gameService.getGameImages(anyLong())).thenReturn(images);

        mockMvc.perform(get("/games/{gameId}/images", 1L))
                .andExpect(status().isOk())
                .andExpect(content().json(gson.toJson(images)));

        verify(gameService, times(1)).getGameImages(1L);
    }

    @Test
    void testDeleteGameImage() throws Exception {
        mockMvc.perform(delete("/games/{gameId}/images/{imageId}", 1L, 1L))
                .andExpect(status().isOk());

        verify(gameService, times(1)).deleteGameImage(1L, 1L);
    }

//    @Test
//    void testGetGameHistory() throws Exception {
//        GameHistory gameHistory = new GameHistory();
//        when(gameService.getGameHistory(anyLong(), anyLong())).thenReturn(gameHistory);
//
//        mockMvc.perform(get("/games/{gameId}/history/{userId}", 1L, 1L))
//                .andExpect(status().isOk())
//                .andExpect(content().json(gson.toJson(gameHistory)));
//
//        verify(gameService, times(1)).getGameHistory(1L, 1L);
//    }

    @Test
    void testGetGames2() throws Exception {
        List<Game> games = Arrays.asList(new Game(), new Game());
        when(gameService.getGames()).thenReturn(games);

        mockMvc.perform(get("/games")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(games)));

        verify(gameService, times(1)).getGames();
    }

    @Test
    void testGetGame2() throws Exception {
        Long gameId = 1L;
        Game game = new Game();
        when(gameService.getGame(gameId)).thenReturn(game);

        mockMvc.perform(get("/games/{gameId}", gameId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(game)));

        verify(gameService, times(1)).getGame(gameId);
    }

    @Test
    void testGetPlayer2() throws Exception {
        Long playerId = 1L;
        Player player = new Player();
        when(gameUserService.getPlayer(playerId)).thenReturn(player);

        mockMvc.perform(get("/player/{playerid}", playerId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(player)));

        verify(gameUserService, times(1)).getPlayer(playerId);
    }


    @Test
    void testGetGameImages2() throws Exception {
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
    void testDeleteGameImage2() throws Exception {
        Long gameId = 1L;
        Long imageId = 1L;

        mockMvc.perform(delete("/games/{gameId}/images/{imageId}", gameId, imageId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(gameService, times(1)).deleteGameImage(gameId, imageId);
    }

}
