package ch.uzh.ifi.hase.soprafs24.rest.dto;

import java.util.List;

public class LobbyPostDTO {

    private Long lobbyid;

    private Long lobbytoken;

    private List<Long> users;

    private Long host;

    public Long getlobbyid() {
        return lobbyid;
    }

    public void setlobbyid(Long lobbyid) {
        this.lobbyid = lobbyid;
    }

    public Long getlobbytoken(){
        return lobbytoken;
    }

    public void setLobbytoken(Long lobbytoken){
        this.lobbytoken = lobbytoken;
    }

    public List<Long> getusers(){
        return users;
    }

    public void setusers(List<Long> users){
        this.users = users;
    }

    public Long gethost(){
        return host;
    }

    public void sethost(Long host){
        this.host = host;
    }


}
