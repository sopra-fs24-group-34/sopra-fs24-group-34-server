package ch.uzh.ifi.hase.soprafs24.rest.dto;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.Lobby;
import ch.uzh.ifi.hase.soprafs24.entity.User;

import java.util.List;
import java.util.Set;

public class UserPutDTO {
  // smailalijagic: all updatable data
  private String password;

  private String username;

  private UserStatus status;

  private String profilePicture;

  private String token;

  private Long id;

  private Set<User> friendsList; // smailalijagic: adding and removing friends from friendlist

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

  public Set<User> getFriendsList() {
    return friendsList;
  }

  public void setFriendsList(Set<User> friendsList) {
    this.friendsList = friendsList;
  }


  public Long getTotalplayed() {
    return totalplayed;
  }


  public Long getTotalwins() {
    return totalwins;
  }

  public void setTotalwins(Long totalwins) {
    this.totalwins = totalwins;
  }

}
