package Utils;

public class ClientException extends Exception {
    public ClientException(String message) {
        super(message);
    }
    public static class ConnectionException extends ClientException {
        public ConnectionException(String host) { super("Не удалось подключиться к " + host); }
    }

    public static class ProtocolException extends ClientException {
        public ProtocolException() { super("Получены некорректные данные от сервера"); }
    }
}
