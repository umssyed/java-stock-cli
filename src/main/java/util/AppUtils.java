package util;
import FinanceClient.FinanceClient;
import Holder.ParsedArguments;

import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.google.gson.*;


public class AppUtils {

    // Check for dates

    public static String formatDate(String inputDate) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = "";
        try {
            // Parse the date string into a LocalDate
            LocalDate date = LocalDate.parse(inputDate, inputFormatter);
            // Format the LocalDate to the desired string format
            formattedDate = date.format(outputFormatter);
        } catch (DateTimeException e) {
            System.err.println("Invalid date provided. Using current date to retrieve the stock price. Use --help for more information.");
            formattedDate = getYesterdaysDate();
        }
        return formattedDate;
    }

    public static String getYesterdaysDate() {
        LocalDate yesterdayDate = LocalDate.now().minusDays(1);
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = yesterdayDate.format(outputFormatter);
        return formattedDate;
    }

    public static void getSingleStockPrice(ParsedArguments parsedArgs) throws IOException {
        String symbol = parsedArgs.getSymbol();
        String date = parsedArgs.getStart_date();
        FinanceClient fc = new FinanceClient(symbol, date, date);
        String json = fc.getResponseJSON();

        JsonArray jsonArray = JsonParser.parseString(json).getAsJsonArray();

        // Get the first element of the array (assuming it contains the data for the requested date)
        JsonObject stockData = jsonArray.get(0).getAsJsonObject();

        // Get the "close" value from the JsonObject
        double closePrice = stockData.get("close").getAsDouble();

        System.out.println("\n=========== LATEST STOCK PRICE ===========");
        System.out.println("Stock: " + symbol);
        System.out.printf("Most Current Trading Date: %s, Closing Price: $%.2f %n", date, closePrice);

    }

    public static void compareStockPrice(ParsedArguments parsedArgs) throws IOException {
        String symbol = parsedArgs.getSymbol();
        String start_date = parsedArgs.getStart_date();
        String end_date = parsedArgs.getEnd_date();
        FinanceClient fc = new FinanceClient(symbol, start_date, end_date);
        String json = fc.getResponseJSON();

        JsonArray jsonArray = JsonParser.parseString(json).getAsJsonArray();

        // Start date stock price
        JsonObject sd_stockData = jsonArray.get(0).getAsJsonObject();
        double sd_closePrice = sd_stockData.get("close").getAsDouble();

        // End date stock price
        JsonObject ed_stockData = jsonArray.get(jsonArray.size()-1).getAsJsonObject();
        double ed_closePrice = ed_stockData.get("close").getAsDouble();

        // Print Data
        double changeInValue = ed_closePrice - sd_closePrice;
        System.out.println("\n=========== PRICE CHANGE ===========");
        System.out.println("Stock: " + symbol);
        //System.out.println("From: " + start_date + ", Closing Price: $" + sd_closePrice);
        System.out.printf("From: %s, Closing Price: $%.2f %n", start_date, sd_closePrice);
        //System.out.println("To: " + end_date + ", Closing Price: $" + ed_closePrice);
        System.out.printf("To: %s, Closing Price: $%.2f %n", end_date, ed_closePrice);

        if (changeInValue < 0) {
            //System.out.println("Change in value: -$" + (-changeInValue));
            System.out.printf("Change in value: -$%.2f %n", (-changeInValue));
        } else {
            //System.out.println("Change in value: $" + changeInValue);
            System.out.printf("Change in value: $%.2f %n", changeInValue);
        }

        if (parsedArgs.percentageChange) {
            double percentageChange = (changeInValue/sd_closePrice) * 100;
            //System.out.println("Percentage change: " + percentageChange + "%");
            System.out.printf("Percentage change: %.2f", percentageChange);
            System.out.println("%");
        }


    }

}
