package ch.uzh.ifi.hase.soprafs24.rest.mapper;

import static org.junit.jupiter.api.Assertions.*;

import ch.uzh.ifi.hase.soprafs24.entity.*;
import ch.uzh.ifi.hase.soprafs24.rest.dto.*;
import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SpringBootTest
public class DTOMapperTest {

    private DTOMapper dtoMapper = Mappers.getMapper(DTOMapper.class);

    //@Autowired
    //private DTOMapper dtoMapperInjected;

    @Test
    public void testConvertUserPostDTOtoEntitySuccess() {
        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setUsername("testUser");
        userPostDTO.setPassword("testPassword");

        User user = dtoMapper.convertUserPostDTOtoEntity(userPostDTO);

        assertNotNull(user);
        assertEquals("testUser", user.getUsername());
        assertEquals("testPassword", user.getPassword());
    }

    @Test
    public void testConvertUserPostDTOtoEntityFailure() {
        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setUsername(null);
        userPostDTO.setPassword(null);

        User user = dtoMapper.convertUserPostDTOtoEntity(userPostDTO);

        assertNotNull(user);
        assertNull(user.getUsername());
        assertNull(user.getPassword());
    }

    @Test
    public void testConvertEntityToAuthenticationDTOSuccess() {
        User user = new User();
        user.setId(1L);
        user.setToken("token123");

        AuthenticationDTO authDTO = dtoMapper.convertEntityToAuthenticationDTO(user);

        assertNotNull(authDTO);
        assertEquals(1L, authDTO.getId());
        assertEquals("token123", authDTO.getToken());
    }

    @Test
    public void testConvertEntityToAuthenticationDTOFailure() {
        User user = new User();
        user.setId(null);
        user.setToken(null);

        AuthenticationDTO authDTO = dtoMapper.convertEntityToAuthenticationDTO(user);

        assertNotNull(authDTO);
        assertNull(authDTO.getId());
        assertNull(authDTO.getToken());
    }

    @Test
    public void testConvertUserGetDTOtoEntitySuccess() {
        UserGetDTO userGetDTO = new UserGetDTO();
        userGetDTO.setId(1L);
        userGetDTO.setUsername("testUser");

        User user = dtoMapper.convertUserGetDTOtoEntity(userGetDTO);

        assertNotNull(user);
        assertEquals(1L, user.getId());
        assertEquals("testUser", user.getUsername());
    }

    @Test
    public void testConvertUserGetDTOtoEntityFailure() {
        UserGetDTO userGetDTO = new UserGetDTO();
        userGetDTO.setId(null);
        userGetDTO.setUsername(null);

        User user = dtoMapper.convertUserGetDTOtoEntity(userGetDTO);

        assertNotNull(user);
        assertNull(user.getId());
        assertNull(user.getUsername());
    }

    @Test
    public void testConvertEntityToUserGetDTOSuccess() {
        User user = new User();
        user.setId(1L);
        user.setUsername("testUser");
        user.setPassword("testPassword");
        user.setStatus(UserStatus.ONLINE);

        UserGetDTO userGetDTO = dtoMapper.convertEntityToUserGetDTO(user);

        assertNotNull(userGetDTO);
        assertEquals(1L, userGetDTO.getId());
        assertEquals("testUser", userGetDTO.getUsername());
        assertEquals(UserStatus.ONLINE, userGetDTO.getStatus());
    }

    @Test
    public void testConvertEntityToUserGetDTOFailure() {
        User user = new User();
        user.setId(null);
        user.setUsername(null);

        UserGetDTO userGetDTO = dtoMapper.convertEntityToUserGetDTO(user);

        assertNotNull(userGetDTO);
        assertNull(userGetDTO.getId());
        assertNull(userGetDTO.getUsername());
    }

    @Test
    public void testConvertEntityToUserStatsGetDTOSuccess() {
        User user = new User();
        user.setId(1L);
        user.setUsername("testUser");
        user.setStatus(UserStatus.ONLINE);
        user.setTotalplayed(10L);
        user.setTotalwins(5L);

        UserStatsGetDTO userStatsGetDTO = dtoMapper.convertEntityToUserStatsGetDTO(user);

        assertNotNull(userStatsGetDTO);
        assertEquals(1L, userStatsGetDTO.getId());
        assertEquals("testUser", userStatsGetDTO.getUsername());
        assertEquals(UserStatus.ONLINE, userStatsGetDTO.getStatus());
        assertEquals(10L, userStatsGetDTO.getTotalplayed());
        assertEquals(5L, userStatsGetDTO.getTotalwins());
    }

