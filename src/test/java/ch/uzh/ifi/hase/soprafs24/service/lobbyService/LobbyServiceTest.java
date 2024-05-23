package ch.uzh.ifi.hase.soprafs24.service.lobbyService;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.Game;
import ch.uzh.ifi.hase.soprafs24.entity.Lobby;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.LobbyRepository;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs24.rest.dto.AuthenticationDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs24.service.AuthenticationService;
import ch.uzh.ifi.hase.soprafs24.service.LobbyService;
import ch.uzh.ifi.hase.soprafs24.websocket.WebSocketMessenger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class LobbyServiceTest {

    @Autowired
    private LobbyService lobbyService;

    @MockBean
    private LobbyRepository lobbyRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private AuthenticationService authenticationService;

    @MockBean
    private WebSocketMessenger webSocketMessenger;

    private User testUser;
    private Lobby testLobby;
    private AuthenticationDTO authenticationDTO;

    @BeforeEach
    public void setup() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testUser");

        testLobby = new Lobby();
        testLobby.setLobbyid(1L);
        testLobby.setCreatorUserId(1L);

        authenticationDTO = new AuthenticationDTO();
    }

    @Test
    public void testGetUsersSuccess() {
        when(userRepository.findAll()).thenReturn(Arrays.asList(testUser));
        List<User> users = lobbyService.getUsers();
        assertEquals(1, users.size());
        assertEquals(testUser.getUsername(), users.get(0).getUsername());
    }

    @Test
    public void testGetUserSuccess() {
        when(userRepository.findUserById(1L)).thenReturn(testUser);
        User user = lobbyService.getUser(1L);
        assertEquals(testUser.getUsername(), user.getUsername());
    }

//    @Test
//    public void testGetUserFailure() {
//        when(userRepository.findUserById(1L)).thenReturn(null);
//        assertThrows(ResponseStatusException.class, () -> {
//            lobbyService.getUser(1L);
//        });
//    }

    @Test
    public void testCheckIfUserExistsTrue() {
        when(userRepository.findByUsername(anyString())).thenReturn(testUser);
        assertTrue(lobbyService.checkIfUserExists(testUser));
    }

    @Test
    public void testCheckIfUserExistsFalse() {
        when(userRepository.findByUsername(anyString())).thenReturn(null);
        assertFalse(lobbyService.checkIfUserExists(testUser));
    }

    @Test
    public void testGetLobbiesSuccess() {
        when(lobbyRepository.findAll()).thenReturn(Arrays.asList(testLobby));
        List<Lobby> lobbies = lobbyService.getLobbies();
        assertEquals(1, lobbies.size());
        assertEquals(testLobby.getLobbyid(), lobbies.get(0).getLobbyid());
    }

    @Test
    public void testGetLobbySuccess() {
        when(lobbyRepository.findByLobbyid(1L)).thenReturn(testLobby);
        Lobby lobby = lobbyService.getLobby(1L);
        assertEquals(testLobby.getLobbyid(), lobby.getLobbyid());
    }

