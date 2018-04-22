package app;

import java.util.Scanner;

public interface CommandHandler {
    void handle(Integer command, Scanner sc) throws Exception;
//    void handle(Integer command) throws Exception;
}

