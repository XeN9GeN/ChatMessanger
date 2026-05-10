package Server.Utils.ExceptionPack;

public class ProtocolExceptions extends ServerException {
    public ProtocolExceptions(String message) { super("Нарушение протокола: " + message); }

    public static class MessageTooLong extends ProtocolExceptions {
        public MessageTooLong(int length) { super("Сообщение слишком длинное (" + length + " симв.)."); }
    }
    public static class FloodException extends ProtocolExceptions {
        public FloodException() { super("Обнаружен спам/флуд."); }
    }
    public static class UnknownMessageType extends ProtocolExceptions {
        public UnknownMessageType() { super("Получен неизвестный тип объекта."); }
    }
}
