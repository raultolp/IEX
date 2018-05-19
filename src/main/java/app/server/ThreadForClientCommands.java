package app.server;

import com.google.gson.Gson;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import static app.server.MyUtils.createHeader;
import static app.server.StaticData.*;

class ThreadForClientCommands implements Runnable {

    private final Server socket;
    private final Iu masterHandler;
    private final int clientId;

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

            RemoteClientIO io = new RemoteClientIO(out, in);
            List<User> users = masterHandler.getUserList();
            List<User> userList = new ArrayList<>(users);

            //LOGIN AND CREATION OF IU FOR USER:
            io.println("\n" + ANSI_YELLOW + createHeader(mainTitle) + ANSI_RESET);
            io.println(ANSI_BLUE + subTitle + ANSI_RESET);
            io.print("Username: ");
            String username = io.getln();
            User user = new User(username, 1000000);

            //Existing game, new user:
            for (User userInList : userList) {
                if (userInList.getUserName().equals(username)) {
                    user = userInList;
                    io.println(ANSI_YELLOW + "Welcome back!" + ANSI_RESET);
                    break;
                }

            }
            if (!(userList.contains(user))) {
                io.println("New user " + ANSI_GREEN + user.getUserName() + ANSI_RESET + " has been created. Welcome to the game!");

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
