package app.client;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Scanner;

public class SendingUserInput implements Runnable {

    DataOutputStream out;
    Scanner sc;
    Client client;

    SendingUserInput(DataOutputStream out, Client client) {
        this.out = out;
        this.sc = new Scanner(System.in, "UTF-8");
        this.client = client;
    }

    @Override
    public void run() {
        try {
            while (client.isRunning()) {  //When the other thread (receiving from Server) receives keyword "Quitting...",
                //it's 'isRunning' becomes 'false'- which is also the signal for the other thread to stop.
                String userInput = sc.nextLine();
                try {
                    out.writeUTF(userInput);
                } catch (IOException e) {
                    throw new RuntimeException();
                }
            }

        } finally {
            sc.close();
        }

    }
}
