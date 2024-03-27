package ch.uzh.ifi.hase.soprafs24.rest.dto;

import java.awt.*;

public class UserPutDTO {
  // smailalijagic: all updatable data
  private String password;

  private String username;

  private Image usericon;

  private String token;

  private Long id;

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public Image getUsericon() {
    return usericon;
  }

  public void setUsericon(Image usericon) {
    this.usericon = usericon;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

}
