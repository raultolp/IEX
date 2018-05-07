package app.server.actions;

import app.server.CommandHandler;
import app.server.Iu;
import app.server.User;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

//View active user's portfolio - showUserPortfolio

public class ShowUserPortfolio implements CommandHandler {

    @Override
    public void handle(Integer command, Iu handler) throws IOException {
        if (command == 3) {
            showUserPortfolio(handler);
        }
    }

    private static void showUserPortfolio(Iu handler) throws IOException {
        User user = handler.getActiveUser();
        String username = user.getUserName();
        boolean isAdmin = handler.isAdmin();
        DataOutputStream out = handler.getOut();

        //Info from Portfolio:
        String userPortf = "\n" + username.toUpperCase() + "'s PORTFOLIO: \n";
        userPortf += user.getPortfolio().toString();

        //Info from User:
        String portValueIncrease = String.format("%.2f", user.getValueIncrease());
        String portfValueIncreasePercent = String.format("%.4f", user.getPercentageIncrease());
        userPortf += "\nPORTFOLIO'S TOTAL INCREASE IN VALUE (REALIZED + UNREALIZED PROFIT): " +
                portValueIncrease + " USD (" + portfValueIncreasePercent + "%)\n";

        //Getting user ranking in TopList:
        List<User> userList = handler.getUserList();
        Collections.sort(userList);
        int ranking = userList.indexOf(user) + 1;
        userPortf += username + "'s ranking in game: " + ranking + "\n";

        if (isAdmin) {
            System.out.println(userPortf);
        } else {
            out.writeUTF(userPortf);
        }

    }

}
