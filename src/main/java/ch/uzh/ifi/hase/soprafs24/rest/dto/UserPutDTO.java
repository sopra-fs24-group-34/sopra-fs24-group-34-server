package ch.uzh.ifi.hase.soprafs24.rest.dto;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.Lobby;
import ch.uzh.ifi.hase.soprafs24.entity.User;

import java.awt.*;
import java.util.List;

public class UserPutDTO {
  // smailalijagic: all updatable data
  private String password;

  private String username;

  private UserStatus status;

  private String profilePicture;

  private String token;

  private Long id;

  private List<User> userfriendlist; // smailalijagic: adding and removing friends from friendlist

  private List<Lobby> usergamelobbylist; // smailalijagic: adding lobbyId when creating a game to array

  private Long totalplayed; // smailalijagic: natural number in range [0, n)

  private Long totalwins; // smailalijagic: natural number in range [0, n)

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

  public String getProfilePicture() {
    return profilePicture;
  }

  public void setProfilePicture(String profilePicture) {
    this.profilePicture = profilePicture;
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

  public void setStatus(UserStatus satus) {
    this.status = status;
  }

  public List<User> getUserfriendlist() {
    return userfriendlist;
  }

  public void setUserfriendlist(List<User> userfriendlist) {
    this.userfriendlist = userfriendlist;
  }

  public List<Lobby> getUsergamelobbylist() {
    return usergamelobbylist;
  }

  public void setUsergamelobbylist(List<Lobby> usergamelobbylist) {
    this.usergamelobbylist = usergamelobbylist;
  }

  public Long getTotalplayed() {
    return totalplayed;
  }

  public void setTotalplayed(Long totalplayed) {
    this.totalplayed = totalplayed;
  }

  public Long getTotalwins() {
    return totalwins;
  }

  public void setTotalwins(Long totalwins) {
    this.totalwins = totalwins;
  }

}
