package app.server.actions;

import app.server.CommandHandler;
import app.server.IO;
import app.server.Iu;

import java.io.IOException;

import static app.server.MyUtils.textRed;

public class AcceptClientConnections implements CommandHandler {

    @Override
    public void handle(String command, Iu handler, IO io) throws IOException {
        if (command.equals("14")) {
            boolean isAdmin = handler.isAdmin();

            if (isAdmin) {
                if (handler.doesAcceptConnections()) {
                    io.println("Server is already accepting connections.");
                } else {
                    handler.setAcceptConnections(true);
                    io.println("Clients' connections are now accepted");
                }

            } else {
              io.println(textRed("Wrong input."));
            }
        }
    }

}
