package ch.uzh.ifi.hase.soprafs24.rest.dto;

public class GamePutDTO {
    private Long gameid;
    private Long playerid;
    private Long imageid;

    public Long getPlayerid(){
        return playerid;
    }

    public void setPLayerid(Long playerid){
        this.playerid = playerid;
    }

    public Long getImageid(){
        return imageid;
    }

    public void setImageid(Long imageid){
        this.imageid = imageid;
    }
}