package app.server.actions;

import app.server.*;

import static app.server.StaticData.ANSI_RESET;
import static app.server.StaticData.ANSI_YELLOW;
import static app.server.actions.ShowUsersList.showUsersList;

//Set activeUser user

public class SetActiveUser implements CommandHandler {

    @Override
    public void handle(Integer command, Iu handler, IO io) throws Exception {
        if (command == 18) {
            boolean isAdmin = handler.isAdmin();
            if (isAdmin) {
                showUsersList(handler, io);

                String name = MyUtils.enterUserName(handler, false, io);
                Integer index = MyUtils.nameInList(name, handler.getUserList());

                if (index > -1) {
                    User activeUser = handler.getUserList().get(index);
                    handler.setActiveUser(activeUser);
                    io.println(ANSI_YELLOW + "User " + name + " is now active." + ANSI_RESET);
                }
            } else {
               io.println("Wrong input.");
            }
        }
    }
}
