package General;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class Message implements Serializable {
    private List<String> onlineUsers = new LinkedList<>();
    private MSGType messageType;
    private String text;
    private User user;

    public Message(String text,User user, MSGType type){
        this.text=text;
        this.user=user;
        this.messageType=type;
    }

    public void setOnlineUsers(List<String> users) {
        this.onlineUsers=users;
    }
    public List<String> getOnlineUsers() {
        return onlineUsers;
    }
    public MSGType getMessageType() {
        return messageType;
    }
    public String getText() {
        return text;
    }
    public User getUser() {
        return user;
    }
}
