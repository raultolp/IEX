package app;

import javax.print.DocFlavor;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

public class Transaction {

    private String symbol;
    private String type;
    private double transactionFee;  // 10 cents per stock
    private double price; //current price
    private int volume; //number of stocks
    private String date;
    private String time;
    private double profitFromSell;
    private double averagePurchasePrice;


    public Transaction(String symbol, double price, int volume, String type, LocalDateTime transactionTime) {
        this.symbol = symbol;
        this.transactionFee = 0.1;
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

    public String toStringForReport() {
        String sep = "\t";
        String report = type + sep + sep + date + sep + time + sep + volume + sep + sep + price + sep +
                String.format("%.2f", getTransactionFees()) + sep +
                String.format("%.2f", getTransactionAmount()) + sep + sep;
        if (type.equals("buy")) {
            report += "-";
        } else {
            report += String.format("%.2f", profitFromSell);
        }

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
