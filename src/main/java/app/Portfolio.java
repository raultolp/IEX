package app;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static app.StaticData.ANSI_RESET;
import static app.StaticData.ANSI_YELLOW;

public class Portfolio extends IEXdata {

    private double availableFunds;
    private Map<String, Stock> portfolioStocks;
    private Map<String, Position> positions;
    private double totalValueOfPositions = 0.0; //sum of all totals (i.e. sum of all current positions)
    private double profit = 0.0; //realised profit (from closed positions (sold stocks))
    private double unrealisedProfit = 0.0; //gains/losses in value of stocks in portfolio (i.e of stocks not sold yet)
    private Iu handler;


    //Constructor - creates an empty portfolio for new user:
    public Portfolio(double initialFunds, Iu handler) {
        this.portfolioStocks = new HashMap<>();
        this.positions = new HashMap<>();
        this.totalValueOfPositions = 0.0;
        this.profit = 0.0;
        this.unrealisedProfit = 0.0;
        this.availableFunds = initialFunds;
        this.handler = handler;
    }

    //TODO: (PRIORITY 3): IT WOULD ALSO BE POSSIBLE TO DOWNLOAD CHART DATA IN ONE GO, IF NEEDED:
    //https://api.iextrading.com/1.0/stock/market/batch?symbols=aapl,fb&types=quote,news,chart&range=1m&last=5

    //Constructor - creates a MasterPortfolio with all availableStocks for admin:
    public Portfolio(String[] availableStocks, double availableFunds, Iu handler) throws IOException {
        this(availableFunds, handler); //uses another constructor for generating empty portfolio

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
    public Portfolio(JsonObject portfObj, Iu handler) {
        this(-10, handler); //uses another constructor for generating empty portfolio
        this.availableFunds = portfObj.get("availableFunds").getAsDouble();

        JsonArray posListArray = portfObj.getAsJsonArray("positions").getAsJsonArray(); //kasutaja portfelli positsioonid

        if (posListArray.size() != 0) {
            for (int i = 0; i < posListArray.size(); i++) {
                JsonObject posObj = posListArray.get(i).getAsJsonObject();
                Position position = new Position(posObj);
                String stockSymb = position.getSymbol();
                positions.put(stockSymb, position);
                Stock stock = handler.getMasterPortfolio().getStock(stockSymb);
                portfolioStocks.put(stockSymb, stock);
            }
            calculateTotals();
        }
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
            stock = handler.getMasterPortfolio().getStock(symbol);
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
            //System.out.println(symbol+"vol"+positions.get(symbol).getVolume()+"value: "+positions.get(symbol).getCurrentValue());
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
        if (positions.size() != 0) {
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
    }
    //-----------------------------------------------

    //GET/PRINT TRANSACTIONS REPORT:
    public String getTransactionsReport() {
        String report = MyUtils.createHeader("TRANSACTIONS REPORT");

        double totalProfit = 0.0;
        for (String symb : positions.keySet()) {
            double positionProfit = 0.0;
            report += ANSI_YELLOW + ">>> " + symb + ANSI_RESET + '\n';
            report += "TYPE   DATE       TIME       VOLUME      PRICE    FEES   PAID/RECEIVED     PROFIT\n";
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

    //UPDATE CURRENT PRICES FOR ALL STOCKS IN MASTER PORTFOLIO AND USER PORTFOLIOS:
    public void updatePrices(List<User> userList) {

        //Constructing URL:
        String stockSymbols = String.join(",", portfolioStocks.keySet());
        String url = "https://api.iextrading.com/1.0/stock/market/batch?symbols=" + stockSymbols + "+&types=price";

        //List<User> userList= Iu.getUserList();  //VT KAS PANNA TAGASI

        try {
            JsonElement root = IEXdata.downloadData(url);  // array or object
            JsonObject rootobj = root.getAsJsonObject();

            // Updating prices of all stocks (updates prices in MasterPortfolio, but also in
            //  Users' portfolios, given that stock instances contained in them are just taken
            // from MasterPortfolio):
            for (String stockSymb : portfolioStocks.keySet()) {
                Stock stock = portfolioStocks.get(stockSymb);
                double newPrice = rootobj.getAsJsonObject(stockSymb).get("price").getAsDouble();
                stock.setCurrentPrice(newPrice);
            }

            //Updating positions in MasterPortfolio:
            if (positions.size() != 0) {  //if not empty portfolio
                updatePositions(positions);
                calculateTotals();
            }

            //Updating positions in users' portfolios:
            if (userList.size() > 0) {
                for (User user : userList) {
                    Portfolio userportf = user.getPortfolio();
                    Map<String, Position> userPositions = userportf.getPositions();
                    updatePositions(userPositions);
                    userportf.calculateTotals();
                }
            }

        } catch (IOException e) {
            System.out.println("Connection to IEX failed. Prices were not updated.");
        }
    }

    //Updating positions of MasterPortfolio and all Users' portfolios:
    public void updatePositions(Map<String, Position> userPositions) {
        Portfolio masterPortfolio = handler.getMasterPortfolio();
        if (userPositions.size() != 0) {  //if not empty portfolio
            for (String stockSymb : userPositions.keySet()) {
                Position position = userPositions.get(stockSymb);
                double newprice = masterPortfolio.getStock(stockSymb).getCurrentPrice();
                //System.out.print("old: "+position.getCurrentValue());
                //System.out.print(", newprice*volume: "+newprice*position.getVolume());
                position.priceUpdate(newprice);
                //System.out.println(", actual new: "+position.getCurrentValue());
            }
        }
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
        String header = "STOCK   VOLUME      PRICE        VALUE   UNREALIZED P/L   REALIZED P/L";
        String info = MyUtils.createHeader(header);

        if (positions.size() > 0) {
            for (String symbol : positions.keySet()) {
                if (positions.get(symbol).isOpen()) { // only open positions are shown when viewing in portfolio
                    info += positions.get(symbol).toStringForPortfolio();
                }
            }
        } else {
            info += "Portfolio contains no open positions...\n";
        }

        info.trim();

        //Information on portfolio totals:
        info += MyUtils.createSeparator(header.length());
        info += "PORTFOLIO TOTAL VALUE: " + String.format("%.2f", getTotalValueOfPortfolio()) + " USD\n";
        info += "  - incl. OPEN POSITIONS: " + String.format("%.2f", totalValueOfPositions) + " USD\n";
        info += "    - of which: TOTAL ACQUISITION PRICE (INCL. TRANSACTION FEES): " + String.format("%.2f", totalValueOfPositions - unrealisedProfit) + " USD\n";
        info += "    - of which: INCERASE IN VALUE (UNREALIZED PROFIT): " + String.format("%.2f", unrealisedProfit) + " USD\n";
        info += "  - incl. CASH: " + String.format("%.2f", availableFunds) + " USD\n";
        info += MyUtils.createSeparator(header.length());
        info += "REALIZED PROFIT (FROM SALES): " + String.format("%.2f", profit) + " USD";

        return info;
    }

}
