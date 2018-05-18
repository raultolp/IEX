package app.server;

public interface CommandHandler {
    void handle(Integer command, Iu handler, IO io) throws Exception;
}

