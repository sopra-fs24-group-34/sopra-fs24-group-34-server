package ch.uzh.ifi.hase.soprafs24.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class FriendRequestTest {
    @Test
    public void testSetFriendRequestIdSuccess() {
        // Create a FriendRequest instance
        FriendRequest request = new FriendRequest();

        // Set friendRequestId
        request.setFriendRequestId(1L);

        // Verify friendRequestId
        assertEquals(1L, request.getFriendRequestId());
    }

    @Test
    public void testSetSenderIdSuccess() {
        // Create a FriendRequest instance
        FriendRequest request = new FriendRequest();

        // Set senderId
        request.setSenderId(2L);

        // Verify senderId
        assertEquals(2L, request.getSenderId());
    }

    @Test
    public void testSetSenderUserNameSuccess() {
        // Create a FriendRequest instance
        FriendRequest request = new FriendRequest();

        // Set senderUserName
        request.setSenderUserName("JohnDoe");

        // Verify senderUserName
        assertEquals("JohnDoe", request.getSenderUserName());
    }

    @Test
    public void testSetReceiverIdSuccess() {
        // Create a FriendRequest instance
        FriendRequest request = new FriendRequest();

        // Set receiverId
        request.setReceiverId(3L);

        // Verify receiverId
        assertEquals(3L, request.getReceiverId());
    }

    @Test
    public void testSetReceiverUserNameSuccess() {
        // Create a FriendRequest instance
        FriendRequest request = new FriendRequest();

        // Set receiverUserName
        request.setReceiverUserName("JaneDoe");

        // Verify receiverUserName
        assertEquals("JaneDoe", request.getReceiverUserName());
    }

    @Test
    public void testSetterAndGetterSuccess() {
        // Create a FriendRequest instance
        FriendRequest request = new FriendRequest();

        // Set values using setter methods
        request.setFriendRequestId(1L);
        request.setSenderId(2L);
        request.setSenderUserName("JohnDoe");
        request.setReceiverId(3L);
        request.setReceiverUserName("JaneDoe");

        // Verify values using getter methods
        assertEquals(1L, request.getFriendRequestId());
        assertEquals(2L, request.getSenderId());
        assertEquals("JohnDoe", request.getSenderUserName());
        assertEquals(3L, request.getReceiverId());
        assertEquals("JaneDoe", request.getReceiverUserName());
    }


}

