package app.client;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.Map;

public class UpdatingPrices {

    //TÕSTETUD PORTFELLI ALT SIIA, ET KOGU UPDATE'IMISE LOOGIKA OLEKS ÜHES KOHAS:

    //UPDATE CURRENT PRICES FOR ALL STOCKS IN MASTER PORTFOLIO AND
    // PRICES AND CONTENTS OF USER PORTFOLIO:
    public static void updatePrices(Portfolio masterPortfolio, Portfolio userPortfolio, String masterPriceUpdateAsString, String userAsString) {

        JsonParser jp = new JsonParser();
        JsonObject priceUpdate = jp.parse(masterPriceUpdateAsString).getAsJsonObject(); //from String to json
        JsonObject userUpdate = jp.parse(userAsString).getAsJsonObject();

        Map<String, Position> positions = masterPortfolio.getPositions();
        Map<String, Stock> portfolioStocks = masterPortfolio.getPortfolioStocks();

        // Updating prices in master portfolio:
        for (String stockSymb : portfolioStocks.keySet()) {
            Stock stock = portfolioStocks.get(stockSymb);
            double newPrice = priceUpdate.get(stockSymb).getAsDouble();
            stock.setCurrentPrice(newPrice);
        }

        //Updating user portfolio:

        //TODO - STILL TO BE DONE... in case FX is to come to life
        // (I believe user portfolio object should be updated rather than creating a new Portfolio
        // object for user from json (which would be easy)... (might cause problems in FX)


/*
            JsonObject rootobj = root.getAsJsonObject();

            // Updating prices of all stocks:
            // (updates prices in MasterPortfolio, but also in Users' portfolios, given that stock
            //  instances contained in them are just taken from MasterPortfolio)
            for (String stockSymb : portfolioStocks.keySet()) {
                Stock stock = portfolioStocks.get(stockSymb);
                double newPrice = rootobj.getAsJsonObject(stockSymb).get("price").getAsDouble();
                stock.setCurrentPrice(newPrice);
                priceUpdateForClients.addProperty(stockSymb, newPrice);
            }

*//*            //Updating positions in MasterPortfolio:
            if (positions.size() != 0) {  //if not empty portfolio
                updatePositions(positions, masterPortfolio);
                masterPortfolio.calculateTotals();
            }*//*

            //Updating positions in users' portfolios:
            for (User user : userList) {
                Portfolio userportf = user.getPortfolio();
                Map<String, Position> userPositions = userportf.getPositions();
                updatePositions(userPositions, masterPortfolio);
                userportf.calculateTotals();
            }

    }

    //Updating positions of MasterPortfolio and all Users' portfolios:
    public void updatePositions(Map<String, Position> userPositions, Portfolio masterPortfolio) {
        for (String stockSymb : userPositions.keySet()) {
            Position position = userPositions.get(stockSymb);
            double newprice = masterPortfolio.getStock(stockSymb).getCurrentPrice();
            //System.out.print("old: "+position.getCurrentValue());
            //System.out.print(", newprice*volume: "+newprice*position.getVolume());
            position.priceUpdate(newprice);
            //System.out.println(", actual new: "+position.getCurrentValue());
        }*/
    }

}


