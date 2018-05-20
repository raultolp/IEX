package app.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

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
    public void print(String message) throws IOException {
        out.writeUTF(message);
    }

    @Override
    public String getln() throws IOException {
        return in.readUTF();
    }

    @Override
    public void close() throws IOException {
        out.close();
        in.close();
    }
}
