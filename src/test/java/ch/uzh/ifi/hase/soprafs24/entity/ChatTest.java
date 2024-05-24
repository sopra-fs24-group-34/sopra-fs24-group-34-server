package ch.uzh.ifi.hase.soprafs24.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ChatTest {
    @Test
    public void testSetMessagesAndGetMessages() {
        // Create a new chat instance
        Chat chat = new Chat();

        // Add messages
        chat.setMessages("Message 1");
        chat.setMessages("Message 2");
        chat.setMessages("Message 3");

        // Get messages and verify
        assertEquals(3, chat.getMessages().size());
        assertEquals("Message 1", chat.getMessages().get(0));
        assertEquals("Message 2", chat.getMessages().get(1));
        assertEquals("Message 3", chat.getMessages().get(2));
        assertEquals("Message 3", chat.getLastmessage());
    }

    @Test
    public void testSetLastMessageAndGetLastMessage() {
        // Create a new chat instance
        Chat chat = new Chat();

        // Set last message
        chat.setLastmessage("Last Message");

        // Get last message and verify
        assertEquals("Last Message", chat.getLastmessage());
    }
}