    @Test
    public void testConvertEntityToUserStatsGetDTOFailure() {
        User user = new User();
        user.setId(null);
        user.setUsername(null);

        UserStatsGetDTO userStatsGetDTO = dtoMapper.convertEntityToUserStatsGetDTO(user);

        assertNotNull(userStatsGetDTO);
        assertNull(userStatsGetDTO.getId());
        assertNull(userStatsGetDTO.getUsername());
    }

    @Test
    public void testConvertUserPutDTOtoEntitySuccess() {
        UserPutDTO userPutDTO = new UserPutDTO();
        userPutDTO.setId(1L);
        userPutDTO.setUsername("testUser");
        userPutDTO.setPassword("testPassword");
        Set<User> friendlist = new HashSet<>();
        userPutDTO.setFriendsList(friendlist);

        User user = dtoMapper.convertUserPutDTOtoEntity(userPutDTO);

        assertNotNull(user);
        assertEquals(1L, user.getId());
        assertEquals("testUser", user.getUsername());
        assertEquals("testPassword", user.getPassword());
    }

    @Test
    public void testConvertUserPutDTOtoEntityFailure() {
        UserPutDTO userPutDTO = new UserPutDTO();
        userPutDTO.setId(null);
        userPutDTO.setUsername(null);
        userPutDTO.setPassword(null);

        User user = dtoMapper.convertUserPutDTOtoEntity(userPutDTO);

        assertNotNull(user);
        assertNull(user.getId());
        assertNull(user.getUsername());
        assertNull(user.getPassword());
    }

    @Test
    public void testConvertAuthenticationDTOtoUserSuccess() {
        AuthenticationDTO authenticationDTO = new AuthenticationDTO();
        authenticationDTO.setId(1L);
        authenticationDTO.setToken("token123");

        User user = dtoMapper.convertAuthenticationDTOtoUser(authenticationDTO);

        assertNotNull(user);
        assertEquals(1L, user.getId());
        assertEquals("token123", user.getToken());
    }

    @Test
    public void testConvertAuthenticationDTOtoUserFailure() {
        AuthenticationDTO authenticationDTO = new AuthenticationDTO();
        authenticationDTO.setId(null);
        authenticationDTO.setToken(null);

        User user = dtoMapper.convertAuthenticationDTOtoUser(authenticationDTO);

        assertNotNull(user);
        assertNull(user.getId());
        assertNull(user.getToken());
    }

    @Test
    public void testConvertMessagePostDTOtoEntitySuccess() {
        MessagePostDTO messagePostDTO = new MessagePostDTO();
        messagePostDTO.setMessage("Hello, World!");

        Chat chat = dtoMapper.convertMessagePostDTOtoEntity(messagePostDTO);

        assertNotNull(chat);
        assertEquals("Hello, World!", chat.getLastmessage());
    }

    @Test
    public void testConvertMessagePostDTOtoEntityFailure() {
        MessagePostDTO messagePostDTO = new MessagePostDTO();
        messagePostDTO.setMessage(null);

        Chat chat = dtoMapper.convertMessagePostDTOtoEntity(messagePostDTO);

        assertNotNull(chat);
        assertNull(chat.getLastmessage());
    }

    @Test
    public void testConvertEntityToMessageGetDTOSuccess() {
        Chat chat = new Chat();
        chat.setLastmessage("Hello, World!");

        MessageGetDTO messageGetDTO = dtoMapper.convertEntityToMessageGetDTO(chat);

        assertNotNull(messageGetDTO);
        assertEquals("Hello, World!", messageGetDTO.getMessage());
    }

    @Test
    public void testConvertEntityToMessageGetDTOFailure() {
        Chat chat = new Chat();
        chat.setLastmessage(null);

        MessageGetDTO messageGetDTO = dtoMapper.convertEntityToMessageGetDTO(chat);

        assertNotNull(messageGetDTO);
        assertNull(messageGetDTO.getMessage());
    }

