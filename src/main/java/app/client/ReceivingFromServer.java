package app.client;

import java.io.DataInputStream;
import java.io.IOException;

class ReceivingFromServer implements Runnable {

    private final Client client;
    private final DataInputStream in;
    private final String[] menu = {
            "Buy stock",
            "Sell stock",
            "View user portfolio",
            "View user transactions report",
            "View available stock list",
            "View stock list base data",
            "View company data",
            "View stock base data",
            "View stock historical data",
            "View stock news",
            "View game Top List",
            "Delete user",
            "Quit"
    };

    //TODO
    //boolean isRunning=true;
    //Portfolio masterPortfolio;
    //Portfolio userPortfolio;


    public ReceivingFromServer(DataInputStream in, Client client) {
        this.in = in;
        this.client = client;
    }

    @Override
    public void run() {


        while (true) {
            try {
                if (!client.isRunning()) {
                    return;
                }
                String receivedString = in.readUTF();
                //System.out.println("received " + receivedString);

                //Quitting:
                if (receivedString.endsWith("Quitting...")) {
                    System.out.println(receivedString);
                    client.setRunning(false);
                    break; //thread stops

                    //Printing menu:
                } else if (receivedString.trim().equals("MENU")) {
                    printMenu();

                    //Receiving JSON:
                    // All updates can be distinguished from other data flow by charAt(0)='{'
                    // (marks beginning of json string).
//                } else if (receivedString.charAt(0) == '{') {
//                    String[] splitted = receivedString.split("@");
//                    String userAsString = splitted[0];
//                    String masterAsString = splitted[1];

                    //Initiating masterPortfolio and user portfolio at the beginning of the game:
                   /* if (!initiated) {
                        //System.out.println("MASTER: "+masterAsString);
                        //System.out.println("USER"+userAsString);
                        masterPortfolio = new Gson().fromJson(masterAsString, Portfolio.class);
                        //System.out.println(masterPortfolio.getStock("AAPL").getCurrentPrice());   // ok
                        userPortfolio = new Gson().fromJson(masterAsString, Portfolio.class);
                        initiated = true;
                        System.out.println("INITIATED PORTFOLIO.");

                        //Updating master and user portfolio:
                    } else {
                        //AJUTINE!!!
                        masterPortfolio = new Gson().fromJson(masterAsString, Portfolio.class);
                        //System.out.println(masterPortfolio.getStock("AAPL").getCurrentPrice());   // ok
                        userPortfolio = new Gson().fromJson(masterAsString, Portfolio.class);
                        //app.client.UpdatingPrices.updatePrices(masterPortfolio, userPortfolio, masterAsString, userAsString);
                    }*/
                } else {
                    System.out.println(receivedString);
                }
            } catch (IOException e) {
                //return;
                client.setRunning(false);
            }
        }
    }

    private void printMenu() {
        System.out.println();
        for (int i = 0; i < menu.length / 2 + menu.length % 2; i++) {
            int next = menu.length / 2 + i + menu.length % 2;
            System.out.printf("%2d) %-30s ", i + 1, menu[i]);
            if (next < menu.length)
                System.out.printf("%2d) %-30s\n", next + 1, menu[next]);
            else
                System.out.println();
        }
    }


/*    private void updateMaster(String receivedJSONString) {  //price update for masterPortfolio
        JsonObject pricesObj=new Gson().fromJson();
    }

    private void updateUser(String receivedJSONString) {
        userPortfolio = new Gson().fromJson(receivedJSONString, Portfolio.class);
    }*/


}
