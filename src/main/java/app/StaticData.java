package app;

public class StaticData {
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_RESET = "\u001B[0m";

    public static final String[] availableStocks = {"AAPL", "AMZN", "CSCO", "F", "GE", "GM", "GOOG",
            "HPE", "IBM", "INTC", "JNJ", "K", "KO", "MCD", "MSFT", "NFLX", "NKE", "PEP", "PG", "SBUX",
            "TSLA", "TWTR", "V", "WMT"};

    //TAGASISIDE Märdilt:
    //need võiks kuskilt failist lugeda
    //TODO: (PRIORITY 2)-> LOE STOCKID FAILIST

    //ETF-dega list
//    public static final String[] availableStocks = {"AAPL", "AMZN", "CSCO", "F", "GE", "GM", "GOOG",
//            "HPE", "IBM", "INTC", "JNJ", "K", "KO", "MCD", "MSFT", "NFLX", "NKE", "PEP", "PG", "SBUX",
//            "TSLA", "TWTR", "V", "WMT", "SPY", "XLB", "XLE", "XLF", "XLK", "XLP", "XLU", "XLV", "TLT"};


    //ORIGINAL LIST
//    public static final String[] availableStocks = {"AAPL", "AMZN", "AMD", "BA", "BABA", "BAC", "BBY", "BIDU",
//            "C", "CAT", "COST", "CRM", "CSCO", "DE", "F", "FSLR", "GE", "GM", "GME", "GOOG", "GS",
//            "HD", "HLF", "HPE", "HPQ", "HTZ", "IBM", "INTC", "JAZZ", "JCP", "JNJ", "JNPR", "JPM",
//            "K", "KO", "LMT", "LOGI", "MA", "MCD", "MMM", "MS", "MSFT", "NFLX", "NKE", "NTAP",
//            "NTNX", "NVDA", "ORCL", "P", "PEP", "PG", "QCOM", "RHT", "SBUX", "SINA", "SSYS", "STX",
//            "SYMC", "TGT", "TIF", "TRIP", "TSLA", "TWTR", "TXN", "UA", "UAL", "V", "VMW", "VNET",
//            "WDX", "WFC", "WFM", "WHR", "WMT", "X", "XONE", "YELP", "ZG"};

    public static final String mainTitle = "\n+++ BÖRSIMÄNG +++\n\nLoading stock data from web ...\n";

    public static final String[] mainMenu = {"Add user",
            "Delete user",
            "List users",
            "Set active user",
            "Buy stock",
            "Sell stock",
            "View user portfolio",
            "View user transactions report",
            "View available stock list",
            "View stock list base data",
            "View company data",
            "View stock base data",
            "View stock historical data",
            "View stock news",
            "View all portfolios performance",
            "Load data file",
            "Save data file",
            "Quit"};

    public static int getMainMenuSize() {
        return mainMenu.length;
    }
}
