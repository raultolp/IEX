package app.actions;

import app.CommandHandler;
import app.Iu;
import app.MyUtils;
import app.User;

import static app.StaticData.ANSI_RESET;
import static app.StaticData.ANSI_YELLOW;
import static app.actions.ShowUsersList.showUsersList;

//Set activeUser user

public class SetActiveUser implements CommandHandler {

    @Override
    public void handle(Integer command, Iu handler) throws Exception {
        if (command == 4) {
            showUsersList(handler.getUserList());
            handler.getSc().nextLine();
            String name = MyUtils.enterUserName(handler);
            Integer index = MyUtils.nameInList(name, handler.getUserList());
            if (index > -1) {
                User activeUser = handler.getUserList().get(index);
                handler.setActiveUser(activeUser);
                System.out.println(ANSI_YELLOW + "User " + name + " is now active." + ANSI_RESET);
            }
        }
    }
}
