package app.actions;

import app.CommandHandler;
import app.Iu;
import app.MyUtils;
import app.User;

import java.util.ArrayList;
import java.util.List;

//Add user

public class AddUser implements CommandHandler {

    @Override
    public void handle(Integer command, Iu handler) throws Exception {
        List<User> newUserList = new ArrayList<>();

        if (command == 1) {
            handler.getSc().nextLine();
            String name = MyUtils.enterUserName(handler);
            if (name != null) {
                newUserList = handler.getUserList();
                newUserList.add(new User(name, 100000, handler));
                MyUtils.colorPrintYellow("Created user: " + name);
            }
            handler.setUserList(newUserList);
        }
    }


}