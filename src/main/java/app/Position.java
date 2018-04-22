package app;

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



    public Position(Transaction transaction, String symbol, double price, int volume) {
        this.symbol = symbol;
        this.price = price;
        this.volume = volume;
        this.averagePrice=transaction.getTransactionAmount()/volume;
        this.profit=0-transaction.getTransactionFees();
        this.unrealisedProfit=0.0;
        this.currentValue=price*volume;
        this.open=true;
    }

    public void priceUpdate(double newPrice) {
        price=newPrice;
        unrealisedProfit=(newPrice-averagePrice)*volume;
        currentValue=newPrice*volume;
    }

    public void increasePosition(Transaction transaction, double purchasePrice, double addedVolume){
        profit-=transaction.getTransactionFees();

        // new weighted average purchase price:
        averagePrice= (volume * averagePrice + transaction.getTransactionAmount()) / (volume + addedVolume);

        volume+=addedVolume;
        priceUpdate(purchasePrice);
    }

    public void decreasePosition(Transaction transaction, double salesPrice, double salesVolume){
        volume-=salesVolume;
        priceUpdate(salesPrice);
        profit+=salesVolume*(salesPrice-averagePrice)-transaction.getTransactionFees();

        if (volume==0){
            open=false; //position is closed
        }
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

    //TODO: (PRIORITY 2) - ADD SHORT POSITIONS

}
