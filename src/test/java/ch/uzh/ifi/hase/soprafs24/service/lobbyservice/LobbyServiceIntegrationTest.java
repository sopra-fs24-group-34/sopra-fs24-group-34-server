//package ch.uzh.ifi.hase.soprafs24.service.lobbyservice;
//
//import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
//import ch.uzh.ifi.hase.soprafs24.entity.Lobby;
//import ch.uzh.ifi.hase.soprafs24.repository.LobbyRepository;
//import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
//import ch.uzh.ifi.hase.soprafs24.service.LobbyService;
//import ch.uzh.ifi.hase.soprafs24.entity.User;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.web.WebAppConfiguration;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@WebAppConfiguration
//@SpringBootTest
//public class LobbyServiceIntegrationTest {
//
//    @Qualifier("userRepository")
//    @Autowired
//    private UserRepository userRepository;
//
//    @Qualifier("lobbyRepository")
//    @Autowired
//    private LobbyRepository lobbyRepository;
//
//    @Autowired
//    private LobbyService lobbyService;
//
//
//    @BeforeEach
//    public void setup() {
//        userRepository.deleteAll();
//        lobbyRepository.deleteAll();
//    }
//
//    @Test
//    public void getUser_valid() {
//        User user = new User();
//        user.setId(1L);
//        user.setUsername("test");
//        user.setPassword("test");
//        user.setToken("1");
//        user.setStatus(UserStatus.ONLINE);
//        user = userRepository.save(user);
//        userRepository.flush();
//
//        User resUser = lobbyService.getUser(user.getId());
//
//        System.out.print(resUser.getId());
//        assertEquals(resUser.getId(), user.getId());
//        assertEquals(resUser.getToken(), user.getToken());
//        assertEquals(resUser.getUsername(), user.getUsername());
//        assertEquals(resUser.getPassword(), user.getPassword());
//    }
//
//    @Test
//    void getLobby_valid() {
//        Lobby lobby = new Lobby();
//        lobby.setLobbyid(1L);
//        lobby.setToken("1");
//        lobbyRepository.save(lobby);
//        lobbyRepository.flush();
//
//        Lobby resLobby = lobbyService.getLobby(lobby.getLobbyid());
//
//
//        assertEquals(resLobby.getLobbyid(), lobby.getLobbyid());
//        assertEquals(resLobby.getlobbyToken(), lobby.getlobbyToken());
//    }
//
//    @Test
//    void checkIfLobbyExists_returns_true() {
//        Lobby lobby = new Lobby();
//        lobby.setLobbyid(1L);
//        lobby.setToken("1");
//        lobby = lobbyRepository.save(lobby);
//        lobbyRepository.flush();
//        System.out.print(lobbyService.checkIfLobbyExists(lobby.getLobbyid()));
//        assertTrue(lobbyService.checkIfLobbyExists(lobby.getLobbyid()));
//    }
//
//    @Test
//    void deleteLobby_valid() {
//        Lobby lobby = new Lobby();
//        lobby.setLobbyid(1L);
//        lobby.setToken("1");
//        lobbyRepository.save(lobby);
//        lobbyRepository.flush();
//
//        lobbyService.deleteLobby(lobby);
//
//        assertNull(lobbyRepository.findByLobbyid(lobby.getLobbyid()));
//    }
//
//    @Test
//    void addUserToLobby_valid() {
//        Lobby lobby = new Lobby();
//        lobby.setLobbyid(1L);
//        lobby.setToken("1");
//        lobby = lobbyRepository.save(lobby);
//        lobbyRepository.flush();
//
//        User user = new User();
//        user.setId(1L);
//        user.setUsername("test");
//        user.setToken("1");
//
//        lobbyService.addUserToLobby(lobby, user);
//        lobby = lobbyRepository.findByLobbyid(lobby.getLobbyid());
//        System.out.print(lobby.getInvited_userid());
//        assertEquals(lobby.getInvited_userid(), user.getId());
//    }
//}
