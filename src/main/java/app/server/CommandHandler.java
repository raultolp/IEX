package app.server;

public interface CommandHandler {
    void handle(Integer command, Iu handler) throws Exception;

//    void handle(Integer command) throws Exception;
}

