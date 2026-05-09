package Server.Model.ServerConfig;

import General.Message;
import General.User;
import Server.Model.ClientHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerMain {
    public String host;
    public int port;
    private List<ClientHandler> clientHandlers = new CopyOnWriteArrayList<>();
    private ExecutorService executorService = Executors.newFixedThreadPool(50);

    public ServerMain(String host, int port){
        this.host=host;
        this.port=port;
    }

    public void runServer() throws IOException {
        try(ServerSocket serverSocket = new ServerSocket(port)){
            System.out.println("Сервер запущен на порту: " + port);
            while(true) {
                Socket socket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(socket, this);
                executorService.execute(clientHandler);
            }

        }
        catch (IOException e){

        }
    }

    public void broadcastMSG(Message message){
        for(ClientHandler cl : clientHandlers){
            cl.senMSGToClient(message);
        }
    }

    public List<String> getOnline(){
        List<String> names = new ArrayList<>();
        for(ClientHandler cl : clientHandlers){
            if(cl.getUser()!=null){
                names.add(cl.getUser().getName());
            }
        }
        return names;
    }

    public void addHandler(ClientHandler clientHandler){
        clientHandlers.add(clientHandler);
    }
    public void removeHandler(ClientHandler clientHandler) {
        clientHandlers.remove(clientHandler);
    }
}
