package config;


import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ReadPropertyFile {

    private static String API;
    public ReadPropertyFile() throws IOException {
        Properties props = new Properties();
        FileInputStream ip = new FileInputStream("src/main/java/config/config.properties");
        props.load(ip);
        this.API = props.getProperty("API_KEY");
    }

    public String getAPI() {
        return this.API;
    }
}
