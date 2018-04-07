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

        //Testing historical info for dates:
        Map<String, Double []> aaplHistorical=aapl.getHistoricalPrices("1m");
        Double [] aaplPriceAndVolume=aaplHistorical.get("2018-04-06");
        double aaplPrice=aaplPriceAndVolume[0];
        double aaplVolume=aaplPriceAndVolume[1];
        System.out.println("AAPL price at 2018-04-06: "+aaplPrice+", volume: "+aaplVolume+" (volume in millions)");

        //Testing historical info for minutes (in case of 1-day chart):
        Map<String, Double []> aaplIntraday=aapl.getHistoricalPrices("1d");
        Double [] aaplPriceAndVolume2=aaplIntraday.get("09:32");
        double aaplPrice2=aaplPriceAndVolume2[0];
        double aaplVolume2=aaplPriceAndVolume2[1];
        System.out.println("AAPL price at 09:32: "+aaplPrice2+", volume: "+aaplVolume2+" (volume in thousands)");


        //------------------------------------


    }



}
