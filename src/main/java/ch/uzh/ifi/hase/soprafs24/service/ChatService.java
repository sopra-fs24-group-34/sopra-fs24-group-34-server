package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.entity.ChatTuple;
import ch.uzh.ifi.hase.soprafs24.entity.Chat;
import ch.uzh.ifi.hase.soprafs24.entity.Game;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.exceptions.GlobalExceptionAdvice;
import ch.uzh.ifi.hase.soprafs24.repository.ChatRepository;
import ch.uzh.ifi.hase.soprafs24.repository.GameRepository;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Transactional
public class ChatService {
  private final ChatRepository chatRepository;
  private final GameRepository gameRepository;
  private final UserRepository userRepository;

  @Autowired
  public ChatService(ChatRepository chatRepository, GameRepository gameRepository, UserRepository userRepository) {
    this.chatRepository = chatRepository;
    this.gameRepository = gameRepository;
    this.userRepository = userRepository;
    }

  //@Transactional
  public void addMessage(Chat chat, Long userid, Long gameid) {
    Game game = gameRepository.findByGameid(gameid);
    Chat existingchat = game.getChat();
    if (game.getChat() != chat) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "chat not found"); // smailalijagic: double check
    }

    // smailalijagic: final update
    String message = chat.getLastmessage();
    User user = userRepository.findUserById(gameid);
    assert checkIfUserExists(user);
    if (message.length() > Chat.MAX_MESSAGE_LENGTH) {
      message = message.substring(0, Chat.MAX_MESSAGE_LENGTH); // smailalijagic: only return first 250 characters
    }
    String myMessage = user.getUsername() + ": " + message;
    existingchat.addMessage(myMessage, userid);

    chat = chatRepository.save(existingchat);
    chatRepository.flush();

  }

//  public List<Chat> getMessages() {
//    return this.chatRepository.findAll();
//    //Chat chat = getOrCreateChat();
//    //return chat.getMessages(); // smailalijagic: return message as Chat type
//  }

  public Game getGame(Long gameid) {
    try {
      return this.gameRepository.findByGameid(gameid);
    } catch (Exception e) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found error");
    }
  }

//  private Chat getOrCreateChat() {
//    List<Chat> chats = chatRepository.findAll();
//    if (chats.isEmpty()) {
//      return chatRepository.save(new Chat());
//    } else {
//      return chats.get(0);
//    }
//  }

  private Boolean checkIfUserExists(User userToBeCreated) {
    // smailalijagic: changed to boolean
    User userByUsername = userRepository.findByUsername(userToBeCreated.getUsername());

    String baseErrorMessage = "The %s provided %s not unique. Therefore, the user could not be created!";
    if (userByUsername != null) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, String.format(baseErrorMessage, "username", "is"));
    }
    return false; // smailalijagic: user = null --> does not exist yet
  }

}
