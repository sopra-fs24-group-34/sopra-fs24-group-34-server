package ch.uzh.ifi.hase.soprafs24.entity;

import javax.persistence.Embeddable;

@Embeddable
public class Friend {

    private Long friendId;
    private String friendUsername;
    private String friendIcon;

    public Long getFriendId() {
        return friendId;
    }

    public void setFriendId(Long friendId) {
        this.friendId = friendId;
    }

    public String getFriendUsername() {
        return friendUsername;
    }

    public void setFriendUsername(String friendUsername) {
        this.friendUsername = friendUsername;
    }

    public String getFriendIcon() {
        return friendIcon;
    }

    public void setFriendIcon(String friendIcon) {
        this.friendIcon = friendIcon;
    }

    @Override
    public String toString() {
        return "Friend{id=" + friendId + ", name=" + friendUsername + "}";
    }


}
