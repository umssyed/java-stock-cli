package App;

import Holder.ParsedArguments;
import util.AppUtils;
import FinanceClient.FinanceClient;

import java.io.IOException;

// IMPLEMENTING PICOCLI

// Stock Price
// -s AAPL dd/mm/yyyy

public class App {
    private static String symbol;
    private static String date1;
    private static String date2;
    private static boolean comparePrice;

    public static void main(String[] args) throws IOException {

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
    }
}
