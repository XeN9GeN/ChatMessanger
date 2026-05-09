package Model;

import General.MSGType;
import General.Message;
import General.NetworkConnection;
import General.User;

import java.io.IOException;
import java.net.Socket;

public class ClientTet {
    public static void main(String[] args) throws IOException {
        try(Socket socket = new Socket("127.0.0.1",8081)){
            NetworkConnection networkConnection = new NetworkConnection(socket);

            User user = new User("xe",123);
            Message msg = new Message("HI",user, MSGType.LOGIN);
            networkConnection.sendMSG(msg);

            Message resp = (Message) networkConnection.recvMSG();//отправили со стороны сервера сообщение
            //получаем на стороне клиента
            System.out.println("S: " + resp.getMessageType() + " U: " + resp.getUser().getName());

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
