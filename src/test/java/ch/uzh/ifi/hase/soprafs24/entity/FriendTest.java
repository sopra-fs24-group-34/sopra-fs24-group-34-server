package ch.uzh.ifi.hase.soprafs24.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class FriendTest {
    @Test
    public void testSetterAndGetterSuccess() {
        // Create a Friend instance
        Friend friend = new Friend();

        // Set values using setter methods
        friend.setFriendId(1L);
        friend.setFriendUsername("JohnDoe");
        friend.setFriendIcon("icon.jpg");

        // Verify values using getter methods
        assertEquals(1L, friend.getFriendId());
        assertEquals("JohnDoe", friend.getFriendUsername());
        assertEquals("icon.jpg", friend.getFriendIcon());
    }


    @Test
    public void testToStringSuccess() {
        // Create a Friend instance
        Friend friend = new Friend();

        // Set values
        friend.setFriendId(1L);
        friend.setFriendUsername("JohnDoe");

        // Verify the toString method
        assertEquals("Friend{id=1, name=JohnDoe}", friend.toString());
    }

    @Test
    public void testToStringFailure() {
        // Create a Friend instance without setting any values
        Friend friend = new Friend();

        // Verify the toString method
        assertEquals("Friend{id=null, name=null}", friend.toString());
    }
}
