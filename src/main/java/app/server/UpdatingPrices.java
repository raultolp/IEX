package app.server;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.List;
import java.util.Map;

class UpdatingPrices implements Runnable {
    private final Iu handler;
    private final Portfolio masterPortfolio;
    private final List<User> userList;


    public UpdatingPrices(Iu handler) {
        this.handler = handler;
        this.masterPortfolio = handler.getMasterPortfolio();
        this.userList = handler.getUserList();
    }

    //Downloading price information from IEX at regular time intervals,
    //and updating masterPortfolio and all userPortfolios:

    @Override
    public void run() {
        int timeToSleep = 10000; // in milliseconds

        while (true) {
            updatePrices(userList, masterPortfolio, handler);
            //System.out.println("ok");  //for testing

            try {
                Thread.sleep(timeToSleep);
            } catch (InterruptedException e) {
                System.out.println("PRICE UPDATE STOPPED!");
                return;  //thread is terminated
            }
        }
    }

    //-----------------------------------------------

    //TÕSTETUD PORTFELLI ALT SIIA, ET KOGU UPDATE'IMISE LOOGIKA OLEKS ÜHES KOHAS:

    //UPDATE CURRENT PRICES FOR ALL STOCKS IN MASTER PORTFOLIO AND USER PORTFOLIOS:
    private void updatePrices(List<User> userList, Portfolio masterPortfolio, Iu handler) {

        Map<String, Position> positions = masterPortfolio.getPositions();
        Map<String, Stock> portfolioStocks = masterPortfolio.getPortfolioStocks();

        //Constructing URL:
        String stockSymbols = String.join(",", portfolioStocks.keySet());
        String url = "https://api.iextrading.com/1.0/stock/market/batch?symbols=" + stockSymbols + "+&types=price";

        //List<User> userList= Iu.getUserList();  //VT KAS PANNA TAGASI

        try {
            JsonElement root = IEXdata.downloadData(url);  // array or object
            JsonObject rootobj = root.getAsJsonObject();
            JsonObject priceUpdateForClients = new JsonObject(); //masterPortfolio's updated stock prices for Client

            // Updating prices of all stocks:
            // (updates prices in MasterPortfolio, but also in Users' portfolios, given that stock
            //  instances contained in them are just taken from MasterPortfolio)
            for (String stockSymb : portfolioStocks.keySet()) {
                Stock stock = portfolioStocks.get(stockSymb);
                double newPrice = rootobj.getAsJsonObject(stockSymb).get("price").getAsDouble();
                stock.setCurrentPrice(newPrice);
                priceUpdateForClients.addProperty(stockSymb, newPrice);
            }

            //Updating positions in MasterPortfolio:
            if (positions.size() != 0) {  //if not empty portfolio
                updatePositions(positions, masterPortfolio);
                masterPortfolio.calculateTotals();
                //masterPortfolio.setPortfolioHasChanged(true);
            }

            //Updating positions in users' portfolios:
            for (User user : userList) {
                Portfolio userportf = user.getPortfolio();
                Map<String, Position> userPositions = userportf.getPositions();
                updatePositions(userPositions, masterPortfolio);
                userportf.calculateTotals();
                userportf.setPortfolioHasChanged(true);
            }
            handler.setPriceUpdateForClients(priceUpdateForClients);

        } catch (IOException e) {
            System.out.println("Connection to IEX failed. Prices were not updated.");  //ainult admin'ile
        }
    }

    //Updating positions of MasterPortfolio and all Users' portfolios:
    private void updatePositions(Map<String, Position> userPositions, Portfolio masterPortfolio) {
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


