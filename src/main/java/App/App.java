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
        header="Sample Command",
        mixinStandardHelpOptions = true,
        requiredOptionMarker = '*',
        optionListHeading = "%nOptions are%n",
        description = "This is a stock CLI to get real-time information on stock prices.",
        footerHeading = "%nCopyright",
        footer="%nDeveloped by Uzair Syed")
public class App implements Runnable {
    @Option(names={"-s"}, description = "Stock Symbol", required = true)
    private static String symbol;

    @Option(names={"-sd"}, description = "Start Date Format:yyyy-mm-dd", paramLabel = "Start Date")
    private static String start_date;

    @Option(names={"-ed"}, description = "End Date Format:yyyy-mm-dd", paramLabel = "End Date")
    private static String end_date;

    @Option(names={"-%"}, description = "Percentage change")
    private static boolean percentageChange;
    @Option(names={"-h", "--help"}, description = "Show Help", usageHelp = true)
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

        //System.out.println("Your stock symbol: " + symbol);
        //System.out.println("Start Date: " + start_date);
        //System.out.println("End Date: " + end_date);
        //System.out.println("Percentage Change: " + percentageChange);

    }

    public static void main(String[] args) {
        CommandLine.run(new App(), args);
    }
}

/**
 if (args.length >= 2) {
 ParsedArguments parsedArguments = AppUtils.parseArgs(args);
 symbol = parsedArguments.symbol;
 date1 = parsedArguments.date1;
 date2 = parsedArguments.date2;
 comparePrice = parsedArguments.comparePrice;
 //System.out.println("Stock symbol: " + symbol + ", Date1: " + date1 + ", Date 2: " + date2 + ", Compare Price: " + comparePrice);

 FinanceClient stockData = new FinanceClient(symbol);
 String respJSON = stockData.getResponseJSON();

 String stockPrice = AppUtils.getPrice(respJSON, date1);
 System.out.println("The price of " + symbol + " on date " + date1 + ": " + stockPrice);

 } else {
 System.out.println("!You need to provide arguments. -help or -h for more information");
 }
 * */

