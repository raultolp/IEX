package app;

public class UpdatingPrices implements Runnable{

    Portfolio masterPortfolio= Iu.getMasterPortfolio();

    @Override
    public void run() {
        int timeToSleep=10000; // in seconds

        //Downloading price information from IEX at regular time intervals:
        while(true) {
            masterPortfolio.updatePrices();
            //System.out.println("ok");  //for testing
            try {
                Thread.sleep(timeToSleep);
            } catch (InterruptedException e) {
                return;  //thread is terminated
            }
        }
    }
}


