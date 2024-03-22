package FinanceClient;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.*;
import config.ReadPropertyFile;

public class FinanceClient {
    private String symbol;
    private String fullURL;
    private static String ResponseJSON;
    private static String API_KEY = "";

    private static final String baseURL = "https://eodhd.com/api/eod/";
    public FinanceClient(String symbol, String start_date, String end_date) throws IOException {
        this.symbol = symbol;

        ReadPropertyFile pf = new ReadPropertyFile();
        API_KEY = pf.getAPI();

        fullURL = baseURL + symbol + ".US?from=" + start_date + "&to=" + end_date + "&period=d&api_token=" + API_KEY + "&fmt=json";
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
