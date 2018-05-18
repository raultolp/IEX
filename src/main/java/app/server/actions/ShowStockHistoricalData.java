package app.server.actions;

import app.server.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;


//View stock historical data

public class ShowStockHistoricalData implements CommandHandler {

    @Override
    public void handle(Integer command, Iu handler, IO io) throws IOException {
        if (command == 9) {
            showStockHistoricalData(handler, io);
        }
    }

    private void showStockHistoricalData(Iu handler, IO io) throws IOException {

        Portfolio masterPortfolio = handler.getMasterPortfolio();
        String header = MyUtils.createHeader("Stock price change (%)");
        String stockSym;

        //TODO: add option to choose time period

        io.println("Enter stock symbol: ");
        stockSym = io.getln().toUpperCase();
        io.println(header.substring(0, header.length() - 1));

        try {
            Stock stock = masterPortfolio.getStock(stockSym);

            io.println("\nOne month    : " + stock.getChange1Month() * 100 + "\n" +
                    "Three months : " + stock.getChange3Month() * 100 + "\n" +
                    "One Year     : " + stock.getChange1Year() * 100 + "\n");
        } catch (Exception e) {
           io.println("Stock info not available.");
        }
    }
}
