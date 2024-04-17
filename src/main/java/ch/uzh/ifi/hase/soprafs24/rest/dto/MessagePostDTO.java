package ch.uzh.ifi.hase.soprafs24.rest.dto;
import java.util.List;

public class MessagePostDTO {
  // smailalijagic: writing messages
  //private Long id;

  private String message; // smailalijagic: client --> server: sending String

  //public Long getId() {
  //  return id;
  //}

  //public void setId(Long id) {
  //  this.id = id;
  //}

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

}
