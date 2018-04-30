package app.actions;

//View all portfolios progress

import app.CommandHandler;
import app.Iu;
import app.MyUtils;
import app.User;

import java.util.Collections;
import java.util.List;

//Top list, based on portfolio total value:

public class ShowGameTopList implements CommandHandler {

    @Override
    public void handle(Integer command, Iu handler) {
        if (command == 15) {
            ShowPortfoliosProgress(handler);
        }
    }

    public void ShowPortfoliosProgress(Iu handler) {
        List<User> userList = handler.getUserList();
        for (User user : userList) {
            user.setPortfolioTotalValue();
        }

        Collections.sort(userList);

        System.out.println("\nTOP LIST:");
        System.out.println(MyUtils.createHeader(String.format("%-5s%-15s%16s%30s",
                "RANK", "USER", "PORTFOLIO VALUE", "INCREASE IN VALUE")));

        for (int i = 1; i < userList.size() + 1; i++) {
            User user = userList.get(i - 1);

            System.out.println(String.format("%4d. %-12s %13.2f USD %13.2f USD (%8.3f%%)",
                    i, user.getUserName(),
                    user.getPortfolioTotalValue(),
                    user.getValueIncrease(),
                    user.getPercentageIncrease()));
        }
    }
}
