package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.Lobby;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.LobbyRepository;
import ch.uzh.ifi.hase.soprafs24.rest.dto.LobbyDeleteDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserPutDTO;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs24.service.LobbyService;
import ch.uzh.ifi.hase.soprafs24.service.UserService;
import net.bytebuddy.asm.Advice;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class LobbyController {

  private final LobbyService lobbyService;

  LobbyController(LobbyService lobbyService) {
    this.lobbyService = lobbyService;
  }




  @PostMapping("/lobbies/create") // smailalijagic: does it need to handle settings here already or only a button?
  @ResponseStatus(HttpStatus.CREATED)
  @ResponseBody
  public Long createLobby(@RequestBody UserPutDTO userPutDTO) {
    // smailalijagic: created lobby -> needs to return lobby which will be added to usergamelobbylist
    User user = DTOMapper.INSTANCE.convertUserPutDTOtoEntity(userPutDTO); // smailalijagic: get user entity object
    assert lobbyService.checkIfUserExists(user);
    Lobby createdLobby = new Lobby();
    List<Lobby> existingLobbies = user.getUsergamelobbylist(); // smailalijagic: get all existing lobbies
    existingLobbies.add(createdLobby); // smailalijagic: add newly created lobby
    user.setUsergamelobbylist(existingLobbies); // smailalijagic: overwrite complete lobby list
    // smailalijagic: does lobby know which user it holds???
    assert lobbyService.checkIfLobbyExists(createdLobby.getLobbyid()); // smailalijagic: make sure lobby was created
    return createdLobby.getLobbyid(); // smailalijagic: return lobby id of created lobby and navigate to "lobbies/settings/{lobbyId}"
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

  @PutMapping("lobbies/join/{lobbyId}")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public Long joinLobby(@PathVariable("lobbyId") String id) {
    // smailalijagic: check if lobby exists
    Long lobbyId = Long.valueOf(id);
    //assert lobbyService.checkIfLobbyExists(lobbyId);
    //Lobby lobby = lobbyService.getLobby(lobbyId); // smailalijagic: get lobby
    // smailalijagic: create guest_user
    //User userInput = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);
    User guestUser = lobbyService.createGuestUser();
    Long guestUserId = guestUser.getId();
    // smailalijagic: update lobby
    //lobby.setInvited_userid(guestUserId); // smailalijagic: update lobby
    // smailalijagic: load lobby screen
    return lobbyId;
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