//    @Test
//    public void testGetLobbyFailure() {
//        when(lobbyRepository.findByLobbyid(1L)).thenReturn(null);
//        assertThrows(ResponseStatusException.class, () -> {
//            lobbyService.getLobby(1L);
//        });
//    }

    @Test
    public void testCheckIfLobbyExistsTrue() {
        when(lobbyRepository.existsByLobbyid(1L)).thenReturn(true);
        assertTrue(lobbyService.checkIfLobbyExists(1L));
    }

    @Test
    public void testCheckIfLobbyExistsFalse() {
        when(lobbyRepository.existsByLobbyid(1L)).thenReturn(false);
        assertFalse(lobbyService.checkIfLobbyExists(1L));
    }

    @Test
    public void testIsLobbyOwnerTrue() {
        when(lobbyRepository.findByLobbyid(1L)).thenReturn(testLobby);
        when(userRepository.findUserById(1L)).thenReturn(testUser);
        when(authenticationService.isAuthenticated(any(User.class), any(AuthenticationDTO.class))).thenReturn(true);
        assertTrue(lobbyService.isLobbyOwner(1L, authenticationDTO));
    }

    @Test
    public void testIsLobbyOwnerFalse() {
        when(lobbyRepository.findByLobbyid(1L)).thenReturn(testLobby);
        when(userRepository.findUserById(1L)).thenReturn(testUser);
        when(authenticationService.isAuthenticated(any(User.class), any(AuthenticationDTO.class))).thenReturn(false);
        assertFalse(lobbyService.isLobbyOwner(1L, authenticationDTO));
    }

    @Test
    public void testCloseLobbySuccess() {
        when(lobbyRepository.findByLobbyid(1L)).thenReturn(testLobby);
        when(userRepository.findUserById(1L)).thenReturn(testUser);
        when(authenticationService.isAuthenticated(any(User.class), any(AuthenticationDTO.class))).thenReturn(true);
        doNothing().when(lobbyRepository).delete(any(Lobby.class));
        lobbyService.closeLobby(1L, authenticationDTO);
        verify(lobbyRepository, times(1)).delete(any(Lobby.class));
    }

    @Test
    public void testCloseLobbyFailure() {
        when(lobbyRepository.findByLobbyid(1L)).thenReturn(testLobby);
        when(userRepository.findUserById(1L)).thenReturn(testUser);
        when(authenticationService.isAuthenticated(any(User.class), any(AuthenticationDTO.class))).thenReturn(false);
        lobbyService.closeLobby(1L, authenticationDTO);
        verify(lobbyRepository, times(0)).delete(any(Lobby.class));
    }

    @Test
    public void testCreateLobbySuccess() {
        when(lobbyRepository.save(any(Lobby.class))).thenReturn(testLobby);
        when(userRepository.findUserById(1L)).thenReturn(testUser);
        Long lobbyId = lobbyService.createLobby(1L);
        assertEquals(testLobby.getLobbyid(), lobbyId);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void testUpdateReadyStatusSuccess() {
        when(userRepository.findUserById(1L)).thenReturn(testUser);
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        UserGetDTO userGetDTO = lobbyService.updateReadyStatus(1L, "INLOBBY_READY");
        assertEquals(UserStatus.INLOBBY_READY, userGetDTO.getStatus());
    }

    @Test
    public void testUpdateReadyStatusFailure() {
        when(userRepository.findUserById(1L)).thenReturn(testUser);
        assertThrows(ResponseStatusException.class, () -> {
            lobbyService.updateReadyStatus(1L, "INVALID_STATUS");
        });
    }

    @Test
    public void testAddUserToLobbySuccess() {
        when(lobbyRepository.save(any(Lobby.class))).thenReturn(testLobby);
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        lobbyService.addUserToLobby(testLobby, testUser);
        assertEquals(testUser.getId(), testLobby.getInvitedUserId());
        verify(lobbyRepository, times(1)).save(any(Lobby.class));
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void testRemoveUserFromLobbySuccess() {
        when(lobbyRepository.findByLobbyid(1L)).thenReturn(testLobby);
        when(userRepository.findUserById(1L)).thenReturn(testUser);
        lobbyService.removeUserFromLobby(1L, 1L);
        verify(lobbyRepository, times(1)).delete(any(Lobby.class));
    }

    @Test
    public void testGetGameIdFromLobbyIdSuccess() {
        Game testGame = new Game();
        testGame.setGameId(1L);
        testLobby.setGame(testGame);
        when(lobbyRepository.findByLobbyid(1L)).thenReturn(testLobby);
        Long gameId = lobbyService.getGameIdFromLobbyId(1L);
        assertEquals(1L, gameId);
    }

    @Test
    public void testTranslateAddUserToLobbySuccess() {
        when(lobbyRepository.findByLobbyid(1L)).thenReturn(testLobby);
        when(userRepository.findUserById(2L)).thenReturn(testUser);
        lobbyService.translateAddUserToLobby(1L, 2L);
        verify(webSocketMessenger, times(1)).sendMessage(anyString(), anyString(), any(User.class));
    }

    @Test
    public void testTranslateAddUserToLobbyFailure() {
        when(lobbyRepository.findByLobbyid(1L)).thenReturn(testLobby);
        when(userRepository.findUserById(1L)).thenReturn(testUser);
        lobbyService.translateAddUserToLobby(1L, 1L);
        verify(webSocketMessenger, times(0)).sendMessage(anyString(), anyString(), any(User.class));
    }

    @Test
    void testGetUsers() {
        // Mock data
        List<User> users = List.of(new User(), new User());
        when(userRepository.findAll()).thenReturn(users);

        // Call the method
        List<User> fetchedUsers = lobbyService.getUsers();

        // Verify the result
        assertEquals(users, fetchedUsers);
    }

    @Test
    void testGetUser() {
        // Mock data
        Long userId = 1L;
        User user = new User();
        when(userRepository.findUserById(userId)).thenReturn(user);

        // Call the method
        User fetchedUser = lobbyService.getUser(userId);

        // Verify the result
        assertEquals(user, fetchedUser);
    }

    @Test
    void testCheckIfUserExists() {
        // Mock data
        User userToBeCreated = new User();
        String username = "testUser";
        userToBeCreated.setUsername(username);
        when(userRepository.findByUsername(username)).thenReturn(null);

        // Call the method
        boolean exists = lobbyService.checkIfUserExists(userToBeCreated);

        // Verify the result
        assertFalse(exists);
    }

    @Test
    void testGetLobbies() {
        // Mock data
        List<Lobby> lobbies = List.of(new Lobby(), new Lobby());
        when(lobbyRepository.findAll()).thenReturn(lobbies);

        // Call the method
        List<Lobby> fetchedLobbies = lobbyService.getLobbies();

        // Verify the result
        assertEquals(lobbies, fetchedLobbies);
    }

    @Test
    void testCheckIfLobbyExists() {
        // Mock data
        Long lobbyId = 1L;
        when(lobbyRepository.existsByLobbyid(lobbyId)).thenReturn(true);

        // Call the method
        boolean exists = lobbyService.checkIfLobbyExists(lobbyId);

        // Verify the result
        assertTrue(exists);
    }

    @Test
    void testIsLobbyOwner_WhenAuthenticated() {
        // Mock data
        Long lobbyId = 1L;
        Long userId = 1L;
        AuthenticationDTO authenticationDTO = new AuthenticationDTO();
        Lobby lobby = new Lobby();
        lobby.setCreatorUserId(userId);
        when(lobbyRepository.findByLobbyid(lobbyId)).thenReturn(lobby);
        when(authenticationService.isAuthenticated(any(), any())).thenReturn(true);

        // Call the method
        boolean isOwner = lobbyService.isLobbyOwner(lobbyId, authenticationDTO);

        // Verify the result
        assertTrue(isOwner);
    }

    @Test
    void testIsLobbyOwner_WhenNotAuthenticated() {
        // Mock data
        Long lobbyId = 1L;
        AuthenticationDTO authenticationDTO = new AuthenticationDTO();
        when(lobbyRepository.findByLobbyid(lobbyId)).thenReturn(new Lobby());
        when(authenticationService.isAuthenticated(any(), any())).thenReturn(false);

        // Call the method
        boolean isOwner = lobbyService.isLobbyOwner(lobbyId, authenticationDTO);

        // Verify the result
        assertFalse(isOwner);
    }

    @Test
    void getLobby_throwsResponseStatusException_whenLobbyNotFound() {
        Long lobbyId = 1L;

        when(lobbyRepository.findByLobbyid(Mockito.anyLong())).thenThrow(new NullPointerException());
        assertThrows(ResponseStatusException.class, () -> {lobbyService.getLobby(lobbyId);});
        }

    @Test
    void getUser_validUserId_returnsUser() {
        User user = new User();
        user.setId(1L);
        user.setUsername("Smail123");
        user.setPassword("123");
        user.setToken("1");

        when(userRepository.findUserById(Mockito.anyLong())).thenReturn(user);

        User resUser = lobbyService.getUser(user.getId());

        assertEquals(resUser.getId(), user.getId());
        assertEquals(resUser.getUsername(), user.getUsername());
        assertEquals(resUser.getPassword(), user.getPassword());
        assertEquals(resUser.getToken(), user.getToken());
    }

    @Test
    void getUser_throwsResponseStatusException_whenUserNotFound() {
        Long userId = 1L;

        when(userRepository.findUserById(Mockito.anyLong())).thenThrow(new IllegalArgumentException());

        assertThrows(ResponseStatusException.class, () -> {lobbyService.getUser(userId);});
    }

    @Test
    void checkIfUserExists_true() {
        User user = new User();
        user.setUsername("Liam123");

        when(userRepository.findByUsername(Mockito.anyString())).thenReturn(user);

        assertTrue(lobbyService.checkIfUserExists(user));
    }

    @Test
    void checkIfUsersExists_false() {
        User user = new User();

        assertFalse(lobbyService.checkIfUserExists(user));
    }

    @Test
    void getLobbies_return_lobbies() {
        Lobby lobby1 = new Lobby();
        lobby1.setLobbyid(1L);
        Lobby lobby2 = new Lobby();
        lobby2.setLobbyid(2L);
        Lobby lobby3 = new Lobby();
        lobby3.setLobbyid(3L);

        List<Lobby> lobbies = new ArrayList<>();
        lobbies.add(lobby1); lobbies.add(lobby2); lobbies.add(lobby3);

        when(lobbyRepository.findAll()).thenReturn(lobbies);
        List<Lobby> retLobbies = lobbyService.getLobbies();

        assertEquals(lobbies.size(), retLobbies.size());
        assertEquals(lobby1, retLobbies.get(0));
        assertEquals(lobby2, retLobbies.get(1));
        assertEquals(lobby3, retLobbies.get(2));
    }

    @Test
    void testGetUser_WhenUserExists() {
        // Mock data
        Long userId = 1L;
        User user = new User();
        when(userRepository.findUserById(userId)).thenReturn(user);

        // Call the method
        User result = lobbyService.getUser(userId);

        // Verify the result
        assertNotNull(result);
        assertEquals(user, result);
    }

    @Test
    void testCheckIfUserExists_WhenUserExists() {
        // Mock data
        User user = new User();
        user.setUsername("username");
        when(userRepository.findByUsername("username")).thenReturn(user);

        // Call the method
        boolean exists = lobbyService.checkIfUserExists(user);

        // Verify the result
        assertTrue(exists);
    }

    @Test
    void testCheckIfUserExists_WhenUserDoesNotExist() {
        // Mock data
        when(userRepository.findByUsername(anyString())).thenReturn(null);

        // Call the method
        boolean exists = lobbyService.checkIfUserExists(new User());

        // Verify the result
        assertFalse(exists);
    }

    @Test
    void testGetLobby_WhenLobbyExists() {
        // Mock data
        Long lobbyId = 1L;
        Lobby lobby = new Lobby();
        when(lobbyRepository.findByLobbyid(lobbyId)).thenReturn(lobby);

        // Call the method
        Lobby result = lobbyService.getLobby(lobbyId);

        // Verify the result
        assertNotNull(result);
        assertEquals(lobby, result);
    }

    @Test
    void testGetUsers2() {
        // Given
        User user = new User();
        when(userRepository.findAll()).thenReturn(Collections.singletonList(user));

        // When
        List<User> users = lobbyService.getUsers();

        // Then
        assertEquals(1, users.size());
        assertEquals(user, users.get(0));
    }

    @Test
    void testGetUser2() {
        // Given
        User user = new User();
        user.setId(1L);
        when(userRepository.findUserById(1L)).thenReturn(user);

        // When
        User retrievedUser = lobbyService.getUser(1L);

        // Then
        assertEquals(user, retrievedUser);
    }

    @Test
    void testCheckIfUserExists_UserDoesNotExist() {
        // Given no user with the username
        when(userRepository.findByUsername("test")).thenReturn(null);

        // When
        boolean exists = lobbyService.checkIfUserExists(new User());

        // Then
        assertFalse(exists);
    }

    @Test
    void testGetLobbies2() {
        // Given
        Lobby lobby = new Lobby();
        when(lobbyRepository.findAll()).thenReturn(Collections.singletonList(lobby));

        // When
        List<Lobby> lobbies = lobbyService.getLobbies();

        // Then
        assertEquals(1, lobbies.size());
        assertEquals(lobby, lobbies.get(0));
    }

    @Test
    void testGetLobby() {
        // Given
        Lobby lobby = new Lobby();
        lobby.setLobbyid(1L);
        when(lobbyRepository.findByLobbyid(1L)).thenReturn(lobby);

        // When
        Lobby retrievedLobby = lobbyService.getLobby(1L);

        // Then
        assertEquals(lobby, retrievedLobby);
    }

    @Test
    void testCheckIfLobbyExists2() {
        // Given a lobby exists with the ID
        when(lobbyRepository.existsByLobbyid(1L)).thenReturn(true);

        // When
        boolean exists = lobbyService.checkIfLobbyExists(1L);

        // Then
        assertTrue(exists);
    }

    @Test
    void testCheckIfLobbyExists_LobbyDoesNotExist() {
        // Given no lobby with the ID
        when(lobbyRepository.existsByLobbyid(1L)).thenReturn(false);

        // When
        boolean exists = lobbyService.checkIfLobbyExists(1L);

        // Then
        assertFalse(exists);
    }

    @Test
    void testIsLobbyOwner() {
        // Given
        AuthenticationDTO authenticationDTO = new AuthenticationDTO();
        Lobby lobby = new Lobby();
        lobby.setCreatorUserId(1L);
        User host = new User();
        when(lobbyRepository.findByLobbyid(1L)).thenReturn(lobby);
        when(userRepository.findUserById(1L)).thenReturn(host);
        when(authenticationService.isAuthenticated(host, authenticationDTO)).thenReturn(true);

        // When
        boolean isOwner = lobbyService.isLobbyOwner(1L, authenticationDTO);

        // Then
        assertTrue(isOwner);
    }
}