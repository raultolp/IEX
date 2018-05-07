package app.client.sampleFX;

import app.server.User;

import java.util.List;

public class ProcessingMaster implements Runnable {

    int timeToSleep = 8900; // in milliseconds

    @Override
    public void run() {

        List<User> userList; //= handler.getUserList();

/*        while (true) {
            Portfolio masterPortfolio= handler.getMasterPortfolio();

            JsonObject masterAsJson=masterPortfolio.covertToJson();
            String masterAsString=masterAsJson.toString();
            JsonParser jp =new JsonParser();
            JsonObject masterBackToJson=jp.parse(masterAsString).getAsJsonObject(); //from String to json



            try {
                Thread.sleep(timeToSleep);
            } catch (InterruptedException e) {
                //System.out.println("PRICE UPDATE STOPPED");;
                return;  //thread is terminated
            }
        }*/
    }
}
