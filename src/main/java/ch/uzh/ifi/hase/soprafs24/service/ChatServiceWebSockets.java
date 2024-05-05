package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.entity.Chat;
import ch.uzh.ifi.hase.soprafs24.entity.Game;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.GameRepository;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs24.rest.dto.MessageGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ChatServiceWebSockets {
    private final GameRepository gameRepository;

    private final UserRepository userRepository;

    @Autowired
    public ChatServiceWebSockets(GameRepository gameRepository, UserRepository userRepository) {
        this.gameRepository = gameRepository;
        this.userRepository = userRepository;
    }

    public MessageGetDTO addMessage(String message, Long userId, Long gameId) {
        Game game = gameRepository.findByGameId(gameId);
        Chat existingchat = game.getChat();
        //if (game.getChat() != chat) {
        //  throw new ResponseStatusException(HttpStatus.NOT_FOUND, "chat not found"); // smailalijagic: double check
        //}

        // smailalijagic: final update
        //String message = chat.getLastmessage();

        User user = userRepository.findUserById(userId);
        //assert checkIfUserExists(user);
        if (message.length() > Chat.MAX_MESSAGE_LENGTH) {
            message = message.substring(0, Chat.MAX_MESSAGE_LENGTH); // smailalijagic: only return first 250 characters
        }
        String myMessage = user.getUsername() + ": " + message;
        existingchat.addMessage(myMessage, userId);

        //chat.setLastmessage(message);

        //game.setChat(existingchat);
        //gameRepository.save(game);
        //gameRepository.flush();

        return DTOMapper.INSTANCE.convertEntityToMessageGetDTO(existingchat);

    }

    public Game getGameByGameId(Long gameid) {
        try {
            if (this.gameRepository.findByGameId(gameid) == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found error");
            }
            return this.gameRepository.findByGameId(gameid);
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found error");
        }
    }

//    public List<Chat> getAllChats() {
//        return chatRepository.findAll();
//    }
//
//    public Chat getChat(Long chatid) {
//        return this.chatRepository.findChatById(chatid);
//    }
}