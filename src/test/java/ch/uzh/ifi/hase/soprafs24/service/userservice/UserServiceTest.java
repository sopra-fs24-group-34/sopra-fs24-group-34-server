//package ch.uzh.ifi.hase.soprafs24.service.userservice;
//
//import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
//import ch.uzh.ifi.hase.soprafs24.entity.User;
//import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
//import ch.uzh.ifi.hase.soprafs24.rest.dto.AuthenticationResponseDTO;
//import ch.uzh.ifi.hase.soprafs24.service.UserService;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.http.HttpStatus;
//import org.springframework.web.server.ResponseStatusException;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.UUID;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//public class UserServiceTest {
//
//    @Mock
//    private UserRepository userRepository;
//
//    @InjectMocks
//    private UserService userService;
//
//    @Test
//    void createUser_ValidUser_ReturnsAuthenticationResponseDTO() {
//        // Given
//        User newUser = new User();
//        newUser.setUsername("testuser");
//        newUser.setPassword("password");
//
//        when(userRepository.findByUsername(newUser.getUsername())).thenReturn(null);
//        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
//            User savedUser = invocation.getArgument(0);
//            savedUser.setId(1L);
//            savedUser.setToken(UUID.randomUUID().toString());
//            return savedUser;
//        });
//
//        // When
//        AuthenticationResponseDTO responseDTO = userService.createUser(newUser);
//
//        // Then
//        assertNotNull(responseDTO);
//        assertNotNull(responseDTO.getId());
//        assertNotNull(responseDTO.getToken());
//    }
//
//    @Test
//    void createUser_DuplicateUsername_ThrowsResponseStatusException() {
//        // Given
//        User existingUser = new User();
//        existingUser.setUsername("existingUser");
//
//        User newUser = new User();
//        newUser.setUsername("existingUser");
//        newUser.setPassword("password");
//
//        when(userRepository.findByUsername(newUser.getUsername())).thenReturn(existingUser);
//
//        // When/Then
//        assertThrows(ResponseStatusException.class, () -> userService.createUser(newUser));
//    }
//
//    @Test
//    void createUser_NullUsername_ThrowsResponseStatusException() {
//        // Given
//        User newUser = new User();
//        newUser.setPassword("password");
//
//        // When/Then
//        assertThrows(ResponseStatusException.class, () -> userService.createUser(newUser));
//    }
//
//    @Test
//    void createUser_NullPassword_ThrowsResponseStatusException() {
//        // Given
//        User newUser = new User();
//        newUser.setUsername("testuser");
//
//        // When/Then
//        assertThrows(ResponseStatusException.class, () -> userService.createUser(newUser));
//    }
//
//    @Test
//    void loginUser_ValidCredentials_ReturnsAuthenticationResponseDTO() {
//        // Given
//        User newUser = new User();
//        newUser.setUsername("testuser");
//        newUser.setPassword("password");
//
//        when(userRepository.findByUsername(newUser.getUsername())).thenReturn(null);
//        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
//            User savedUser = invocation.getArgument(0);
//            savedUser.setId(1L);
//            savedUser.setToken(UUID.randomUUID().toString());
//            return savedUser;
//        });
//
//        // When
//        AuthenticationResponseDTO responseDTO = userService.createUser(newUser);
//
//
////        // Given
////        User existingUser = new User();
////        existingUser.setUsername("existingUser");
////        existingUser.setPassword("password");
////        userService.createUser(existingUser);
////        //existingUser.setStatus(UserStatus.OFFLINE);
//
//        when(userRepository.findByUsernameAndPassword(newUser.getUsername(), newUser.getPassword())).thenReturn(newUser);
//
//        // When
//        responseDTO = userService.loginUser(newUser);
//
//        // Then
//        assertNotNull(responseDTO);
//        assertNotNull(responseDTO.getId());
//        assertNotNull(responseDTO.getToken());
//    }
//
//    @Test
//    void loginUser_InvalidCredentials_ThrowsResponseStatusException() {
//        // Given
//        User loginUser = new User();
//        loginUser.setUsername("nonExistingUser");
//        loginUser.setPassword("invalidPassword");
//
//        when(userRepository.findByUsernameAndPassword(loginUser.getUsername(), loginUser.getPassword())).thenReturn(null);
//
//        // When/Then
//        assertThrows(ResponseStatusException.class, () -> userService.loginUser(loginUser));
//    }
//
//    @Test
//    void getUsers_ReturnsListOfUsers() {
//        // Given
//        List<User> userList = new ArrayList<>();
//        User user1 = new User();
//        user1.setId(1L);
//        user1.setUsername("user1");
//        userList.add(user1);
//
//        User user2 = new User();
//        user2.setId(2L);
//        user2.setUsername("user2");
//        userList.add(user2);
//
//        when(userRepository.findAll()).thenReturn(userList);
//
//        // When
//        List<User> users = userService.getUsers();
//
//        // Then
//        assertEquals(2, users.size());
//        assertEquals(user1, users.get(0));
//        assertEquals(user2, users.get(1));
//    }
//
//    @Test
//    void getUser_ExistingUserId_ReturnsUser() {
//        // Given
//        Long userId = 1L;
//        User user = new User();
//        user.setId(userId);
//        user.setUsername("testuser");
//
//        when(userRepository.findUserById(userId)).thenReturn(user);
//
//        // When
//        User foundUser = userService.getUser(userId);
//
//        // Then
//        assertNotNull(foundUser);
//        assertEquals(userId, foundUser.getId());
//    }
//
//    //smailalijagic: fails at the moment
//    @Test
//    void getUser_NonExistingUserId_ThrowsResponseStatusException() {
//        // Given
//        Long userId = 1L;
//        when(userRepository.findUserById(userId)).thenReturn(null);
//
//        // When/Then
//        assertThrows(ResponseStatusException.class, () -> userService.getUser(userId));
//    }
//
//    @Test
//    void getDeletedUser_ReturnsFalse() {
//        // Given
//        User notexistingUser = new User();
//        notexistingUser.setUsername("NotExistingUser");
//        notexistingUser.setPassword("existingUser");
//        userService.deleteGuestUser(notexistingUser.getId());
//
//        assertThrows(ResponseStatusException.class, () -> userService.getUser(notexistingUser.getId()));
//    }
//}
