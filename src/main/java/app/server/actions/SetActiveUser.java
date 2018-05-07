package app.server.actions;

import app.server.CommandHandler;
import app.server.Iu;
import app.server.MyUtils;
import app.server.User;

import static app.server.StaticData.ANSI_RESET;
import static app.server.StaticData.ANSI_YELLOW;
import static app.server.actions.ShowUsersList.showUsersList;

//Set activeUser user

public class SetActiveUser implements CommandHandler {

    @Override
    public void handle(Integer command, Iu handler) throws Exception {
        if (command == 17) {
            boolean isAdmin = handler.isAdmin();
            if (isAdmin) {
                showUsersList(handler);

                handler.getSc().nextLine();

                String name = MyUtils.enterUserName(handler, false);
                Integer index = MyUtils.nameInList(name, handler.getUserList());

                if (index > -1) {
                    User activeUser = handler.getUserList().get(index);
                    handler.setActiveUser(activeUser);
                    System.out.println(ANSI_YELLOW + "User " + name + " is now active." + ANSI_RESET);
                }
            } else {
                handler.getOut().writeUTF("Wrong input.");
            }
        }
    }
}
