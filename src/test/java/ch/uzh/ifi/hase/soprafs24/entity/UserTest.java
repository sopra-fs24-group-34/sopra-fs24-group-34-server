package ch.uzh.ifi.hase.soprafs24.entity;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserTest {
    @Test
    public void testSetIdSuccess() {
        // Create a User instance
        User user = new User();

        // Set id
        user.setId(1L);

        // Verify id
        assertEquals(1L, user.getId());
    }

    @Test
    public void testSetPasswordSuccess() {
        // Create a User instance
        User user = new User();

        // Set password
        user.setPassword("password123");

        // Verify password
        assertEquals("password123", user.getPassword());
    }

    @Test
    public void testSetUsernameSuccess() {
        // Create a User instance
        User user = new User();

        // Set username
        user.setUsername("JohnDoe");

        // Verify username
        assertEquals("JohnDoe", user.getUsername());
    }

    @Test
    public void testSetTokenSuccess() {
        // Create a User instance
        User user = new User();

        // Set token
        user.setToken("token123");

        // Verify token
        assertEquals("token123", user.getToken());
    }

    @Test
    public void testSetStatusSuccess() {
        // Create a User instance
        User user = new User();

        // Set status
        user.setStatus(UserStatus.ONLINE);

        // Verify status
        assertEquals(UserStatus.ONLINE, user.getStatus());
    }
}

