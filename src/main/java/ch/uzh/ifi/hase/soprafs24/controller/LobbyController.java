package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.Lobby;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.LobbyRepository;
import ch.uzh.ifi.hase.soprafs24.rest.dto.*;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs24.service.LobbyService;
import ch.uzh.ifi.hase.soprafs24.service.UserService;
import net.bytebuddy.asm.Advice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
public class LobbyController {

  private final LobbyService lobbyService;

  LobbyController(LobbyService lobbyService) {
    this.lobbyService = lobbyService;
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<String> handleException(Exception ex) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
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

//  @GetMapping("lobbies/{lobbyId}")
//  @ResponseStatus(HttpStatus.OK)
//  @ResponseBody
//  public LobbyGetDTO getLobby(@PathVariable("lobbyId") Long lobbyId) {
//    Lobby lobby = lobbyService.getLobby(lobbyId); // smailalijagic: find lobby
//    return DTOMapper.INSTANCE.convertEntityToLobbyGetDTO(lobby); // smailalijagic: convert lobby to api representation and return it
//  }

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
     return lobbyService.createlobby(userId);
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
  public LobbyPutDTO joinLobby(@PathVariable("lobbyId") String id1, @PathVariable("userId") String id2) {
    // smailalijagic: update lobby for guest
    // smailalijagic: split into two api calls --> api.post(createGuest) -> returns UserPostDTO & takes UserPostDTO to api.put(joinLobbyAsGuest)
    Long lobbyId = Long.valueOf(id1);
    Long userId = Long.valueOf(id2);
    if (lobbyService.checkIfLobbyExists(lobbyId)) {
      Lobby lobby = lobbyService.getLobby(lobbyId); // smailalijagic: get lobby
      if (lobby.getInvited_userid() != null) { // smailalijagic: check if lobby is full
        throw new ResponseStatusException(HttpStatus.IM_USED, "Lobby code is not valid anymore or already in use");
      }
      User user = lobbyService.getUser(userId); // smailalijagic: get user
      lobbyService.addUserToLobby(lobby, user); // smailalijagic: update lobby

      return DTOMapper.INSTANCE.convertEntityToLobbyPutDTO(lobby); // smailalijagic: return api representation
      // smailalijagic: load lobby screen

    } else {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lobby does not exist");
    }
  }


  @DeleteMapping("/lobbies/{lobbyId}/start")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public Boolean deleteLobby(@PathVariable("lobbyId") String id, @RequestBody LobbyDeleteDTO lobbyDeleteDTO, User user_creator, User user_invited) {
    // smailalijagic: when a game is started lobby should be automatically deleted --> user presses start
    // smailalijagic: check that lobbyid exists
    Long lobbyid = Long.valueOf(id);
    Lobby lobbyToDelete = DTOMapper.INSTANCE.convertEntityToLobbyDeleteDTO(lobbyDeleteDTO);

    assert lobbyService.checkIfLobbyExists(Long.valueOf(id));
    // smailalijagic: check that both users exist
    assert lobbyService.checkIfUserExists(user_creator);
    assert lobbyService.checkIfUserExists(user_invited);
    // smailalijagic: check that user is creator
    assert lobbyService.isLobbyOwner(user_creator, Long.valueOf(id));
    // 4. delete lobby
    lobbyService.deleteLobby(lobbyToDelete);
    // 5. load game
    // smailalijagic: some return statement...
    return true;
  }
}
