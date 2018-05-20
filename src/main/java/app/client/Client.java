package app.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import static app.server.MyUtils.colorPrintRed;
import static app.server.MyUtils.colorPrintYellow;
import static app.server.MyUtils.createHeader;
import static app.server.StaticData.serverHost;
import static app.server.StaticData.serverPort;

class Client {

    private boolean isRunning = true;
    private DataOutputStream out;
    private DataInputStream in;


    private Client() {
        Client client = this;
    }

    public static void main(String[] args) throws InterruptedException {
        Client client = new Client();
        System.out.println("connecting to server: <" + serverHost + ":" + serverPort + ">");

        try ( Socket socket = new Socket(serverHost, serverPort) ) {

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

        colorPrintYellow("See you soon!"); // TODO PRIORITY 0 .. numbri asemel teksti sisestades ei tule mitte Wrong input vaid See ya ja server panges
        System.exit(0);
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }
}
