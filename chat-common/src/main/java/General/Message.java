package General;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class Message implements Serializable {
    private String text;
    private User user;
    private MSGType messageType;
    private List<String> onlineUsers = new LinkedList<>();

    public Message(String text,User user, MSGType type){
        this.text=text;
        this.user=user;
        this.messageType=type;
    }

    public MSGType getMessageType() {
        return messageType;
    }

    public User getUser() {
        return user;
    }
    public void setText(String text){
        this.text = text;
    }

    public void setOnlineUsers(List<String> users) {
        this.onlineUsers=users;
    }
}
