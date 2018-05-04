package app;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.List;

public class Position {

    //private String posType;  //"long" or "short"
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


    public Position(Transaction transaction, String symbol, double price, int volume) {
        this.symbol = symbol;
        this.price = price;
        this.volume = volume;
        this.averagePrice = price + transaction.getTransactionFee();  //transaction.getTransactionAmount()/volume;
        this.profit = 0.0;
        this.unrealisedProfit = 0.0 - transaction.getTransactionFees();
        this.currentValue = price * volume;
        this.open = true;
        transaction.setAveragePurchasePrice(averagePrice);
        transactions.add(transaction);
    }

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


    public void priceUpdate(double newPrice) {
        price = newPrice;
        unrealisedProfit = (newPrice - averagePrice) * volume;
        currentValue = newPrice * volume;
    }

    public void increasePosition(Transaction transaction, double purchasePrice, double addedVolume) {
        profit -= transaction.getTransactionFees();

        // new weighted average purchase price:
        averagePrice = (volume * averagePrice + transaction.getTransactionAmount()) / (volume + addedVolume);

        volume += addedVolume;
        priceUpdate(purchasePrice);
        transaction.setAveragePurchasePrice(averagePrice);
        transactions.add(transaction);
    }

    public void decreasePosition(Transaction transaction, double salesPrice, double salesVolume) {
        volume -= salesVolume;
        priceUpdate(salesPrice);
        profit += salesVolume * (salesPrice - averagePrice) - transaction.getTransactionFees();

        if (volume == 0) {
            open = false; //position is closed
        }
        transactions.add(transaction);
    }

    public JsonObject covertToJson() {
        JsonObject posObj = new JsonObject(); //kasutaja portf. positsiooni väljad
        posObj.addProperty("open", open);
        posObj.addProperty("symbol", symbol);
        posObj.addProperty("price", price);
        posObj.addProperty("volume", volume);
        posObj.addProperty("averagePrice", averagePrice);
        posObj.addProperty("profit", profit);
        posObj.addProperty("unrealisedProfit", unrealisedProfit);
        posObj.addProperty("currentValue", currentValue);

        //Adding transactions:
        JsonArray transListObj = new JsonArray(); //kasutaja portf. positsiooni tehingute list
        for (Transaction trans : transactions) {
            JsonObject transObj = trans.covertToJson();
            transListObj.add(transObj);
        }
        posObj.add("transactions", transListObj);  //transactions!!!

        return posObj;

        //ALTERNATIIV OLEKS:
        //new Gson().toJson(this) // ei anna küll JsonObjecti, vaid teeb lihtsalt kogu objekti stringiks
    }


    //For printing out position info when viewing User's portfolio:
    public String toStringForPortfolio() {
        String info = String.format("%-5s %8d %10.2f %12.2f %16.2f %14.2f\n",
                        symbol, volume, price, currentValue, unrealisedProfit, profit);
        //NB! Average purchase price ('averagePrice') currently not shown in portfolio.
        //System.out.println(info);  //TESTIMISEKS
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

    public double getPrice() {
        return price;
    }

    //TODO: (PRIORITY 2) - ADD SHORT POSITIONS

}
