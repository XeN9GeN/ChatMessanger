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
import java.util.List;

public class ClientHandler implements Runnable {
    private final NetworkConnection networkConnection;
    private User user;
    private final ServerMain server;

    public ClientHandler(Socket socket, ServerMain server) throws IOException {
        this.networkConnection = new NetworkConnection(socket);
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
        } catch (Exception e) {//всё остальное если есть
            ServerLoger.logAndEat(new SocketExceptions.ConnectionResetException(user != null ? user.getName() : "unknown"));
        }
        finally {
            server.removeHandler(this);
            try {
                networkConnection.close();
            } catch (IOException e) {
                ServerLoger.error("Ошибка закрытия сокета", e);
            }
        }
    }

    public void handleIncomingMessage(Message m) throws ServerException {
        if (m.getMessageType() == MSGType.TEXT) {
            if (m.getText() == null || m.getText().isEmpty()) {
                return;
            }
            if (m.getText().length() > 1000) {
                throw new ProtocolExceptions.MessageTooLong(m.getText().length());
            }
            server.broadcastMSG(m);
            server.getArchive().loadToTXT(m);
        }

        else if (m.getMessageType() == MSGType.LOGIN) {
            if (user == null || user.getName() == null || user.getName().isEmpty()) {
                throw new AuthExceptions.InvalidNickname("Ник пуст");
            }
            if (server.isNicknameTaken(user.getName())) {
                senMSGToClient(new Message("Ник уже занят", new User("System", 0), MSGType.TEXT));
                throw new AuthExceptions.UserAlreadyExists(user.getName());
            }

            this.user = m.getUser();
            server.addHandler(this);

            List<Message> history = server.getArchive().uploadFromTXT();
            for (Message oldMsg : history) {
                this.senMSGToClient(oldMsg);
            }

            List<String> users = server.getOnline();
            Message msg = new Message("log in new user " + user.getName(), user, MSGType.UPDATE_USERS);
            msg.setOnlineUsers(users);

            server.broadcastMSG(msg);
        }
    }

    public void senMSGToClient(Message message) {
        try {
            networkConnection.sendMSG(message);
        } catch (IOException e) {
            ServerLoger.logAndEat(new SocketExceptions.ConnectionResetException(user != null ? user.getName() : "unknown"));
        }
    }

    public User getUser() {
        return this.user;
    }
}
