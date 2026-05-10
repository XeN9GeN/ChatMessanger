package Server.Utils.ExceptionPack;

public class AuthExceptions extends ServerException {
    public AuthExceptions(String message) { super("Ошибка входа: " + message); }

    public static class UserAlreadyExists extends AuthExceptions {
        public UserAlreadyExists(String name) { super("Ник '" + name + "' уже занят."); }
    }
    public static class InvalidNickname extends AuthExceptions {
        public InvalidNickname(String reason) { super("Некорректный ник: " + reason); }
    }
    public static class BannedUser extends AuthExceptions {
        public BannedUser(String name) { super("Пользователь '" + name + "' заблокирован."); }
    }
}