    @Test
    public void testConvertEntityToLobbyDeleteDTOSuccess() {
        LobbyDeleteDTO lobbyDeleteDTO = new LobbyDeleteDTO();
        lobbyDeleteDTO.setId(1L);

        Lobby lobby = dtoMapper.convertEntityToLobbyDeleteDTO(lobbyDeleteDTO);

        assertNotNull(lobby);
        assertEquals(1L, lobby.getLobbyid());
    }

    @Test
    public void testConvertEntityToLobbyDeleteDTOFailure() {
        LobbyDeleteDTO lobbyDeleteDTO = new LobbyDeleteDTO();
        lobbyDeleteDTO.setId(null);

        Lobby lobby = dtoMapper.convertEntityToLobbyDeleteDTO(lobbyDeleteDTO);

        assertNotNull(lobby);
        assertNull(lobby.getLobbyid());
    }

    @Test
    public void testConvertEntityToImageDTOSuccess() {
        Image image = new Image();
        image.setId(1L);
        image.setUrl("http://example.com/image.png");

        ImageDTO imageDTO = dtoMapper.convertEntityToImageDTO(image);

        assertNotNull(imageDTO);
        assertEquals(1L, imageDTO.getId());
        assertEquals("http://example.com/image.png", imageDTO.getUrl());
    }

    @Test
    public void testConvertEntityToImageDTOFailure() {
        Image image = new Image();
        image.setId(null);
        image.setUrl(null);

        ImageDTO imageDTO = dtoMapper.convertEntityToImageDTO(image);

        assertNotNull(imageDTO);
        assertNull(imageDTO.getId());
        assertNull(imageDTO.getUrl());
    }

    @Test
    public void testConvertEntityToLobbyGetDTOSuccess() {
        Lobby lobby = new Lobby();
        lobby.setLobbyid(1L);
        lobby.setCreator_userid(2L);
        lobby.setInvited_userid(3L);

        LobbyGetDTO lobbyGetDTO = dtoMapper.convertEntityToLobbyGetDTO(lobby);

        assertNotNull(lobbyGetDTO);
        assertEquals(1L, lobbyGetDTO.getId());
        assertEquals(2L, lobbyGetDTO.getCreator_userid());
        assertEquals(3L, lobbyGetDTO.getInvited_userid());
    }

    @Test
    public void testConvertEntityToLobbyGetDTOFailure() {
        Lobby lobby = new Lobby();
        lobby.setLobbyid(null);
        lobby.setCreator_userid(null);
        lobby.setInvited_userid(null);

        LobbyGetDTO lobbyGetDTO = dtoMapper.convertEntityToLobbyGetDTO(lobby);

        assertNotNull(lobbyGetDTO);
        assertNull(lobbyGetDTO.getId());
        assertNull(lobbyGetDTO.getCreator_userid());
        assertNull(lobbyGetDTO.getInvited_userid());
    }

    @Test
    public void testConvertEntityToLobbyPutDTOSuccess() {
        Lobby lobby = new Lobby();
        lobby.setLobbyid(1L);
        lobby.setCreator_userid(2L);
        lobby.setInvited_userid(3L);
        //lobby.setLobbyToken("lobbyToken123");

        LobbyPutDTO lobbyPutDTO = dtoMapper.convertEntityToLobbyPutDTO(lobby);

        assertNotNull(lobbyPutDTO);
        assertEquals(1L, lobbyPutDTO.getId());
        assertEquals(2L, lobbyPutDTO.getCreator_userid());
        assertEquals(3L, lobbyPutDTO.getInvited_userid());
        //assertEquals("lobbyToken123", lobbyPutDTO.getLobbyToken());
    }

    @Test
    public void testConvertEntityToLobbyPutDTOFailure() {
        Lobby lobby = new Lobby();
        lobby.setLobbyid(null);
        lobby.setCreator_userid(null);
        lobby.setInvited_userid(null);
        //lobby.setLobbyToken(null);

        LobbyPutDTO lobbyPutDTO = dtoMapper.convertEntityToLobbyPutDTO(lobby);

        assertNotNull(lobbyPutDTO);
        assertNull(lobbyPutDTO.getId());
        assertNull(lobbyPutDTO.getCreator_userid());
        assertNull(lobbyPutDTO.getInvited_userid());
        //assertNull(lobbyPutDTO.getLobbyToken());
    }

