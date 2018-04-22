package app;

import javafx.geometry.Pos;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Portfolio {

    private double availableFunds;
    //private User user;
    private Map<String, Stock> portfolio;
    private Map<String, Position> positions;
    private Map<LocalDateTime, Transaction> transactions;
    private double totalValueOfPositions; //sum of all totals (i.e. sum of all current positions)
    private double profit; //realised profit (from closed positions (sold stocks))
    private double unrealisedProfit; //gains/losses in value of stocks in portfolio (i.e of stocks not sold yet)
    //private double totalValueOfPortfolio; //positions + availableFunds

    //Constructor - creates an empty portfolio:
    public Portfolio(double initialFunds) {
        this.portfolio = new HashMap<>();
        this.positions= new HashMap<>();
        this.transactions= new HashMap<>();
        this.totalValueOfPositions = 0.0;
        //this.totalValueOfPortfolio=0.0;
        this.profit = 0.0;
        this.unrealisedProfit = 0.0;
        //this.availableFunds = 0.0;
        this.availableFunds =initialFunds;
        //this.user = user;
    }

    public Portfolio(double availableFunds,  Map<String, Stock> portfolio,
                     Map<String, Position> positions, Map<LocalDateTime, Transaction> transactions,
                     double totalValueOfPositions, double profit, double unrealisedProfit
                    //, User user
    ) {
        this.availableFunds = availableFunds;
        //this.user = user;
        this.portfolio = portfolio;
        this.positions = positions;
        this.transactions=transactions;
        this.totalValueOfPositions = totalValueOfPositions;
        //this.totalValueOfPortfolio=totalValueOfPositions+availableFunds;
        this.profit = profit;
        this.unrealisedProfit = unrealisedProfit;
    }


    //-----------------------------------------------
    //BUYING STOCK:

    public void buyStock(String symbol, int volume) {

        double price;
        String transactionType="buy";
        boolean newStock=!portfolio.containsKey(symbol);  // if stock already included in portfolio
        Stock stock;
        Position position;
        LocalDateTime transactionTime=java.time.LocalDateTime.now();

        if (newStock==true) {
            stock = new Stock(symbol);
            price = stock.getCurrentPrice();
        }
        else {
            stock = portfolio.get(symbol);
            price = stock.getLatestPrice();
        }

        Transaction transaction=new Transaction(symbol, price, volume, transactionType, transactionTime);

        if(transaction.getTransactionAmount()>availableFunds){
            throw new RuntimeException("Not enough funds!");
        }
        else {
            availableFunds-= transaction.getTransactionAmount();
            //user.setAvailableFunds(availableFunds);
            transaction.reportTransaction();
            transactions.put(transactionTime, transaction);

            if (newStock==true) {
                portfolio.put(symbol, stock);
                position = new Position(transaction, symbol, price, volume);
                positions.put(symbol, position);
            }
            else {
                position = positions.get(symbol);
                position.priceUpdate(price);
                position.increasePosition(transaction, price, volume);
            }

            calculateTotals();
        }
    }

    //-----------------------------------------------
    //SELLING STOCK:

    //- If user tries to sell more stocks than included in portfolio, only the
    //stocks included in portfolio will be sold (negative number of stocks not allowed)
    //- If user tries to sell stock not included in portfolio, nothing happens.

    public void sellStock(String symbol, int volume) {

        //checking if stock is present in portfolio is already included in main class
        //TODO: (PRIORITY 1) - SEE IF THIS CHECK STILL WORKS IN IU.

        String transactionType="sell";
        Position position = positions.get(symbol);
        LocalDateTime transactionTime=java.time.LocalDateTime.now();

        if (position.getVolume() < volume) { //max number of shares to be sold is their number in portfolio
            System.out.println("Portfolio only contains " + volume + " stocks. Now selling them all...");  //LISATUD
            volume = position.getVolume(); //if user tries to sell more, only the max number is sold
        }

        //double transactionFeeTotal = volume * transactionFee;
        Stock stock = portfolio.get(symbol);
        double price = stock.getLatestPrice();  //current price
        position.priceUpdate(price);

        Transaction transaction = new Transaction(symbol, price, volume, transactionType, transactionTime);
        availableFunds += transaction.getTransactionAmount();;
        profit+=volume*(price-position.getAveragePrice())-transaction.getTransactionFees();
        //user.setAvailableFunds(availableFunds);
        transaction.reportTransaction();
        transactions.put(transactionTime, transaction);

        //TODO: (PIORITY 1)- CREATE POSSIBILITY TO VIEW ALL TRANSACTIONS OF USER
        //(MAYBE CREATE A NEW CLASS "TRANSACTIONS REPORT" VMS FOR THIS) TO SEE TRANSACTIONS
        //SORTED EITHER BY STOCK SYMBOL OR BY DATE (WITH TRANSACTION PRICE, FEE, AMOUNT PAID, PROFIT,
        // DATE, SYMBOL, AVAILABLE FUNDS AFTER TRANSACTION).
        // SOME INFO FOR THIS IS IN "TRANSACTION" CLASS AND SOME IN "POSITION" CLASS.

        position.decreasePosition(transaction, price, volume);

        calculateTotals();

        //removing stock from portfolio in case its volume is zero after sell
        if (positions.get(symbol).getVolume()==0){
            portfolio.remove(symbol);
            //positions.remove(symbol); //Should NOT be included, because: a) calculateTotals() depends on it,
            // and b) keeping closed positsions is important for creating the (planned) transactions report.
        }
    }

    //TODO: (PRIORITY 2) Possibility for short selling could be added (allows negative number of stocks)

    //-----------------------------------------------

    //calculate portfolio total (sum of all current positions in stock):
    public void calculateTotals() {
        totalValueOfPositions =0.0;
        profit=0.0;
        unrealisedProfit=0.0;

        for (String s : positions.keySet()) {
            Position position=positions.get(s);
            totalValueOfPositions+=position.getCurrentValue();
            profit+=position.getProfit(); //for both open and closed positions
            unrealisedProfit+=position.getUnrealisedProfit();
        }

        //totalValueOfPortfolio=totalValueOfPositions+availableFunds;

/*        for (String p : portfolio.keySet()) {
            profit+=position.getProfit();
        }*/


    }

    //-----------------------------------------------

/*    //update current prices of all stocks in portfolio:
    public void updatePrices() {

        for (String symbol : portfolio.keySet()) {
            int indexOfStock = symbolList.indexOf(symbol);
            Stock stock = portfolio.get(symbol);
            int volume = volumes.get(indexOfStock);
            double price = stock.getLatestPrice();

            stock.setCurrentPrice(price);
            prices.set(indexOfStock, price);
            currentValuesOfPositions.set(indexOfStock, volume * price);  //current value of position in this stock
            unrealisedProfitsOrLosses.set(indexOfStock, volume * (price - averagePrices.get(indexOfStock)));
            //profitsOrLosses does not change (because it indicates realised profit/loss from transactions)

            totalValueOfPositions = calculateTotal(currentValuesOfPositions);
            profit = calculateTotal(profitsOrLosses);
            unrealisedProfit = calculateTotal(unrealisedProfitsOrLosses);

        }
    }*/

    //-----------------------------------------------
    //GETTER & SETTERS:

    //Getting stock from portfolio:
    public Stock getStock(String symbol) {
        return portfolio.get(symbol);
    }

    //Getting portfolio total value:
    public double getTotalValueOfPortfolio() {
        return availableFunds + totalValueOfPositions;
    }


    //Other getters:
    public Map<String, Stock> getPortfolio() {
        return portfolio;
    }

    public double getTotalValueOfPositions() {
        return totalValueOfPositions;
    }

    public double getProfit() {
        return profit;
    }

    public double getUnrealisedProfit() {
        return unrealisedProfit;
    }

    public void setAvailableFunds(double availableFunds) {
        this.availableFunds = availableFunds;
    }

    public double getAvailableFunds() {
        return availableFunds;
    }

/*    public void setUser(User user) {
        this.user = user;
    }*/









    //-----------------------------------------------


    public List<String> roundDoubleList(List<Double> arrayList) {
        DecimalFormat df = new DecimalFormat("###.##");
        List<String> roundedDoubles = new ArrayList<>();

        for (Double dbl : arrayList) {
            roundedDoubles.add(df.format(dbl));

        }
        return roundedDoubles;
    }

/*    @Override
    public String toString() {
        return
                "Stock names: \n" + symbolList.toString() + '\n' +
                        "Prices: \n" + prices.toString() + '\n' +
                        "Volumes: \n" + volumes.toString() + '\n' +
                        "Average purchase prices: \n" + averagePrices + '\n' +
                        "Profits or losses: \n" + profitsOrLosses + '\n' +
                        "Unrealised profits or losses: \n" + roundDoubleList(unrealisedProfitsOrLosses) + '\n' +
                        "Current values of positions: \n" + currentValuesOfPositions + '\n' +
                        "Total current value of positions: \n" + totalValueOfPositions + '\n' +
                        "Total profit or loss: \n" + profit + '\n' +
                        "Total unrealised profit or loss: \n" + unrealisedProfit + '\n' +
                        "Transaction fee: \n" + transactionFee + '\n';
    }

    public String toStringForFile() {
        return
                "" + user.getUserName() + ";" + availableFunds + ";" +
                        symbolList + ';' +
                        prices + ';' +
                        volumes + ';' +
                        averagePrices + ';' +
                        profitsOrLosses + ';' +
                        roundDoubleList(unrealisedProfitsOrLosses) + ';' +
                        currentValuesOfPositions + ';' +
                        totalValueOfPositions + ';' +
                        profit + ';' +
                        unrealisedProfit + ';' +
                        transactionFee + '\n';
    }*/

}
