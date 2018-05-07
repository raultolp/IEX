package app.server.actions;

import app.server.CommandHandler;
import app.server.Company;
import app.server.Iu;
import app.server.MyUtils;

import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.Arrays;

import static app.server.actions.ShowStockList.showStockList;

// News on the company (last 10 news items)

public class ShowStockNews implements CommandHandler {

    @Override
    public void handle(Integer command, Iu handler) throws Exception {
        if (command == 10) {
            showStockList(handler);
            String name = MyUtils.enterStockName(handler);
            boolean isAdmin = handler.isAdmin();
            DataOutputStream out = handler.getOut();

            if (Arrays.asList(handler.getAvailableStocks()).contains(name)) {
                Company company = new Company(name, handler);
                ArrayList<String> news = company.getCompanyNews(handler);

                if (isAdmin) {
                    for (String n : news) {
                        System.out.println(n);
                    }
                } else {
                    String stocknews = "";
                    for (String n : news) {
                        stocknews += n + "\n\n";
                    }
                    out.writeUTF(stocknews);
                }


            } else {
                if (isAdmin) {
                    MyUtils.colorPrintRed("This stock is not available.");
                } else {
                    out.writeUTF("This stock is not available.");
                }
            }

        }
    }
}
