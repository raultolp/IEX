package app.server.actions;

import app.server.CommandHandler;
import app.server.Iu;
import app.server.MyUtils;
import app.server.Portfolio;

//Sell stock

public class SellStock implements CommandHandler {

    @Override
    public void handle(Integer command, Iu handler) throws Exception {
        if (command == 2) {
            boolean isAdmin = handler.isAdmin();

            Portfolio portfolio;
            if (isAdmin) {
                portfolio = handler.getActiveUser().getPortfolio();
            } else {
                int userIndex = handler.getUserList().indexOf(handler.getActiveUser());
                portfolio = handler.getUserList().get(userIndex).getPortfolio();
            }

            if (portfolio != null)
                portfolio.toString();
            String name = MyUtils.enterStockName(handler);

            if (portfolio.getPortfolioStocks().keySet().contains(name)) {
                Integer qty = MyUtils.enterQty(handler);
                portfolio.sellStock(name, qty, handler);
            } else {
                if (isAdmin) {
                    MyUtils.colorPrintYellow("Stock not included in portfolio!");
                } else {
                    handler.getOut().writeUTF("Stock not included in portfolio!");
                }


            }
        }
    }
}
