package ch.uzh.ifi.hase.soprafs24.entity;

import ch.uzh.ifi.hase.soprafs24.constant.FriendShipStatus;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "FRIENDREQUEST")
public class FriendRequest implements Serializable {

    @Id
    @GeneratedValue
    private Long friendRequestId;

    @Column(nullable = false)
    private Long senderId;

    @Column
    private String senderUserName;

    @Column(nullable = false)
    private Long receiverId;

    @Column
    private String receiverUserName;

    public Long getFriendRequestId() {
        return friendRequestId;
    }

    public void setFriendRequestId(Long friendRequestId) {
        this.friendRequestId = friendRequestId;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }

    public String getSenderUserName() {
        return senderUserName;
    }

    public void setSenderUserName(String senderUserName) {
        this.senderUserName = senderUserName;
    }

    public String getReceiverUserName() {
        return receiverUserName;
    }

    public void setReceiverUserName(String receiverUserName) {
        this.receiverUserName = receiverUserName;
    }
}
