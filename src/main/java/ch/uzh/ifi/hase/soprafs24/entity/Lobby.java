package ch.uzh.ifi.hase.soprafs24.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "LOBBY")
public class Lobby implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long lobbyid;

    //@ManyToOne(cascade = CascadeType.ALL)
    private Long creatorUserId; // smailalijagic: every lobby belongs to exactly one user

    private Long invitedUserId; // smailalijagic: every lobby can hold two players

    private String lobbyToken;

    @OneToOne(cascade = CascadeType.ALL)
    private Game game;// = new Game();

    public Long getLobbyid() {
        return lobbyid;
    }

    public void setLobbyid(Long lobbyid) {
        this.lobbyid = lobbyid;
    }

    public Long getCreatorUserId() {
        return creatorUserId;
    }

    public void setCreatorUserId(Long creator_userid) {
        this.creatorUserId = creator_userid;
    }

    public String getlobbyToken(){
        return lobbyToken;
    }

    public void setToken(String lobbyToken){
        this.lobbyToken = lobbyToken;
    }

    public Long getInvitedUserId() {
        return invitedUserId;
    }

    public void setInvitedUserId(Long invited_userid) {
        this.invitedUserId = invited_userid;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }
}