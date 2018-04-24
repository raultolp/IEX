package app;

public class UpdatingPrices implements Runnable{

    Portfolio masterPortfolio= Iu.getMasterPortfolio();

    @Override
    public void run() {
        int timeToSleep=10000; // in seconds

        while(true) {
            masterPortfolio.updatePrices();
            try {
                Thread.sleep(timeToSleep);
            } catch (InterruptedException e) {
                return;  //thread is terminated
            }
        }
    }

}
