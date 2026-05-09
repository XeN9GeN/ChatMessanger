package Server.Model;

import General.MSGType;
import General.Message;
import General.NetworkConnection;
import General.User;
import Server.Model.ServerConfig.ServerMain;

import java.io.IOException;
import java.net.Socket;
import java.util.List;


public class ClientHandler implements Runnable{
    private final NetworkConnection networkConnection;
    private User user;
    private ServerMain server;


    public ClientHandler(Socket socket, ServerMain server) throws IOException {
        this.networkConnection = new NetworkConnection(socket);
        this.server=server;

    }

    @Override
    public void run() {
        try {
            while (true) {
                Message msg = (Message) networkConnection.recvMSG();
                handleIncomingMessage(msg);
            }
        } catch (Exception e) {
            System.out.println("Клиент отключился");
        } finally {
            server.removeHandler(this);
            try {
                networkConnection.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void handleIncomingMessage(Message m){
        if(m.getMessageType()== MSGType.TEXT){
            //server.getArchive().loadToTXT();
            server.broadcastMSG(m);
        }

        else if(m.getMessageType()== MSGType.LOGIN){
            this.user=m.getUser();
            server.addHandler(this);

            //Подумать чё с этим делать
            List<String> users = server.getOnline();
            Message msg = new Message("log in", user, MSGType.UPDATE_USERS);
            msg.setOnlineUsers(users);

            server.broadcastMSG(msg);
        }
    }
    public void senMSGToClient(Message message){
        try {
            networkConnection.sendMSG(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public User getUser(){
        return this.user;
    }
}
