package Server.Utils.Archive;

import General.MSGType;
import General.Message;
import General.User;
import Server.Utils.ExceptionPack.InternalExceptions;
import Server.Utils.ExceptionPack.ServerException;
import Server.Utils.ServerLogs.ServerLoger;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class ChatArchive {
    public ChatArchive(String user){
    }

    public void loadToTXT(Message m){
        String path = "chat-server/src/main/java/Server/Utils/Archive/chatArchive.txt";

        try(Writer writer = new FileWriter(path,true)){
            writer.write(m.getUser().getName() + ": " + m.getText() + "\n");
        }

        catch (FileNotFoundException e) {
            ServerLoger.info("Файл истории еще не создан.");
        }
        catch (IOException e) {
            ServerLoger.logAndEat(new InternalExceptions.ArchiveWriteException(path));
        }
    }

    public List<Message> uploadFromTXT(){
        List<Message> history = new ArrayList<>();
        String path = "chat-server/src/main/java/Server/Utils/Archive/chatArchive.txt";

        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains(": ")) {
                    //сообщение, где весь текст из файла идет в поле текста
                    history.add(new Message(line, new User("", 0), MSGType.TEXT));
                }
            }
        } catch (IOException e) {
            ServerLoger.logAndEat(new InternalExceptions.ArchiveReadException(path));
        }
        return history;
    }

    public List<String> getAllRegisteredUsers() {
        List<String> allUsers = new ArrayList<>();
        String path = "chatArchive.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains(": ")) {
                    String name = line.split(": ")[0].trim();
                    if (!allUsers.contains(name)) {
                        allUsers.add(name);
                    }
                }
                else {
                    String name = line.trim();
                    if (!name.isEmpty() && !allUsers.contains(name)) {
                        allUsers.add(name);
                    }
                }
            }

        } catch (IOException e) {
        }
        return allUsers;
    }

    public void registerUser(String name) {
        String path = "chatArchive.txt";
        try (Writer writer = new FileWriter(path, true)) {
            writer.write(name + "\n");
        } catch (IOException e) {
            ServerLoger.logAndEat(new InternalExceptions.ArchiveWriteException(path));
        }
    }


}
