package app.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import static app.server.StaticData.*;


public class Server {

    private Socket socket;
    private InputStream in;
    private OutputStream out;

    public Server(Socket socket) throws IOException {
        this.socket = socket;
        this.in = socket.getInputStream();
        this.out = socket.getOutputStream();
    }


    public static void main(String[] args) throws Exception {

        //START SERVER
        try ( ServerSocket ss = new ServerSocket(1337) ) {
            System.out.println("now listening on port: 1337");

            //START IU FOR SERVER (ADMIN)
            System.out.println(mainTitle);
            System.out.println("RUNNING GAME AS ADMIN: For running an existing game, first load the game, then choose to accept\n" +
                    "client connections. For running a new game, just choose to accept client connections.\n" +
                    "You can also at any time use any of the other Menu items. Before actions taken as any of the users\n" +
                    "(viewing a user's portfolio, buying/selling stocks as a user, etc.), you must first activate that user\n" +
                    "by choosing the Menu item 'Set active user'.");

            Iu masterHandler = new Iu(); //for creating Iu (handlers, masterportfolio) for admin

            //RUN IEX DATA COLLECTOR AS THREAD
            Thread dataCollector = new Thread(new UpdatingPrices(masterHandler));
            dataCollector.start();

            //RUN ADMIN AS THREAD

            Thread adminThread = new Thread(() -> {
                try {
                    masterHandler.runInteractive(masterHandler);
                } catch (InterruptedException ignored) {
                } catch (Exception e) {
                    throw new RuntimeException();
                }
            });

            adminThread.start();

            //THREAD FOR CREATING NEW THREADS FOR USERS:
            List<Thread> commandthreads = new ArrayList<>();
            List<Thread> updateThreads = new ArrayList<>();

            //}

            Thread userThreadFactory = new Thread(() -> {
                int clientId = 0;  //clientId is assigned when client connects (at that point, its identity
                // as user is not verified yet), and communicated to the user. After user has verified
                // his identity and a handler has been created for him, the user's update thread is attached
                // to this user for receiveing price updates.
                try {
                    while (true) {
                        Server socket = new Server(ss.accept());  //Listens for a connection to be made to this socket and accepts it.
                        Thread t = new Thread(new ThreadForClientCommands(socket, masterHandler, clientId));
                        Thread t2 = new Thread(new ThreadForDataUpdates(socket, masterHandler, clientId));
                        t.setPriority(Thread.MAX_PRIORITY);
                        t2.setPriority(Thread.MIN_PRIORITY);  // et üksteist ei segaks
                        commandthreads.add(t);
                        updateThreads.add(t2);
                        masterHandler.addClientUpdateThread(clientId, t2);
                        t.start();
                        t2.start();
                        clientId++;

                    }
                } catch (IOException e) {
                    for (Thread thread : updateThreads) {
                        thread.interrupt();
                        //thread.join();  // ?
                    }
                    for (Thread thread : commandthreads) {
                        thread.interrupt();
                        //thread.join();  // ?
                    }
                }

            });


            userThreadFactory.start();
            //TODO: Kuidas peatada userThreadFactory ?


            // QUIT PROGRAM
            while (true) {
                if (!masterHandler.isRunning()) {
                    dataCollector.interrupt();
                    userThreadFactory.interrupt();
                    if (adminThread.isAlive()) {
                        adminThread.interrupt();
                    }
                    System.out.println(ANSI_YELLOW + "Bye-bye!" + ANSI_RESET);
                    break;
                }
            }
        }
    }


    public InputStream getIn() {
        return in;
    }

    public OutputStream getOut() {
        return out;
    }

    public Socket getSocket() {
        return socket;
    }

}



