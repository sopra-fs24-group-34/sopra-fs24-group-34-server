package ch.uzh.ifi.hase.soprafs24.rest.dto;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.Lobby;
import ch.uzh.ifi.hase.soprafs24.entity.User;

import java.util.List;

public class UserStatsGetDTO {
    // smailalijagic: all updatable data
    private String username;

    private UserStatus status;

    private String profilePicture; // smailalijagic: check this again --> String correct datatype?
    
    private Long id;

    private Long totalplayed; // smailalijagic: natural number in range [0, n)

    private Long totalwins; // smailalijagic: natural number in range [0, n)

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


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
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
