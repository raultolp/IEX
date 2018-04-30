package app.actions;

import app.*;

//View stock historical data

public class ShowStockHistoricalData implements CommandHandler {

    @Override
    public void handle(Integer command, Iu handler) {
        if (command == 13) {
            showStockHistoricalData(handler);
        }
    }

    private void showStockHistoricalData(Iu handler) {

        Portfolio masterPortfolio = handler.getMasterPortfolio();
        String header = MyUtils.createHeader("Stock price change (%)");

        //TODO: add option to choose time period

        handler.getSc().nextLine();
        System.out.println("Enter stock symbol: ");
        String stockSym = handler.getSc().nextLine().toUpperCase();

        System.out.println(header.substring(0, header.length() - 1));

        try {
            Stock stock = masterPortfolio.getStock(stockSym);
            System.out.printf("One month    : %+7.2f\nThree months : %+7.2f\nOne Year     : %+7.2f\n",
                    stock.getChange1Month() * 100,
                    stock.getChange3Month() * 100,
                    stock.getChange1Year() * 100);
        } catch (Exception e) {
            System.out.println("Stock info not available.");
        }
    }

}
