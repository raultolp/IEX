package app;

import java.util.List;

public class UpdatingPrices implements Runnable {
    private Iu handler;
    private Portfolio masterPortfolio;
    private List<User> userList;

    public UpdatingPrices(Iu handler) {
        this.handler = handler;
        this.masterPortfolio = handler.getMasterPortfolio();
        this.userList = handler.getUserList();
    }

    @Override
    public void run() {
        int timeToSleep = 10000; // in milliseconds

        //TODO: check if ok (vb olla peaks portfliost vastava meetodi hoopis siia tõstma
        //Downloading price information from IEX at regular time intervals:
        while (true) {
            masterPortfolio.updatePrices(userList);
            //System.out.println("ok");  //for testing
            try {
                Thread.sleep(timeToSleep);
            } catch (InterruptedException e) {
                //System.out.println("PRICE UPDATE STOPPED");;
                return;  //thread is terminated
            }
        }
    }
}


