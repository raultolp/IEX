package app.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Scanner;

public interface IO {
    void println(String message) throws IOException;

    String getln() throws IOException;

     void close() throws IOException;
}
