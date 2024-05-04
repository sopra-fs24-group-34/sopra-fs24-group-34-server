package ch.uzh.ifi.hase.soprafs24.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

//@Entity
//@Table(name = "CHAT")
public class Chat implements Serializable {

  //@ElementCollection
  //@CollectionTable(name = "MESSAGES", joinColumns = @JoinColumn(name = "chat_id"))
  //@Column(name = "MESSAGES")
  // private List<ChatTuple<String, Long>> messages;

    //@Id
    //@GeneratedValue
    //private Long chatId;

    //@OneToOne
    //@JoinColumn(name = "game_id")
    //private Game game;

  //@OneToMany(cascade = CascadeType.ALL)
  //@JoinColumn(name = "chat_id")
  private List<ChatTuple> messages = new ArrayList<ChatTuple>(); // smailalijagic: messages = {"1", "Is it male?", "2", "Yes", "2", "Does she have red hair?", "1", "Yes", "1", "..."}

  //private List<String> messages;

  private String lastmessage;

  public static int MAX_MESSAGE_LENGTH = 250; // smailalijagic: allow max 250 char per message, issue #58

  //public Chat() {
  //  this.messages = new ArrayList<>(); // smailalijagic: initialization
  //}

  public void addMessage(String message, Long writerid) {
    ChatTuple finalMessage = new ChatTuple();
    finalMessage.setMessage(message);
    finalMessage.setUserid(writerid);
    this.messages.add(finalMessage); // smailalijagic: adding messages to DB/repository (=sending message)
    this.setLastmessage(message);
    // smailalijagic: messages = {("1", "Is it male?"), ("2", "Yes"), ("2", "Does she have red hair?"), ("1", "Yes"), ("1", "...")}
  }

  //public List<ChatTuple<String, Long>> getMessages() {
  //  return messages; // smailalijagic: getting message from DB/repository (=reading message)
  //}

  public List<ChatTuple> getMessages() {
    return messages; // smailalijagic: getting message from DB/repository (=reading message)
  }

  public void setLastmessage(String lastmessage) {
    this.lastmessage = lastmessage;
  }

  public String getLastmessage() {
    return lastmessage;
  }

  // smailalijagic: just for testing, once chat works fine delete this
//  public static void main(String[] args) {
//    Chat chat = new Chat();
//    chat.addMessage("hello", 1L);
//    List<ChatTuple> messages = chat.getMessages();
//
//  }

}
