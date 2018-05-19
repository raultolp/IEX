package app.server;

public interface CommandHandler {
    void handle(String command, Iu handler, IO io) throws Exception;
}

