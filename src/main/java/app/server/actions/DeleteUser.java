package app.server.actions;

import app.server.CommandHandler;
import app.server.Iu;
import app.server.MyUtils;

//Delete user

public class DeleteUser implements CommandHandler {

    @Override
    public void handle(Integer command, Iu handler) throws Exception {
        boolean isAdmin = handler.isAdmin();
        if (command == 12) {

            if (isAdmin) {
                handler.getSc().nextLine();
            }
            String name = MyUtils.enterUserName(handler, false);
            Integer index = MyUtils.nameInList(name, handler.getUserList());
            if (index > -1) {
                handler.getUserList().remove(index); //TODO ???

                if (isAdmin) {
                    MyUtils.colorPrintYellow("User " + name + " has been deleted.");
                } else {
                    handler.getOut().writeUTF("User " + name + " has been deleted.");
                }

            }
        }
    }
}