package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.entity.Chat;
import ch.uzh.ifi.hase.soprafs24.repository.ChatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class ChatService {
    private final ChatRepository chatRepository;

    @Autowired
    public ChatService(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    //@Transactional
    public void addMessage(String message) {
        if (message.length() > Chat.MAX_MESSAGE_LENGTH) {
            message = message.substring(0, Chat.MAX_MESSAGE_LENGTH); // smailalijagic: only return first 250 characters
        }
        Chat chat = getOrCreateChat();
        if (chat.getMessages().size() < Chat.MAX_MESSAGE_LENGTH) {
            chat.addMessage(message);
            chatRepository.save(chat);
        }
    }

    public List<Chat> getMessages() {
        return this.chatRepository.findAll();
        //Chat chat = getOrCreateChat();
        //return chat.getMessages(); // smailalijagic: return message as Chat type
    }

    private Chat getOrCreateChat() {
        List<Chat> chats = chatRepository.findAll();
        if (chats.isEmpty()) {
            return chatRepository.save(new Chat());
        } else {
            return chats.get(0);
        }
    }
}
