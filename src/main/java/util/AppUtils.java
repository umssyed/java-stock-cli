package util;
import FinanceClient.FinanceClient;
import Holder.ParsedArguments;

import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.*;


public class AppUtils {

    // Check for if any date provided is larger than the current date (in future)
    public static void checkDateValidity(String start_date, String end_date) {
        // Check if start date is in correct yyyy-mm-dd format
        boolean sd_isValidFormat = isValidDateFormat(start_date);
        boolean ed_isValidFormat = isValidDateFormat(end_date);

        if (!sd_isValidFormat || !ed_isValidFormat) {
            System.err.println("Incorrect date format. Date format should be in yyyy-mm-dd.");
            System.err.println("Use --help for more information.");
            System.exit(0);
        }

        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            // Parse the date string into a LocalDate
            LocalDate sd = LocalDate.parse(start_date, outputFormatter);
            LocalDate ed = LocalDate.parse(end_date, outputFormatter);

            if (sd.isAfter(ed)) {
                System.err.println("Start date is after end date.");
                System.err.println("Use --help for more information.");
                System.exit(0);
            }

            if (sd.isAfter(LocalDate.now()) || ed.isAfter(LocalDate.now())) {
                System.err.println("One of the dates provided is in the future. Please provide a date before today's date of " + LocalDate.now());
                System.err.println("Use --help for more information.");
                System.exit(0);
            }
        } catch (DateTimeException e) {
            System.err.println("Invalid date provided. Use --help for more information.");
            System.exit(0);
        }

        LocalDate yesterdayDate = LocalDate.now().minusDays(1);

    }

    public static boolean isValidDateFormat(String inputDate) {
        // Define the regex pattern for the format yyyy-MM-dd
        String pattern = "\\d{4}-\\d{2}-\\d{2}";

        // Compile the regex pattern
        Pattern regex = Pattern.compile(pattern);

        // Create a matcher with the input date
        Matcher matcher = regex.matcher(inputDate);

        // Return true if the input date matches the pattern, false otherwise
        return matcher.matches();
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

        System.out.println("\n================== STOCK CLI - LATEST VALUE ===================\n");
        System.out.println("Stock: " + symbol);
        System.out.printf("Most Current Trading Date: %s, Closing Price: $%.2f %n", date, closePrice);

        System.out.println("\n\n\n                          ---**---                              ");
        System.out.println("Developed by Uzair Syed");
        System.out.println("Github: https://github.com/umssyed");
        System.out.println("===============================================================");

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
        System.out.println("\n==================== STOCK CLI - PRICE CHANGE =================\n");
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

        System.out.println("\n\n\n                          ---**---                              ");
        System.out.println("Developed by Uzair Syed");
        System.out.println("Github: https://github.com/umssyed");
        System.out.println("===============================================================");

    }

    public void printHelp() {
        System.err.println("Use --help for more information.");
    }

}