    @Test
    public void testConvertEntityToLobbyPostDTOSuccess() {
        Lobby lobby = new Lobby();
        lobby.setLobbyid(1L);
        lobby.setCreator_userid(2L);
        lobby.setInvited_userid(3L);

        LobbyPostDTO lobbyPostDTO = dtoMapper.convertEntityToLobbyPostDTO(lobby);

        assertNotNull(lobbyPostDTO);
        assertEquals(1L, lobbyPostDTO.getId());
        assertEquals(2L, lobbyPostDTO.getCreator_userid());
    }

    @Test
    public void testConvertEntityToLobbyPostDTOFailure() {
        Lobby lobby = new Lobby();
        lobby.setLobbyid(null);
        lobby.setCreator_userid(null);

        LobbyPostDTO lobbyPostDTO = dtoMapper.convertEntityToLobbyPostDTO(lobby);

        assertNotNull(lobbyPostDTO);
        assertNull(lobbyPostDTO.getId());
        assertNull(lobbyPostDTO.getCreator_userid());
    }

    @Test
    public void testConvertGuessPostDTOtoEntitySuccess() {
        GuessPostDTO guessPostDTO = new GuessPostDTO();
        guessPostDTO.setGameid(1L);
        guessPostDTO.setPlayerid(2L);
        guessPostDTO.setImageid(3L);

        Guess guess = dtoMapper.convertGuessPostDTOtoEntity(guessPostDTO);

        assertNotNull(guess);
        assertEquals(1L, guess.getGameId());
        assertEquals(2L, guess.getPlayerId());
        assertEquals(3L, guess.getImageId());
    }

    @Test
    public void testConvertGuessPostDTOtoEntityFailure() {
        GuessPostDTO guessPostDTO = new GuessPostDTO();
        guessPostDTO.setGameid(null);
        guessPostDTO.setPlayerid(null);
        guessPostDTO.setImageid(null);

        Guess guess = dtoMapper.convertGuessPostDTOtoEntity(guessPostDTO);

        assertNotNull(guess);
        assertNull(guess.getGameId());
        assertNull(guess.getPlayerId());
        assertNull(guess.getImageId());
    }

    @Test
    public void testConvertEntityToResponsePostDTOSuccess() {
        Response response = new Response();
        response.setGuess(false);
        response.setStrikes(2);

        ResponsePostDTO responsePostDTO = dtoMapper.convertEntitytoReponsePostDTO(response);

        assertNotNull(responsePostDTO);
        assertEquals(false, responsePostDTO.getGuess());
        assertEquals(Integer.valueOf(2), Integer.valueOf(responsePostDTO.getStrikes()));
    }

    @Test
    public void testConvertGamePostDTOtoEntitySuccess() {
        GamePostDTO gamePostDTO = new GamePostDTO();
        gamePostDTO.setCreator_userid(1L);
        gamePostDTO.setInvited_userid(2L);
        gamePostDTO.setMaxStrikes(3);

        Game game = dtoMapper.convertGamePostDTOtoEntity(gamePostDTO);

        assertNotNull(game);
        assertEquals(1L, game.getCreatorPlayerId());
        assertEquals(2L, game.getInvitedPlayerId());
        assertEquals(3, game.getMaxStrikes());
    }

    @Test
    public void testConvertGamePostDTOtoEntityFailure() {
        GamePostDTO gamePostDTO = new GamePostDTO();
        gamePostDTO.setCreator_userid(null);
        gamePostDTO.setInvited_userid(null);
        gamePostDTO.setMaxStrikes(0);

        Game game = dtoMapper.convertGamePostDTOtoEntity(gamePostDTO);

        assertNotNull(game);
        assertNull(game.getCreatorPlayerId());
        assertNull(game.getInvitedPlayerId());
        assertEquals(0, game.getMaxStrikes());
    }

