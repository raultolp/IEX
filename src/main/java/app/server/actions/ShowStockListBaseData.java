package app.server.actions;

import app.server.*;

import java.io.IOException;

import static app.server.MyUtils.createHeader;
import static app.server.MyUtils.textRed;
import static app.server.StaticData.availableStocks;

//View stock list base data

//TODO Priority 1

public class ShowStockListBaseData implements CommandHandler {

    @Override
    public void handle(String command, Iu handler, IO io) throws IOException {
        if (command.equals("6")) {
            showStockListBaseData(handler, io);
        }
    }

    private void showStockListBaseData(Iu handler, IO io) throws IOException {
        Portfolio masterPortfolio = handler.getMasterPortfolio();
        StringBuilder report = new StringBuilder(createHeader("Stocks Fundamental Data"));
        final String formatText = "%6s %7s %10s %6s %10s %11s %12s %16s %9s %8s";
        final String formatData = "%-6s %7.2f %10.2f %6.2f %10.2f %11d %12.2f %15.2f%% %8.2f%% %7.2f%%\n";
        Stock stock;

        try {
            report.append(createHeader(String.format(formatText,
                    "SYMBOL", "PRICE", "DIV.YIELD", "EPS", "P/E RATIO",
                    "MARKET CAP", "SHORT RATIO", "CHANGE: 1 MONTH", "3 MONTHS", "1 YEAR")));

            for (String stockSymb : availableStocks) {
                stock = masterPortfolio.getStock(stockSymb);
                report.append(String.format(formatData,
                        stock.getSymbol(),
                        stock.getCurrentPrice(),
                        stock.getDividendYield(),
                        stock.getEps(),
                        stock.getPeRatio(),
                        stock.getMarketCap(),
                        stock.getShortRatio(),
                        stock.getChange1Month(),
                        stock.getChange3Month(),
                        stock.getChange1Year()));
            }

            io.println(report.toString());
        } catch (Exception e) {
            io.println(textRed("Stock information not available."));
        }
    }
}

