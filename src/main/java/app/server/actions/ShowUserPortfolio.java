package app.server.actions;

import app.server.CommandHandler;
import app.server.IO;
import app.server.Iu;
import app.server.User;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

//View active user's portfolio - showUserPortfolio

public class ShowUserPortfolio implements CommandHandler {

    @Override
    public void handle(Integer command, Iu handler, IO io) throws IOException {
        if (command == 3) {
            showUserPortfolio(handler, io);
        }
    }

    private static void showUserPortfolio(Iu handler, IO io) throws IOException {
        User user = handler.getActiveUser();
        String username = user.getUserName();

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

        io.println(userPortf);

    }

}
