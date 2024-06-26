package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.entity.Chat;
import ch.uzh.ifi.hase.soprafs24.entity.Game;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.GameRepository;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ChatServiceWebSockets {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final GameRepository gameRepository;

    private final UserRepository userRepository;

    @Autowired
    public ChatServiceWebSockets(GameRepository gameRepository, UserRepository userRepository) {
        this.gameRepository = gameRepository;
        this.userRepository = userRepository;
    }

    public Chat addMessage(String message, Long userId, Long gameId) {
        Game game = gameRepository.findByGameId(gameId);
        Chat chat = game.getChat();
        Chat updated_chat = chat;

        User user = userRepository.findUserById(userId);
        //assert checkIfUserExists(user);
        if (message.length() > Chat.MAX_MESSAGE_LENGTH) {
            message = message.substring(0, Chat.MAX_MESSAGE_LENGTH); // smailalijagic: only return first 250 characters
        }
        String myMessage = user.getUsername() + ": " + message;
        updated_chat.setMessages(myMessage);

        return updated_chat;
    }

    public void updateGameChat(Game game, Chat chat) {
        game.setChat(chat);
        gameRepository.save(game);
        gameRepository.flush();
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

    public List<String> getAllMessages(Long gameId) {
        Game game = this.gameRepository.findByGameId(gameId);
        Chat chat = game.getChat();
        return chat.getMessages();
    }

}