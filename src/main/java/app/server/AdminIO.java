package app.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
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
    public Scanner getSc() {
        return sc;
    }

    @Override
    public DataOutputStream getOut() {
        return null;
    }

    @Override
    public DataInputStream getIn() {
        return null;
    }
}
