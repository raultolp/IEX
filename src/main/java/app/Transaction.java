package app;

import com.google.gson.JsonObject;

import java.time.LocalDateTime;

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


    public Transaction(String symbol, double price, int volume, String type, LocalDateTime transactionTime) {
        this.symbol = symbol;
        this.price = price;
        this.volume = volume;
        this.type = type; //buy or sell
        this.date = transactionTime.getDayOfMonth() + "-" + transactionTime.getMonthValue() + "-" + transactionTime.getYear(); //String.valueOf(...)
        this.time = transactionTime.getHour() + ":" + transactionTime.getMinute() + ":" + transactionTime.getSecond();
        if (type.equals("buy")) {
            this.profitFromSell = 0.0;
        } else {
            this.profitFromSell = volume * (price - averagePurchasePrice - 2 * transactionFee);
        }
    }

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
//        String sep = "\t";
        String report = String.format("%-7s%-10s %-10s %6d %10.2f %7.2f %+15.2f %10.2f",
                type, date, time, volume, price, getTransactionFees(),
                type.equals("buy") ? -getTransactionAmount() : getTransactionAmount(),
                type.equals("buy") ? 0 : profitFromSell);

        // VANA -- ootab delete

//        String report = type + sep + sep + date + sep + time + sep + volume + sep + sep + price + sep +
//                "-" + String.format("%.2f", getTransactionFees()) + sep;
//        if (type.equals("buy")) {
//            report += "-" + String.format("%.2f", getTransactionAmount()) + sep + sep + "-";
//        } else {
//            report += "+" + String.format("%.2f", getTransactionAmount()) + sep + String.format("%.2f", profitFromSell);
//        }

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

    public void reportTransaction() { //inform how many stocks were bought/sold at what price
        String verb = type.equals("buy") ? "bought" : "sold";
        System.out.print(volume + " stocks of " + symbol + " " + verb + " @" + price + " USD (total value ");
        System.out.printf("%.2f", getTransactionAmount());
        System.out.println(" USD).");
    }

    //For sending data from Server to Client, in order to initiate Client's portfolio
    // (possibly also for saving/loading):
    public JsonObject covertToJson() {
        JsonObject transObj = new JsonObject();  //kasutaja portf. positsiooni tehing
        transObj.addProperty("symbol", symbol);
        transObj.addProperty("type", type);
        transObj.addProperty("price", price);
        transObj.addProperty("volume", volume);
        transObj.addProperty("date", date);
        transObj.addProperty("time", time);
        transObj.addProperty("profitFromSell", profitFromSell);
        transObj.addProperty("averagePurchasePrice", averagePurchasePrice);
        return transObj;
    }

    public double getTransactionFee() {
        return transactionFee;
    }

    public void setAveragePurchasePrice(double avgPurchasePrice) {
        averagePurchasePrice = avgPurchasePrice;
    }

    public double getProfitFromSell() {
        return profitFromSell;
    }
}
