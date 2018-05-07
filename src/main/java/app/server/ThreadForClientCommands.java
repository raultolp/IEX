package app.server;

import com.google.gson.Gson;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.util.List;

import static app.server.StaticData.mainTitle;

public class ThreadForClientCommands implements Runnable {

    private Server socket;
    private Iu masterHandler;
    private int clientId;

    public ThreadForClientCommands(Server socket, Iu masterHandler, int clientId) {
        this.socket = socket;
        this.masterHandler = masterHandler;
        this.clientId = clientId;
    }

    @Override
    public void run() {

        try {
            System.out.println(Thread.currentThread().getName() + " is working.");
            System.out.println("client connected.");

            DataInputStream in = new DataInputStream(socket.getIn());
            DataOutputStream out = new DataOutputStream(socket.getOut());

            //LOGIN AND CREATION OF IU FOR USER:
            out.writeUTF(mainTitle);
            File activeGame = masterHandler.getActiveGame();
            out.writeUTF("Username:");
            String username = in.readUTF();
            //System.out.println(username);
            User user = new User(username, 1000000);

            //New game, new user:
            if (activeGame == null) {
                out.writeUTF("New user has been created. Welcome to the game!");
            }

            //Existing game, existing user:
            else {
                List<User> userList = masterHandler.getUserList();
                boolean newUser = true;
                for (User userInList : userList) {
                    if (userInList.getUserName().equals(username)) {
                        user = userInList;
                        out.writeUTF("Welcome back!");
                        newUser = false;
                        break;
                    }
                }
                //Existing game, new user:
                if (newUser == true) {
                    out.writeUTF("New user has been created. Welcome to the game!");
                }
            }
            Iu handler = new Iu(in, out, masterHandler, user, clientId); //Command handler for user


            //SENDING MASTERPORTFOLIO AS SOURCE OF STOCK INFORMATION (ONLY SENT ONCE):
            Portfolio masterPortfolio = masterHandler.getMasterPortfolio();
            String masterAsString = new Gson().toJson(masterPortfolio);
            //System.out.println("SERVER: MASTERASTRING: "+masterAsString);

            //ALSO SENDING USER PORTFOLIO OBJECT:
            Portfolio userPortfolio = user.getPortfolio();
            String userAsString = new Gson().toJson(userPortfolio);
            //System.out.println("SERVER: USERASTRING: "+userAsString);

            //System.out.println("KOOS: "+masterAsString+"@"+userAsString);
            out.writeUTF(masterAsString + "@" + userAsString);


            //PLAYING THE GAME:
            try {
                handler.runInteractive(handler);
            } catch (InterruptedException e) {
                out.writeUTF("Server stopped...");
                return;
            } finally {
                in.close();
                out.close();
                socket.getSocket().close();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
