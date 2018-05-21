package app.server.actions;

import app.server.*;

import static app.server.MyUtils.textRed;
import static app.server.MyUtils.textYellow;
import static app.server.actions.ShowUsersList.showUsersList;

//Set activeUser user

public class SetActiveUser implements CommandHandler {

    @Override
    public void handle(String command, Iu handler, IO io) throws Exception {
        if (command.equals("19")) {
            boolean isAdmin = handler.isAdmin();
            if (isAdmin) {
                showUsersList(handler, io);

                String name = MyUtils.enterUserName(handler, false, io);
                Integer index = MyUtils.nameInList(name, handler.getUserList());

                if (index > -1) {
                    User activeUser = handler.getUserList().get(index);
                    handler.setActiveUser(activeUser);
                    io.println(textYellow("User " + name + " is now active."));
                }
            } else {
               io.println(textRed("Wrong input."));
            }
        }
    }
}
