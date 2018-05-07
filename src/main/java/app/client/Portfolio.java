package app.client;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Portfolio {

    private double availableFunds;
    private Map<String, Stock> portfolioStocks;
    private Map<String, Position> positions;
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

/*    //Constructor - creates a MasterPortfolio with all availableStocks for admin:
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
    }*/

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

    //SELLING STOCK:


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
    public String getTransactionsReport() {
        String report = MyUtils.createHeader("TRANSACTIONS REPORT");

        double totalProfit = 0.0;
        for (String symb : positions.keySet()) {
            double positionProfit = 0.0;
            report += ">>> " + symb + '\n';
            report += "TYPE   DATE       TIME       VOLUME      PRICE    FEES   PAID/RECEIVED     PROFIT\n";
            Position position = positions.get(symb);
            List<Transaction> transactions = position.getTransactions();

            for (Transaction t : transactions) {
                report += t.toStringForReport() + "\n";
                positionProfit += t.getProfitFromSell();
            }

            report += "POSITION PROFIT(" + symb + "): " + String.format("%.2f", positionProfit) + " USD";
            totalProfit += positionProfit;
            report += "\n\n";
        }
        report += "TOTAL PROFIT: " + String.format("%.2f", totalProfit) + " USD";
        //System.out.println(report);
        return report;
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

/*    public JsonObject covertToJson() {
        JsonObject portfObj = new JsonObject();  // kasutaja portf
        JsonArray posListArray = new JsonArray(); //kasutaja portfelli positsioonid

        for (String stockSymb : positions.keySet()) {
            JsonObject posObj = positions.get(stockSymb).covertToJson();
            posListArray.add(posObj);
        }
        portfObj.add("positions", posListArray);
        portfObj.addProperty("availableFunds", availableFunds);
        return portfObj;

    }*/


    @Override
    public String toString() {

        //Information on positsions:
        String header = "STOCK   VOLUME   CURRENT PRICE   VALUE      UNREALIZED P/L   AVG.PURCHASE PRICE  REALIZED P/L";
        String info = MyUtils.createHeader(header);

        boolean positionsEmpty = true;
        for (String symbol : positions.keySet()) {
            if (positions.get(symbol).isOpen()) { // only open positions are shown when viewing in portfolio
                info += positions.get(symbol).toStringForPortfolio();
                positionsEmpty = false;
            }
        }
        if (positionsEmpty == true) {
            info += "Portfolio contains no open positions...\n";
        }

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

    public void setPortfolioHasChanged(boolean portfolioHasChanged) {
        this.portfolioHasChanged = portfolioHasChanged;
    }

    public boolean isPortfolioChanged() {
        return portfolioHasChanged;
    }
}
