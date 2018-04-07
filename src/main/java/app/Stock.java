package app;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Stock {

    private final String symbol; //aktsia sümbol
    private String companyName;
    private String sector;
    private double dividendYield; // (protsendina) dividendi tootlus (s.o dividendid/aktsia hind)
    private double eps; //Earnings-per-share (kasum aktsia kohta viimase majandusaasta aruande kohaselt)



    private int marketCap; //turukapitalisatsioon miljonites dollarites (näitab ettevõtte suurust)
    // (s.o kõigi noteeritud aktsiate hetkehindade summa)
    private double peRatio;
    private double previousClose; //eelmine sulgemishind
    private double change1Year; //hinnamuutus viimase 1 aasta jooksul
    private double change1Month; //hinnamuutus viimase 1 kuu jooksul
    private double change3Month; //hinnamuutus viimase 3 kuu jooksul
    private double shortRatio; //kui palju aktsiaid on lühikeseks müüdud (protsendi

    private double currentPrice;  //hetke hind
    //private double iexRealtimePrice;  //https://api.iextrading.com/1.0/stock/aapl/book  - kas erineb currentPrice'ist?
    //kui börs suletud, siis väärtus on "null"






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
            companyName=rootobj.getAsJsonObject("quote").get("symbol").getAsString(); //AAPL
            sector=rootobj.getAsJsonObject("quote").get("sector").getAsString(); //"Technology"
            currentPrice=rootobj.getAsJsonObject("quote").get("latestPrice").getAsDouble(); //168.38
            long marketCapAsLong=rootobj.getAsJsonObject("quote").get("marketCap").getAsLong();
            marketCap=(int) (marketCapAsLong/1000000); //miljonites dollarites
            peRatio=rootobj.getAsJsonObject("quote").get("peRatio").getAsDouble();
            previousClose=rootobj.getAsJsonObject("quote").get("previousClose").getAsDouble();

            //TODO:
            //Include bid price, bid size, ask price, ask size and respective calculations -
            // in order to take into account the volume of each buy/sell order
            // (would avoid selling/buying more stocks than included in the respective bid/ask)

            request.disconnect();
            is.close();


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
/*            dividendYield=rootobj2.get("previousClose").getAsDouble();
            eps=rootobj2.get("latestEPS").getAsDouble();*/

/*            private double change1Year; //hinnamuutus viimase 1 aasta jooksul
            private double change1Month; //hinnamuutus viimase 1 kuu jooksul
            private double change3Month; //hinnamuutus viimase 3 kuu jooksul
            private double shortRatio; //kui palju aktsiaid on lühikeseks müüdud (protsendi*/

            request2.disconnect();
            is2.close();

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

        try {
            URL url = new URL(sURL);
            HttpURLConnection request = (HttpURLConnection) url.openConnection();
            request.connect();

            // Convert to a JSON object to print data
            JsonParser jp = new JsonParser(); //from gson
            JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent())); //from input stream to json
            currentPrice= root.getAsDouble();
            //System.out.println("hind "+currentPrice);  //ok

        } catch (IOException e) {
            System.out.println("Connection to IEX failed.");
            throw new RuntimeException(e);
        }
        return currentPrice;
    }


    //-----------------------------------------------

    public String getCompanyName() {
        return companyName;
    }


}