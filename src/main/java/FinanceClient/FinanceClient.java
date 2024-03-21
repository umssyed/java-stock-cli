package FinanceClient;

//import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.*;

public class FinanceClient {
    private String symbol;
    private String fullURL;
    private static String ResponseJSON;

    //private static final String API_KEY = "8Y8X5REKLY8I7QKE";
    private static final String API_KEY = "65fc0932e1bde2.60235549";

    //private static final String baseURL = "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=";
    private static final String baseURL = "https://eodhd.com/api/eod/";
    public FinanceClient(String symbol, String start_date, String end_date) throws IOException {
        this.symbol = symbol;
        //https://eodhd.com/api/eod/MCD.US?from=2020-01-05&to=2020-02-10&period=d&api_token=65fc0932e1bde2.60235549&fmt=json
        fullURL = baseURL + symbol + ".US?from=" + start_date + "&to=" + end_date + "&period=d&api_token=" + API_KEY + "&fmt=json";
        //fullURL = baseURL + this.symbol + "&outputsize=compact&apikey=" + API_KEY;
        ResponseJSON = getStockData(fullURL);
    }


    private static String getStockData(String fullURL) throws IOException {
        //System.out.println("**** Get Stock Date: " + fullURL);
        URL url = new URL(fullURL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        int responseCode = conn.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            StringBuffer response = new StringBuffer();

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            //System.out.println(response);
            //System.out.println("\n");

            return response.toString();

        } else {
            System.err.println("Error getting stock information. Please ensure the correct stock ticker is provided.");
            System.err.println("Use --help for more information.");
            System.exit(0);
            return "";
        }
    }

    public String getResponseJSON() {
        return ResponseJSON;
    }
}
