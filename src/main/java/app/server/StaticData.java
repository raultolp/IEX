/**
+++ IEX Stock Exchange Game for Beginner Level Traders - Version 1.0 +++
(C) 2018 Renata Siimon, Helena Rebane, Raul Tölp. All rights reserved.
**/

package app.server;

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

    public static final String mainTitle = "+++ IEX Stock Exchange Game for Beginner Level Traders - Version 1.0 +++";
    public static final String subTitle = "(C) 2018 Renata Siimon, Helena Rebane, Raul Tölp. All rights reserved.\n\n";


    public static final String[] mainMenu = {
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
            "View game Top List",
            "Delete user",
            "Quit",
            "Accept clients' connections (ADMIN)",
            "Stop accepting clients' connections (ADMIN)",
            "Add user (ADMIN)",
            "List users (ADMIN)",
            "Set active user (ADMIN)",
            "Load data file (ADMIN)",
            "Save data file (ADMIN)",
    };

    public static int getMainMenuSize() {
        return mainMenu.length;
    }
}
