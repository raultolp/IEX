package app.server;

public class SystemSettings {
    private String serverHost = "localhost";
    int serverPort = 1337;

    public SystemSettings(String[] args) {
        if (args.length > 0) {
            for (String arg : args) {
                if (arg.startsWith("-host=")) {
                    try {
                        this.serverHost = arg.substring(arg.indexOf("=") + 1).split(":")[0];
                        this.serverPort = Integer.parseInt(arg.split(":" + 1)[1]);
                    } catch (Exception e) {
                        System.out.println("Wrong host parameter, use: -host=server_name:port_number");
                    }
                } else if (arg.startsWith("-port=")) {
                    try {
                        this.serverPort = Integer.parseInt(arg.split("=" + 1)[1]);
                    } catch (Exception e) {
                        System.out.println("Wrong port parameter, use: -port=port_number");
                    }
                }
            }
        }
    }

    public String getServerHost() {
        return serverHost;
    }

    public int getServerPort() {
        return serverPort;
    }
}
