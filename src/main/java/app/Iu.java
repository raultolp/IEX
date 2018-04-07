package app;

import java.util.Map;

public class Iu {
    public static void main(String[] args) {

        //------------------------------------
        //TESTING STOCK:
        String symbol="AAPL";

        Stock aapl=new Stock("AAPL");

        System.out.println(aapl.getCompanyName());

        double aaplLatestPrice=aapl.getLatestPrice();
        System.out.println("AAPL current price: " +aaplLatestPrice);

        Map<String, Double []> aaplHistorical=aapl.getHistoricalPrices("1m");
        Double [] aaplPriceAndVolume=aaplHistorical.get("2018-03-27");
        double aaplPrice=aaplPriceAndVolume[0];
        double aaplVolume=aaplPriceAndVolume[1];
        System.out.println("AAPL price at 2018-04-06: "+aaplPrice+", volume: "+aaplVolume);
        //------------------------------------


    }


}
