package app.client;

import app.server.SystemSettings;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import static app.server.MyUtils.*;

class Client {

    private boolean isRunning = true;
//    private DataOutputStream out;
//    private DataInputStream in;


    private Client() {
//        Client client = this;
    }

    public static void main(String[] args) throws InterruptedException {
        SystemSettings systemSettings = new SystemSettings(args);
        Client client = new Client();
        colorPrintGreen("Starting IEX client ...");
        System.out.println("Command line start options: client [-host=server_name:port_number] [-port=port_number]");
        System.out.println("Connecting to server: " + systemSettings.getServerHost() + ":" + systemSettings.getServerPort() + "\n");

        try ( Socket socket = new Socket(systemSettings.getServerHost(), systemSettings.getServerPort()) ) {

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
