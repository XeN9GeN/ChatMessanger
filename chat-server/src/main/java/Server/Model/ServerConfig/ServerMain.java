package Server.Model.ServerConfig;

import General.Message;
import Server.Model.ClientHandler;

import java.util.ArrayList;

public class ServerMain {
    public String host;
    public int port;
    private ArrayList<ClientHandler> clientHandlers = new ArrayList<>();


    public ServerMain(String host, int port){
        this.host=host;
        this.port=port;
    }

    public void broadcastMSG(Message message){
        for(ClientHandler cl : clientHandlers){
            cl.senMSGToClient(message);
        }
    }

    public void addHandler(ClientHandler clientHandler){
        clientHandlers.add(clientHandler);
    }
    public void removeHandler(ClientHandler clientHandler) {
        clientHandlers.remove(clientHandler);
    }

}
