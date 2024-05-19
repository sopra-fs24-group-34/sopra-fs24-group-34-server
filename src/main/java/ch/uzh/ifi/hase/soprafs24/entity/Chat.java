package ch.uzh.ifi.hase.soprafs24.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Chat implements Serializable {
    private List<String> messages = new ArrayList<>();
    private String lastmessage;
    public static int MAX_MESSAGE_LENGTH = 250; // smailalijagic: allow max 250 char per message, issue #58

    public List<String> getMessages() {
        return messages;
    }

    public void setMessages(String message) {
        List<String> temp = messages;
        temp.add(message);
        this.messages = temp;
        setLastmessage(message);
    }
    public void setLastmessage(String lastmessage) {
        this.lastmessage = lastmessage;
    }
    public String getLastmessage() {
        return lastmessage;
    }
}
