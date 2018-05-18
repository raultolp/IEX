package app;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class ClientStreams {
    DataOutputStream dos;
    DataInputStream dis;

    public ClientStreams(DataOutputStream dos, DataInputStream dis) {
        this.dos = dos;
        this.dis = dis;
    }

    public DataOutputStream getDos() {
        return dos;
    }

    public DataInputStream getDis() {
        return dis;
    }
}
