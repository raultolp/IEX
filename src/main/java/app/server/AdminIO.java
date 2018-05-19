package app.server;

import java.util.Scanner;

public class AdminIO implements IO {
    private final Scanner sc = new Scanner(System.in);

    @Override
    public void println(String message) {
        System.out.println(message);
    }

    @Override
    public String getln() {
        return sc.nextLine();
    }

    @Override
    public void close(){
        sc.close();
    }
}
