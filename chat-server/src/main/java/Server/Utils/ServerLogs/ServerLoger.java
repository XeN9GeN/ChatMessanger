package Server.Utils.ServerLogs;

import Server.Utils.ExceptionPack.ServerException;

import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class ServerLoger {
    private static final Logger log = Logger.getLogger("Server");
    private static FileHandler fh;

    public ServerLoger(){}

    static {
        try {
            fh = new FileHandler("Serverlog.log",true);
            log.addHandler(fh);
            fh.setFormatter(new SimpleFormatter());
        } catch (Exception e) {
            log.log(Level.SEVERE, "Log dead");
        }
    }

    public static void info(String m){log.info(m);}
    public static void error(String m, Exception e){log.log(Level.SEVERE, m, e);}
    public static void logAndEat(ServerException e) {
        error(e.getMessage(), e);
    }

}
