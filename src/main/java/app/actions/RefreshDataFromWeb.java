package app.actions;

import app.CommandHandler;
import app.Portfolio;
import app.Stock;

import java.util.Scanner;

import static app.Iu.getActiveUser;
import static app.Iu.getStockMap;

//Refresh data from web (refreshes stock prices in stockMap)

public class RefreshDataFromWeb implements CommandHandler {

    @Override
    public void handle(Integer command, Scanner sc) {
        if (command == 13) {
            for (String symbol : getStockMap().keySet()) {
                Stock stock = getStockMap().get(symbol);
                double price = stock.getLatestPrice();
                stock.setCurrentPrice(price);
            }
            Portfolio portf = getActiveUser().getPortfolio();
            portf.updatePrices();
        }
    }
}
