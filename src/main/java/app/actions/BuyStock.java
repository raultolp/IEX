package app.actions;

import app.CommandHandler;
import app.Iu;
import app.MyUtils;
import app.Portfolio;

import java.util.Arrays;

import static app.actions.ShowStockList.showStockList;

//Buy stock

public class BuyStock implements CommandHandler {

    @Override
    public void handle(Integer command, Iu handler) {
        if (command == 5) {
            showStockList(handler);

//            sc.nextLine();
            String name = MyUtils.enterStockName(handler.getSc());

            if (Arrays.asList(handler.getAvailableStocks()).contains(name)) {
                Integer qty = MyUtils.enterQty(handler.getSc());
                Portfolio portfolio = handler.getActiveUser().getPortfolio();

                try {
                    portfolio.buyStock(name, qty);
                } catch (RuntimeException e) { //if not enough funds
                    MyUtils.colorPrintYellow("Not enough funds!");
                }
            } else
                MyUtils.colorPrintRed("This stock is not available.");
        }
    }
}
