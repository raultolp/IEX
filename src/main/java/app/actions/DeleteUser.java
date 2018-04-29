package app.actions;

import app.CommandHandler;
import app.Iu;
import app.MyUtils;

//Delete user

public class DeleteUser implements CommandHandler {

    @Override
    public void handle(Integer command, Iu handler) throws Exception {
        if (command == 2) {
            handler.getSc().nextLine();
            String name = MyUtils.enterUserName(handler, false);
            Integer index = MyUtils.nameInList(name, handler.getUserList());
            if (index > -1) {
                handler.getUserList().remove(index); //TODO ???
                MyUtils.colorPrintYellow("User " + name + " has been deleted.");
            }
        }
    }
}