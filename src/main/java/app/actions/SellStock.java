package app.actions;

import app.CommandHandler;
import app.Iu;
import app.MyUtils;
import app.Portfolio;

//Sell stock

public class SellStock implements CommandHandler {

    @Override
    public void handle(Integer command, Iu handler) throws Exception {
        if (command == 6) {
            Portfolio portfolio = handler.getActiveUser().getPortfolio();
            if (portfolio != null)
                portfolio.toString();
            String name = MyUtils.enterStockName(handler.getSc());

            if (portfolio.getPortfolioStocks().keySet().contains(name)) {
                Integer qty = MyUtils.enterQty(handler.getSc());
                portfolio.sellStock(name, qty);
            } else {
                MyUtils.colorPrintYellow("Stock not included in portfolio!");
            }
        }
    }
}
