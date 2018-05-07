package app.server.actions;

import app.server.CommandHandler;
import app.server.Company;
import app.server.Iu;

//View company base data

public class ShowCompanyData implements CommandHandler {

    @Override
    public void handle(Integer command, Iu handler) throws Exception {
        if (command == 7) {
            showCompanyData(handler);
        }
    }

    private void showCompanyData(Iu handler) throws Exception {
        //Portfolio masterPortfolio = handler.getMasterPortfolio();
        String stockSym;
        boolean isAdmin = handler.isAdmin();

        if (isAdmin) {
            handler.getSc().nextLine();
            System.out.println("Enter stock symbol: ");
            stockSym = handler.getSc().nextLine();
        } else {
            handler.getOut().writeUTF("Enter stock symbol: ");
            stockSym = handler.getIn().readUTF();
        }

        //company base data:
        Company comp = new Company(stockSym, handler);

        if (isAdmin) {
            System.out.println(comp);
        } else {
            handler.getOut().writeUTF(comp.toString());
        }


    }

}
