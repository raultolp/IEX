package app.server;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static app.server.StaticData.ANSI_RESET;
import static app.server.StaticData.ANSI_YELLOW;

public class Portfolio {

    private double availableFunds;
    private final Map<String, Stock> portfolioStocks;
    private final Map<String, Position> positions;
    private double totalValueOfPositions = 0.0; //sum of all totals (i.e. sum of all current positions)
    private double profit = 0.0; //realised profit (from closed positions (sold stocks))
    private double unrealisedProfit = 0.0; //gains/losses in value of stocks in portfolio (i.e of stocks not sold yet)

    private boolean portfolioHasChanged = false;  //for checking if portfolio update should be sent to user


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
    public Portfolio(String[] availableStocks, double availableFunds) throws IOException {
        this(availableFunds); //uses another constructor for generating empty portfolio

        String symbols = String.join(",", availableStocks);
        String url = "https://api.iextrading.com/1.0/stock/market/batch?symbols=" + symbols + "&types=quote,stats"; //,news,chart&range=1m&last=10";

        JsonElement root = IEXdata.downloadData(url);  // array or object
        JsonObject rootobj = root.getAsJsonObject();

        for (String stockSymb : availableStocks) {
            JsonObject stockObject = rootobj.getAsJsonObject(stockSymb);
            Stock stock = new Stock(stockSymb, stockObject);
            portfolioStocks.put(stockSymb, stock);
        }
    }

    //Constructor - for initiating user portfolio from Json:
    public Portfolio(JsonObject portfObj, Portfolio masterPortfolio) {
        //this(-10, handler); //uses another constructor for generating empty portfolio
        //this.availableFunds = portfObj.get("availableFunds").getAsDouble();
        this(portfObj.get("availableFunds").getAsDouble()); //uses another constructor for generating empty portfolio

        JsonArray posListArray = portfObj.getAsJsonArray("positions").getAsJsonArray(); //kasutaja portfelli positsioonid

        for (int i = 0; i < posListArray.size(); i++) {
            JsonObject posObj = posListArray.get(i).getAsJsonObject();
            Position position = new Position(posObj);
            String stockSymb = position.getSymbol();
            positions.put(stockSymb, position);
            Stock stock = masterPortfolio.getStock(stockSymb);
            portfolioStocks.put(stockSymb, stock);
        }
        calculateTotals();
    }


    //-----------------------------------------------
    //BUYING STOCK:

