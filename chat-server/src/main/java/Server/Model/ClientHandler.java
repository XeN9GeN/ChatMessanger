package Server.Model;

import General.MSGType;
import General.Message;
import General.NetworkConnection;
import General.User;
import Server.Model.ServerConfig.ServerMain;
import Server.Utils.ExceptionPack.*;
import Server.Utils.ServerLogs.ServerLoger;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

public class ClientHandler implements Runnable {
    private final NetworkConnection networkConnection;
    private User user;
    private final ServerMain server;
    private final List<Long> messageTimestamps = new ArrayList<>();
    private static final int MAX_MESSAGES = 5;
    private static final long TIME_WINDOW_MS = 3000;



    public ClientHandler(Socket socket, ServerMain server) throws IOException {
        this.networkConnection = new NetworkConnection(socket);
        //задаёт максимальное время ожидания входящих данных при блокирующем чтении (readObject внутри recvMSG())
        this.networkConnection.socket.setSoTimeout(20000);//от клиента к серверу
        this.server = server;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Object obj = networkConnection.recvMSG();
                if (!(obj instanceof Message)) {
                    throw new ProtocolExceptions.UnknownMessageType();
                }
                handleIncomingMessage((Message) obj);
            }

        } catch (ProtocolExceptions | AuthExceptions e) {
            ServerLoger.logAndEat(e);
        } catch (SocketTimeoutException e) {
            ServerLoger.logAndEat(new SocketExceptions.TimeoutException());
        } catch (Exception e) {
            ServerLoger.info("Сессия пользователя " + (user != null ? user.getName() : "unknown") + " завершена.");
        }

        finally {
            server.removeHandler(this);
            List<String> usersWithStatus = server.getAllUsersWithStatus();
            Message logoutMsg = new Message(null, null, MSGType.UPDATE_USERS);
            logoutMsg.setOnlineUsers(usersWithStatus);
            server.broadcastMSG(logoutMsg);
        }
    }

    //обработка типов сообщений от клиента
    public void handleIncomingMessage(Message m) throws ServerException {

        if (m.getMessageType() == MSGType.TEXT) {
            try {
                checkFlood();

                if (m.getText() == null || m.getText().isEmpty() || m.getText().length() > 1000) {
                    throw new ProtocolExceptions.MessageTooLong(m.getText().length());
                }
                server.broadcastMSG(m);
                server.getArchive().loadToTXT(m);

            } catch (ProtocolExceptions.MessageTooLong e) {
                ServerLoger.logAndEat(e);
                senMSGToClient(new Message("Ошибка: сообщение слишком длинное!",
                        new User("System", 0), MSGType.TEXT));

            } catch (ProtocolExceptions.FloodException e) {
                ServerLoger.logAndEat(e);
                senMSGToClient(new Message("Предупреждение: не спамьте! Сообщение не отправлено.",
                        new User("System", 0), MSGType.TEXT));
            }
        }

        else if (m.getMessageType() == MSGType.LOGIN) {
            User temp = m.getUser();
            if (temp == null || temp.getName() == null || temp.getName().isEmpty()) {
                throw new AuthExceptions.InvalidNickname("Ник пуст");
            }
            if (server.isNicknameTaken(temp.getName())) {
                throw new AuthExceptions.UserAlreadyExists(temp.getName());
            }

            this.user = m.getUser();
            server.addHandler(this);
            server.getArchive().registerUser(this.user.getName());

            List<Message> history = server.getArchive().uploadFromTXT();
            for (Message oldMsg : history) {
                this.senMSGToClient(oldMsg);
            }
            List<String> usersWithStatus = server.getAllUsersWithStatus();


            Message msg = new Message("log in user " + user.getName(), user, MSGType.UPDATE_USERS);
            msg.setOnlineUsers(usersWithStatus);

            server.broadcastMSG(msg);
        }

        else if(m.getMessageType()==MSGType.EXIT){
            throw new ProtocolExceptions.ClientExitException();//не ловится внутри, чтобы метод выше поймал его и
            //завёл в finally
        }
    }

    private void checkFlood() throws ProtocolExceptions.FloodException {
        long now = System.currentTimeMillis();
        messageTimestamps.add(now);

        messageTimestamps.removeIf(t -> now - t > TIME_WINDOW_MS);

        if (messageTimestamps.size() > MAX_MESSAGES) {
            throw new ProtocolExceptions.FloodException();
        }
    }

    public void senMSGToClient(Message message) {
        try {
            networkConnection.sendMSG(message);
        } catch (IOException e) {
            ServerLoger.logAndEat(new SocketExceptions.ConnectionResetException(user != null ?
                    user.getName() : "unknown"));
        }
    }

    public User getUser() {
        return this.user;
    }
}
