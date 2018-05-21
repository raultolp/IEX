package app.server.actions;

import app.server.*;

import java.io.IOException;
import java.util.Arrays;

import static app.server.MyUtils.textRed;
import static app.server.actions.ShowStockList.showStockList;

//Buy stock

public class BuyStock implements CommandHandler {

    @Override
    public void handle(String command, Iu handler, IO io) throws IOException {
        if (command.equals("1")) {
            boolean isAdmin = handler.isAdmin();
            showStockList(handler, io, true);
            String name = MyUtils.enterStockName(io);

            if (Arrays.asList(handler.getAvailableStocks()).contains(name)) {
                Integer qty = MyUtils.enterQty(io);
                Portfolio portfolio;
                if (isAdmin) {
                    portfolio = handler.getActiveUser().getPortfolio();
                } else {
                    int userIndex = handler.getUserList().indexOf(handler.getActiveUser());
                    portfolio = handler.getUserList().get(userIndex).getPortfolio();
                }

                try {
                    portfolio.buyStock(name, qty, handler, io);
                } catch (RuntimeException e) { //if not enough funds
                    io.println("Exception while buying stocks.");
                }
            } else {
                io.println(textRed("This stock is not available."));
            }
        }
    }
}

