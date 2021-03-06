package app.server.actions;

import app.server.*;

import java.io.IOException;

import static app.server.MyUtils.textRed;


//View stock historical data

public class ShowStockHistoricalData implements CommandHandler {

    @Override
    public void handle(String command, Iu handler, IO io) throws IOException {
        if (command.equals("9")) {
            showStockHistoricalData(handler, io);
        }
    }

    private void showStockHistoricalData(Iu handler, IO io) throws IOException {

        Portfolio masterPortfolio = handler.getMasterPortfolio();
        String header = MyUtils.createHeader("Stock price change");
        String stockSym;

        //TODO: add option to choose time period

        io.print("Enter stock symbol: ");
        stockSym = io.getln().toUpperCase();
        io.println(header.substring(0, header.length() - 1));

        try {
            Stock stock = masterPortfolio.getStock(stockSym);

            io.println(String.format("\nOne month    : %5.2f %%\nThree months : %5.2f %%\nOne Year     : %5.2f %%\n",
                    stock.getChange1Month(),
                    stock.getChange3Month(),
                    stock.getChange1Year()));
            io.println(MyUtils.externalLinks(stockSym));
        } catch (Exception e) {
           io.println(textRed("Stock info not available."));
        }
    }
}
