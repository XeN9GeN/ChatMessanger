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
import java.util.List;

public class ClientHandler implements Runnable {
    private final NetworkConnection networkConnection;
    private User user;
    private final ServerMain server;

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

    public void handleIncomingMessage(Message m) throws ServerException {

        if (m.getMessageType() == MSGType.TEXT) {
            try {
                if (m.getText() == null || m.getText().isEmpty() || m.getText().length() > 1000) {
                    throw new ProtocolExceptions.MessageTooLong(m.getText().length());
                }
                server.broadcastMSG(m);
                server.getArchive().loadToTXT(m);

            } catch (ProtocolExceptions.MessageTooLong e) {
                ServerLoger.logAndEat(e);
                senMSGToClient(new Message("Ошибка: сообщение слишком длинное!",
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


            List<Message> history = server.getArchive().uploadFromTXT();
            for (Message oldMsg : history) {
                this.senMSGToClient(oldMsg);
            }

            List<String> usersWithStatus = server.getAllUsersWithStatus();
            Message msg = new Message("log in user " + user.getName(), user, MSGType.UPDATE_USERS);
            msg.setOnlineUsers(usersWithStatus);

            server.broadcastMSG(msg);
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
