package App;

import Holder.ParsedArguments;
import util.AppUtils;
import FinanceClient.FinanceClient;

import java.io.IOException;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

// IMPLEMENTING PICOCLI
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

// Stock Price
// -s AAPL dd/mm/yyyy

@Command(name="stockcli",
        version="1.0.0",
        header="%n========================== STOCK CLI ==========================",
        mixinStandardHelpOptions = true,
        requiredOptionMarker = '*',
        optionListHeading = "Options for arguments are:%n",
        description = "%nThis is a stock CLI to get real-time information on stock prices.%n" +
                "The CLI enables user to provide one stock ticker to get the latest stock price.%n" +
                "There is an option to see the price or percentage change in the stock price between two dates.%n" +
                "%nSample command 1 - Return the latest stock price of NVDA:" +
                "%nstock-cli -s NVDA" +
                "%nSample command 2 - Return the change in price/percentage between two dates:" +
                "%nstock-cli -s MSFT -sd 2024-02-10 -ed 2024-02-19 %n" +
                "===============================================================%n",
        footerHeading = "%n===============================================================%n",
        footer="Copyright" +
                "Developed by Uzair Syed %n" +
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

    @Override
    public void run() {
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        ParsedArguments parsedArgs;

        // If neither start or end dates are provided, provide yesterday's date stock pricing.
        if (start_date == null && end_date == null) {
            double yesterdayStockPrice = 0;
            start_date = AppUtils.getYesterdaysDate();
            parsedArgs = new ParsedArguments(symbol, start_date);
            try {
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

            // Check if provided dates are valid
            AppUtils.checkDateValidity(start_date, end_date);

            if (!percentageChange) {
                parsedArgs = new ParsedArguments(symbol, start_date, end_date);
            } else {
                parsedArgs = new ParsedArguments(symbol, start_date, end_date, true);
            }

            try {
                AppUtils.compareStockPrice(parsedArgs);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public static void main(String[] args) {
        CommandLine.run(new App(), args);
    }
}

