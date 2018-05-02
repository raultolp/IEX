package app.actions;

import app.CommandHandler;
import app.Company;
import app.Iu;
import app.Portfolio;

//View company base data

public class ShowCompanyData implements CommandHandler {

    @Override
    public void handle(Integer command, Iu handler) throws Exception {
        if (command == 11) {
            showCompanyData(handler);
        }
    }

    private void showCompanyData(Iu handler) throws Exception {
        Portfolio masterPortfolio = handler.getMasterPortfolio();

        handler.getSc().nextLine();
        System.out.println("Enter stock symbol: ");
        String stockSym = handler.getSc().nextLine();

        //company base data:
        Company comp = new Company(stockSym);
        System.out.println(comp);

    }

}
