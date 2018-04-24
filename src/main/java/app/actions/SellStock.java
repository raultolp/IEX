package app.actions;

import app.CommandHandler;
import app.Iu;
import app.Portfolio;

import java.util.Scanner;

import static app.actions.BuyStock.enterQty;
import static app.actions.BuyStock.enterStockName;

//Sell stock

public class SellStock implements CommandHandler {

    @Override
    public void handle(Integer command, Scanner sc) throws Exception {
        if (command == 6) {
            Portfolio portfolio = Iu.getActiveUser().getPortfolio();
            if (portfolio != null)
                portfolio.toString();
            String name = enterStockName(sc);

            if (portfolio.getPortfolioStocks().keySet().contains(name)) {
                Integer qty = enterQty(sc);
                portfolio.sellStock(name, qty);
            } else {
                System.out.println("Stock not included in portfolio!");
            }
        }
    }
}
