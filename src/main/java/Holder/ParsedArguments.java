package Holder;

public class ParsedArguments {
    public String symbol;
    public String start_date;
    public String end_date;
    public boolean percentageChange;


    public ParsedArguments(String symbol) {
        this.symbol = symbol;
    }

    public ParsedArguments(String symbol, String start_date) {
        this(symbol);
        this.start_date = start_date;
        this.percentageChange = false;
    }

    public ParsedArguments(String symbol, String start_date, String end_date) {
        this(symbol,start_date);
        this.end_date = end_date;
        this.percentageChange = false;
    }

    public ParsedArguments(String symbol, String start_date, String end_date, boolean percentageChange) {
        this(symbol, start_date, end_date);
        this.percentageChange = percentageChange;
    }

    public String getSymbol() {
        return this.symbol;
    }

    public String getStart_date() {
        return this.start_date;
    }

    public String getEnd_date() {
        return this.end_date;
    }
}
