package FinanceClient;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.*;
import config.ReadPropertyFile;

/**
 * The FinanceClient class is responsible for making the GET API calls
 * to eodhd.com's financial API
 */
public class FinanceClient {
    private String symbol;
    private String fullURL;
    private static String ResponseJSON;
    private static String API_KEY = "";

    // Base URL for eodhd.com
    private static final String baseURL = "https://eodhd.com/api/eod/";

    /**
     * Constructor:
     * - This gets the API KEY from the config.properties.
     * - Creates the full URL to connect to the Finance API
     * - Updates the
     * @param symbol
     * @param start_date
     * @param end_date
     * @throws IOException
     */
    public FinanceClient(String symbol, String start_date, String end_date) throws IOException {
        // Set the symbol
        this.symbol = symbol;
        // Get the API_KEY from the config.properties file
        ReadPropertyFile pf = new ReadPropertyFile();
        API_KEY = pf.getAPI();
        // Create the full URL
        fullURL = baseURL + symbol + ".US?from=" + start_date + "&to=" + end_date + "&period=d&api_token=" + API_KEY + "&fmt=json";
        // Update the ResponseJSON variable
        ResponseJSON = getStockData(fullURL);
    }

    /**
     * This method gets the entire stock's pricing information via the API call to eodhd.com
     * @param fullURL The full URL for the API call
     * @return The JSON in String format
     * @throws IOException Throws exception if API call to the finance API is unsuccessful
     */
    private static String getStockData(String fullURL) throws IOException {
        // Make the URL object from the String fullURL
        URL url = new URL(fullURL);
        // Create the HttpURLConnection object and open the connection
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        // Set the request method to GET
        conn.setRequestMethod("GET");
        // Set the integer value from the response for the status code
        int responseCode = conn.getResponseCode();

        // Check if status code OK (200)
        if (responseCode == HttpURLConnection.HTTP_OK) {
            // Create a reader, line and response variables
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            StringBuffer response = new StringBuffer();

            // Read from the conn.getInputStream and append to the String line until end
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            // Close the reader
            reader.close();
            // Return the JSON response in String
            return response.toString();

        } else {
            // Show the following information if the connection was unsuccessful
            System.err.println("Error getting stock information. Please ensure the correct stock ticker is provided.");
            System.err.println("Use --help for more information.");
            System.exit(0);
            return "";
        }
    }

    /**
     * This is getter for getting the response JSON variable
     * @return JSON response in String format
     */
    public String getResponseJSON() {
        return ResponseJSON;
    }
}
