package app.server.actions;

//View all portfolios progress

import app.server.*;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

//Top list, based on portfolio total value:

public class ShowGameTopList implements CommandHandler {

    @Override
    public void handle(Integer command, Iu handler, IO io) throws IOException {
        if (command == 11) {
            showGameTopList(handler, io);
        }
    }

    public void showGameTopList(Iu handler, IO io) throws IOException {
        List<User> userList = handler.getUserList();
        for (User user : userList) {
            user.setPortfolioTotalValue();
        }

        Collections.sort(userList);

        io.println("\nTOP LIST:" + MyUtils.createHeader(String.format("%-5s%-15s%16s%30s",
                "RANK", "USER", "PORTFOLIO VALUE", "INCREASE IN VALUE")));  //kas toimib?

        for (int i = 1; i < userList.size() + 1; i++) {
            User user = userList.get(i - 1);

            io.println(String.format("%4d. %-12s %13.2f USD %13.2f USD (%8.3f%%)",
                    i, user.getUserName(),
                    user.getPortfolioTotalValue(),
                    user.getValueIncrease(),
                    user.getPercentageIncrease()));
        }
    }
}