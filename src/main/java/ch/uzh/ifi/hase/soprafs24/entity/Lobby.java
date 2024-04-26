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

    //@ManyToOne(cascade = CascadeType.ALL)
    private Long creator_userid; // smailalijagic: every lobby belongs to exactly one user

    private Long invited_userid; // smailalijagic: every lobby can hold two players

    private String lobbyToken;

    @OneToOne(cascade = CascadeType.ALL)
    private Game game;// = new Game();

    public Long getLobbyid() {
        return lobbyid;
    }

    public void setLobbyid(Long lobbyid) {
        this.lobbyid = lobbyid;
    }

    public Long getCreator_userid() {
        return creator_userid;
    }

    public void setCreator_userid(Long creator_userid) {
        this.creator_userid = creator_userid;
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

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }
}