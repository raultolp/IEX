package app;

import javax.print.DocFlavor;
import java.time.LocalDateTime;
import java.util.Date;

public class Transaction {

    private String symbol;
    private String type;
    private double transactionFee;; // 10 cents per stock
    private double price; //current price
    private int volume; //number of stocks
    private LocalDateTime transactionTime;

    //TODO: ADD TOSTRING

    public Transaction(String symbol, double price, int volume, String type, LocalDateTime transactionTime) {
        this.symbol=symbol;
        this.transactionFee = 0.1;
        this.price = price;
        this.volume = volume;
        this.type=type;
        this.transactionTime=transactionTime;

    }

    public double getTransactionAmount() {
        if (type.equals("buy")){
            return (price+transactionFee)*volume; //total price paid when buying stocks
        }
        else {
            return (price-transactionFee)*volume; //total amount received for selling stocks
        }
    }

    public double getTransactionFees(){
        return volume*transactionFee;
    }

    public void reportTransaction(){ //inform how many stocks were bought/sold at what price
        String verb=type.equals("buy")? "bought" : "sold";
        System.out.print(volume + " stocks of " + symbol + " " + verb+ " @" + price + " USD (total value ");
        System.out.printf("%.2f", getTransactionAmount());
        System.out.println(" USD).");
    }

    public LocalDateTime getTransactionTime() {
        return transactionTime;
    }
}
