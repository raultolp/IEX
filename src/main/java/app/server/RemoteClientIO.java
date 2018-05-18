package app.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Scanner;

public class RemoteClientIO implements IO {
    private final DataOutputStream out;
    private final DataInputStream in;

    public RemoteClientIO(DataOutputStream out, DataInputStream in) {
        this.out = out;
        this.in = in;
    }

    @Override
    public void println(String message) throws IOException {
        out.writeUTF(message);
    }

    @Override
    public String getln() throws IOException {
        return in.readUTF();
    }

    public DataOutputStream getOut() {
        return out;
    }

    public DataInputStream getIn() {
        return in;
    }

    public Scanner getSc() {
        return null;
    }
}
