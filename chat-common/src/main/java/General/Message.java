package General;
import General.MSGType;

import java.io.Serializable;

public class Message implements Serializable {
    private String text;
    private User user;
    private MSGType messageType;

    public Message(String text){

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
}
