package app;

import java.io.IOException;
import java.util.*;
//import static app.Iu.masterPortfolio;

public class ATestKlass implements Comparator<String> {

    public static void main(String[] args) throws Exception {
        Iu handler = new Iu();

        Portfolio masterPortfolio = handler.getMasterPortfolio();

/*        //TESTING BATCH CREATION OF POTRFOLIO FOR ADMIN (WITH BATCH DOWNLOAD DATA FOR ALL STOCKS):
        String [] availableSt = {"AAPL", "AMZN", "CSCO", "F", "GE", "GM", "GOOG",
                "HPE", "IBM", "INTC", "JNJ", "K", "KO", "MCD", "MSFT", "NFLX", "NKE", "PEP", "PG", "SBUX",
                "TSLA", "TWTR", "V", "WMT", "SPY"};
        Portfolio masterPortfolio=new Portfolio(availableSt, 100000);
        Map<String, Stock> portfolioStocks=masterPortfolio.getPortfolioStocks();
        //System.out.println(portfolioStocks.get("WMT"));  //IT REALLY WORKS!!! :)
        //System.out.println(portfolioStocks.get("SPY"));*/

        //-------------------------------------------------
/*        //PRINTING OUT COMPANY BASE DATA:
        Company comp = new Company("XLV");  //XLU
        Company comp2 = new Company("T");  //XLU
        Company comp3 = new Company("AT");  //XLU
        //Company comp4 = new Company("FB");

        System.out.println(comp);

        String[] availableStocks2 = {
                "SPY", "XLB", "XLE", "XLF", "XLK", "XLP", "XLU", "XLV", "TLT"};  //ETFs*/

        //-------------------------------------------------
/*        //GETTING COMPANY NEWS (LAST 10 NEWS ITEMS):
        Company fb = new Company("AAPL");
        ArrayList<String> news = fb.getCompanyNews();
        for (String n : news) {
            System.out.println(n);
        }*/
        //-------------------------------------------------

        //GETTING STOCK FUNDAMENTAL DATA:
        Stock stock = masterPortfolio.getStock("AAPL");
        System.out.println(stock);
/*        Thread.sleep(5000);
        masterPortfolio.updatePrices();
        System.out.println(stock);*/

        //-------------------------------------------------

/*        //GETTING STOCK CHART DATA:
        //possible periods:  5y, 2y, 1y, ytd, 6m, 3m, 1m, (1d)
        Stock stock = masterPortfolio.getStock("FB");
        String period = "1d";  // "1m"
        Map<String, Double[]> historicalPrices = stock.getHistoricalPrices(period);
        //String - date; Double[]- close price, volume in millions

        //Panen võtmed (kuupäevad) listi, sorteerin listi ja prindin väärtused koos kuupäevadega:
        ArrayList<String> dates = new ArrayList();
        for (String date : historicalPrices.keySet()) {
            dates.add(date);
        }

        Collections.sort(dates);  //vanemast uuemani

        for (int i = 0; i < 20; i++) {
            Double[] priceData = historicalPrices.get(dates.get(i));
            String volumeSize = period.equals("1d") ? ", volume (in thousands): " : ", volume (in millions): ";
            System.out.println(dates.get(i) + " - price: " + priceData[0] + volumeSize + priceData[1]);
        }*/
        //-------------------------------------------------

       /*  //TESTING PORTFOLIO:
        User pets = new User("Pets", 100000);
        Portfolio port = pets.getPortfolio();

        //TRANSACTIONS REPORT:
        port.buyStock("AAPL", 200);
        //port.sellStock("AAPL", 100);
        port.buyStock("GE", 100);
        //port.sellStock("GE", 10);*//*

        String transactionsReport=port.getTransactionsReport();
        System.out.println(transactionsReport);

        //STOCK PRICES BATCH UPDATE:
        port.updatePrices();
        */

        //-------------------------------------------------



/*        //TESTING BATCH UPDATE OF PRICES AT REGULAR TIME INTERVALS:
        for (int i = 0; i < 4; i++) {

            if (i>0) {
                masterPortfolio.updatePrices();
            }
            for (String s : availableSt) {
                System.out.print(s+"\t");
            }
            System.out.println("");

            for (int j = 0; j < availableSt.length; j++) {
                String symb=availableSt[j];
                System.out.print(portfolioStocks.get(symb).getCurrentPrice()+"\t");
            }

            System.out.println("\n");
            int timeToSleep=7000;
            System.out.print("Now Sleeping for " + timeToSleep/1000 +" seconds... ");
            Thread.sleep(timeToSleep);  // in seconds
            System.out.println("Woke up!");

        }*/





/*        {"AAPL", "AMZN", "CSCO", "F", "GE", "GM", "GOOG",
                "HPE", "IBM", "INTC", "JNJ", "K", "KO", "MCD", "MSFT", "NFLX", "NKE", "PEP", "PG", "SBUX",
                "TSLA", "TWTR", "V", "WMT"};*/

/*        Set<String> h = new HashSet<>(Arrays.asList("a", "b", "c"));
        String [] h1= {"a", "b", "c"};
        String joined = String.join(" and ", h1); // "foo and bar and baz"
        System.out.println(joined);*/
    }


    @Override
    public int compare(String s1, String s2) {
        return -s1.compareTo(s2);
    }

}