    @Test
    public void testConvertUserDeleteDTOtoEntitySuccess() {
        UserDeleteDTO userDeleteDTO = new UserDeleteDTO();
        userDeleteDTO.setId(1L);
        userDeleteDTO.setUsername("testUser");

        User user = dtoMapper.convertUserDeleteDTOtoEntity(userDeleteDTO);

        assertNotNull(user);
        assertEquals(1L, user.getId());
        assertEquals("testUser", user.getUsername());
    }

    @Test
    public void testConvertUserDeleteDTOtoEntityFailure() {
        UserDeleteDTO userDeleteDTO = new UserDeleteDTO();
        userDeleteDTO.setId(null);
        userDeleteDTO.setUsername(null);

        User user = dtoMapper.convertUserDeleteDTOtoEntity(userDeleteDTO);

        assertNotNull(user);
        assertNull(user.getId());
        assertNull(user.getUsername());
    }

    @Test
    public void testConvertFriendRequestPostDTOtoEntitySuccess() {
        FriendRequestPostDTO friendRequestPostDTO = new FriendRequestPostDTO();
        friendRequestPostDTO.setSenderId(1L);
        friendRequestPostDTO.setReceiverUserName("testUser");

        FriendRequest friendRequest = dtoMapper.convertFriendRequestPostDTOtoEntity(friendRequestPostDTO);

        assertNotNull(friendRequest);
        assertEquals(1L, friendRequest.getSenderId());
        assertEquals("testUser", friendRequest.getReceiverUserName());
    }

    @Test
    public void testConvertFriendRequestPostDTOtoEntityFailure() {
        FriendRequestPostDTO friendRequestPostDTO = new FriendRequestPostDTO();
        friendRequestPostDTO.setSenderId(null);
        friendRequestPostDTO.setReceiverUserName(null);

        FriendRequest friendRequest = dtoMapper.convertFriendRequestPostDTOtoEntity(friendRequestPostDTO);

        assertNotNull(friendRequest);
        assertNull(friendRequest.getSenderId());
        assertNull(friendRequest.getReceiverUserName());
    }

    @Test
    public void testConvertFriendRequestPutDTOtoEntitySuccess() {
        FriendRequestPutDTO friendRequestPutDTO = new FriendRequestPutDTO();
        friendRequestPutDTO.setSenderId(1L);
        friendRequestPutDTO.setReceiverId(2L);

        FriendRequest friendRequest = dtoMapper.convertFriendRequestPutDTOtoEntity(friendRequestPutDTO);

        assertNotNull(friendRequest);
        assertEquals(1L, friendRequest.getSenderId());
        assertEquals(2L, friendRequest.getReceiverId());
    }

    @Test
    public void testConvertFriendRequestPutDTOtoEntityFailure() {
        FriendRequestPutDTO friendRequestPutDTO = new FriendRequestPutDTO();
        friendRequestPutDTO.setSenderId(null);
        friendRequestPutDTO.setReceiverId(null);

        FriendRequest friendRequest = dtoMapper.convertFriendRequestPutDTOtoEntity(friendRequestPutDTO);

        assertNotNull(friendRequest);
        assertNull(friendRequest.getSenderId());
        assertNull(friendRequest.getReceiverId());
    }

    // smailalijagic: old tests that pass as well
    // line coverage the same as without the old tests

    @Test
    public void test_convertUserPostDTOtoEntity() {
        // create UserPostDTO
        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setPassword("name");
        userPostDTO.setUsername("username");

        // MAP -> Create user
        User user = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);

