package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.Lobby;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.rest.dto.AuthenticationDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.LobbyGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs24.service.GameUserService;
import ch.uzh.ifi.hase.soprafs24.service.LobbyService;
import ch.uzh.ifi.hase.soprafs24.websocket.WebSocketMessenger;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class LobbyController {

    private final LobbyService lobbyService;
    private final WebSocketMessenger webSocketMessenger;
    private final GameUserService gameUserService;
    private final Gson gson = new Gson();

    LobbyController(LobbyService lobbyService, WebSocketMessenger webSocketMessenger, GameUserService gameUserService) {
        this.lobbyService = lobbyService;
        this.webSocketMessenger = webSocketMessenger;
        this.gameUserService = gameUserService;
    }

    @GetMapping("/lobbies")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<LobbyGetDTO> getAllLobbies() {
        // fetch all users in the internal representation
        List<Lobby> lobbies = lobbyService.getLobbies();
        List<LobbyGetDTO> lobbyGetDTOs = new ArrayList<>();

        // convert each user to the API representation
        for (Lobby lobby : lobbies) {
            lobbyGetDTOs.add(DTOMapper.INSTANCE.convertEntityToLobbyGetDTO(lobby));
        }
        return lobbyGetDTOs;
    }

    @GetMapping("lobbies/{lobbyId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Lobby getLobby(@PathVariable("lobbyId") Long lobbyId) {
        return lobbyService.getLobby(lobbyId); // smailalijagic: find lobby
    }


    @PostMapping("/lobbies/create/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public Long createlobby(@PathVariable("userId") Long userId) {
        Long lobbyId = lobbyService.createLobby(userId);
        //webSocketsConfig.configureMessageBrokerForLobby(lobbyId);
        return lobbyId;
    }

    @PutMapping("/lobbies/settings/{lobbyId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void updateLobby(@PathVariable("lobbyId") String id) {
        // smailalijagic: change lobby settings --> setting attributes need to be defined first
        Long lobbyId = Long.valueOf(id);
        Lobby updatedLobby = lobbyService.getLobby(lobbyId);
        // smailalijagic: update all lobby settings
    }

    @GetMapping("/lobbies/settings/{lobbyId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Lobby getLobbySettings(@PathVariable("lobbyId") String id) {
        // smailalijagic: return lobby settings to client
        return null;
    }

    @PutMapping("lobbies/join/{lobbyId}/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void joinLobby(@PathVariable("lobbyId") String id1, @PathVariable("userId") String id2) {
        Long lobbyId = Long.valueOf(id1);
        Long userId = Long.valueOf(id2);

        if (!lobbyService.checkIfLobbyExists(lobbyId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lobby with ID " + lobbyId + " does not exist");
        }

        Lobby lobby = lobbyService.getLobby(lobbyId);
        if (lobby.getInvitedUserId() != null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lobby code is not valid anymore or already in use");
        } else {
            User user = lobbyService.getUser(userId);
            lobbyService.addUserToLobby(lobby, user);
            user.setStatus(UserStatus.INLOBBY_PREPARING);
            gameUserService.saveUserChanges(user);

            UserGetDTO u = DTOMapper.INSTANCE.convertEntityToUserGetDTO(user);
            webSocketMessenger.sendMessage("/lobbies/" + lobbyId, "user-joined", u);
        }

    }

    @MessageMapping("/updateReadyStatus")
    public void updateStatus(String stringJsonRequest) {
        try {
            //nedim-j: search/create an own decorator for string parsing maybe
            JsonParser parser = new JsonParser();
            JsonObject jsonObject = parser.parse(stringJsonRequest).getAsJsonObject();

            String readyStatus = jsonObject.get("readyStatus").getAsString();
            Long lobbyId = Long.parseLong(jsonObject.get("lobbyId").getAsString());
            Long userId = Long.parseLong(jsonObject.get("userId").getAsString());

            //System.out.println("Request translated: " + readyStatus + " " + lobbyId + " " + userId);

            UserGetDTO userGetDTO = lobbyService.updateReadyStatus(userId, readyStatus);

            String destination = "/lobbies/" + lobbyId;

            webSocketMessenger.sendMessage(destination, "user-statusUpdate", userGetDTO);
        } catch(Exception e) {
            System.out.println("Something went wrong with ready status: "+e);
        }
    }

    @MessageMapping("/closeLobby")
    public void closeLobby(String stringJsonRequest) {
        try {

            Map<String, Object> requestMap = gson.fromJson(stringJsonRequest, Map.class);
            Long lobbyId = gson.fromJson(gson.toJson(requestMap.get("lobbyId")), Long.class);
            AuthenticationDTO authenticationDTO = gson.fromJson(gson.toJson(requestMap.get("authenticationDTO")), AuthenticationDTO.class);

            //System.out.println("Request translated: " + readyStatus + " " + lobbyId + " " + userId);
            lobbyService.closeLobby(lobbyId, authenticationDTO);

            String destination = "/lobbies/" + lobbyId;

            webSocketMessenger.sendMessage(destination, "lobby-closed", "Lobby has been closed");
        } catch(Exception e) {
            System.out.println("Something went wrong with Lobby closing: "+e);
        }
    }
}
