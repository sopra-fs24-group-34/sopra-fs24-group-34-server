package ch.uzh.ifi.hase.soprafs24.rest.dto;
import java.util.List;

public class MessagePostDTO {
  // smailalijagic: writing messages
  private Long id;

  private List<String> messages;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public List<String> getMessage() {
    return messages;
  }

  public void setMessage(String message) {
    this.messages.add(message);
  }

}
