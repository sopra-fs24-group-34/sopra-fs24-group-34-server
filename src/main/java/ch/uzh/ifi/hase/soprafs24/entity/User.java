package ch.uzh.ifi.hase.soprafs24.entity;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import org.hibernate.mapping.Array;
import org.mapstruct.Mapping;

import javax.persistence.*;
import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Internal User Representation
 * This class composes the internal representation of the user and defines how
 * the user is stored in the database.
 * Every variable will be mapped into a database field with the @Column
 * annotation
 * - nullable = false -> this cannot be left empty
 * - unique = true -> this value must be unqiue across the database -> composes
 * the primary key
 */
@Entity
@Table(name = "USER")
public class User implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue
  private Long id;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false, unique = true)
  private String username;

  @Column(nullable = false, unique = true)
  private String token;

  @Column(nullable = false)
  private UserStatus status;

  @Column(nullable = true)
  private String usericon; // smailalijagic: added --> String datatype correct?

  @ManyToMany(cascade = CascadeType.ALL) // smailalijagic: user can have many friends and be friends with many
  private List<User> userfriendlist; // smailalijagic: userfriendlist contains Users

  @OneToMany(cascade = CascadeType.ALL) // smailalijagic: users can create as many lobbies as they want, but every lobby is owned by one user
  private List<Lobby> usergamelobbylist; // smailalijagic: contains all game lobbies that a user created

  @Column(nullable = true)
  private Long totalplayed; // smailalijagic: number of played games

  @Column(nullable = true)
  private Long totalwins; // smailalijagic: number of won games

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

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

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public UserStatus getStatus() {
    return status;
  }

  public void setStatus(UserStatus status) {
    this.status = status;
  }

  public String getUsericon() {
    return usericon;
  }

  public void setUsericon(String usericon) {
    this.usericon = usericon;
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
