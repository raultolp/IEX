package app.server.actions;

import app.server.CommandHandler;
import app.server.IO;
import app.server.Iu;
import app.server.Portfolio;

import java.io.IOException;

//View stock base data

public class ShowStockBaseData implements CommandHandler {

    @Override
    public void handle(String command, Iu handler, IO io) throws IOException {
        if (command.equals("8")) {
            showStockBaseData(handler, io);
        }
    }

    private void showStockBaseData(Iu handler, IO io) throws IOException {
        Portfolio masterPortfolio = handler.getMasterPortfolio();
        String stockSym;


        //TODO ?
       /* if (isAdmin) {
            handler.getSc().nextLine();
            System.out.println("Enter stock symbol: ");
            stockSym = handler.getSc().nextLine().toUpperCase();
        } else {*/
            io.print("Enter stock symbol: ");
            stockSym = io.getln().toUpperCase();


        try {
            //stock fundamentals:
            io.println(masterPortfolio.getStock(stockSym).toString());
        } catch (Exception e) {
            io.println("Stock information not available.");
        }
    }
}

