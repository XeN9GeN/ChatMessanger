package Server.Utils.ExceptionPack;

public class SocketExceptions extends ServerException {
    public SocketExceptions(String message) {
        super(message);
    }
    public static class BindException extends SocketExceptions {
        public BindException(int port) { super("Порт " + port + " уже занят."); }
    }
    public static class ConnectionResetException extends SocketExceptions {
        public ConnectionResetException(String user) { super("Соединение с " + user + " разорвано (EOF)."); }
    }
    public static class TimeoutException extends SocketExceptions {
        public TimeoutException() { super("Клиент слишком долго не отвечает."); }
    }
}
