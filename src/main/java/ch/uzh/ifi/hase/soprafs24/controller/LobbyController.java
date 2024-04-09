package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.entity.Lobby;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.LobbyRepository;
import ch.uzh.ifi.hase.soprafs24.rest.dto.LobbyDeleteDTO;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs24.service.LobbyService;
import ch.uzh.ifi.hase.soprafs24.service.UserService;
import net.bytebuddy.asm.Advice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class LobbyController {

    private final LobbyService lobbyService;

    public LobbyController(LobbyService lobbyService) {
        this.lobbyService = lobbyService;
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }


  @PostMapping("/lobbies/create/{userId}")
  @ResponseStatus(HttpStatus.CREATED)
  @ResponseBody
  public Lobby createlobby(@PathVariable("userId") Long userId) {

     return lobbyService.createlobby(userId);
  }

  @PutMapping("/lobbies/settings/{lobbyId}")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public void updateLobby(@PathVariable("lobbyId") String lobbyId) {
    // smailalijagic: change lobby settings --> setting attributes need to be defined first
  }

  @GetMapping("lobbies/join/{lobbyId}")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public Long joinLobby(@PathVariable("lobbyId") String lobbyid) {
    // smailalijagic: check if lobby exists
    // smailalijagic: load lobby screen
    return null;
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
