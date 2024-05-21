package ch.uzh.ifi.hase.soprafs24.service.userservice;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.Friend;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs24.rest.dto.AuthenticationDTO;
import ch.uzh.ifi.hase.soprafs24.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void createGuestUser_ValidUser_ReturnsAuthenticationResponseDTO() {
        // Given
        User newGuestUser = new User();
        newGuestUser.setUsername("Guest");
        newGuestUser.setPassword("12345");

        when(userRepository.findByUsername(newGuestUser.getUsername())).thenReturn(null);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User savedUser = invocation.getArgument(0);
            savedUser.setId(1L);
            savedUser.setToken(UUID.randomUUID().toString());
            return savedUser;
        });

        // When
        AuthenticationDTO responseDTO = userService.createGuestUser(newGuestUser);

        // Then
        assertNotNull(responseDTO);
        assertNotNull(responseDTO.getId());
        assertNotNull(responseDTO.getToken());
        assertEquals(newGuestUser.getUsername(), "Guest1");
    }

    @Test
    void updateUser_ValidInput_ReturnsUser() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("testpassword");
        user.setId(1L);

        User updateduser = new User();
        updateduser.setUsername("testuser123");
        updateduser.setPassword("testpassword");

        when(userRepository.findUserById(1L)).thenReturn(user);

        user = userService.updateUser(updateduser, 1L);

        assertEquals(user.getUsername(), updateduser.getUsername());
    }

    @Test
    void updateUser_InvalidInput_ReturnsUser() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("testpassword");
        user.setId(1L);

        User updateduser = new User();
        updateduser.setUsername("");
        updateduser.setPassword("12345678");

        when(userRepository.findUserById(1L)).thenReturn(user);

        user = userService.updateUser(updateduser, 1L);

        assertEquals(user.getUsername(), "testuser");

    }

    @Test
    void updateUser_UserHasFriends_UpdatesFriendListOfFriends() {
        //create 2 Users and Friend version of them to add each other and ultimately check if the User is updated in the friends list
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("testpassword");
        user.setId(1L);
        Friend userFriend = new Friend();
        userFriend.setFriendId(1L);
        userFriend.setFriendUsername("testuser");
        //2nd user and Friend version of it
        User invited = new User();
        invited.setUsername("invited");
        invited.setPassword("testpassword");
        invited.setId(2L);
        Friend invitedFriend = new Friend();
        invitedFriend.setFriendId(2L);
        invitedFriend.setFriendUsername("invited");
        //add each other as friends
        user.addFriend(invitedFriend);
        invited.addFriend(userFriend);

        //New values for User
        User updateduser = new User();
        updateduser.setUsername("testuser123");
        updateduser.setPassword("testpassword");
        updateduser.setId(2L);

        when(userRepository.findUserById(1L)).thenReturn(user);
        when(userRepository.findUserById(2L)).thenReturn(invited);

        userService.updateUser(updateduser, 1L);

        Friend friend = invited.getFriendsList().get(0);
        assertEquals(friend.getFriendUsername(), "testuser123");


    }

    @Test
    void updateUser_InvalidInputExistingUsername_ThrowsResponseStatusException() {
        User user1 = new User();
        user1.setUsername("testuser");
        user1.setPassword("testpassword");
        user1.setId(1L);

        User user2 = new User();
        user2.setUsername("Hans");
        user2.setPassword("12345678");
        user2.setId(2L);

        User updateduser = new User();
        updateduser.setUsername("Hans");
        updateduser.setPassword("12345678");

        when(userRepository.findUserById(1L)).thenReturn(user1);
        when(userRepository.findByUsername("Hans")).thenReturn(user2);

        assertThrows(ResponseStatusException.class, () -> userService.updateUser(updateduser, 1L));
    }

    @Test
    void updateUser_InvalidInputUsername_ThrowsResponseStatusException() {
        User user1 = new User();
        user1.setUsername("testuser");
        user1.setPassword("testpassword");
        user1.setId(1L);

        User user2 = new User();
        user2.setUsername(" ");
        user2.setPassword("12345678");
        user2.setId(2L);

        User updateduser = new User();
        updateduser.setUsername("Hans");
        updateduser.setPassword("12345678");

        when(userRepository.findUserById(1L)).thenReturn(user1);
        when(userRepository.findByUsername("Hans")).thenReturn(user2);

        assertThrows(ResponseStatusException.class, () -> userService.updateUser(updateduser, 1L));
    }

    @Test
    void updateUser_InvalidInputPassword_ThrowsResponseStatusException() {
        User user1 = new User();
        user1.setUsername("testuser");
        user1.setPassword("testpassword");
        user1.setId(1L);

        User user2 = new User();
        user2.setUsername("testuser");
        user2.setPassword(" 12345678");
        user2.setId(2L);

        User updateduser = new User();
        updateduser.setUsername("Hans");
        updateduser.setPassword("12345678");

        when(userRepository.findUserById(1L)).thenReturn(user1);
        when(userRepository.findByUsername("Hans")).thenReturn(user2);

        assertThrows(ResponseStatusException.class, () -> userService.updateUser(updateduser, 1L));
    }


    @Test
    void createUser_ValidUser_ReturnsAuthenticationResponseDTO() {
        // Given
        User newUser = new User();
        newUser.setUsername("testuser");
        newUser.setPassword("password");

        when(userRepository.findByUsername(newUser.getUsername())).thenReturn(null);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User savedUser = invocation.getArgument(0);
            savedUser.setId(1L);
            savedUser.setToken(UUID.randomUUID().toString());
            return savedUser;
        });

        // When
        AuthenticationDTO responseDTO = userService.createUser(newUser);

        // Then
        assertNotNull(responseDTO);
        assertNotNull(responseDTO.getId());
        assertNotNull(responseDTO.getToken());
    }

    @Test
    void createUser_DuplicateUsername_ThrowsResponseStatusException() {
        // Given
        User existingUser = new User();
        existingUser.setUsername("existingUser");

        User newUser = new User();
        newUser.setUsername("existingUser");
        newUser.setPassword("password");

        when(userRepository.findByUsername(newUser.getUsername())).thenReturn(existingUser);

        // When/Then
        assertThrows(ResponseStatusException.class, () -> userService.createUser(newUser));
    }

    @Test
    void createUser_NullUsername_ThrowsResponseStatusException() {
        // Given
        User newUser = new User();
        newUser.setPassword("password");

        // When/Then
        assertThrows(ResponseStatusException.class, () -> userService.createUser(newUser));
    }

    @Test
    void createUser_NullPassword_ThrowsResponseStatusException() {
        // Given
        User newUser = new User();
        newUser.setUsername("testuser");

        // When/Then
        assertThrows(ResponseStatusException.class, () -> userService.createUser(newUser));
    }

    @Test
    void loginUser_ValidCredentials_ReturnsAuthenticationResponseDTO() {
        // Given
        User newUser = new User();
        newUser.setUsername("testuser");
        newUser.setPassword("password");

        when(userRepository.findByUsername(newUser.getUsername())).thenReturn(null);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User savedUser = invocation.getArgument(0);
            savedUser.setId(1L);
            savedUser.setToken(UUID.randomUUID().toString());
            return savedUser;
        });

        // When
        AuthenticationDTO responseDTO = userService.createUser(newUser);

        when(userRepository.findByUsernameAndPassword(newUser.getUsername(), newUser.getPassword())).thenReturn(newUser);

        // When
        responseDTO = userService.loginUser(newUser);

        // Then
        assertNotNull(responseDTO);
        assertNotNull(responseDTO.getId());
        assertNotNull(responseDTO.getToken());
    }

    @Test
    void loginUser_InvalidCredentials_ThrowsResponseStatusException() {
        // Given
        User loginUser = new User();
        loginUser.setUsername("nonExistingUser");
        loginUser.setPassword("invalidPassword");

        when(userRepository.findByUsernameAndPassword(loginUser.getUsername(), loginUser.getPassword())).thenReturn(null);

        // When/Then
        assertThrows(ResponseStatusException.class, () -> userService.loginUser(loginUser));
    }

    @Test
    void getUsers_ReturnsListOfUsers() {
        // Given
        List<User> userList = new ArrayList<>();
        User user1 = new User();
        user1.setId(1L);
        user1.setUsername("user1");
        userList.add(user1);

        User user2 = new User();
        user2.setId(2L);
        user2.setUsername("user2");
        userList.add(user2);

        when(userRepository.findAll()).thenReturn(userList);

        // When
        List<User> users = userService.getUsers();

        // Then
        assertEquals(2, users.size());
        assertEquals(user1, users.get(0));
        assertEquals(user2, users.get(1));
    }

    @Test
    void getUser_ExistingUserId_ReturnsUser() {
        // Given
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setUsername("testuser");

        when(userRepository.findUserById(userId)).thenReturn(user);

        // When
        User foundUser = userService.getUser(userId);

        // Then
        assertNotNull(foundUser);
        assertEquals(userId, foundUser.getId());
    }

    @Test
    void getUser_ExistingUserName_ReturnsUser() {
        Long userId = 1L;
        String username = "testuser";
        User user = new User();
        user.setId(userId);
        user.setUsername(username);

        when(userRepository.findByUsername(username)).thenReturn(user);

        User founduser = userService.getUserByUsername(username);

        assertNotNull(founduser);
        assertEquals(username, founduser.getUsername());

    }

    //smailalijagic: fails at the moment
    @Test
    void getUser_NonExistingUserId_ThrowsResponseStatusException() {
        // Given
        Long userId = 1L;
        when(userRepository.findUserById(userId)).thenReturn(null);

        // When/Then
        assertThrows(ResponseStatusException.class, () -> userService.getUser(userId));
    }

    @Test
    void getDeletedUser_ReturnsFalse() {
        // Given
        User notexistingUser = new User();
        notexistingUser.setUsername("NotExistingUser");
        notexistingUser.setPassword("existingUser");
        notexistingUser.setId(Long.valueOf(132412));
        userRepository.save(notexistingUser);
        userRepository.delete(notexistingUser);

        // smailalijagic: When/Then
        assertThrows(ResponseStatusException.class, () -> userService.getUser(notexistingUser.getId()));
    }

    @Test
    public void testDeleteUser_GuestOffline_Success() {
        // Initialize test users
        User offlineGuestUser = new User();
        offlineGuestUser.setId(1L);
        offlineGuestUser.setUsername("GUESTUser");
        offlineGuestUser.setStatus(UserStatus.OFFLINE);

        // Arrange
        Long userId = offlineGuestUser.getId();
        when(userRepository.findUserById(userId)).thenReturn(offlineGuestUser);

        // Act
        userService.deleteUser(userId);

        // Assert
        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    public void testDeleteUser_OnlineUser_Success() {
        // Initialize test users
        User onlineUser = new User();
        onlineUser.setId(2L);
        onlineUser.setUsername("RegularUser");
        onlineUser.setStatus(UserStatus.ONLINE);

        // Arrange
        Long userId = onlineUser.getId();
        when(userRepository.findUserById(userId)).thenReturn(onlineUser);

        // Act
        userService.deleteUser(userId);

        // Assert
        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    public void testDeleteUser_GuestOnline_NoDelete() {
        // Initialize test users
        User onlineGuestUser = new User();
        onlineGuestUser.setId(1L);
        onlineGuestUser.setUsername("GUESTUser");
        onlineGuestUser.setStatus(UserStatus.ONLINE);

        // Arrange
        Long userId = onlineGuestUser.getId();
        when(userRepository.findUserById(userId)).thenReturn(onlineGuestUser);

        // Act
        assertThrows(ResponseStatusException.class, () -> userService.deleteUser(userId));

        // Assert
        assertNotNull(userService.getUser(userId)); // smailalijagic: user was not deleted
        verify(userRepository, never()).deleteById(userId);
    }

    @Test
    public void testDeleteUser_UserOffline_NoDelete() {
        // Initialize test users
        User offlineUser = new User();
        offlineUser.setId(2L);
        offlineUser.setUsername("RegularUser");
        offlineUser.setStatus(UserStatus.OFFLINE);

        // Arrange
        Long userId = offlineUser.getId();
        when(userRepository.findUserById(userId)).thenReturn(offlineUser);

        // Act
        assertThrows(ResponseStatusException.class, () -> userService.deleteUser(userId));

        // Assert
        assertNotNull(userService.getUser(userId)); // smailalijagic: user was not deleted
        verify(userRepository, never()).deleteById(userId);
    }

    @Test
    public void testDeleteUser_UserPlaying_NoDelete() {
        // Initialize test users
        User offlineUser = new User();
        offlineUser.setId(2L);
        offlineUser.setUsername("RegularUser");
        offlineUser.setStatus(UserStatus.PLAYING);

        // Arrange
        Long userId = offlineUser.getId();
        when(userRepository.findUserById(userId)).thenReturn(offlineUser);

        // Act
        assertThrows(ResponseStatusException.class, () -> userService.deleteUser(userId));

        // Assert
        assertNotNull(userService.getUser(userId)); // smailalijagic: user was not deleted
        verify(userRepository, never()).deleteById(userId);
    }

    @Test
    public void testDeleteUser_UserInLobby_NoDelete() {
        // Initialize test users
        User offlineUser = new User();
        offlineUser.setId(2L);
        offlineUser.setUsername("RegularUser");
        offlineUser.setStatus(UserStatus.INLOBBY);

        // Arrange
        Long userId = offlineUser.getId();
        when(userRepository.findUserById(userId)).thenReturn(offlineUser);

        // Act
        assertThrows(ResponseStatusException.class, () -> userService.deleteUser(userId));

        // Assert
        assertNotNull(userService.getUser(userId)); // smailalijagic: user was not deleted
        verify(userRepository, never()).deleteById(userId);
    }

    @Test
    public void testDeleteUser_UserInLobbyPreparing_NoDelete() {
        // Initialize test users
        User offlineUser = new User();
        offlineUser.setId(2L);
        offlineUser.setUsername("RegularUser");
        offlineUser.setStatus(UserStatus.INLOBBY_PREPARING);

        // Arrange
        Long userId = offlineUser.getId();
        when(userRepository.findUserById(userId)).thenReturn(offlineUser);

        // Act
        assertThrows(ResponseStatusException.class, () -> userService.deleteUser(userId));

        // Assert
        assertNotNull(userService.getUser(userId)); // smailalijagic: user was not deleted
        verify(userRepository, never()).deleteById(userId);
    }

    @Test
    public void testDeleteUser_UserInLobbyReady_NoDelete() {
        // Initialize test users
        User offlineUser = new User();
        offlineUser.setId(2L);
        offlineUser.setUsername("RegularUser");
        offlineUser.setStatus(UserStatus.INLOBBY_READY);

        // Arrange
        Long userId = offlineUser.getId();
        when(userRepository.findUserById(userId)).thenReturn(offlineUser);

        // Act
        assertThrows(ResponseStatusException.class, () -> userService.deleteUser(userId));

        // Assert
        assertNotNull(userService.getUser(userId)); // smailalijagic: user was not deleted
        verify(userRepository, never()).deleteById(userId);
    }

    @Test
    public void deleteUser_UserHasFriends_DeletesUserFromFriendsList() {
        //create 2 Users and Friend version of them to add each other and ultimately check if the User is updated in the friends list
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("testpassword");
        user.setId(1L);
        user.setStatus(UserStatus.ONLINE);
        Friend userFriend = new Friend();
        userFriend.setFriendId(1L);
        userFriend.setFriendUsername("testuser");
        //2nd user and Friend version of it
        User invited = new User();
        invited.setUsername("invited");
        invited.setPassword("testpassword");
        invited.setId(2L);
        Friend invitedFriend = new Friend();
        invitedFriend.setFriendId(2L);
        invitedFriend.setFriendUsername("invited");
        //add each other as friends
        user.addFriend(invitedFriend);
        invited.addFriend(userFriend);

        when(userRepository.findUserById(1L)).thenReturn(user);
        when(userRepository.findUserById(2L)).thenReturn(invited);


        assert !invited.getFriendsList().isEmpty();

        userService.deleteUser(1L);

        assert invited.getFriendsList().isEmpty();

    }

}
