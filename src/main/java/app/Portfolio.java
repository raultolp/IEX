package app;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import javafx.geometry.Pos;

import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Portfolio extends IEXdata{

    private double availableFunds;
    private Map<String, Stock> portfolioStocks;
    private Map<String, Position> positions;
    private double totalValueOfPositions; //sum of all totals (i.e. sum of all current positions)
    private double profit; //realised profit (from closed positions (sold stocks))
    private double unrealisedProfit; //gains/losses in value of stocks in portfolio (i.e of stocks not sold yet)

    //Constructor - creates an empty portfolio for new user:
    public Portfolio(double initialFunds) {
        this.portfolioStocks = new HashMap<>();
        this.positions = new HashMap<>();
        this.totalValueOfPositions = 0.0;
        this.profit = 0.0;
        this.unrealisedProfit = 0.0;
        this.availableFunds = initialFunds;
    }

    //Constructor - creates a dummy portfolio with no funds but with all availableStocks for admin:
    public Portfolio(String[] availableStocks){
        this(0.0); //uses another constructor




        //Stock stock= new Stock(String symbol, double dividendYield, double eps, double peRatio, int marketCap,
        //double previousClose, double change1Year, double change1Month, double change3Month,
        //double shortRatio, double currentPrice);


    }
/*    //Constructing URL:
    String stockSymbols=stockList();
    String url="https://api.iextrading.com/1.0/stock/market/batch?symbols="+stockSymbols+"+&types=price";

        try {
        JsonElement root = IEXdata.downloadData(url);  // array or object
        JsonObject rootobj = root.getAsJsonObject();

        for (String stockSymb : portfolioStocks.keySet()) {
            Stock stock=portfolioStocks.get(stockSymb);
            Position position=positions.get(stockSymb);

            double newPrice=rootobj.getAsJsonObject(stockSymb).get("price").getAsDouble();
            System.out.println(stockSymb+": "+newPrice);
            stock.setCurrentPrice(newPrice);
            position.priceUpdate(newPrice);
        }

        calculateTotals();

    } catch(IOException e) {
        System.out.println("Connection to IEX failed. Prices were not updated.");
    }*/






    //Constructor - for loading user portfolio from file::
    public Portfolio(double availableFunds,  Map<String, Stock> portfolio,
                     Map<String, Position> positions,
                     double totalValueOfPositions, double profit, double unrealisedProfit
    ) {
        this.availableFunds = availableFunds;
        this.portfolioStocks = portfolio;
        this.positions = positions;
        this.totalValueOfPositions = totalValueOfPositions;
        this.profit = profit;
        this.unrealisedProfit = unrealisedProfit;
    }


    //-----------------------------------------------
    //BUYING STOCK:

    public void buyStock(String symbol, int volume) {
        double price;
        String transactionType = "buy";
        boolean newStock = !portfolioStocks.containsKey(symbol);  // if stock already included in portfolio
        Stock stock;
        Position position;
        LocalDateTime transactionTime = java.time.LocalDateTime.now();

        if (newStock) {
            stock = new Stock(symbol);
            price = stock.getCurrentPrice();
        }
        else {
            stock = portfolioStocks.get(symbol);
            price = stock.getLatestPrice();
        }
        Transaction transaction = new Transaction(symbol, price, volume, transactionType, transactionTime);

        if (transaction.getTransactionAmount() > availableFunds) {
            throw new RuntimeException("Not enough funds!");
        } else {
            availableFunds -= transaction.getTransactionAmount();
            transaction.reportTransaction();

            if (newStock) {
                portfolioStocks.put(symbol, stock);
                position = new Position(transaction, symbol, price, volume);
                positions.put(symbol, position);
            } else {
                position = positions.get(symbol);
                position.priceUpdate(price);
                position.increasePosition(transaction, price, volume);
            }
            calculateTotals();
        }
    }

    //-----------------------------------------------
    //SELLING STOCK:

    public void sellStock(String symbol, int volume) {

        //checking if stock is present in portfolio is already included in main class
        //TODO: (PRIORITY 1) - SEE IF THIS CHECK STILL WORKS IN IU.

        String transactionType = "sell";
        Position position = positions.get(symbol);
        LocalDateTime transactionTime = java.time.LocalDateTime.now();

        //If user tries to sell more stocks than included in portfolio, only the
        //stocks included in portfolio will be sold (negative number of stocks not allowed):
        if (position.getVolume() < volume) { //max number of shares to be sold is their number in portfolio
            System.out.println("Portfolio only contains " + volume + " stocks. Now selling them all...");  //LISATUD
            volume = position.getVolume(); //if user tries to sell more, only the max number is sold
        }

        Stock stock = portfolioStocks.get(symbol);
        double price = stock.getLatestPrice();  //current price
        position.priceUpdate(price);

        Transaction transaction = new Transaction(symbol, price, volume, transactionType, transactionTime);
        availableFunds += transaction.getTransactionAmount();

        profit += volume * (price - position.getAveragePrice()) - transaction.getTransactionFees();
        transaction.reportTransaction();

        //TODO: (PIORITY 1)- CREATE POSSIBILITY TO VIEW ALL TRANSACTIONS OF USER
        //(MAYBE CREATE A NEW CLASS "TRANSACTIONS REPORT" VMS FOR THIS) TO SEE TRANSACTIONS
        //SORTED EITHER BY STOCK SYMBOL OR BY DATE (WITH TRANSACTION PRICE, FEE, AMOUNT PAID, PROFIT,
        // DATE, SYMBOL, AVAILABLE FUNDS AFTER TRANSACTION).
        // SOME INFO FOR THIS IS IN "TRANSACTION" CLASS AND SOME IN "POSITION" CLASS.

        position.decreasePosition(transaction, price, volume);

        calculateTotals();

        //removing stock from portfolio in case its volume is zero after sell
        if (positions.get(symbol).getVolume() == 0) {
            portfolioStocks.remove(symbol);
            //positions.remove(symbol); //Should NOT be included, because: a) calculateTotals() depends on it,
            // and b) keeping closed positsions is important for creating the (planned) transactions report.
        }
    }

    //TODO: (PRIORITY 2) Possibility for short selling could be added (allows negative number of stocks)

    //-----------------------------------------------
    //calculate portfolio total (sum of all current positions in stock):
    public void calculateTotals() {
        totalValueOfPositions = 0.0;
        profit = 0.0;
        unrealisedProfit = 0.0;

        for (String s : positions.keySet()) {
            Position position = positions.get(s);
            totalValueOfPositions += position.getCurrentValue();
            profit += position.getProfit(); //for both open and closed positions
            unrealisedProfit += position.getUnrealisedProfit();
        }
    }
    //-----------------------------------------------

    //GET/PRINT TRANSACTIONS REPORT:
    public String getTransactionsReport() {
        String report = "\n--------------------------------------------------" +
                "\nTRANSACTIONS REPORT" +
                "\n--------------------------------------------------\n";
        double totalProfit = 0.0;
        for (String symb : positions.keySet()) {
            double positionProfit = 0.0;
            report += symb;
            report += "\nTYPE\tDATE\t\tTIME\tVOLUME\tPRICE\tFEES\tPAID/RECEIVED\tPROFIT\n";
            Position position = positions.get(symb);
            List<Transaction> transactions = position.getTransactions();
            for (Transaction t : transactions) {
                report += t.toStringForReport() + "\n";
                positionProfit += t.getProfitFromSell();
            }
            report += "POSITION PROFIT(" + symb + "): " + positionProfit;
            totalProfit += positionProfit;
            report += "\n\n";
        }
        report += "TOTAL PROFIT: " + totalProfit;
        //System.out.println(report);
        return report;
    }


    //-----------------------------------------------



    //UPDATE CURRENT PRICES FOR ALL STOCKS IN PORTFOLIO:
    public void updatePrices() {

        //Constructing URL:
        String stockSymbols=stockList();
        String url="https://api.iextrading.com/1.0/stock/market/batch?symbols="+stockSymbols+"+&types=price";

        try {
            JsonElement root = IEXdata.downloadData(url);  // array or object
            JsonObject rootobj = root.getAsJsonObject();

            for (String stockSymb : portfolioStocks.keySet()) {
                Stock stock=portfolioStocks.get(stockSymb);
                Position position=positions.get(stockSymb);

                double newPrice=rootobj.getAsJsonObject(stockSymb).get("price").getAsDouble();
                System.out.println(stockSymb+": "+newPrice);
                stock.setCurrentPrice(newPrice);
                position.priceUpdate(newPrice);
            }

            calculateTotals();

        } catch(IOException e) {
            System.out.println("Connection to IEX failed. Prices were not updated.");
        }
    }


    //-------------------------

    public String stockList(){  //used in constructing URL for batch downloads
        String stockSymbols="";
        int i=0;

        for (String symb : portfolioStocks.keySet()) {
            stockSymbols+=symb;
            if (i!=portfolioStocks.keySet().size()){
                stockSymbols+=",";
            }
        }
        return stockSymbols;
    }

/*        for (String symbol : portfolio.keySet()) {
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

        }*/


    //-----------------------------------------------
    //GETTER & SETTERS:

    public Stock getStock(String symbol) {
        return portfolioStocks.get(symbol);
    }

    public double getTotalValueOfPortfolio() {
        return availableFunds + totalValueOfPositions;
    }

    public Map<String, Stock> getPortfolio() {
        return portfolioStocks;
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


    //-----------------------------------------------

    public List<String> roundDoubleList(List<Double> arrayList) {
        DecimalFormat df = new DecimalFormat("###.##");
        List<String> roundedDoubles = new ArrayList<>();

        for (Double dbl : arrayList) {
            roundedDoubles.add(df.format(dbl));

        }
        return roundedDoubles;
    }

    @Override
    public String toString() {
        return "Portfolio{" +
                "availableFunds=" + availableFunds +
                ", portfolio=" + portfolioStocks +
                '}';
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
