package ch.uzh.ifi.hase.soprafs24.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "CHAT")
public class Chat {
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue //(strategy = GenerationType.IDENTITY)
  private Long id;

  @ElementCollection
  @CollectionTable(name = "MESSAGES", joinColumns = @JoinColumn(name = "chat_id"))
  @Column(name = "MESSAGE")
  private List<String> messages;

  public static int MAX_MESSAGE_LENGTH = 250; // smailalijagic: allow max 250 char per message, issue #58

  //public Chat() {
  //  this.messages = new ArrayList<>(); // smailalijagic: initialization
  //}

  public void addMessage(String messages) {
    this.messages.add(messages); // smailalijagic: adding messages to DB/repository (=sending message)
  }

  public List<String> getMessages() {
    return messages; // smailalijagic: getting message from DB/repository (=reading message)
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getId() {
    return id;
  }

}
