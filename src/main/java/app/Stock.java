package app;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.collections.FXCollections;
import javafx.scene.control.ChoiceBox;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Stock {

    private final String symbol; //aktsia sümbol
    private String companyName;
    private String sector;
    private String industry; // täpsustab sektorit
    private String description; //ettevõtte tegevusalade kirjeldus
    private String CEO;
    private String website;
    private double dividendYield; // (protsendina) dividendi tootlus (s.o dividend aktsia kohta/aktsia hind)
    private double eps; //Earnings-per-share (kasum aktsia kohta viimase majandusaasta aruande kohaselt)


    private int marketCap; //turukapitalisatsioon miljonites dollarites (näitab ettevõtte suurust)
    // (s.o kõigi noteeritud aktsiate hetkehindade summa)
    private double peRatio;
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
        this.symbol = symbol;
        loadDataFromIEX();
    }


    //-----------------------------------------------------------------------

    public void loadDataFromIEX() {

        //-----------------------------------------------------------------------
        //Source for reading from url and conversion to JSON:
        //https://stackoverflow.com/questions/4308554/simplest-way-to-read-json-from-a-url-in-java
        // user: user2654569 (Feb 23 '14 at 4:02)
        //-----------------------------------------------------------------------

        //Reading from URL:
        try {
            String URLa = "https://api.iextrading.com/1.0/stock/"+symbol+"/book";
            URL url = new URL(URLa);
            HttpURLConnection request = (HttpURLConnection) url.openConnection();
            request.connect();

            // Convert to a JSON object to print data
            JsonParser jp = new JsonParser(); //from gson
            InputStream is=(InputStream) request.getContent();
            JsonElement root = jp.parse(new InputStreamReader(is));
            //JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent())); //from input stream to json
            JsonObject rootobj = root.getAsJsonObject(); // array or object
            currentPrice=rootobj.getAsJsonObject("quote").get("latestPrice").getAsDouble(); //168.38
            long marketCapAsLong=rootobj.getAsJsonObject("quote").get("marketCap").getAsLong();
            marketCap=(int) (marketCapAsLong/1000000); //miljonites dollarites
            peRatio=rootobj.getAsJsonObject("quote").get("peRatio").getAsDouble();
            previousClose=rootobj.getAsJsonObject("quote").get("previousClose").getAsDouble();

            //TODO:
            //Maybe include bid price, bid size, ask price, ask size and respective calculations -
            // in order to take into account the volume of each buy/sell order
            // (would avoid selling/buying more stocks than included in the respective bid/ask)

            request.disconnect();
            is.close();

            //------------------------------------------------
            String URLb = "https://api.iextrading.com/1.0/stock/"+symbol+"/stats";
            URL url2 = new URL(URLb);
            HttpURLConnection request2 = (HttpURLConnection) url2.openConnection();
            request2.connect();

            // Convert to a JSON object to print data
            JsonParser jp2 = new JsonParser(); //from gson
            InputStream is2=(InputStream) request2.getContent();
            JsonElement root2 = jp.parse(new InputStreamReader(is2));
            //JsonElement root2 = jp.parse(new InputStreamReader((InputStream) request2.getContent())); //from input stream to json
            JsonObject rootobj2 = root2.getAsJsonObject(); // array or object
            //dividendYield=rootobj2.getAsJsonObject("dividendYield").getAsDouble();
            dividendYield=rootobj2.get("dividendYield").getAsDouble();
            eps=rootobj2.get("latestEPS").getAsDouble();
            change1Year=rootobj2.get("year1ChangePercent").getAsDouble();
            change1Month=rootobj2.get("day30ChangePercent").getAsDouble();
            change3Month=rootobj2.get("month3ChangePercent").getAsDouble();
            shortRatio=rootobj2.get("shortRatio").getAsDouble();

            request2.disconnect();
            is2.close();
            //------------------------------------------------


            String URLc = "https://api.iextrading.com/1.0/stock/"+symbol+"/company";
            URL url3 = new URL(URLc);
            HttpURLConnection request3 = (HttpURLConnection) url3.openConnection();
            request3.connect();

            // Convert to a JSON object to print data
            JsonParser jp3 = new JsonParser(); //from gson
            InputStream is3=(InputStream) request3.getContent();
            JsonElement root3 = jp.parse(new InputStreamReader(is3));
            JsonObject rootobj3 = root3.getAsJsonObject(); // array or object
            companyName=rootobj3.get("companyName").getAsString(); //AAPL
            sector=rootobj3.get("sector").getAsString(); //"Technology"
            industry=rootobj3.get("industry").getAsString(); // "Computer Hardware"
            description=rootobj3.get("description").getAsString();
            CEO=rootobj3.get("CEO").getAsString();
            website=rootobj3.get("website").getAsString();


            request3.disconnect();
            is3.close();
            //------------------------------------------------



        } catch (IOException e) {
            System.out.println("Connection to IEX failed.");
            throw new RuntimeException(e);
        }

    }


    //-----------------------------------------------

    public double getLatestPrice(){ //kiiresti viimase hinna leidmiseks
        //kuna sellel lehel on väga lihtne json objekt, peaks selle
        //kasutamine kiirendama nt portfelli kõigi hindade uuednamist
        String sURL = "https://api.iextrading.com/1.0/stock/"+symbol+"/price";
        double latestPrice;

        try {
            URL url = new URL(sURL);
            HttpURLConnection request = (HttpURLConnection) url.openConnection();
            request.connect();

            // Convert to a JSON object to print data
            JsonParser jp = new JsonParser(); //from gson
            InputStream is=(InputStream) request.getContent();
            JsonElement root = jp.parse(new InputStreamReader(is));  //from input stream to json
            latestPrice= root.getAsDouble();

            request.disconnect();
            is.close();

        } catch (IOException e) {
            System.out.println("Connection to IEX failed.");
            throw new RuntimeException(e);
        }
        return latestPrice;
    }


    //-----------------------------------------------

    // DATA FOR DRAWING CHART (includes price and volume data):
    public Map<String, Double []> getHistoricalPrices(String period){
        //possible periods:  5y, 2y, 1y, ytd, 6m, 3m, 1m, (1d)

        String sURL = "https://api.iextrading.com/1.0/stock/"+symbol+"/chart/"+period;

        Map<String, Double []> historical=new HashMap<>(); //String - date; Double[]- close price, volume in millions

        try {
            URL url = new URL(sURL);
            HttpURLConnection request = (HttpURLConnection) url.openConnection();
            request.connect();

            // Convert to a JSON object to print data
            JsonParser jp = new JsonParser(); //from gson
            InputStream is=(InputStream) request.getContent();
            JsonElement root = jp.parse(new InputStreamReader(is));  //from input stream to json
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

            request.disconnect();
            is.close();

        } catch (IOException e) {
            System.out.println("Connection to IEX failed.");
            throw new RuntimeException(e);
        }
        return historical;
    }

    //TODO:
    //Also possible to request data points with certain interval by using param. chartInterval in url

    //TODO:
    //If deemed necessary: add volatility calculation

    //TODO:
    //It is possible to add also news:
    // https://api.iextrading.com/1.0/stock/aapl/news

    //TODO:
    //Possible to do batch requests, e.g:
    //https://api.iextrading.com/1.0/stock/aapl/batch?types=quote,news,chart&range=1m&last=10

    //-----------------------------------------------


    public String getSymbol() {
        return symbol;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getSector() {
        return sector;
    }

    public String getIndustry() {
        return industry;
    }

    public String getDescription() {
        return description;
    }

    public String getCEO() {
        return CEO;
    }

    public String getWebsite() {
        return website;
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

    public void setCurrentPrice(double currentPrice) {
        this.currentPrice = currentPrice;
    }

    @Override
    public String toString() {
        return "Stock symbol: " + symbol + '\n' +
                "CompanyName: " + companyName + '\n' +
                "Sector: " + sector + '\n' +
                "Industry: " + industry + '\n' +
                "Description: " + description + '\n' +
                "CEO: " + CEO + '\n' +
                "Website: " + website + '\n' +
                "Dividend yield: " + dividendYield + '\n' +
                "EPS: " + eps + '\n' +
                "Market cap: " + marketCap +'\n'+
                "PeRatio: " + peRatio +'\n'+
                "Previous close: " + previousClose +'\n'+
                "Change in the previous month: " + change1Month +'\n'+
                "Change in 3 months: " + change3Month +'\n'+
                "Change in 1 yr: " + change1Year +'\n'+
                "Short ratio: " + shortRatio +'\n'+
                "Current price: " + currentPrice +'\n';
    }
}
