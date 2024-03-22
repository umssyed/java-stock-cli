package Holder;

/**
 * This class holds the passed arguments from the user from
 * the command line interface
 */
public class ParsedArguments {
    public String symbol;
    public String start_date;
    public String end_date;
    public boolean percentageChange;

    /**
     * Constructor:
     * Set the symbol
     * @param symbol Stock symbol
     */
    public ParsedArguments(String symbol) {
        this.symbol = symbol;
    }

    /**
     * Constructor:
     * Set the symbol and start date
     * @param symbol Stock symbol
     * @param start_date Start date
     */
    public ParsedArguments(String symbol, String start_date) {
        this(symbol);
        this.start_date = start_date;
        this.percentageChange = false;
    }

    /**
     * Constructor:
     * Set the symbol, start date and end date
     * @param symbol Stock symbol
     * @param start_date Start date
     * @param end_date End date
     */
    public ParsedArguments(String symbol, String start_date, String end_date) {
        this(symbol,start_date);
        this.end_date = end_date;
        this.percentageChange = false;
    }

    /**
     * Constructor:
     * Set the symbol, start date, end date and percentage change
     * @param symbol Stock symbol
     * @param start_date Start date
     * @param end_date End date
     * @param percentageChange Percentage Change
     */
    public ParsedArguments(String symbol, String start_date, String end_date, boolean percentageChange) {
        this(symbol, start_date, end_date);
        this.percentageChange = percentageChange;
    }

    /**
     * Getter - gets symbol
     * @return Symbol
     */
    public String getSymbol() {
        return this.symbol;
    }

    /**
     * Getter - gets start date
     * @return Start Date
     */
    public String getStart_date() {
        return this.start_date;
    }

    /**
     * Getter - gets end date
     * @return End Date
     */
    public String getEnd_date() {
        return this.end_date;
    }
}
