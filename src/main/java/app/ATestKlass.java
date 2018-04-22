package app;

import java.io.IOException;
import java.util.ArrayList;

public class ATestKlass {

    public static void main(String[] args) throws Exception {

        //-------------------------------------------------
        //PRINTING OUT COMPANY BASE DATA:
        Company comp = new Company("AAPL");  //XLU
        System.out.println(comp);

        String[] availableStocks2 ={
                "SPY", "XLB", "XLE", "XLF", "XLK", "XLP", "XLU", "XLV", "TLT"};

        //-------------------------------------------------
        //GETTING COMPANY NEWS (LAST 10 NEWS ITEMS):
      Company fb = new Company("FB");
        ArrayList<String> news=fb.getCompanyNews();
        for (String n: news) {
            System.out.println(n);
        }
        //-------------------------------------------------

        //GETTING STOCK FUNDAMENTAL DATA:
        Stock stock = new Stock("XLP");
        System.out.println(stock);

    }



}
