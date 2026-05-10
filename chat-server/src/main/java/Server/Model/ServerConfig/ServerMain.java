package Server.Model.ServerConfig;

import General.MSGType;
import General.Message;
import Server.Model.ClientHandler;
import Server.Utils.Archive.ChatArchive;
import Server.Utils.ExceptionPack.InternalExceptions;
import Server.Utils.ExceptionPack.SocketExceptions;
import Server.Utils.ServerLogs.ServerLoger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerMain {
    private List<ClientHandler> clientHandlers = new CopyOnWriteArrayList<>();
    //виртуальный поток внутри JVM, если есть блок операция(in/out), то он «открепляется» от реального потока процессора
    private ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor();
    private ChatArchive archive = new ChatArchive("Server");
    public String host;
    public int port;


    public ServerMain(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void runServer() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            ServerLoger.info("Сервер запущен на порту: " + port);

            startPinger();

            while (true) {
                try {
                    Socket socket = serverSocket.accept();
                    ClientHandler clientHandler = new ClientHandler(socket, this);
                    executorService.execute(clientHandler);
                } catch (IOException e) {
                    ServerLoger.error("Ошибка при подключении клиента", e);
                }
            }

        } catch (IOException e) {
            ServerLoger.logAndEat(new SocketExceptions.BindException(port));
        } catch (Exception e) {
            ServerLoger.logAndEat(new InternalExceptions.MemoryOverflow());
        }
    }

    private void startPinger() {
        executorService.execute(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Thread.sleep(4000);
                    broadcastMSG(new Message(null, null, MSGType.PING));
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
    }

    public void broadcastMSG(Message message) {
        for (ClientHandler cl : clientHandlers) {
            cl.senMSGToClient(message);
        }
    }

    public List<String> getAllUsersWithStatus(){
        List<String> allFromHistory = archive.getAllRegisteredUsers();
        List<String> currentlyOnline = getOnline();
        List<String> result = new ArrayList<>();

        for(String name : allFromHistory){
            if(currentlyOnline.contains(name)){
                result.add(name + " (online)");
            } else {
                result.add(name + " (offline)");
            }
        }
        return result;
    }
    public List<String> getOnline() {
        List<String> names = new ArrayList<>();
        for (ClientHandler cl : clientHandlers) {
            if (cl.getUser() != null) {
                names.add(cl.getUser().getName());
            }
        }
        return names;
    }

    public void addHandler(ClientHandler clientHandler) {
        clientHandlers.add(clientHandler);
        ServerLoger.info("Клиент добавлен. В сети: " + clientHandlers.size());
    }
    public void removeHandler(ClientHandler clientHandler) {
        clientHandlers.remove(clientHandler);
        ServerLoger.info("Клиент удален. В сети: " + clientHandlers.size());
    }
    public boolean isNicknameTaken(String nickname) {
        for (ClientHandler cl : clientHandlers) {
            if (cl.getUser() != null && cl.getUser().getName().equalsIgnoreCase(nickname)) {
                return true;
            }
        }
        return false;
    }


    public ChatArchive getArchive(){ return archive; }
}
