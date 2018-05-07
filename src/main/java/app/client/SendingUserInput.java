package app.client;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Scanner;

public class SendingUserInput extends Client implements Runnable {

    DataOutputStream out;
    Scanner sc;

    SendingUserInput(DataOutputStream out) {
        this.out = out;
        this.sc = new Scanner(System.in, "UTF-8");
    }

    @Override
    public void run() {

        try {
            while (true) {  //When the other thread (receiving from Server) receives keyword "Quiting...",
                //it's 'isRunning' becomes 'false'- which is also the signal for the other thread to stop.
                if (!super.isRunning()) {
                    break;
                }
                String userInput = sc.nextLine();
                try {
                    out.writeUTF(userInput);
                } catch (IOException e) {
                    throw new RuntimeException();
                }
            }
            return;
        } finally {
            sc.close();
            return;
        }

    }
}
