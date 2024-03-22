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

/**
 * The AppUtils class provides date validity checks, date formatting,
 * gets single stock closing price and stock price changes between two dates
 */
public class AppUtils {

    // Check for if any date provided is larger than the current date (in future)

    /**
     * This method performs date validity checks
     * 1. If date format (start and end date) are valid
     * 2. If start date provided is after the end date
     * 3. If one of the dates provided are in the future (later than current date)
     * This method does not return anything, if there are errors, the program exits.
     * @param start_date Start date (from date), use -sd in cli
     * @param end_date End date (to date), use -ed in cli
     */
    public static void checkDateValidity(String start_date, String end_date) {
        // Check if start date is in correct yyyy-mm-dd format
        boolean sd_isValidFormat = isValidDateFormat(start_date);
        boolean ed_isValidFormat = isValidDateFormat(end_date);

        // Check if either dates are in valid format.
        if (!sd_isValidFormat || !ed_isValidFormat) {
            System.err.println("Incorrect date format. Date format should be in yyyy-mm-dd.");
            System.err.println("Use --help for more information.");
            System.exit(0);
        }
        // Performs formatting for the date
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            // Parse the date string into a LocalDate object
            LocalDate sd = LocalDate.parse(start_date, outputFormatter);
            LocalDate ed = LocalDate.parse(end_date, outputFormatter);

            // Check if start date is after end date
            if (sd.isAfter(ed)) {
                System.err.println("Start date is after end date.");
                System.err.println("Use --help for more information.");
                System.exit(0);
            }

            // Check if either date is in the future
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

    /**
     * This method checks if the date is in the format of yyyy-mm-dd
     * @param inputDate Date
     * @return True if correct format, False if incorrect
     */
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

    /**
     * This method takes the current local date and returns the previous date
     * in the format yyyy-mm-dd
     * @return Yesterday's date in String
     */
    public static String getYesterdaysDate() {
        LocalDate yesterdayDate = LocalDate.now().minusDays(1);
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = yesterdayDate.format(outputFormatter);
        return formattedDate;
    }

    /**
     * This method prints to the CLI the single stock's latest closing price
     * @param parsedArgs Parsed Argument object with updated values
     * @throws IOException Exception when Finance Client throws exception in getting JSON object
     */
    public static void getSingleStockPrice(ParsedArguments parsedArgs) throws IOException {
        String symbol = parsedArgs.getSymbol();
        String date = parsedArgs.getStart_date();

        // Finance Client object created with the symbol, start date and end date (in this case equal to start date)
        FinanceClient fc = new FinanceClient(symbol, date, date);
        // Get the JSON response in String format from the Finance Client object
        String json = fc.getResponseJSON();
        // Converts to JSON array
        JsonArray jsonArray = JsonParser.parseString(json).getAsJsonArray();
        // Get the first element of the array (assuming it contains the data for the requested date)
        JsonObject stockData = jsonArray.get(0).getAsJsonObject();
        // Get the "close" value from the JsonObject
        double closePrice = stockData.get("close").getAsDouble();

        //Prints to CLI
        System.out.println("\n================== STOCK CLI - LATEST VALUE ===================\n");
        System.out.println("Stock: " + symbol);
        System.out.printf("Most Current Trading Date: %s, Closing Price: $%.2f %n", date, closePrice);

        // Prints Footer
        System.out.println("\n\n\n                          ---**---                              ");
        System.out.println("Developed by Uzair Syed");
        System.out.println("Github: https://github.com/umssyed");
        System.out.println("===============================================================");

    }

    /**
     * This method prints to the CLI the stock's price changes between the two dates
     * @param parsedArgs Parsed Argument object with updated values
     * @throws IOException Exception when Finance Client throws exception in getting JSON object
     */
    public static void compareStockPrice(ParsedArguments parsedArgs) throws IOException {
        String symbol = parsedArgs.getSymbol();
        String start_date = parsedArgs.getStart_date();
        String end_date = parsedArgs.getEnd_date();

        // Finance Client object created with the symbol, start date and end date
        FinanceClient fc = new FinanceClient(symbol, start_date, end_date);
        // Get the JSON response in String format from the Finance Client object
        String json = fc.getResponseJSON();
        // Converts to JSON array
        JsonArray jsonArray = JsonParser.parseString(json).getAsJsonArray();

        // Get the first element of the JSON Array into a JSON Object. The first element
        // is the start date data of the stock
        JsonObject sd_stockData = jsonArray.get(0).getAsJsonObject();
        double sd_closePrice = sd_stockData.get("close").getAsDouble();

        // Get the last element of the JSON Array into a JSON Object. The last element
        // is the end date data of the stock
        JsonObject ed_stockData = jsonArray.get(jsonArray.size()-1).getAsJsonObject();
        double ed_closePrice = ed_stockData.get("close").getAsDouble();

        //Prints to CLI
        double changeInValue = ed_closePrice - sd_closePrice;
        System.out.println("\n==================== STOCK CLI - PRICE CHANGE =================\n");
        System.out.println("Stock: " + symbol);
        System.out.printf("From: %s, Closing Price: $%.2f %n", start_date, sd_closePrice);
        System.out.printf("To: %s, Closing Price: $%.2f %n", end_date, ed_closePrice);
        if (changeInValue < 0) {
            System.out.printf("Change in value: -$%.2f %n", (-changeInValue));
        } else {
            System.out.printf("Change in value: $%.2f %n", changeInValue);
        }
        if (parsedArgs.percentageChange) {
            double percentageChange = (changeInValue/sd_closePrice) * 100;
            System.out.printf("Percentage change: %.2f", percentageChange);
            System.out.println("%");
        }

        // Prints Footer
        System.out.println("\n\n\n                          ---**---                              ");
        System.out.println("Developed by Uzair Syed");
        System.out.println("Github: https://github.com/umssyed");
        System.out.println("===============================================================");

    }

    /**
     * This method is not used.
     */
    public void printHelp() {
        System.err.println("Use --help for more information.");
    }

}
