package Server.Utils.Archive;

import General.Message;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;


public class ChatArchive {
    public ChatArchive(String user){
    }

    public void loadToTXT(Message m) throws IOException {
        String path = "chat-server/src/main/java/Server/Utils/Archive/chatArchive.txt";
        try(Writer writer = new FileWriter(path,true)){
            writer.write("U " + m.getUser().getName() + " | " + "M: " + m.getText() + "\n");
        }
        catch (IOException e ){ System.out.println("trouble with txt file");}
    }
    public void uploadFromTXT(){

    }

}
