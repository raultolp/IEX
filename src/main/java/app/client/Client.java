/**
 +++ IEX Stock Exchange Game for Beginner Level Traders - Version 1.0 +++
 (C) 2018 Renata Siimon, Helena Rebane, Raul Tölp. All rights reserved.
**/

package app.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import static app.server.MyUtils.colorPrintRed;
import static app.server.MyUtils.colorPrintYellow;
import static app.server.MyUtils.createHeader;

public class Client {

    private boolean isRunning = true;
    private final Client client;
    private DataOutputStream out;
    private DataInputStream in;


    public Client() {
        this.client = this;
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        Client client = new Client();
        System.out.println("connecting to server");


        try ( Socket socket = new Socket("localhost", 1337) ) {

            try ( DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                  DataInputStream in = new DataInputStream(socket.getInputStream()) ) {

                //SENDING CLIENT COMMANDS AND RECEIVING ANSWERS FROM SERVER:
                Thread.sleep(1000);
                Thread thread1 = new Thread(new ReceivingFromServer(in, client));
                Thread thread2 = new Thread(new SendingUserInput(out, client));
                thread1.start();
                thread2.start();

                while (true) {
                    if (!thread2.isAlive() && thread1.isAlive()) {
                        //thread1.join();
                        thread1.interrupt();
                        break;
                    } else if (!thread1.isAlive() && thread2.isAlive()) {
                        //thread2.join();
                        thread2.interrupt();
                        break;
                    } else if (!thread2.isAlive()) {
                        //thread1.join();
                        //thread2.join();
                        thread1.interrupt();
                        thread2.interrupt();
                        break;
                    }
                }
            }
        } catch (IOException e) {
            colorPrintRed(createHeader("Server connection failed!"));
        }


        colorPrintYellow("See you soon!");
        System.exit(0);
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }
}
