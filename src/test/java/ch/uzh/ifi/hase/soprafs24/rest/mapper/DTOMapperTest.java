package ch.uzh.ifi.hase.soprafs24.rest.mapper;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.*;
import ch.uzh.ifi.hase.soprafs24.rest.dto.*;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DTOMapperTest {

    private final DTOMapper mapper = Mappers.getMapper(DTOMapper.class);

    // smailalijagic: old tests
    @Test
    public void testCreateUser_fromUserPostDTO_toUser_success() {
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

    // smailallijagic: old tests
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
        User user = mapper.convertUserPostDTOtoEntity(userPostDTO);

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
        User user = mapper.convertUserGetDTOtoEntity(userGetDTO);

        // Then
        assertNotNull(user);
        assertEquals(123L, user.getId());
    }

    @Test
    public void testconvertEntityToUserGetDTO() {
        // Given
        User user = new User();
        user.setId(456L);
        user.setUsername("testUser");

        // When
        UserGetDTO userGetDTO = mapper.convertEntityToUserGetDTO(user);

        // Then
        assertNotNull(userGetDTO);
        assertEquals(456L, userGetDTO.getId());
        assertEquals("testUser", userGetDTO.getUsername());
    }

    @Test
    public void testconvertUserPutDTOtoEntity() {
        // Given
        UserPutDTO userPutDTO = new UserPutDTO();
        userPutDTO.setUsername("updatedUser");

        // When
        User user = mapper.convertUserPutDTOtoEntity(userPutDTO);

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
        AuthenticationDTO responseDTO = mapper.convertEntityToAuthenticationDTO(user);

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
        Chat chat = mapper.convertMessagePostDTOtoEntity(messagePostDTO);

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
        MessageGetDTO messageGetDTO = mapper.convertEntityToMessageGetDTO(chat);

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
        Lobby lobby = mapper.convertEntityToLobbyDeleteDTO(lobbyDeleteDTO);

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
        ImageDTO imageDTO = mapper.convertEntityToImageDTO(image);

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
        LobbyGetDTO lobbyGetDTO = mapper.convertEntityToLobbyGetDTO(lobby);

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
        LobbyPutDTO lobbyPutDTO = mapper.convertEntityToLobbyPutDTO(lobby);

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
        LobbyPostDTO lobbyPostDTO = mapper.covertEntityToLobbyPostDTO(lobby);

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
        Guess guess = mapper.convertGuessPostDTOtoEntity(guessPostDTO);

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
        ResponsePostDTO responsePostDTO = mapper.convertEntitytoReponsePostDTO(response);

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

        // When
        Game game = mapper.convertGamePostDTOtoEntity(gamePostDTO);

        // Then
        assertNotNull(game);
        assertEquals(123L, game.getCreatorPlayerId());
        assertEquals(456L, game.getInvitedPlayerId());
    }
}
//package ch.uzh.ifi.hase.soprafs24.rest.mapper;
//
//import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
//import ch.uzh.ifi.hase.soprafs24.entity.*;
//import ch.uzh.ifi.hase.soprafs24.rest.dto.*;
//import org.junit.jupiter.api.Test;
//import org.mapstruct.factory.Mappers;
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//public class DTOMapperTest {
//
//    private final DTOMapper mapper = Mappers.getMapper(DTOMapper.class);
//
//    // smailalijagic: old tests
//    @Test
//    public void testCreateUser_fromUserPostDTO_toUser_success() {
//        // create UserPostDTO
//        UserPostDTO userPostDTO = new UserPostDTO();
//        userPostDTO.setPassword("name");
//        userPostDTO.setUsername("username");
//
//        // MAP -> Create user
//        User user = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);
//
//        // check content
//        assertEquals(userPostDTO.getPassword(), user.getPassword());
//        assertEquals(userPostDTO.getUsername(), user.getUsername());
//    }
//
//    // smailallijagic: old tests
//    @Test
//    public void testGetUser_fromUser_toUserGetDTO_success() {
//        // create User
//        User user = new User();
//        user.setPassword("Firstname Lastname");
//        user.setUsername("firstname@lastname");
//        user.setStatus(UserStatus.OFFLINE);
//        user.setToken("1");
//
//        // MAP -> Create UserGetDTO
//        UserGetDTO userGetDTO = DTOMapper.INSTANCE.convertEntityToUserGetDTO(user);
//
//        // check content
//        assertEquals(user.getId(), userGetDTO.getId());
//        assertEquals(user.getPassword(), userGetDTO.getPassword());
//        assertEquals(user.getUsername(), userGetDTO.getUsername());
//        assertEquals(user.getStatus(), userGetDTO.getStatus());
//    }
//
//
//    @Test
//    public void testconvertUserPostDTOtoEntity() {
//        // Given
//        UserPostDTO userPostDTO = new UserPostDTO();
//        userPostDTO.setUsername("testUser");
//        userPostDTO.setPassword("testPassword");
//
//        // When
//        User user = mapper.convertUserPostDTOtoEntity(userPostDTO);
//
//        // Then
//        assertNotNull(user);
//        assertEquals("testUser", user.getUsername());
//        assertEquals("testPassword", user.getPassword());
//    }
//
//    @Test
//    public void testconvertUserGetDTOtoEntity() {
//        // Given
//        UserGetDTO userGetDTO = new UserGetDTO();
//        userGetDTO.setId(123L);
//
//        // When
//        User user = mapper.convertUserGetDTOtoEntity(userGetDTO);
//
//        // Then
//        assertNotNull(user);
//        assertEquals(123L, user.getId());
//    }
//
//    @Test
//    public void testconvertEntityToUserGetDTO() {
//        // Given
//        User user = new User();
//        user.setId(456L);
//        user.setUsername("testUser");
//
//        // When
//        UserGetDTO userGetDTO = mapper.convertEntityToUserGetDTO(user);
//
//        // Then
//        assertNotNull(userGetDTO);
//        assertEquals(456L, userGetDTO.getId());
//        assertEquals("testUser", userGetDTO.getUsername());
//    }
//
//    @Test
//    public void testconvertUserPutDTOtoEntity() {
//        // Given
//        UserPutDTO userPutDTO = new UserPutDTO();
//        userPutDTO.setUsername("updatedUser");
//
//        // When
//        User user = mapper.convertUserPutDTOtoEntity(userPutDTO);
//
//        // Then
//        assertNotNull(user);
//        assertEquals("updatedUser", user.getUsername());
//    }
//
//    @Test
//    public void testconvertEntityToAuthenticationResponseDTO() {
//        // Given
//        User user = new User();
//        user.setId(789L);
//        user.setToken("testToken");
//
//        // When
//        AuthenticationResponseDTO responseDTO = mapper.convertEntityToAuthenticationResponseDTO(user);
//
//        // Then
//        assertNotNull(responseDTO);
//        assertEquals(789L, responseDTO.getId());
//        assertEquals("testToken", responseDTO.getToken());
//    }
//
//    @Test
//    public void testconvertMessagePostDTOtoEntity() {
//        // Given
//        MessagePostDTO messagePostDTO = new MessagePostDTO();
//        messagePostDTO.setMessage("testMessage");
//
//        // When
//        Chat chat = mapper.convertMessagePostDTOtoEntity(messagePostDTO);
//
//        // Then
//        assertNotNull(chat);
//        assertEquals("testMessage", chat.getLastmessage());
//    }
//
//    @Test
//    public void testconvertEntityToMessageGetDTO() {
//        // Given
//        Chat chat = new Chat();
//        chat.addMessage("testMessage", 123L);
//
//        List<ChatTuple> messages = chat.getMessages();
//
//        // When
//        MessageGetDTO messageGetDTO = mapper.convertEntityToMessageGetDTO(chat);
//
//        // Then
//        assertNotNull(messageGetDTO);
//        assertEquals(messages, messageGetDTO.getMessage());
//    }
//
//    @Test
//    public void testconvertEntityToLobbyDeleteDTO() {
//        // Given
//        LobbyDeleteDTO lobbyDeleteDTO = new LobbyDeleteDTO();
//        lobbyDeleteDTO.setId(456L);
//
//        // When
//        Lobby lobby = mapper.convertEntityToLobbyDeleteDTO(lobbyDeleteDTO);
//
//        // Then
//        assertNotNull(lobby);
//        assertEquals(456L, lobby.getLobbyid());
//    }
//
//    @Test
//    public void testconvertEntityToImageDTO() {
//        // Given
//        Image image = new Image();
//        image.setId(789L);
//        image.setUrl("testUrl");
//
//        // When
//        ImageDTO imageDTO = mapper.convertEntityToImageDTO(image);
//
//        // Then
//        assertNotNull(imageDTO);
//        assertEquals(789L, imageDTO.getId());
//        assertEquals("testUrl", imageDTO.getUrl());
//    }
//
//    @Test
//    public void testconvertEntityToLobbyGetDTO() {
//        // Given
//        Lobby lobby = new Lobby();
//        lobby.setLobbyid(123L);
//        lobby.setCreator_userid(456L);
//        lobby.setInvited_userid(789L);
//
//        // When
//        LobbyGetDTO lobbyGetDTO = mapper.convertEntityToLobbyGetDTO(lobby);
//
//        // Then
//        assertNotNull(lobbyGetDTO);
//        assertEquals(123L, lobbyGetDTO.getId());
//        assertEquals(456L, lobbyGetDTO.getCreator_userid());
//        assertEquals(789L, lobbyGetDTO.getInvited_userid());
//    }
//
//    @Test
//    public void testconvertEntityToLobbyPutDTO() {
//        // Given
//        Lobby lobby = new Lobby();
//        lobby.setLobbyid(123L);
//        lobby.setCreator_userid(456L);
//        lobby.setInvited_userid(789L);
//
//        // When
//        LobbyPutDTO lobbyPutDTO = mapper.convertEntityToLobbyPutDTO(lobby);
//
//        // Then
//        assertNotNull(lobbyPutDTO);
//        assertEquals(123L, lobbyPutDTO.getId());
//        assertEquals(456L, lobbyPutDTO.getCreator_userid());
//        assertEquals(789L, lobbyPutDTO.getInvited_userid());
//    }
//
//    @Test
//    public void testcovertEntityToLobbyPostDTO() {
//        // Given
//        Lobby lobby = new Lobby();
//        lobby.setLobbyid(123L);
//        lobby.setCreator_userid(456L);
//        //lobby.setInvited_userid(789L);
//
//        // When
//        LobbyPostDTO lobbyPostDTO = mapper.covertEntityToLobbyPostDTO(lobby);
//
//        // Then
//        assertNotNull(lobbyPostDTO);
//        assertEquals(123L, lobbyPostDTO.getId());
//        assertEquals(456L, lobbyPostDTO.getCreator_userid());
//        //assertEquals(789L, lobbyPostDTO.getInvited_userid());
//    }
//
//    @Test
//    public void testconvertGuessPostDTOtoEntity() {
//        // Given
//        GuessPostDTO guessPostDTO = new GuessPostDTO();
//        guessPostDTO.setGameid(123L);
//        guessPostDTO.setPlayerid(456L);
//        guessPostDTO.setImageid(789L);
//
//        // When
//        Guess guess = mapper.convertGuessPostDTOtoEntity(guessPostDTO);
//
//        // Then
//        assertNotNull(guess);
//        assertEquals(123L, guess.getGameId());
//        assertEquals(456L, guess.getPlayerId());
//        assertEquals(789L, guess.getImageId());
//    }
//
//    @Test
//    public void testconvertEntitytoReponsePostDTO() {
//        // Given
//        Response response = new Response();
//        response.setGuess(false);
//        response.setStrikes(2);
//
//        // When
//        ResponsePostDTO responsePostDTO = mapper.convertEntitytoReponsePostDTO(response);
//
//        // Then
//        assertNotNull(responsePostDTO);
//        assertEquals(false, responsePostDTO.getGuess());
//        assertEquals(Long.valueOf(2), Long.valueOf(responsePostDTO.getStrikes()));
//    }
//
//    @Test
//    public void testconvertGamePostDTOtoEntity() {
//        // Given
//        GamePostDTO gamePostDTO = new GamePostDTO();
//        gamePostDTO.setCreator_userid(123L);
//        gamePostDTO.setInvited_userid(456L);
//
//        // When
//        Game game = mapper.convertGamePostDTOtoEntity(gamePostDTO);
//
//        // Then
//        assertNotNull(game);
//        assertEquals(123L, game.getCreatorId());
//        assertEquals(456L, game.getInvitedPlayerId());
//    }
//}
