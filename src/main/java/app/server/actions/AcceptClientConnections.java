package app.server.actions;

import app.server.CommandHandler;
import app.server.Iu;

import java.io.IOException;

public class AcceptClientConnections implements CommandHandler {

    @Override
    public void handle(Integer command, Iu handler) throws IOException {
        if (command == 14) {
            boolean isAdmin = handler.isAdmin();

            if (isAdmin) {
                if (handler.doesAcceptConnections()) {
                    System.out.println("Server is already accepting connections.");
                } else {
                    handler.setAcceptConnections(true);
                    System.out.println("Clients' connections are now accepted");
                }

            } else {
                handler.getOut().writeUTF("Wrong input.");
            }
        }
    }

}
