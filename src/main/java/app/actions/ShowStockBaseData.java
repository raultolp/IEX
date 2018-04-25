package app.actions;

import app.*;

//View stock base data

public class ShowStockBaseData implements CommandHandler {

    @Override
    public void handle(Integer command, Iu handler) {
        if (command == 11) {
            showStockBaseData(handler);
        }
    }

    private void showStockBaseData(Iu handler) {
        Portfolio masterPortfolio = handler.getMasterPortfolio();

        handler.getSc().nextLine();
        System.out.println("Enter stock symbol: ");
        String stockSym = handler.getSc().nextLine();

        try {
            //company base data:
            Company comp = new Company(stockSym);
            System.out.println(comp);

            //stock fundamentals:
            System.out.println(masterPortfolio.getStock(stockSym));


        } catch (Exception e) {
            MyUtils.colorPrintYellow("Stock information not available.");
        }
    }

}
