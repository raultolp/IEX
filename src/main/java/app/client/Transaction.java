package app.client;

import com.google.gson.JsonObject;

public class Transaction {

    private String symbol;
    private String type;
    private double transactionFee = 0.1;  // 10 cents per stock
    private double price; //current price
    private int volume; //number of stocks
    private String date;
    private String time;
    private double profitFromSell;
    private double averagePurchasePrice;


    //For initiating transaction from JSON:
    public Transaction(JsonObject transObj) {
        this.symbol = transObj.get("symbol").getAsString();
        this.price = transObj.get("price").getAsDouble();
        this.volume = transObj.get("volume").getAsInt();
        this.type = transObj.get("type").getAsString(); //buy or sell
        this.date = transObj.get("date").getAsString();
        this.time = transObj.get("time").getAsString();
        this.profitFromSell = transObj.get("profitFromSell").getAsDouble();
        this.averagePurchasePrice = transObj.get("averagePurchasePrice").getAsDouble();
    }

    public String toStringForReport() {
        String report = String.format("%-7s%-10s %-10s %6d %10.2f %7.2f %+15.2f %10.2f",
                type, date, time, volume, price, getTransactionFees(),
                type.equals("buy") ? -getTransactionAmount() : getTransactionAmount(),
                type.equals("buy") ? 0 : profitFromSell);

        return report;

    }

    public double getTransactionAmount() {
        if (type.equals("buy")) {
            return (price + transactionFee) * volume; //total price paid when buying stocks
        } else {
            return (price - transactionFee) * volume; //total amount received for selling stocks
        }
    }

    public double getTransactionFees() {
        return volume * transactionFee;
    }


    public double getProfitFromSell() {
        return profitFromSell;
    }
}
