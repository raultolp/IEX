package app.server.actions;

import app.server.CommandHandler;
import app.server.IO;
import app.server.Iu;

import java.io.IOException;

public class AcceptClientConnections implements CommandHandler {

    @Override
    public void handle(Integer command, Iu handler, IO io) throws IOException {
        if (command == 14) {
            boolean isAdmin = handler.isAdmin();

            if (isAdmin) {
                if (handler.doesAcceptConnections()) {
                    io.println("Server is already accepting connections.");
                } else {
                    handler.setAcceptConnections(true);
                    io.println("Clients' connections are now accepted");
                }

            } else {
              io.println("Wrong input.");
            }
        }
    }

}
