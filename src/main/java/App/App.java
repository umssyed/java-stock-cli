/**
 * This is a Command Line Interface application which generates up-to-date stock information
 * The application utilized eodhd.com finance API.
 * Check https://eodhd.com/financial-apis/ for the finance api documentation
 *
 * Author: Uzair Syed
 * Date Published: 2024-03-22
 * Version: 1.0.0
 */

package App;
import Holder.ParsedArguments;
import util.AppUtils;
import java.io.IOException;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;


/**
 * This is the main running App for stock-cli
 * The @Command uses picocli.CommandLine.Command package to generate
 * description and other useful information in the cli
 */
@Command(name="stockcli",
        version="1.0.0",
        header="%n                       --- STOCK CLI ---                        ",
        mixinStandardHelpOptions = true,
        requiredOptionMarker = '*',
        optionListHeading = "Options for arguments are:%n",
        description = "%nThis is a stock CLI to get real-time information on stock prices.%n" +
                "The CLI enables user to provide one stock ticker to get the latest stock price.%n" +
                "There is an option to see the price or percentage change in the stock price between two dates.%n" +
                "%nSample command 1 - Return the latest stock price of NVDA:" +
                "%nstock-cli -s NVDA%n" +
                "%nSample command 2 - Return the change in price/percentage between two dates:" +
                "%nstock-cli -s MSFT -sd 2024-02-10 -ed 2024-02-19 %n" +
                "===============================================================%n",
        footerHeading = "%n                          ---**---                              %n",
        footer= "Developed by Uzair Syed %n" +
                "Github: https://github.com/umssyed %n" +
                "===============================================================")
public class App implements Runnable {
    @Option(names={"-s"}, description = "Stock symbol/ticker (Required)", required = true)
    private static String symbol;

    @Option(names={"-sd"}, description = "Stock price start date. Format: yyyy-mm-dd")
    private static String start_date;

    @Option(names={"-ed"}, description = "Stock price end date. Format: yyyy-mm-dd")
    private static String end_date;

    @Option(names={"-p"}, description = "Use if you want to see a percentage change")
    private static boolean percentageChange;
    @Option(names={"-h", "--help"}, description = "Show help message", usageHelp = true)
    private static boolean help;

    /**
     * The run method uses the Parsed Arguments AppUtils for logic related to
     * calling the Finance API.
     */
    @Override
    public void run() {
        ParsedArguments parsedArgs;

        // If neither start or end dates are provided, provide yesterday's date stock pricing.
        if (start_date == null && end_date == null) {
            start_date = AppUtils.getYesterdaysDate(); // Get the yesterday's date for stock pricing
            parsedArgs = new ParsedArguments(symbol, start_date); // Set the ParsedArguments object
            try {
                // Call AppUtils to generate the stock price for a single stock when
                // user does not provide a start date and end date
                AppUtils.getSingleStockPrice(parsedArgs);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        // If both start and end dates are provided, then we perform a comparison
        else {
            // If start date is provided but no end date. Make end date yesterday's date
            if (start_date != null && end_date == null) {
                end_date = AppUtils.getYesterdaysDate();
            }
            // Check if provided dates are valid and in the correct format. Use AppUtils.
            AppUtils.checkDateValidity(start_date, end_date);
            // If percentage change is requested by user, set the Parsed Arguments object variable to true
            if (!percentageChange) {
                parsedArgs = new ParsedArguments(symbol, start_date, end_date);
            } else {
                parsedArgs = new ParsedArguments(symbol, start_date, end_date, true);
            }
            try {
                // Call AppUtils to generate stock information
                AppUtils.compareStockPrice(parsedArgs);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Main method for App
     * @param args
     */
    public static void main(String[] args) {
        CommandLine.run(new App(), args);
    }
}

