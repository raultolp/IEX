package app.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import static app.server.MyUtils.colorPrintGreen;
import static app.server.MyUtils.createHeader;
import static app.server.StaticData.*;


class Server {

    private final Socket socket;
    private InputStream in;
    private OutputStream out;

    private Server(Socket socket) throws IOException {
        this.socket = socket;
        this.in = socket.getInputStream();
        this.out = socket.getOutputStream();
    }


    public static void main(String[] args) throws Exception {
        SystemSettings systemSettings = new SystemSettings(args);

        //START SERVER
        try ( ServerSocket ss = new ServerSocket(systemSettings.getServerPort()) ) {
            colorPrintGreen("Starting IEX game server ...");
            System.out.println("Command line start options: server [-port=port_number]");
            System.out.println("Server address: " +systemSettings.getServerHost() + ":" + systemSettings.getServerPort() + "\n");

            //START IU FOR SERVER (ADMIN)
            MyUtils.colorPrintBlue(createHeader(mainTitle));
            MyUtils.colorPrintYellow(subTitle);

            System.out.println("RUNNING GAME AS ADMIN: For running an existing game, first load the game, then choose to accept\n" +
                    "client connections. For running a new game, just choose to accept client connections.\n" +
                    "You can also at any time use any of the other Menu items. Before actions taken as any of the users\n" +
                    "(viewing a user's portfolio, buying/selling stocks as a user, etc.), you must first activate that user\n" +
                    "by choosing the Menu item 'Set active user'.");

            IO io = new AdminIO();
            Iu masterHandler = new Iu(io); //for creating Iu (handlers, masterportfolio) for admin

            //RUN IEX DATA COLLECTOR AS THREAD
            Thread dataCollector = new Thread(new UpdatingPrices(masterHandler));
            dataCollector.start();

            //RUN ADMIN AS THREAD

            Thread adminThread = new Thread(() -> {
                try {
                    masterHandler.runInteractive(new AdminIO());
                } catch (Exception ignored) {
                    //TODO?
                }
            });

            adminThread.start();

            //USER THREADS:
            List<Thread> commandthreads = new ArrayList<>();
            List<Thread> updateThreads = new ArrayList<>();

            //}

            int clientId = 0;  //clientId is assigned when client connects (at that point, its identity
            // as user is not verified yet), and communicated to the user. After user has verified
            // his identity and a handler has been created for him, the user's update thread is attached
            // to this user for receiveing price updates.
            try {
                while (true) {
                    Server socket = new Server(ss.accept());  //Listens for a connection to be made to this socket and accepts it.
                    Thread t = new Thread(new ThreadForClientCommands(socket, masterHandler, clientId));
                    Thread t2 = new Thread(new ThreadForDataUpdates(socket, masterHandler, clientId, io));
                    commandthreads.add(t);
                    updateThreads.add(t2);
                    masterHandler.addClientUpdateThread(clientId, t2);
                    t.start();
                    t2.start();
                    clientId++;

                    // QUIT PROGRAM
                    if (!masterHandler.isRunning()) {
                        dataCollector.interrupt();
                        if (adminThread.isAlive()) {
                            adminThread.interrupt();

                        }
                        System.out.println(ANSI_YELLOW + "Bye-bye!" + ANSI_RESET);
                        break;
                    }
                }

            } catch (IOException e) {
                for (Thread thread : updateThreads) {
                    thread.interrupt(); //join?
                }
                for (Thread thread : commandthreads) {
                    thread.interrupt();
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



