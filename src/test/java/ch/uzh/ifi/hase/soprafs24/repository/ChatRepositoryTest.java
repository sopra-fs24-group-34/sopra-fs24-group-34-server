package ch.uzh.ifi.hase.soprafs24.repository;

import ch.uzh.ifi.hase.soprafs24.entity.Chat;
import ch.uzh.ifi.hase.soprafs24.entity.ChatTuple;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class ChatRepositoryTest {

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testFindChatById() {
        // Given
        Chat chat = new Chat();

        ChatTuple chatTuple1 = new ChatTuple();
        chatTuple1.setMessage("Is it male?");
        chatTuple1.setUserid(1L);
        chat.addMessage(chatTuple1.getMessage(), chatTuple1.getUserid());

        ChatTuple chatTuple2 = new ChatTuple();
        chatTuple2.setMessage("Yes");
        chatTuple2.setUserid(2L);
        chat.addMessage(chatTuple2.getMessage(), chatTuple2.getUserid());

        chat.setLastmessage("Yes");


        entityManager.persist(chat);
        entityManager.flush();

        // When
        Chat foundChat = chatRepository.findChatById(chat.getId());

        // Then
        assertNotNull(foundChat);
        assertEquals(chat, foundChat);

        List<ChatTuple> chatTuples = foundChat.getMessages();
        assertNotNull(chatTuples);
        assertEquals(2, chatTuples.size());
        assertEquals(chat.getLastmessage(), "Yes");
        assertEquals(chatTuple1.getUserid(), chatTuples.get(0).getUserid());
        assertEquals(chatTuple2.getMessage(), chatTuples.get(1).getMessage());
        assertEquals(chatTuple2.getUserid(), chatTuples.get(1).getUserid());
    }
}
