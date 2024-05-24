package ch.uzh.ifi.hase.soprafs24.rest.dto;

public class MessagePostDTO {

  private String message; // smailalijagic: client --> server: sending String

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

}
