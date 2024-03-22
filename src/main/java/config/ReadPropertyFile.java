/**
 * To successfully make the API call for the stock-cli application,
 * make sure to create the config.properties file within the config
 * folder. Go to eodhd.com and sign up to get a free API.
 * In the config.properties folder you create, add an entry:
 * API_KEY = your_API_key_from_eodhd
 * Make sure there are no quotations surrounding your API Key.
 * For example:
 * API_KEY = 1234AbCdE567
 */

package config;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * This class is required for getting the API_KEY
 * from the config.properties file
 */
public class ReadPropertyFile {
    private static String API;

    /**
     * This method set the API variable to the API_KEY from the
     * config.properties file. The user must create a config.properties
     * file in the config folder to successfully make API calls
     * @throws IOException Throws exception if File Input Stream returns unsuccessful
     */
    public ReadPropertyFile() throws IOException {
        Properties props = new Properties();
        FileInputStream ip = new FileInputStream("src/main/java/config/config.properties");
        props.load(ip);
        this.API = props.getProperty("API_KEY");
    }

    /**
     * Getter - gets the API key
     * @return API key in String format
     */
    public String getAPI() {
        return this.API;
    }
}
