package util;
import Holder.ParsedArguments;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


public class AppUtils {
    // -s AAPL MSFT -% date1 date2
    // -s AAPL MSFT
    // Get Stock Price:
    //      1. -s AAPL: this will get stock price of AAPL on current date
    //      2. -s AAPL <date>: this will get stock price of AAPL on requested <date>
    // Compare Stock Price:
    // The -price flag provides price change
    // The -perc flag provides percentage change
    //      1. -s AAPL -$ <date1> <date2>: this will compare the "price" change between the two dates
    //      2. -s AAPL -% <date1> <date2>: this will compare the "percentage" change between the two dates

    public static ParsedArguments parseArgs(String[] args) {
        ParsedArguments parsedArgs = new ParsedArguments();
        // For arg length of 2: -s <symbol>, <date> should be current date
        if (args.length == 2) {
            // The first arg should always be -s for stock symbol
            if (!args[0].equals("-s")) {
                System.out.println("First input should start with -s for stock symbol.");
                return parsedArgs;
            }
            else {
                // The second argument should be a symbol
                parsedArgs.symbol = args[1];

                // The third argument is not provided. Set date to current date
                String currentDate = getCurrentDate();
                parsedArgs.date1 = currentDate;
            }
        }
        // For arg length of 3: -s <symbol> <date> where the date is provided
        else if (args.length == 3) {
            // The first arg should always be -s for stock symbol
            if (!args[0].equals("-s")) {
                System.out.println("First input should start with -s for stock symbol.");
                return parsedArgs;
            }
            else {
                // The second argument should be a symbol
                parsedArgs.symbol = args[1];

                // The third argument is provided and it is expected to be a date
                String formattedDate = formatDate(args[2]);
                parsedArgs.date1 = formattedDate;
            }
        }
        // For arg length of 5: -s AAPL -$/% <date1> <date2>
        else if (args.length == 5) {
            // The first arg should always be -s for stock symbol
            if (!args[0].equals("-s")) {
                System.out.println("First input should start with -s for stock symbol.");
                return parsedArgs;
            }

            else {
                // The second argument should be a symbol
                parsedArgs.symbol = args[1];

                // The third argument should either be a $ or a %
                if (args[2].equals("-$")) {
                    parsedArgs.comparePrice = true;
                } else if (args[2].equals("-%")) {
                    parsedArgs.comparePrice = false;
                } else {
                    // Return error
                    System.out.println("Third argument must be -$ for price comparison or -% for percentage comparison.");
                    return parsedArgs;
                }

                // Fourth and Fifth arguments must be dates
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                LocalDate date_1;
                LocalDate date_2;
                try {
                    date_1 = LocalDate.parse(args[3], formatter);
                    date_2 = LocalDate.parse(args[4], formatter);
                    if (date_1.isBefore(date_2)) {
                        parsedArgs.date1 = formatDate(args[3]);
                        parsedArgs.date2 = formatDate(args[4]);
                    } else {
                        parsedArgs.date1 = formatDate(args[4]);
                        parsedArgs.date2 = formatDate(args[3]);
                    }
                } catch (DateTimeException e) {
                    System.err.println("Invalid date provided. Use dd-mm-yyy format. Use --help for more information.");
                    return parsedArgs;
                }
            }
        }
        // For any other arg length return error
        else {
            System.err.println("Incorrect arguments provided. --help for more information");
            return parsedArgs;
        }



        return parsedArgs;
    }

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
            formattedDate = getCurrentDate();
        }
        return formattedDate;
    }

    public static String getCurrentDate() {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = currentDate.format(outputFormatter);
        return formattedDate;
    }

    public static String getPrice(String json, String date) {
        String stockPrice = "";

        JsonElement element = JsonParser.parseString(json);
        JsonObject jsonObject = element.getAsJsonObject();

        JsonObject timeSeriesDaily = jsonObject.getAsJsonObject("Time Series (Daily)");

        if (timeSeriesDaily != null && timeSeriesDaily.has(date)) {
            JsonObject dayData = timeSeriesDaily.getAsJsonObject(date);
            String closeValue = dayData.get("4. close").getAsString();
            stockPrice = closeValue;
        }



        //JSONObject jsonObj = new JSONObject(json);
        //JSONObject timeSeriesObj = jsonObj.getJSONObject("Time Series (Daily)");
        //System.out.println("Date Requested: " + date);
        //JSONObject stockDateObj = timeSeriesObj.getJSONObject(date);

        //stockPrice = stockDateObj.getString("4. close");
        System.out.println("Returnging..");
        return stockPrice;
    }

}
