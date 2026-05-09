package Server.Model.ServerConfig;

import java.io.IOException;

public class ServerApp {
    public static void main(String[] args) throws IOException{
        try {
            ServerMain serverMain = new ServerMain("127.0.0.1", 8081);
            serverMain.runServer();

        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
}
