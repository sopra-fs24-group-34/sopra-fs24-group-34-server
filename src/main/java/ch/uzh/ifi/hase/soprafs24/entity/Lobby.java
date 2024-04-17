package ch.uzh.ifi.hase.soprafs24.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "LOBBY")
public class Lobby implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue
  private Long lobbyid;

 // @ManyToOne(cascade = CascadeType.ALL)
 // private User user; // smailalijagic: every lobby belongs to exactly one user
  private Long creatorid;

  private Long invited_userid; // smailalijagic: every lobby can hold two players

  private String lobbyToken;

  public Long getLobbyid() {
    return lobbyid;
  }

  public void setLobbyid(Long lobbyid) {
    this.lobbyid = lobbyid;
  }

  public Long getUser() {
    return creatorid;
  }

  public void setUser(Long creatorid) {
        this.creatorid = creatorid;
    }

  public String getlobbyToken(){
      return lobbyToken;
  }

  public void setToken(String lobbyToken){
      this.lobbyToken = lobbyToken;
  }

  public Long getInvited_userid() {
    return invited_userid;
  }

  public void setInvited_userid(Long invited_userid) {
    this.invited_userid = invited_userid;
  }

}
