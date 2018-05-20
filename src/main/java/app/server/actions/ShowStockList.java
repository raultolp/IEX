package app.server.actions;

import app.server.*;

import java.io.IOException;
import java.util.Arrays;

import static app.server.MyUtils.createHeader;
import static app.server.MyUtils.textError;
import static app.server.StaticData.ANSI_RED;
import static app.server.StaticData.ANSI_RESET;
import static app.server.StaticData.availableStocks;

//View available stock list - showStockList

public class ShowStockList implements CommandHandler {

    @Override
    public void handle(String command, Iu handler, IO io) throws IOException {
        if (command.equals("5")) {
            showStockList(handler, io);
        }
    }

    public static void showStockList(Iu handler, IO io) throws IOException {
//        boolean isAdmin = handler.isAdmin();
//        Arrays.sort(handler.getAvailableStocks());
//        int i;
//
//        if (isAdmin) {
//            System.out.println(createHeader("Avilable stocks:"));
//            for (i = 0; i < handler.getAvailableStocks().length; i++) {
//                System.out.printf("%-5s%s", handler.getAvailableStocks()[i], (i + 1) % 10 == 0 ? "\n" : " ");
//            }
//            if (i % 10 != 0)
//                System.out.println();
//        } else {
//            String stocksAsString = "Avilable stocks: " + String.join(", ", handler.getAvailableStocks());
//            io.println(stocksAsString);
//
//        }

        StringBuilder report = new StringBuilder(createHeader("All stocks available in this game:"));
        final String formatText = "%-6s %-37s %-20s %-s";

        try {
            report.append(createHeader(String.format(formatText,
                "SYMBOL", "NAME", "SECTOR", "INDUSTRY")));

            for (String stockSymbol : availableStocks) {
                Company comp = new Company(stockSymbol, handler, io);

                report.append(String.format(formatText + "\n",
                        stockSymbol,
                        comp.getCompanyName().substring(0, comp.getCompanyName().length() > 35 ?
                                35 : comp.getCompanyName().length()),
                        comp.getSector(),
                        comp.getIndustry()));
            }

            io.println(report.toString());
        } catch (Exception e) {
            io.println(textError("Stock information not available."));
        }

    }
}