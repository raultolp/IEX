package app.server;

import com.google.gson.Gson;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import static app.server.StaticData.*;
import static app.server.StaticData.ANSI_GREEN;
import static app.server.StaticData.ANSI_RESET;

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
            List<User> users = masterHandler.getUserList();
            List<User> userList = new ArrayList<>(users);
            out.writeUTF("Username:");
            String username = in.readUTF();
            User user = new User(username, 1000000);
            RemoteClientIO io = new RemoteClientIO(out, in);


            //Existing game, new user:
            for (User userInList : userList) {
                if (userInList.getUserName().equals(username)) {
                    user = userInList;
                    io.println("Welcome back!");
                    break;
                }

            }
            if (!(userList.contains(user))) {
                io.println("New user has been created. Welcome to the game!");

            }
            Iu handler = new Iu(io, masterHandler, user, clientId); //Command handler for user

            //SENDING MASTERPORTFOLIO AS SOURCE OF STOCK INFORMATION (ONLY SENT ONCE):
            Portfolio masterPortfolio = masterHandler.getMasterPortfolio();
            String masterAsString = new Gson().toJson(masterPortfolio);
            //System.out.println("SERVER: MASTERASTRING: "+masterAsString);

            //ALSO SENDING USER PORTFOLIO OBJECT:
            Portfolio userPortfolio = user.getPortfolio();
            String userAsString = new Gson().toJson(userPortfolio);
            //System.out.println("SERVER: USERASTRING: "+userAsString);

            //System.out.println("KOOS: "+masterAsString+"@"+userAsString);
            io.println(masterAsString + "@" + userAsString);


            //PLAYING THE GAME:
            try ( in; out ) {
                handler.runInteractive(io);
                System.out.println(in.readUTF());
            } finally {
                socket.getSocket().close();
            }
        } catch (SocketException e1) {
            System.out.println("client disconnected.");
            System.out.print((masterHandler.getActiveGame() != null ? ANSI_BLUE + masterHandler.getActiveGame().getName() + ANSI_RESET :
                    ANSI_RED + "(not saved)" + ANSI_RESET) +
                    " / Active user:" + ANSI_GREEN + masterHandler.getActiveUser().getUserName() + ANSI_RESET + "> ");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
