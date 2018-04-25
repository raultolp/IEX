package app;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import javafx.geometry.Pos;

import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.*;

public class Portfolio extends IEXdata {

    private double availableFunds;
    private Map<String, Stock> portfolioStocks;
    private Map<String, Position> positions;
    private double totalValueOfPositions; //sum of all totals (i.e. sum of all current positions)
    private double profit; //realised profit (from closed positions (sold stocks))
    private double unrealisedProfit; //gains/losses in value of stocks in portfolio (i.e of stocks not sold yet)
    Portfolio masterPortfolio=Iu.getMasterPortfolio();


    //Constructor - creates an empty portfolio for new user:
    public Portfolio(double initialFunds) {
        this.portfolioStocks = new HashMap<>();
        this.positions = new HashMap<>();
        this.totalValueOfPositions = 0.0;
        this.profit = 0.0;
        this.unrealisedProfit = 0.0;
        this.availableFunds = initialFunds;
    }

    //TODO: (PRIORITY 3): IT WOULD ALSO BE POSSIBLE TO DOWNLOAD CHART DATA IN ONE GO, IF NEEDED:
    //https://api.iextrading.com/1.0/stock/market/batch?symbols=aapl,fb&types=quote,news,chart&range=1m&last=5

    //Constructor - creates a MasterPortfolio with all availableStocks for admin:
    public Portfolio(String[] availableStocks, double availableFunds) throws IOException{
        this(availableFunds); //uses another constructor for generating empty portfolio

        String symbols = String.join(",", availableStocks);
        String url="https://api.iextrading.com/1.0/stock/market/batch?symbols="+symbols+"&types=quote,stats"; //,news,chart&range=1m&last=10";

        JsonElement root = IEXdata.downloadData(url);  // array or object
        JsonObject rootobj = root.getAsJsonObject();

        for (String stockSymb : availableStocks) {
            JsonObject stockObject= rootobj.getAsJsonObject(stockSymb);
            Stock stock = new Stock(stockSymb, stockObject);
            portfolioStocks.put(stockSymb, stock);
        }
    }


    //Constructor - for loading user portfolio from file::
    public Portfolio(double availableFunds, Map<String, Stock> portfolioStocks,
                     Map<String, Position> positions,
                     double totalValueOfPositions, double profit, double unrealisedProfit
    ) {
        this.availableFunds = availableFunds;
        this.portfolioStocks = portfolioStocks;
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
            stock = masterPortfolio.getStock(symbol);
        } else {
            stock = portfolioStocks.get(symbol);
        }
        price = stock.getCurrentPrice();

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

    //checking if stock is present in portfolio is already included in main class

    public void sellStock(String symbol, int volume) {

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
        double price = stock.getCurrentPrice();  //current price
        position.priceUpdate(price);

        Transaction transaction = new Transaction(symbol, price, volume, transactionType, transactionTime);
        availableFunds += transaction.getTransactionAmount();

        profit += volume * (price - position.getAveragePrice()) - transaction.getTransactionFees();
        transaction.reportTransaction();
        position.decreasePosition(transaction, price, volume);
        calculateTotals();

        //removing stock from portfolio in case its volume is zero after sell
        if (positions.get(symbol).getVolume() == 0) {
            portfolioStocks.remove(symbol);
            //positions.remove(symbol); //Should NOT be included, because: a) calculateTotals() depends on it,
            // and b) keeping closed positsions is important for creating the (planned) transactions report.
        }
    }

    //TODO: (PRIORITY 3) Possibility for short selling could be added (allows negative number of stocks)

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
        String stockSymbols = String.join(",", portfolioStocks.keySet());
        String url = "https://api.iextrading.com/1.0/stock/market/batch?symbols=" + stockSymbols + "+&types=price";

        try {
            JsonElement root = IEXdata.downloadData(url);  // array or object
            JsonObject rootobj = root.getAsJsonObject();

            for (String stockSymb : portfolioStocks.keySet()) {
                Stock stock = portfolioStocks.get(stockSymb);
                double newPrice = rootobj.getAsJsonObject(stockSymb).get("price").getAsDouble();
                stock.setCurrentPrice(newPrice);

                if (positions.size()!=0){  //if not admin's portfolio (or other empty portfolio)
                    Position position = positions.get(stockSymb);
                    position.priceUpdate(newPrice);
                }
            }

            calculateTotals();

        } catch (IOException e) {
            System.out.println("Connection to IEX failed. Prices were not updated.");
        }
    }



    //-----------------------------------------------
    //GETTER & SETTERS:

    public Stock getStock(String symbol) {
        return portfolioStocks.get(symbol);
    }

    public double getTotalValueOfPortfolio() {
        return availableFunds + totalValueOfPositions;
    }

    public Map<String, Stock> getPortfolioStocks() {
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
