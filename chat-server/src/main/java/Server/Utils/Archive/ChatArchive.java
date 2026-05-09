package Server.Utils.Archive;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;


//Как архивировать чат и где?
public class ChatArchive {
    private final String name;
    //private final Time data;
    private final List<String> history = new ArrayList<>();


    public ChatArchive(String user){
        this.name = user;
    }

    public void loadToTXT(String text) throws IOException {
        try(Writer writer = new FileWriter("chatArchive.txt")){
            writer.write(text);
        }
    }
    public void uploadFromTXT(){

    }

}
