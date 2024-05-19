package ch.uzh.ifi.hase.soprafs24.entity;

import javax.persistence.Entity;

@Entity
public class GameHistory {

    private Long totalgamesplayed;
    private Long totalwins;
    private Long winPercentage;
    private Long totalrounds;
    private Long totaltime;


    public void setTotalgamesplayed(Long totalgamesplayed) {
        this.totalgamesplayed = totalgamesplayed;
    }

    public Long getTotalgamesplayed() {
        return totalgamesplayed;
    }

    public void setTotalwins(Long totalwins) {
        this.totalwins = totalwins;
    }

    public Long getTotalwins() {
        return totalwins;
    }

    public void setWinPercentage(Long winPercentage) {
        this.winPercentage = winPercentage;
    }

    public Long getWinPercentage() {
        return winPercentage;
    }
}
