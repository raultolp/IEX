package app.server.actions;

import app.server.CommandHandler;
import app.server.IO;
import app.server.Iu;

import java.io.IOException;

import static app.server.StaticData.getMainMenuSize;

public class ErrorHandler implements CommandHandler {

    @Override
    public void handle(Integer command, Iu handler, IO io) throws IOException {

        if (command < 1 || command > getMainMenuSize())
            io.println("Wrong input, choose between 1.." + getMainMenuSize() + "!");
    }

}

