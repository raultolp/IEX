package app.server.actions;

import app.server.CommandHandler;
import app.server.IO;
import app.server.Iu;

import static app.server.MyUtils.textRed;

public class StopAcceptingClientConnections implements CommandHandler {
    @Override
    public void handle(String command, Iu handler, IO io) throws Exception {
        if (command.equals("16")) {
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
               io.println(textRed("Wrong input."));
            }
        }
    }
}
