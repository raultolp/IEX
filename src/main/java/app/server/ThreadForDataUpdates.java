package app.server;

import com.google.gson.JsonObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Map;

class ThreadForDataUpdates implements Runnable {

    //FOR UPDATING USERPOTRFOLIO (BOTH PRICES AND OTHER ELEMENTS) AND PRICES OF MASTERPORTFOLIO

    private final Server socket;
    private final Iu masterHandler;
    private final IO io; //TODO viga?
    //private boolean masterHasChanged = false; //for price updates in master portfolio (this ususally also brings along changes in user portf.)
    //private boolean userHasChanged = false; //for changes in user portfolio (also adding stocks, changes in values etc.)
    //private JsonObject updatedUserPortfolio;
    //private JsonObject priceUpdateForClients;

    public ThreadForDataUpdates(Server socket, Iu masterHandler, int clientId, IO io) {
        this.socket = socket;
        this.masterHandler = masterHandler;
        this.io = io;
    }


    @Override
    public void run() {

        Portfolio userPortfolio;
//        Portfolio masterPortfolio = masterHandler.getMasterPortfolio();

        //Getting userPortfolio (possible after User has been created for client- until then, just wait
        // and check again regularily):
        while (true) {
            Map<Thread, User> clientThreads = masterHandler.getClientThreads();
            if (clientThreads.containsKey(this)) {
                User user = clientThreads.get(this);
                userPortfolio = user.getPortfolio();
                break;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                return;
            }

        }
        try (DataOutputStream out = new DataOutputStream(socket.getOut())) {

            while (true) { //TODO cannot complete wihtout throwing exception

                //Sending updated user portfolio if it has changed (either because of
                // global price update or because of buys/sells):
                if (userPortfolio.isPortfolioChanged()) {
                    JsonObject userAsJson = userPortfolio.covertToJson();
                    userPortfolio.setPortfolioHasChanged(false);

                    //Sending masterportfolio's price updates (only stocks with prices) to client:
                    JsonObject masterPriceUpdateAsJson = masterHandler.getPriceUpdateForClients();
                    String allDataAsString = userAsJson.toString() + "@" + masterPriceUpdateAsJson.toString();
                    out.writeUTF(allDataAsString);
                }

                Thread.sleep(1000); //et päris vahetpidamata muutuste kontrollimisega ei tegeleks
            }

        } catch (InterruptedException e) {
            try {
                io.println("Server stopped...");
            } catch (IOException e1) {
                throw new RuntimeException();
            }

        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

}
