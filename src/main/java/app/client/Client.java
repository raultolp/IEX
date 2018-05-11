package app.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;


public class Client {

    private boolean isRunning = true;
    Client client;

    public Client() {
        this.client = this;
    }

    public static void main(String[] args) throws IOException, InterruptedException {

        System.out.println("connecting to server");
        Client client = new Client();

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
        }
        System.out.println("See you soon!");
        System.exit(0);
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }
}
