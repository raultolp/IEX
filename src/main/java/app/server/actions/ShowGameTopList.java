package app.server.actions;

//View all portfolios progress

import app.server.CommandHandler;
import app.server.Iu;
import app.server.MyUtils;
import app.server.User;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

//Top list, based on portfolio total value:

public class ShowGameTopList implements CommandHandler {

    @Override
    public void handle(Integer command, Iu handler) throws IOException {
        if (command == 11) {
            showGameTopList(handler);
        }
    }

    public void showGameTopList(Iu handler) throws IOException {
        List<User> userList = handler.getUserList();
        for (User user : userList) {
            user.setPortfolioTotalValue();
        }

        Collections.sort(userList);

        boolean isAdmin = handler.isAdmin();
        DataOutputStream out = handler.getOut();

        if (isAdmin) {
            System.out.println("\nTOP LIST:\n");
            System.out.println(MyUtils.createHeader(String.format("%-5s%-15s%16s%30s",
                    "RANK", "USER", "PORTFOLIO VALUE", "INCREASE IN VALUE")));
        } else {
            out.writeUTF("\nTOP LIST:" + MyUtils.createHeader(String.format("%-5s%-15s%16s%30s",
                    "RANK", "USER", "PORTFOLIO VALUE", "INCREASE IN VALUE")));  //kas toimib?
        }

        for (int i = 1; i < userList.size() + 1; i++) {
            User user = userList.get(i - 1);

            if (isAdmin) {
                System.out.println(String.format("%4d. %-12s %13.2f USD %13.2f USD (%8.3f%%)",
                        i, user.getUserName(),
                        user.getPortfolioTotalValue(),
                        user.getValueIncrease(),
                        user.getPercentageIncrease()));
            } else {
                out.writeUTF(String.format("%4d. %-12s %13.2f USD %13.2f USD (%8.3f%%)",
                        i, user.getUserName(),
                        user.getPortfolioTotalValue(),
                        user.getValueIncrease(),
                        user.getPercentageIncrease())); //kas toimib?
            }
        }
    }
}
