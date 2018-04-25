package app.actions;

import app.CommandHandler;
import app.Company;
import app.Iu;
import app.MyUtils;

import java.util.ArrayList;
import java.util.Arrays;

import static app.actions.ShowStockList.showStockList;

// News on the company (last 10 news items)

public class ShowStockNews implements CommandHandler {

    @Override
    public void handle(Integer command, Iu handler) throws Exception {
        if (command == 13) {
            showStockList(handler);
            String name = MyUtils.enterStockName(handler.getSc());

            if (Arrays.asList(handler.getAvailableStocks()).contains(name)) {
                Company company = new Company(name);
                ArrayList<String> news = company.getCompanyNews();
                for (String n : news) {
                    System.out.println(n);
                }

            } else
                MyUtils.colorPrintRed("This stock is not available.");

        }
    }
}