        // check content
        assertEquals(userPostDTO.getPassword(), user.getPassword());
        assertEquals(userPostDTO.getUsername(), user.getUsername());
    }

    @Test
    public void test_convertUserGetDTOtoEntity() {
        // smailalijagic: create UserGetDTO
        UserGetDTO userGetDTO = new UserGetDTO();
        userGetDTO.setId(1L);

        // smailalijagic: MAP -> Create user
        User user = DTOMapper.INSTANCE.convertUserGetDTOtoEntity(userGetDTO);

        // smailalijagic: check content
        assertNotNull(userGetDTO);
        assertEquals(userGetDTO.getId(), user.getId());
    }

    @Test
    public void test_convertEntityToUserGetDTO() {
        // Given
        User user = new User();
        user.setId(456L);
        user.setUsername("testUser");
        user.setPassword("testPassword");

        // When
        UserGetDTO userGetDTO = dtoMapper.convertEntityToUserGetDTO(user);

        // Then
        assertNotNull(userGetDTO);
        assertEquals(456L, userGetDTO.getId());
        assertEquals("testUser", userGetDTO.getUsername());
        assertEquals("testPassword", userGetDTO.getPassword());
    }

    @Test
    public void test_convertEntityToUserStatsGetDTO() {

    }


    @Test
    public void testGetUser_fromUser_toUserGetDTO_success() {
        // create User
        User user = new User();
        user.setPassword("Firstname Lastname");
        user.setUsername("firstname@lastname");
        user.setStatus(UserStatus.OFFLINE);
        user.setToken("1");

        // MAP -> Create UserGetDTO
        UserGetDTO userGetDTO = DTOMapper.INSTANCE.convertEntityToUserGetDTO(user);

        // check content
        assertEquals(user.getId(), userGetDTO.getId());
        assertEquals(user.getPassword(), userGetDTO.getPassword());
        assertEquals(user.getUsername(), userGetDTO.getUsername());
        assertEquals(user.getStatus(), userGetDTO.getStatus());
    }


    @Test
    public void testconvertUserPostDTOtoEntity() {
        // Given
        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setUsername("testUser");
        userPostDTO.setPassword("testPassword");

        // When
        User user = dtoMapper.convertUserPostDTOtoEntity(userPostDTO);

        // Then
        assertNotNull(user);
        assertEquals("testUser", user.getUsername());
        assertEquals("testPassword", user.getPassword());
    }

    @Test
    public void testconvertUserGetDTOtoEntity() {
        // Given
        UserGetDTO userGetDTO = new UserGetDTO();
        userGetDTO.setId(123L);

        // When
        User user = dtoMapper.convertUserGetDTOtoEntity(userGetDTO);

        // Then
        assertNotNull(user);
        assertEquals(123L, user.getId());
    }

    @Test
    public void testconvertUserPutDTOtoEntity() {
        // Given
        UserPutDTO userPutDTO = new UserPutDTO();
        userPutDTO.setUsername("updatedUser");

        // When
        User user = dtoMapper.convertUserPutDTOtoEntity(userPutDTO);

        // Then
        assertNotNull(user);
        assertEquals("updatedUser", user.getUsername());
    }

    @Test
    public void testconvertEntityToAuthenticationResponseDTO() {
        // Given
        User user = new User();
        user.setId(789L);
        user.setToken("testToken");

        // When
        AuthenticationDTO responseDTO = dtoMapper.convertEntityToAuthenticationDTO(user);

        // Then
        assertNotNull(responseDTO);
        assertEquals(789L, responseDTO.getId());
        assertEquals("testToken", responseDTO.getToken());
    }

    @Test
    public void testconvertMessagePostDTOtoEntity() {
        // Given
        MessagePostDTO messagePostDTO = new MessagePostDTO();
        messagePostDTO.setMessage("testMessage");

        // When
        Chat chat = dtoMapper.convertMessagePostDTOtoEntity(messagePostDTO);

        // Then
        assertNotNull(chat);
        assertEquals("testMessage", chat.getLastmessage());
    }

    @Test
    public void testconvertEntityToMessageGetDTO() {
        // Given
        Chat chat = new Chat();
        chat.setMessages("testMessage");

        // When
        MessageGetDTO messageGetDTO = dtoMapper.convertEntityToMessageGetDTO(chat);

        // Then
        assertNotNull(messageGetDTO);
        assertEquals(chat.getLastmessage(), messageGetDTO.getMessage());
    }

    @Test
    public void testconvertEntityToLobbyDeleteDTO() {
        // Given
        LobbyDeleteDTO lobbyDeleteDTO = new LobbyDeleteDTO();
        lobbyDeleteDTO.setId(456L);

        // When
        Lobby lobby = dtoMapper.convertEntityToLobbyDeleteDTO(lobbyDeleteDTO);

        // Then
        assertNotNull(lobby);
        assertEquals(456L, lobby.getLobbyid());
    }

    @Test
    public void testconvertEntityToImageDTO() {
        // Given
        Image image = new Image();
        image.setId(789L);
        image.setUrl("testUrl");

        // When
        ImageDTO imageDTO = dtoMapper.convertEntityToImageDTO(image);

        // Then
        assertNotNull(imageDTO);
        assertEquals(789L, imageDTO.getId());
        assertEquals("testUrl", imageDTO.getUrl());
    }

    @Test
    public void testconvertEntityToLobbyGetDTO() {
        // Given
        Lobby lobby = new Lobby();
        lobby.setLobbyid(123L);
        lobby.setCreator_userid(456L);
        lobby.setInvited_userid(789L);

        // When
        LobbyGetDTO lobbyGetDTO = dtoMapper.convertEntityToLobbyGetDTO(lobby);

        // Then
        assertNotNull(lobbyGetDTO);
        assertEquals(123L, lobbyGetDTO.getId());
        assertEquals(456L, lobbyGetDTO.getCreator_userid());
        assertEquals(789L, lobbyGetDTO.getInvited_userid());
    }

    @Test
    public void testconvertEntityToLobbyPutDTO() {
        // Given
        Lobby lobby = new Lobby();
        lobby.setLobbyid(123L);
        lobby.setCreator_userid(456L);
        lobby.setInvited_userid(789L);

        // When
        LobbyPutDTO lobbyPutDTO = dtoMapper.convertEntityToLobbyPutDTO(lobby);

        // Then
        assertNotNull(lobbyPutDTO);
        assertEquals(123L, lobbyPutDTO.getId());
        assertEquals(456L, lobbyPutDTO.getCreator_userid());
        assertEquals(789L, lobbyPutDTO.getInvited_userid());
    }

    @Test
    public void testcovertEntityToLobbyPostDTO() {
        // Given
        Lobby lobby = new Lobby();
        lobby.setLobbyid(123L);
        lobby.setCreator_userid(456L);
        //lobby.setInvited_userid(789L);

        // When
        LobbyPostDTO lobbyPostDTO = dtoMapper.convertEntityToLobbyPostDTO(lobby);

        // Then
        assertNotNull(lobbyPostDTO);
        assertEquals(123L, lobbyPostDTO.getId());
        assertEquals(456L, lobbyPostDTO.getCreator_userid());
        //assertEquals(789L, lobbyPostDTO.getInvited_userid());
    }

    @Test
    public void testconvertGuessPostDTOtoEntity() {
        // Given
        GuessPostDTO guessPostDTO = new GuessPostDTO();
        guessPostDTO.setGameid(123L);
        guessPostDTO.setPlayerid(456L);
        guessPostDTO.setImageid(789L);

        // When
        Guess guess = dtoMapper.convertGuessPostDTOtoEntity(guessPostDTO);

        // Then
        assertNotNull(guess);
        assertEquals(123L, guess.getGameId());
        assertEquals(456L, guess.getPlayerId());
        assertEquals(789L, guess.getImageId());
    }

    @Test
    public void testconvertEntitytoReponsePostDTO() {
        // Given
        Response response = new Response();
        response.setGuess(false);
        response.setStrikes(2);

        // When
        ResponsePostDTO responsePostDTO = dtoMapper.convertEntitytoReponsePostDTO(response);

        // Then
        assertNotNull(responsePostDTO);
        assertEquals(false, responsePostDTO.getGuess());
        assertEquals(Long.valueOf(2), Long.valueOf(responsePostDTO.getStrikes()));
    }

    @Test
    public void testconvertGamePostDTOtoEntity() {
        // Given
        GamePostDTO gamePostDTO = new GamePostDTO();
        gamePostDTO.setCreator_userid(123L);
        gamePostDTO.setInvited_userid(456L);
        gamePostDTO.setMaxStrikes(3);

        // When
        Game game = dtoMapper.convertGamePostDTOtoEntity(gamePostDTO);

        // Then
        assertNotNull(game);
        assertEquals(123L, game.getCreatorPlayerId());
        assertEquals(456L, game.getInvitedPlayerId());
        assertEquals(3, game.getMaxStrikes());
    }
}