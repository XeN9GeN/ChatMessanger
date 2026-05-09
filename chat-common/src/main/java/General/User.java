package General;

import java.io.Serializable;

public class User implements Serializable {
    private String name;
    private int sessionID;

    public User(String name, int sessionID){
        this.name = name;
        this.sessionID = sessionID;
    }

    public String getName(){
        return this.name;
    }
}
