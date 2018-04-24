package app;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Stock extends IEXdata{

    private final String symbol; //aktsia sümbol

    private double dividendYield; // (protsendina) dividendi tootlus (s.o dividend aktsia kohta/aktsia hind)
    private double eps; //Earnings-per-share (kasum aktsia kohta viimase majandusaasta aruande kohaselt)
    private double peRatio;
    private int marketCap; //turukapitalisatsioon miljonites dollarites (näitab ettevõtte suurust)
                            // (s.o kõigi noteeritud aktsiate hetkehindade summa)
    private double previousClose; //eelmine sulgemishind
    private double change1Year; //hinnamuutus viimase 1 aasta jooksul (protsendina)
    private double change1Month; //hinnamuutus viimase 1 kuu jooksul (protsendina)
    private double change3Month; //hinnamuutus viimase 3 kuu jooksul (protsendina)
    private double shortRatio; //kui palju aktsiaid on lühikeseks müüdud (protsendina)

    private double currentPrice;  //hetke hind
    //private double iexRealtimePrice;  //https://api.iextrading.com/1.0/stock/aapl/book  - kas erineb currentPrice'ist?
    //kui börs suletud, siis väärtus on "null"

    //-----------------------------------------------------------------------

    public Stock(String symbol) {
        this.symbol = symbol.toUpperCase();
        loadDataFromIEX();
    }

    //for creating stocks based on batch download (for portfolios loaded from file):
    public Stock(String symbol, double dividendYield, double eps, double peRatio, int marketCap,
                 double previousClose, double change1Year, double change1Month, double change3Month,
                 double shortRatio, double currentPrice){
        this.symbol=symbol;
        this.dividendYield=dividendYield;
        this.eps=eps;
        this.peRatio=peRatio;
        this.marketCap=marketCap;
        this.previousClose=previousClose;
        this.change1Year=change1Year;
        this.change1Month=change1Month;
        this.change3Month=change3Month;
        this.shortRatio=shortRatio;
        this.currentPrice=currentPrice;

    }




    public void loadDataFromIEX() {

        try {
            String URL="https://api.iextrading.com/1.0/stock/"+symbol+"/batch?types=quote,stats"; //,news,chart&range=1m&last=10";
            JsonElement root = IEXdata.downloadData(URL);  // array or object
            JsonObject rootobj = root.getAsJsonObject();

            currentPrice=rootobj.getAsJsonObject("quote").get("latestPrice").getAsDouble(); //168.38
            previousClose=rootobj.getAsJsonObject("quote").get("previousClose").getAsDouble();
            long marketCapAsLong=rootobj.getAsJsonObject("quote").get("marketCap").getAsLong();
            marketCap=(int) (marketCapAsLong/1000000); //miljonites dollarites
            dividendYield = rootobj.getAsJsonObject("stats").get("dividendYield").getAsDouble();

            eps = rootobj.getAsJsonObject("stats").get("latestEPS").getAsDouble();
            change1Year = rootobj.getAsJsonObject("stats").get("year1ChangePercent").getAsDouble();
            change1Month = rootobj.getAsJsonObject("stats").get("day30ChangePercent").getAsDouble();
            change3Month = rootobj.getAsJsonObject("stats").get("month3ChangePercent").getAsDouble();
            shortRatio = rootobj.getAsJsonObject("stats").get("shortRatio").getAsDouble();

            //For ETFs, PE ratio is not provided (is null):
            if (!rootobj.getAsJsonObject("quote").get("peRatio").isJsonNull()) {
                peRatio=rootobj.getAsJsonObject("quote").get("peRatio").getAsDouble();
            }
            else {
                peRatio=0.0;
            }


        } catch(IOException e) {
            System.out.println("Connection to IEX failed. Please try again.");
        }
    }


    //-----------------------------------------------
    //Meetod kiiresti viimase hinna leidmiseks:
    public double getLatestPrice(){
        String sURL = "https://api.iextrading.com/1.0/stock/"+symbol+"/price";
        double latestPrice;

        try {
            JsonElement root = IEXdata.downloadData(sURL);  // array or object
            latestPrice= root.getAsDouble();

        } catch (IOException e) {
            System.out.println("Connection to IEX failed.");
            return currentPrice; //tagastab varasema hinna
        }
        return latestPrice;
    }


    //-----------------------------------------------

    // DATA FOR DRAWING CHART (includes price and volume data):
    public Map<String, Double []> getHistoricalPrices(String period){
        //possible periods:  5y, 2y, 1y, ytd, 6m, 3m, 1m, (1d)

        String sURL = "https://api.iextrading.com/1.0/stock/"+symbol+"/chart/"+period;
        Map<String, Double []> historical=new HashMap<>();
        //String - date; Double[]- close price, volume in millions (or thousands for one-day chart)

        try {
            JsonElement root = IEXdata.downloadData(sURL);  // array or object
            JsonArray rootArray = root.getAsJsonArray(); // here, it is JsonArray instead...

            for (JsonElement jsonElement : rootArray) {
                JsonObject timeObject=jsonElement.getAsJsonObject();
                double price, volume;
                String timeDataPoint;

                if (period.equals("1d")){
                    timeDataPoint=timeObject.get("minute").getAsString();
                    price=timeObject.get("average").getAsDouble();
                    long volumeAsLong=timeObject.get("volume").getAsLong();
                    volume= (int)(volumeAsLong/1000); //tuhandetes dollarites - NB!
                    //Note: in one day chart, the number of data points is ca 390.
                }
                else{
                    timeDataPoint=timeObject.get("date").getAsString();
                    price=timeObject.get("close").getAsDouble();
                    long volumeAsLong=timeObject.get("volume").getAsLong();
                    volume= (int)(volumeAsLong/1000000); //miljonites dollarites - NB!
                }

                Double [] priceAndVolume= {price, volume};
                historical.put(timeDataPoint, priceAndVolume);
            }

        } catch (IOException e) {
            System.out.println("Connection to IEX failed.");
            throw new RuntimeException(e);
        }
        return historical;
    }

    //TODO: (PRIORITY 1) - create new stocks (in IU) only if the are not included in the Available Stocks list -
    //otherwise take them from that list. (Reason: data has to be downloaded from web each time
    //a new stock is created).

    //TODO: (PRIORITY 3) - Maybe include bid price, bid size, ask price, ask size
    // from https://api.iextrading.com/1.0/stock/"+symbol+"/book"
    // and respective calculations - in order to take into account the volume of each buy/sell order
    // (would avoid selling/buying more stocks than included in the respective bid/ask)

    //TODO: (PRIORITY 2)- When drawing up charts, it's possible to request data points with certain interval
    // by using param. chartInterval in url (could be done if deemed necessary).

    //TODO: (PRIORITY 3) - If deemed necessary: add volatility calculation

    //TODO: (PRIORITY 2) - DONE! - It is possible to add also news: https://api.iextrading.com/1.0/stock/aapl/news

    //TODO: (PRIORITY 1) - POOLELI - Possible to do batch requests, e.g:
    //https://api.iextrading.com/1.0/stock/aapl/batch?types=quote,news,chart&range=1m&last=10

    //-----------------------------------------------


    public void setCurrentPrice(double currentPrice) {
        this.currentPrice = currentPrice;
    }

    public String getSymbol() {
        return symbol;
    }

    public double getDividendYield() {
        return dividendYield;
    }

    public double getEps() {
        return eps;
    }

    public int getMarketCap() {
        return marketCap;
    }

    public double getPeRatio() {
        return peRatio;
    }

    public double getPreviousClose() {
        return previousClose;
    }

    public double getChange1Year() {
        return change1Year;
    }

    public double getChange1Month() {
        return change1Month;
    }

    public double getChange3Month() {
        return change3Month;
    }

    public double getShortRatio() {
        return shortRatio;
    }

    public double getCurrentPrice() {
        return currentPrice;
    }

    @Override
    public String toString() {
        return "INFORMATION ON " + symbol + " STOCK:\n" +
                "Market cap: " + marketCap + " million USD\n" +
                "Dividend yield: " + String.format("%.2f", dividendYield) + '\n' +
                "EPS: " + eps + '\n' +
                "P/E Ratio: " + peRatio + '\n' +
                "Previous close: " + previousClose + '\n' +
                "Change in the previous month: " + String.format("%.2f", change1Month) + '\n' +
                "Change in 3 months: " + String.format("%.2f", change3Month) + '\n' +
                "Change in 1 yr: " + String.format("%.2f", change1Year) + '\n' +
                "Short ratio: " + String.format("%.2f", shortRatio) + '\n' +
                "Current price: " + currentPrice + '\n';
    }
    //TODO: (PRIORITY 1) - STOCK INFO SHOULD NOT BE SAVED TO FILE (IT SHOULD BE ALWAYS
    //DOWNLOADED FROM WEB EACH TIME THE PROGRAMME IS LAUNCHED. CHECK IF THIS IS SO.
    //CHECK ALSO IF STOCK INFO IS DOWNLOADED FOR ALL AVAILABLESTOCKS SOMEWHERE IN
    // THE BEGINNING OF PROGRAMME.

}
