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


        String symbol="AAPL";

        Stock aapl=new Stock("AAPL");

        System.out.println(aapl.getCompanyName());

        aapl.getLatestPrice();


    }






}
