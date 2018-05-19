package app.server.actions;

import app.server.*;

import java.util.Objects;

//Sell stock

public class SellStock implements CommandHandler {

    @Override
    public void handle(Integer command, Iu handler, IO io) throws Exception {
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
                portfolio.toString(); //TODO result ignored?  MIKS SEDA SIIN ÜLDSE TEHAKSE? (RAUL)
            String name = MyUtils.enterStockName(handler, io);

            if (Objects.requireNonNull(portfolio).getPortfolioStocks().keySet().contains(name)) {
                Integer qty = MyUtils.enterQty(io);
                portfolio.sellStock(name, qty, handler, io);
            } else {
                io.println("Stock not included in portfolio!");
            }
        }
    }
}
