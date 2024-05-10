//package ch.uzh.ifi.hase.soprafs24.service.lobbyservice;
//
//import ch.uzh.ifi.hase.soprafs24.entity.Lobby;
//import ch.uzh.ifi.hase.soprafs24.entity.User;
//import ch.uzh.ifi.hase.soprafs24.repository.LobbyRepository;
//import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
//import ch.uzh.ifi.hase.soprafs24.service.LobbyService;
//import org.apache.http.HttpStatus;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.TestTemplate;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.web.server.ResponseStatusException;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//public class LobbyServiceTest {
//
//    @Mock
//    private LobbyRepository lobbyRepository;
//
//    @Mock
//    private UserRepository userRepository;
//
//    @InjectMocks
//    private LobbyService lobbyService;
//
//    @Test
//    void getLobby_validLobbyId_returnsLobby() {
//        // Given
//        Lobby lobby = new Lobby();
//        lobby.setLobbyid(1L);
//        lobby.setCreator_userid(1L);
//        lobby.setInvited_userid(2L);
//        lobby.setToken("1");
//
//        when(lobbyRepository.findByLobbyid(Mockito.anyLong())).thenReturn(lobby);
//        Lobby resLobby = lobbyService.getLobby(lobby.getLobbyid());
//
//        assertEquals(resLobby.getLobbyid(), lobby.getLobbyid());
//        assertEquals(resLobby.getCreator_userid(), lobby.getCreator_userid());
//        assertEquals(resLobby.getInvited_userid(), lobby.getInvited_userid());
//        assertEquals(resLobby.getlobbyToken(), lobby.getlobbyToken());
//    }
//
//    @Test
//    void getLobby_throwsResponseStatusException_whenLobbyNotFound() {
//        Long lobbyId = 1L;
//
//        when(lobbyRepository.findByLobbyid(Mockito.anyLong())).thenThrow(new NullPointerException());
//        assertThrows(ResponseStatusException.class, () -> {lobbyService.getLobby(lobbyId);});
//        }
//
//    @Test
//    void getUser_validUserId_returnsUser() {
//        User user = new User();
//        user.setId(1L);
//        user.setUsername("Smail123");
//        user.setPassword("123");
//        user.setToken("1");
//
//        when(userRepository.findUserById(Mockito.anyLong())).thenReturn(user);
//
//        User resUser = lobbyService.getUser(user.getId());
//
//        assertEquals(resUser.getId(), user.getId());
//        assertEquals(resUser.getUsername(), user.getUsername());
//        assertEquals(resUser.getPassword(), user.getPassword());
//        assertEquals(resUser.getToken(), user.getToken());
//    }
//
//    @Test
//    void getUser_throwsResponseStatusException_whenUserNotFound() {
//        Long userId = 1L;
//
//        when(userRepository.findUserById(Mockito.anyLong())).thenThrow(new IllegalArgumentException());
//
//        assertThrows(ResponseStatusException.class, () -> {lobbyService.getUser(userId);});
//    }
//
//    @Test
//    void checkIfUserExists_true() {
//        User user = new User();
//        user.setUsername("Liam123");
//
//        when(userRepository.findByUsername(Mockito.anyString())).thenReturn(user);
//
//        assertTrue(lobbyService.checkIfUserExists(user));
//    }
//
//    @Test
//    void checkIfUsersExists_false() {
//        User user = new User();
//
//        assertFalse(lobbyService.checkIfUserExists(user));
//    }
//
//    @Test
//    void getLobbies_return_lobbies() {
//        Lobby lobby1 = new Lobby();
//        lobby1.setLobbyid(1L);
//        Lobby lobby2 = new Lobby();
//        lobby2.setLobbyid(2L);
//        Lobby lobby3 = new Lobby();
//        lobby3.setLobbyid(3L);
//
//        List<Lobby> lobbies = new ArrayList<>();
//        lobbies.add(lobby1); lobbies.add(lobby2); lobbies.add(lobby3);
//
//        when(lobbyRepository.findAll()).thenReturn(lobbies);
//        List<Lobby> retLobbies = lobbyService.getLobbies();
//
//        assertEquals(lobbies.size(), retLobbies.size());
//        assertEquals(lobby1, retLobbies.get(0));
//        assertEquals(lobby2, retLobbies.get(1));
//        assertEquals(lobby3, retLobbies.get(2));
//    }
//
//    @Test
//    void isLobbyOwner_returns_true() {
//        List<Lobby> lobbies = new ArrayList<>();
//        Lobby lobby = new Lobby();
//        lobby.setLobbyid(8L);
//        lobby.setCreator_userid(1L);
//        lobbies.add(lobby);
//
//        User user = new User();
//        user.setId(1L);
//        user.setUsergamelobbylist(lobbies);
//
//        when(lobbyRepository.findByLobbyid(lobby.getLobbyid())).thenReturn(lobby);
//
//        assertTrue(lobbyService.isLobbyOwner(user, lobby.getLobbyid()));
//    }
//
//    @Test
//    void isLobbyOwner_returns_false() {
//        List<Lobby> lobbies = new ArrayList<>();
//        Lobby lobby = new Lobby();
//        lobby.setLobbyid(8L);
//        lobby.setCreator_userid(1L);
//
//        User user = new User();
//        user.setId(2L);
//        user.setUsergamelobbylist(lobbies);
//
//        when(lobbyRepository.findByLobbyid(lobby.getLobbyid())).thenReturn(lobby);
//
//        assertFalse(lobbyService.isLobbyOwner(user, lobby.getLobbyid()));
//    }
//
//    @Test
//    void createLobby_valid_lobbyId() {
//        User user = new User();
//        user.setId(1L);
//        user.setUsergamelobbylist(new ArrayList<>());
//
//        Lobby lobby = new Lobby();
//        lobby.setToken("1");
//        lobby.setCreator_userid(user.getId());
//
//        when(lobbyRepository.save(Mockito.any(Lobby.class))).thenReturn(lobby);
//        when(userRepository.findUserById(Mockito.anyLong())).thenReturn(user);
//
//        Long lobby_id = lobbyService.createlobby(user.getId());
//
//        assertEquals(lobby.getLobbyid(), lobby_id);
//        assertTrue(user.getUsergamelobbylist().contains(lobby));
//
//    }
//
//}