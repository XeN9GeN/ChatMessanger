package Server.Model.ServerConfig;

import General.MSGType;
import General.Message;
import Server.Model.ClientHandler;
import Server.Utils.Archive.ChatArchive;
import Server.Utils.ExceptionPack.InternalExceptions;
import Server.Utils.ExceptionPack.SocketExceptions;
import Server.Utils.ServerLogs.ServerLoger;

import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

import java.net.InetSocketAddress;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.channels.ServerSocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerMain {
    private List<ClientHandler> clientHandlers = new CopyOnWriteArrayList<>();
    //виртуальный поток внутри JVM, если есть блок операция(in/out), то он «открепляется» от реального потока процессора
    private ExecutorService executorService = Executors.newFixedThreadPool(50);
    private ChatArchive archive = new ChatArchive("Server");
    private volatile boolean isRunning = true;
    public int port;


    public ServerMain( int port) {
        this.port = port;
    }

    public void runServer() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            ServerLoger.info("Сервер запущен на порту: " + port);

            startPinger();

            while (isRunning) {
                try {
                    Socket socket = serverSocket.accept();
                    ClientHandler clientHandler = new ClientHandler(socket, this);
                    executorService.execute(clientHandler);
                } catch (SocketException e) {
                    ServerLoger.error("Ошибка при подключении клиента", e);
                }
            }

        } catch (IOException e) {
            ServerLoger.logAndEat(new SocketExceptions.BindException(port));
        }
    }

    public void runServer2() throws IOException {
        try{
            ServerSocketChannel channel = ServerSocketChannel.open();
            Selector selector = Selector.open();
            channel.socket().bind(new InetSocketAddress(port));
            channel.configureBlocking(false);

            SelectionKey regKey = channel.register(selector, SelectionKey.OP_ACCEPT);

            while(true){
                int numReadyChannels = selector.select();
                if(numReadyChannels==0) continue;

                Set<SelectionKey> selectionKeySet = selector.selectedKeys();
                Iterator<SelectionKey> keyIterator = selectionKeySet.iterator();
                while(keyIterator.hasNext()){
                    SelectionKey key = keyIterator.next();

                    if(key.isAcceptable()){
                        ServerSocketChannel server = (ServerSocketChannel) key.channel();

                    }
                }
            }
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
