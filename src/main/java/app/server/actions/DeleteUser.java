package app.server.actions;

import app.server.CommandHandler;
import app.server.IO;
import app.server.Iu;
import app.server.MyUtils;

//Delete user

public class DeleteUser implements CommandHandler {

    @Override
    public void handle(Integer command, Iu handler, IO io) throws Exception {
        boolean isAdmin = handler.isAdmin();
        if (command == 12) {
            String name = MyUtils.enterUserName(handler, false, io);
            Integer index = MyUtils.nameInList(name, handler.getUserList());
            if (index > -1 && (isAdmin || handler.getActiveUser().getUserName().equals(name))) {
                handler.getUserList().remove(handler.getUserList().get(index)); //TODO ???

                handler.setActiveUser(handler.getAdmin());

                io.println("User " + name + " has been deleted.");
            }
        }
    }
}