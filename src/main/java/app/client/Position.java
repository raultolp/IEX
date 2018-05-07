package app.client;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class Position {

    private boolean open; //true if open (still stocks in portfolio), false if closed (all stocks already sold)
    private String symbol;
    private double price; //current price
    private int volume; //number of stocks
    private double averagePrice; ////average purchase price (includes transaction fees) -
    //necessary also in order to take into account that
    // stocks can be bought with different prices at different times.
    private double profit; ////realised profit/loss (i.e.profit/loss from stocks sold)
    private double unrealisedProfit; // profit/loss that would be gained if stocks held were sold
    // at current market price (does not include transaction fee)
    private double currentValue;  // volume*current price, i.e. current value of stock in portfolio
    private List<Transaction> transactions = new ArrayList<>();


    //For initiating position from JSON:
    public Position(JsonObject posObj) {
        this.symbol = posObj.get("symbol").getAsString();
        this.open = posObj.get("open").getAsBoolean();
        this.price = posObj.get("price").getAsDouble();
        this.volume = posObj.get("volume").getAsInt();
        this.averagePrice = posObj.get("averagePrice").getAsDouble();
        this.profit = posObj.get("profit").getAsDouble();
        this.unrealisedProfit = posObj.get("unrealisedProfit").getAsDouble();
        this.currentValue = posObj.get("currentValue").getAsDouble();

        //Adding transactions related to the position:
        JsonArray transListObj = posObj.get("transactions").getAsJsonArray();
        for (int i = 0; i < transListObj.size(); i++) {
            JsonObject trans = transListObj.get(i).getAsJsonObject();
            Transaction transaction = new Transaction(trans);
            transactions.add(transaction);
        }
    }


    //For printing out position info when viewing User's portfolio:
    public String toStringForPortfolio() {
        String info = String.format("%-5s %8d %10.2f %12.2f %16.2f %14.2f\n",
                symbol, volume, price, currentValue, unrealisedProfit, profit);
        //NB! Average purchase price ('averagePrice') currently not shown in portfolio.

        return info;
    }

    public int getVolume() {
        return volume;
    }

    public double getProfit() {
        return profit;
    }

    public double getUnrealisedProfit() {
        return unrealisedProfit;
    }

    public double getCurrentValue() {
        return currentValue;
    }

    public double getAveragePrice() {
        return averagePrice;
    }

    public String getSymbol() {
        return symbol;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public boolean isOpen() {
        return open;
    }

}