    public void buyStock(String symbol, int volume, Iu handler, IO io) throws IOException {
        double price;
        String transactionType = "buy";
        boolean newStock = !portfolioStocks.containsKey(symbol);  // if stock already included in portfolio
        Stock stock;
        Position position;
        LocalDateTime transactionTime = LocalDateTime.now();
        boolean isAdmin = handler.isAdmin();

        if (newStock) {
            stock = handler.getMasterPortfolio().getStock(symbol);
        } else {
            stock = portfolioStocks.get(symbol);
        }
        price = stock.getCurrentPrice();

        Transaction transaction = new Transaction(symbol, price, volume, 0.0, transactionType, transactionTime);

        if (transaction.getTransactionAmount() > availableFunds) {
            throw new RuntimeException("Not enough funds!");
        } else {
            availableFunds -= transaction.getTransactionAmount();
            String transactionConfirmation = transaction.reportTransaction();
            if (isAdmin) {
                System.out.println(transactionConfirmation);
            } else {
                io.println(transactionConfirmation);
            }
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
        portfolioHasChanged = true;
    }

    //-----------------------------------------------
    //SELLING STOCK:

    //checking if stock is present in portfolio is already included in main class

    public void sellStock(String symbol, int volume, Iu handler, IO io) throws IOException {

        String transactionType = "sell";
        Position position = positions.get(symbol);
        LocalDateTime transactionTime = LocalDateTime.now();

        //If user tries to sell more stocks than included in portfolio, only the
        //stocks included in portfolio will be sold (negative number of stocks not allowed):
        if (position.getVolume() < volume) { //max number of shares to be sold is their number in portfolio
            volume = position.getVolume();
            io.println("Portfolio only contains " + volume + " stocks. Now selling them all...");
             //if user tries to sell more, only the max number is sold
        }

        Stock stock = portfolioStocks.get(symbol);
        double price = stock.getCurrentPrice();  //current price
        position.priceUpdate(price);

        double avgPurchasePrice = position.getAveragePrice();
        Transaction transaction = new Transaction(symbol, price, volume, avgPurchasePrice, transactionType, transactionTime);
        availableFunds += transaction.getTransactionAmount();

        profit += volume * (price - position.getAveragePrice()) - transaction.getTransactionFees();
        String transactionConfirmation = transaction.reportTransaction();
        io.println(transactionConfirmation);


        position.decreasePosition(transaction, price, volume);
        calculateTotals();

        //removing stock from portfolio in case its volume is zero after sell
        if (positions.get(symbol).getVolume() == 0) {
            portfolioStocks.remove(symbol);  //AT the same time, symbol should NOT be removed from positions,
            // because: a) calculateTotals() depends on it, and b) keeping closed positsions is important
            //  for creating the (planned) transactions report.
        }

        portfolioHasChanged = true;
    }

    //TODO: (PRIORITY 3) Possibility for short selling could be added (allows negative number of stocks)

    //-----------------------------------------------
    //CALCULATE PORTFOLIO TOTAL (SUM OF ALL CURRENT POSITIONS IN STOCK):
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
    public String getTransactionsReport(Iu handler) {
        boolean isAdmin = handler.isAdmin();
        StringBuilder report = new StringBuilder(MyUtils.createHeader("TRANSACTIONS REPORT"));

        double totalProfit = 0.0;
        for (String symb : positions.keySet()) {
            double positionProfit = 0.0;
            if (isAdmin) {
                report.append(ANSI_YELLOW + ">>> ").append(symb).append(ANSI_RESET).append('\n');
            } else {
                report.append(">>> ").append(symb).append('\n');
            }

            report.append("TYPE   DATE       TIME       VOLUME      PRICE    FEES   PAID/RECEIVED     PROFIT\n");
            Position position = positions.get(symb);
            List<Transaction> transactions = position.getTransactions();

            for (Transaction t : transactions) {
                report.append(t.toStringForReport()).append("\n");
                positionProfit += t.getProfitFromSell();
            }

            report.append("POSITION PROFIT(").append(symb).append("): ").append(String.format("%.2f", positionProfit)).append(" USD");
            totalProfit += positionProfit;
            report.append("\n\n");
        }
        report.append("TOTAL PROFIT: ").append(String.format("%.2f", totalProfit)).append(" USD");
        //System.out.println(report);
        return report.toString();
    }


    //-----------------------------------------------
    //GETTER & SETTERS:

    public Stock getStock(String symbol) {
        return portfolioStocks.get(symbol);
    }

    public double getTotalValueOfPortfolio() {
        calculateTotals();
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

    public Map<String, Position> getPositions() {
        return positions;
    }

    //-----------------------------------------------

    public JsonObject covertToJson() {
        JsonObject portfObj = new JsonObject();  // kasutaja portf
        JsonArray posListArray = new JsonArray(); //kasutaja portfelli positsioonid

        for (String stockSymb : positions.keySet()) {
            JsonObject posObj = positions.get(stockSymb).covertToJson();
            posListArray.add(posObj);
        }
        portfObj.add("positions", posListArray);
        portfObj.addProperty("availableFunds", availableFunds);
        return portfObj;

    }


    @Override
    public String toString() {

        //Information on positsions:
        String header = "STOCK   VOLUME   CURRENT PRICE   VALUE      UNREALIZED P/L   AVG.PURCHASE PRICE  REALIZED P/L";
        StringBuilder info = new StringBuilder(MyUtils.createHeader(header));

        boolean positionsEmpty = true;
        for (String symbol : positions.keySet()) {
            if (positions.get(symbol).isOpen()) { // only open positions are shown when viewing in portfolio
                info.append(positions.get(symbol).toStringForPortfolio());
                positionsEmpty = false;
            }
        }
        if (positionsEmpty) {
            info.append("Portfolio contains no open positions...\n");
        }

        //Information on portfolio totals:
        info.append(MyUtils.createSeparator(header.length()));
        info.append("PORTFOLIO TOTAL VALUE: ").append(String.format("%.2f", getTotalValueOfPortfolio())).append(" USD\n");
        info.append("  - incl. OPEN POSITIONS: ").append(String.format("%.2f", totalValueOfPositions)).append(" USD\n");
        info.append("    - of which: TOTAL ACQUISITION PRICE (INCL. TRANSACTION FEES): ").append(String.format("%.2f", totalValueOfPositions - unrealisedProfit)).append(" USD\n");
        info.append("    - of which: INCERASE IN VALUE (UNREALIZED PROFIT): ").append(String.format("%.2f", unrealisedProfit)).append(" USD\n");
        info.append("  - incl. CASH: ").append(String.format("%.2f", availableFunds)).append(" USD\n");
        info.append(MyUtils.createSeparator(header.length()));
        info.append("REALIZED PROFIT (FROM SALES): ").append(String.format("%.2f", profit)).append(" USD");

        return info.toString();
    }

    public void setPortfolioHasChanged(boolean portfolioHasChanged) {
        this.portfolioHasChanged = portfolioHasChanged;
    }

    public boolean isPortfolioChanged() {
        return portfolioHasChanged;
    }
}
