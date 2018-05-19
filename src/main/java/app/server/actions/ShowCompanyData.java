package app.server.actions;

import app.server.CommandHandler;
import app.server.Company;
import app.server.IO;
import app.server.Iu;

//View company base data

public class ShowCompanyData implements CommandHandler {

    @Override
    public void handle(Integer command, Iu handler, IO io) throws Exception {
        if (command == 7) {
            showCompanyData(handler, io);
        }
    }

    private void showCompanyData(Iu handler, IO io) throws Exception {
        //Portfolio masterPortfolio = handler.getMasterPortfolio();
        String stockSym;
        //boolean isAdmin = handler.isAdmin();

        /*if (isAdmin) {
            handler.getSc().nextLine();
            System.out.println("Enter stock symbol: ");
            stockSym = handler.getSc().nextLine();
        } else {*/
        io.print("Enter stock symbol: ");
        stockSym = io.getln();

        //company base data:
        Company comp = new Company(stockSym, handler, io);

        io.println(comp.toString());

    }

}
