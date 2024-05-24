package ch.uzh.ifi.hase.soprafs24.rest.dto;

public class MessageGetDTO {

  private String message; // smailalijagic: server --> client: sending List<String>

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

}
