package Server.Utils;

import java.io.InputStream;
import java.util.Properties;

public class ConfigLoader {
    private static final String CONFIG_FILE = "config.txt";

    public static int getPort() {
        Properties prop = new Properties();
        try (InputStream input = ConfigLoader.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (input == null) {
                System.out.println("Sorry, unable to find " + CONFIG_FILE + ". Using default port 8081.");
                return 8081;
            }
            prop.load(input);
            return Integer.parseInt(prop.getProperty("server.port"));
        } catch (Exception ex) {
            ex.printStackTrace();
            return 8081;
        }
    }
}
