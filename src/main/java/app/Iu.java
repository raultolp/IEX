package app;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Iu {
    public static void main(String[] args) {
        System.out.println("Ugh");

        System.out.println(loadDataFromIEX());

    }

    public static String loadDataFromIEX() { //HashMap<String, Stock>

        //-----------------------------------------------------------------------
        //Source for reading from url and conversion to JSON:
        //https://stackoverflow.com/questions/4308554/simplest-way-to-read-json-from-a-url-in-java
        // user: user2654569 (Feb 23 '14 at 4:02)

        String out = " ";
        //Reading from URL:
        String sURL = "http://freegeoip.net/json/";
        try {
            URL url = new URL(sURL);
            HttpURLConnection request = (HttpURLConnection) url.openConnection();
            request.connect();

            // Convert to a JSON object to print data
            JsonParser jp = new JsonParser(); //from gson
            JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent())); //from input stream to json
            JsonObject rootobj = root.getAsJsonObject(); // array or object
            String zipcode = rootobj.get("city").getAsString(); //just grab the zipcode

            out = zipcode;

        } catch (IOException e) {
            System.out.println("Connection to IEX failed.");
            throw new RuntimeException(e);
        }

        //-----------------------------------------------------------------------

        return out;
    }



}
