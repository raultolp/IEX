package app.server.actions;

import app.server.CommandHandler;
import app.server.Iu;
import app.server.MyUtils;
import app.server.Portfolio;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

//View stock base data

public class ShowStockBaseData implements CommandHandler {

    @Override
    public void handle(Integer command, Iu handler) throws IOException {
        if (command == 8) {
            showStockBaseData(handler);
        }
    }

    private void showStockBaseData(Iu handler) throws IOException {
        Portfolio masterPortfolio = handler.getMasterPortfolio();
        boolean isAdmin = handler.isAdmin();
        DataInputStream in = handler.getIn();
        DataOutputStream out = handler.getOut();
        String stockSym;

        if (isAdmin) {
            handler.getSc().nextLine();
            System.out.println("Enter stock symbol: ");
            stockSym = handler.getSc().nextLine().toUpperCase();
        } else {
            out.writeUTF("Enter stock symbol: ");
            stockSym = in.readUTF().toUpperCase();
        }

        try {
            //stock fundamentals:
            if (isAdmin) {
                System.out.println(masterPortfolio.getStock(stockSym));
            } else {
                out.writeUTF(masterPortfolio.getStock(stockSym).toString());
            }

        } catch (Exception e) {
            if (isAdmin) {
                MyUtils.colorPrintYellow("Stock information not available.");
            } else {
                out.writeUTF("Stock information not available.");
            }


        }
    }

}
