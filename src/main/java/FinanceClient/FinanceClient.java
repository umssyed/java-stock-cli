package FinanceClient;

//import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.*;

public class FinanceClient {
    private String symbol;
    private String date;
    private String month;
    private String year;
    private String fullURL;
    private static String ResponseJSON;

    private static final String API_KEY = "8Y8X5REKLY8I7QKE";
    private static final String baseURL = "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=";

    public FinanceClient(String symbol) throws IOException {
        this.symbol = symbol;

        fullURL = baseURL + this.symbol + "&outputsize=compact&apikey=" + API_KEY;
        ResponseJSON = getStockDate(fullURL);
    }


    private static String getStockDate(String fullURL) throws IOException {
        System.out.println("**** Get Stock Date: " + fullURL);
        URL url = new URL(fullURL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        int responseCode = conn.getResponseCode();
        System.out.println("GET STATUS CODE :: " + responseCode);
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            StringBuffer response = new StringBuffer();

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            //System.out.println(response.toString());
            return response.toString();

        } else {
            System.out.println("GET Request Failed.");
            return "GET Request Failed.";
        }
    }

    public String getResponseJSON() {
        return ResponseJSON;
    }
}
