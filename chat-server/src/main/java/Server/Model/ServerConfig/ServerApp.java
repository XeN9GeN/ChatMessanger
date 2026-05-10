package Server.Model.ServerConfig;

import Server.Utils.ExceptionPack.InternalExceptions;
import Server.Utils.ServerLogs.ServerLoger;

public class ServerApp {
    public static void main(String[] args) {

        while (true) {
            try {
                ServerMain serverMain = new ServerMain("87.103.240.220/", 5885);
                serverMain.runServer();
            } catch (Exception e) {
                ServerLoger.logAndEat(new InternalExceptions.MemoryOverflow());

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
    }
}
