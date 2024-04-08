package ch.uzh.ifi.hase.soprafs24.rest.dto;

import java.util.List;

public class MessageGetDTO {
  // smailalijagic: receiving messages
  private Long id;

  private List<String> messages;

//  public MessageGetDTO(List<String> messages) {
//    this.messages = messages;
//  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public List<String> getMessage() {
    return messages;
  }

  public void setMessage(List<String> messages) {
    this.messages = messages;
  }

}
