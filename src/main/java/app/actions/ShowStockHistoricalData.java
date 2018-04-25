package app.actions;

import app.CommandHandler;
import app.Iu;
import app.Portfolio;
import app.Stock;

//View stock historical data

public class ShowStockHistoricalData implements CommandHandler {

    @Override
    public void handle(Integer command, Iu handler) {
        if (command == 13) {
            showStockHistoricalData(handler);
        }
    }

    private void showStockHistoricalData(Iu handler) {

        Portfolio masterPortfolio= handler.getMasterPortfolio();

        //TODO: add option to choose time period

        handler.getSc().nextLine();
        System.out.println("Enter stock symbol: ");
        String stockSym = handler.getSc().nextLine();

        try {
            Stock stock = masterPortfolio.getStock(stockSym);
            System.out.println("One month: " + stock.getChange1Month() + '\n' +
                    "Three months: " + stock.getChange3Month() + '\n' +
                    "Year: " + stock.getChange1Year());
        } catch (Exception e) {
            System.out.println("Stock info not available.");
        }
    }

}
