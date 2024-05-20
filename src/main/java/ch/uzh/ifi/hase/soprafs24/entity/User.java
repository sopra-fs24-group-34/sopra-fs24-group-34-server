package ch.uzh.ifi.hase.soprafs24.entity;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;

import javax.persistence.*;
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

  @Column()
  private String profilePicture; // smailalijagic: added --> String datatype correct?

  @ManyToMany
  @JoinTable(
          name = "friendRequests",
          joinColumns = @JoinColumn(name = "userId"),
          inverseJoinColumns = @JoinColumn(name = "friendId")
    )
    // smailalijagic: user can have many friends and be friends with manyp
  private List<FriendRequest> friendRequests = new ArrayList<>(); // smailalijagic: userfriendlist contains Users


  /**@OneToMany
  @JoinTable(
          name = "friends",
          joinColumns = @JoinColumn(name = "userId"),
          inverseJoinColumns = @JoinColumn(name = "friendId")
  )
  // smailalijagic: user can have many friends and be friends with many*/
  @ElementCollection
  private List<Friend> friendsList = new ArrayList<>(); // smailalijagic: userfriendlist contains Friends

  @Column(nullable = true)
  private Long totalplayed; // smailalijagic: number of played games

  @Column(nullable = true)
  private Long totalwins; // smailalijagic: number of won games

  @ElementCollection
  private List<LobbyInvitation> lobbyInvitations = new ArrayList<>();



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

  public String getProfilePicture() {
    return profilePicture;
  }

  public void setProfilePicture(String profilePicture) {
    this.profilePicture = profilePicture;
  }

  public List<Friend> getFriendsList() {
    return friendsList;
  }

  public void setFriendsList(List<Friend> FriendsList) {
    this.friendsList = FriendsList;
  }

  public void addFriend(Friend friend) {
    this.friendsList.add(friend);
  }

  public void removeFriend(User friend) {
    this.friendsList.remove(friend);
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

  public void addFriendRequest(FriendRequest friendRequest) {
      this.friendRequests.add(friendRequest);
  }

  public void removeFriendRequest(FriendRequest friendRequest) {
      this.friendRequests.remove(friendRequest);
  }

  public List<FriendRequest> getFriendRequests() {
      return friendRequests;
  }

  public void setFriendRequests(List<FriendRequest> pendingFriendRequests) {
      this.friendRequests = pendingFriendRequests;
  }

  public List<LobbyInvitation> getLobbyInvitations() {
      return lobbyInvitations;
  }

  public void setLobbyInvitations(List<LobbyInvitation> LobbyInvitations) {
      lobbyInvitations = LobbyInvitations;
  }

  public void addLobbyInvitation(LobbyInvitation lobbyInvitation) {
      this.lobbyInvitations.add(lobbyInvitation);
  }

  public void deleteLobbyInvitation(LobbyInvitation lobbyInvitation) {
      this.lobbyInvitations.remove(lobbyInvitation);
  }
}
