package General;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class NetworkConnection {
    public Socket socket;
    public ObjectInputStream in;
    public ObjectOutputStream out;

    public NetworkConnection(Socket socket) throws IOException {
        this.socket=socket;
        in = new ObjectInputStream(socket.getInputStream());
        out = new ObjectOutputStream(socket.getOutputStream());
    }

    public void sendMSG(Message msg) throws IOException {
        out.writeObject(msg);
        out.flush();
    }

    public Object recvMSG() throws IOException, ClassNotFoundException {
        return in.readObject();
    }

    public void close() throws IOException {
        in.close();
        out.close();
        socket.close();
    }
}
