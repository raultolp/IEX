package app.actions;

import app.CommandHandler;
import app.Iu;
import app.Company;
import app.Portfolio;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import static app.Iu.getActiveUser;
import static app.actions.BuyStock.enterStockName;
import static app.actions.ShowStockList.showStockList;
import static app.staticData.ANSI_RED;
import static app.staticData.ANSI_RESET;

// News on the company (last 10 news items)

public class ShowStockNews implements CommandHandler {

    @Override
    public void handle(Integer command, Scanner sc) throws Exception {
        if (command == 13) {
            showStockList();
            String name = enterStockName(sc);

            if (Arrays.asList(Iu.getAvailableStocks()).contains(name)) {
                Company company = new Company(name);
                ArrayList<String> news = company.getCompanyNews();
                for (String n : news) {
                    System.out.println(n);
                }

            } else
                System.out.println(ANSI_RED + "This stock is not available." + ANSI_RESET);

        }
    }
}
