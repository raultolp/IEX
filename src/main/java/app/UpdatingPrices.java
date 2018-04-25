package app;

public class UpdatingPrices implements Runnable {
    private Iu handler;
    private Portfolio masterPortfolio;

    public UpdatingPrices(Iu handler) {
        this.handler = handler;
        this.masterPortfolio = handler.getMasterPortfolio();}

    @Override
    public void run() {
        int timeToSleep = 10000; // in milliseconds

        //Downloading price information from IEX at regular time intervals:
        while (true) {
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


