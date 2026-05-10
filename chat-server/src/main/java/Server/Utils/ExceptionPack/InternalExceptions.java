package Server.Utils.ExceptionPack;

public class InternalExceptions extends ServerException {
    public InternalExceptions(String message) { super("Внутренняя ошибка: " + message); }

    public static class ArchiveWriteException extends InternalExceptions {
        public ArchiveWriteException(String path) { super("Не удалось записать в архив: " + path); }
    }
    public static class ArchiveReadException extends InternalExceptions {
        public ArchiveReadException(String path) { super("Не удалось прочитать архив: " + path); }
    }
    public static class MemoryOverflow extends InternalExceptions {
        public MemoryOverflow() { super("Недостаточно памяти для новых потоков."); }
    }
}
