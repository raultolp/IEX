package app;

import java.io.IOException;
import java.util.*;

public class ATestKlass implements Comparator<String> {

    public static void main(String[] args) throws Exception {

        //-------------------------------------------------
/*        //PRINTING OUT COMPANY BASE DATA:
        Company comp = new Company("AAPL");  //XLU
        Company comp2 = new Company("v");  //XLU
        Company comp3 = new Company("TLT");  //XLU
        Company comp4 = new Company("FB");

        System.out.println(comp4);

        String[] availableStocks2 = {
                "SPY", "XLB", "XLE", "XLF", "XLK", "XLP", "XLU", "XLV", "TLT"};  //ETFs
        */

        //-------------------------------------------------
/*        //GETTING COMPANY NEWS (LAST 10 NEWS ITEMS):
        Company fb = new Company("FB");
        ArrayList<String> news = fb.getCompanyNews();
        for (String n : news) {
            System.out.println(n);
        }*/
        //-------------------------------------------------

        //GETTING STOCK FUNDAMENTAL DATA:
/*        Stock stock = new Stock("XLP");
        System.out.println(stock);*/

        //-------------------------------------------------

        //GETTING STOCK CHART DATA:
        //possible periods:  5y, 2y, 1y, ytd, 6m, 3m, 1m, (1d)
        Stock stock = new Stock("FB");
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
        }
        //-------------------------------------------------

       /*  //TESTING PORTFOLIO:
        User pets = new User("Pets", 100000);
        Portfolio port = pets.getPortfolio();

        //TRANSACTIONS REPORT:
        port.buyStock("AAPL", 200);
        //port.sellStock("AAPL", 100);
        port.buyStock("FB", 100);
        port.buyStock("GE", 100);
        //port.sellStock("GE", 10);*//*

        String transactionsReport=port.getTransactionsReport();
        System.out.println(transactionsReport);

        //STOCK PRICES BATCH UPDATE:
        port.updatePrices();
        */


    }

    @Override
    public int compare(String s1, String s2) {
        return -s1.compareTo(s2);
    }

}
