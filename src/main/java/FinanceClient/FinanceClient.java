package FinanceClient;

public class FinanceClient {
    private String symbol;
    private String day;
    private String month;
    private String year;

    private static final String API_KEY = "8Y8X5REKLY8I7QKE";
    private static final String baseURL = "https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&";

    public FinanceClient(String symbol, String day, String month, String year) {
        this.symbol = symbol;
        this.day = day;
        this.month = month;
        this.year = year;
    }
}
