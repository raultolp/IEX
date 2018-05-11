package app.server.actions;

import app.server.CommandHandler;
import app.server.Iu;

public class StopAcceptingClientConnections implements CommandHandler {
    @Override
    public void handle(Integer command, Iu handler) throws Exception {
        if (command == 15) {
            boolean isAdmin = handler.isAdmin();

            if (isAdmin) {
                if (!handler.doesAcceptConnections()) {
                    System.out.println("Server is already not accepting connections.");
                } else {
                    handler.setAcceptConnections(false);
                    System.out.println("Clients' connections are no longer accepted.");
                    System.out.println("Actually, I'm still accepting connections :( Please fix me. ");
                }

            } else {
                handler.getOut().writeUTF("Wrong input.");
            }
        }
    }
}
