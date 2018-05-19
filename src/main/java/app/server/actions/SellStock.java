package app.server.actions;

import app.server.*;

import java.util.Objects;

//Sell stock

public class SellStock implements CommandHandler {

    @Override
    public void handle(String command, Iu handler, IO io) throws Exception {
        if (command.equals("2")) {
            boolean isAdmin = handler.isAdmin();

            Portfolio portfolio;
            if (isAdmin) {
                portfolio = handler.getActiveUser().getPortfolio();
            } else {
                int userIndex = handler.getUserList().indexOf(handler.getActiveUser());
                portfolio = handler.getUserList().get(userIndex).getPortfolio();
            }

            String name = MyUtils.enterStockName(io);

            if (Objects.requireNonNull(portfolio).getPortfolioStocks().keySet().contains(name)) {
                Integer qty = MyUtils.enterQty(io);
                portfolio.sellStock(name, qty, handler, io);
            } else {
                io.println("Stock not included in portfolio!");
            }
        }
    }
}
