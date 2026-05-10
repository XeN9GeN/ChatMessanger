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
        out = new ObjectOutputStream(socket.getOutputStream());
        this.out.flush();
        in = new ObjectInputStream(socket.getInputStream());
    }

    public void sendMSG(Message msg) throws IOException {
        out.writeObject(msg);
        out.flush();
        out.reset();
    }

    public Object recvMSG() throws IOException, ClassNotFoundException {
        return in.readObject();
    }

    public void close() throws IOException {
        if (in != null) in.close();
        if (out != null) out.close();
        if (socket != null && !socket.isClosed()) socket.close();
    }
}
