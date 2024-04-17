package ch.uzh.ifi.hase.soprafs24.rest.dto;

import ch.uzh.ifi.hase.soprafs24.entity.ChatTuple;

import java.util.List;

public class MessageGetDTO {
  // smailalijagic: receiving messages
  //private Long id;

  private List<ChatTuple> messages; // smailalijagic: server --> client: sending List<String>

//  public MessageGetDTO(List<String> messages) {
//    this.messages = messages;
//  }

  //public Long getId() {
  //  return id;
  //}

  //public void setId(Long id) {
  //  this.id = id;
  //}

  public List<ChatTuple> getMessage() {
    return messages;
  }

  public void setMessage(List<ChatTuple> messages) {
    this.messages = messages;
  }

}
