package app.server.actions;

import app.server.CommandHandler;
import app.server.Iu;
import app.server.MyUtils;
import app.server.Portfolio;

import java.io.IOException;
import java.util.Arrays;

import static app.server.actions.ShowStockList.showStockList;

//Buy stock

public class BuyStock implements CommandHandler {

    @Override
    public void handle(Integer command, Iu handler) throws IOException {
        if (command == 1) {
            boolean isAdmin = handler.isAdmin();
            showStockList(handler);

//            sc.nextLine();
            String name = MyUtils.enterStockName(handler);

            if (Arrays.asList(handler.getAvailableStocks()).contains(name)) {
                Integer qty = MyUtils.enterQty(handler);
                Portfolio portfolio;
                if (isAdmin) {
                    portfolio = handler.getActiveUser().getPortfolio();
                } else {
                    int userIndex = handler.getUserList().indexOf(handler.getActiveUser());
                    portfolio = handler.getUserList().get(userIndex).getPortfolio();
                }

                try {
                    portfolio.buyStock(name, qty, handler);
                } catch (RuntimeException e) { //if not enough funds
                    if (isAdmin) {
                        //MyUtils.colorPrintYellow("Not enough funds!");
                        MyUtils.colorPrintYellow("Exception while buying stocks.");
                    } else {
                        //handler.getOut().writeUTF("Not enough funds!");
                        handler.getOut().writeUTF("Exception while buying stocks.");

                    }
                }
            } else {
                if (isAdmin) {
                    MyUtils.colorPrintRed("This stock is not available.");
                } else {
                    handler.getOut().writeUTF("This stock is not available.");
                }
            }
        }
    }
}
