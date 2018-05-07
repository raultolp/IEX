package app.server.actions;

import app.server.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;


//View stock historical data

public class ShowStockHistoricalData implements CommandHandler {

    @Override
    public void handle(Integer command, Iu handler) throws IOException {
        if (command == 9) {
            showStockHistoricalData(handler);
        }
    }

    private void showStockHistoricalData(Iu handler) throws IOException {

        Portfolio masterPortfolio = handler.getMasterPortfolio();
        String header = MyUtils.createHeader("Stock price change (%)");
        boolean isAdmin = handler.isAdmin();
        DataInputStream in = handler.getIn();
        DataOutputStream out = handler.getOut();
        String stockSym;

        //TODO: add option to choose time period

        if (isAdmin) {
            handler.getSc().nextLine();
            System.out.println("Enter stock symbol: ");
            stockSym = handler.getSc().nextLine().toUpperCase();
            System.out.println(header.substring(0, header.length() - 1));
        } else {
            out.writeUTF("Enter stock symbol: ");
            stockSym = in.readUTF().toUpperCase();
            out.writeUTF(header.substring(0, header.length() - 1));  //kas toimib?
        }

        try {
            Stock stock = masterPortfolio.getStock(stockSym);

            if (isAdmin) {
                System.out.printf("One month    : %+7.2f\nThree months : %+7.2f\nOne Year     : %+7.2f\n",
                        stock.getChange1Month() * 100,
                        stock.getChange3Month() * 100,
                        stock.getChange1Year() * 100);
            } else {
                out.writeUTF("\nOne month    : " + stock.getChange1Month() * 100 + "\n" +
                        "Three months : " + stock.getChange3Month() * 100 + "\n" +
                        "One Year     : " + stock.getChange1Year() * 100 + "\n");
            }

        } catch (Exception e) {
            if (isAdmin) {
                System.out.println("Stock info not available.");
            } else {
                out.writeUTF("Stock info not available.");
            }

        }
    }

}
