package ch.uzh.ifi.hase.soprafs24.rest.dto;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;

import java.awt.*;

public class UserPutDTO {
  // smailalijagic: all updatable data
  private String password;

  private String username;

  private UserStatus status;

  private String usericon; // smailalijagic: check this again --> String correct datatype?

  private String token;

  private Long id;

  private Long[] userfriendlist; // smailalijagic: adding and removing friends from friendlist

  private Long[] usergamelobbylist; // smailalijagic: adding lobbyId when creating a game to array

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

  public String getUsericon() {
    return usericon;
  }

  public void setUsericon(String usericon) {
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

  public UserStatus getStatus() {
        return status;
    }

  public void setStatus(UserStatus id) {
        this.status = status;
    }

  public Long[] getUserfriendlist() {
    return userfriendlist;
  }

  public void setUserfriendlist(Long[] userfriendlist) {
    this.userfriendlist = userfriendlist;
  }

  public Long[] getUsergamelobbylist() {
    return usergamelobbylist;
  }

  public void setUsergamelobbylist(Long[] usergamelobbylist) {
    this.usergamelobbylist = usergamelobbylist;
  }

}
