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
        final String[] mainMenu = {" 1 - Add user: add <userName>\n",
                " 2 - List users\n",
                " 3 - Set active user: set <userName>\n",
                " 4 - Buy stock: buy <stock>\n",
                " 5 - Sell stock: sell <stock>\n",
                " 6 - View user portfolio\n",
                " 7 - View stock data base data: base <stock>\n",
                " 8 - View stock historical data graph: historical <stock>\n",
                " 9 - View all portfolios progress graph\n",
                "10 - Refresh data from web\n",
                "11 - Save data\n" };

//        String symbol="AAPL";
//        Stock aapl=new Stock("AAPL");
//        System.out.println(aapl.getCompanyName());
//        aapl.getLatestPrice();

    }
}
