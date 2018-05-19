package app.server;

import java.io.IOException;


public interface IO {
    void println(String message) throws IOException;

    void print(String message) throws IOException;

    String getln() throws IOException;

     void close() throws IOException;
}
