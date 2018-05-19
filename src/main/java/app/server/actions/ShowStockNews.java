package app.server.actions;

import app.server.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import static app.server.actions.ShowStockList.showStockList;

// News on the company (last 10 news items)

public class ShowStockNews implements CommandHandler {

    @Override
    public void handle(String command, Iu handler, IO io) throws Exception {
        if (command.equals("10")) {
            showStockList(handler, io);
            String name = MyUtils.enterStockName(io);

            if (Arrays.asList(handler.getAvailableStocks()).contains(name)) {
                Company company = new Company(Objects.requireNonNull(name), handler, io);
                ArrayList<String> news = company.getCompanyNews(handler, io);

                StringBuilder stocknews = new StringBuilder();
                for (String n : news) {
                    stocknews.append(n).append("\n\n");
                }
                io.println(stocknews.toString());
            } else {
                io.println("This stock is not available.");
            }
        }
    }
}
