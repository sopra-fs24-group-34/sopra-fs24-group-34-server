package ch.uzh.ifi.hase.soprafs24.rest.dto;

public class FriendRequestPostDTO {

    private Long senderId;
    private String receiverUserName;

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public String getReceiverUserName() {
        return receiverUserName;
    }

    public void setReceiverUserName(String receiverUserName) {
        this.receiverUserName = receiverUserName;
    }
}
