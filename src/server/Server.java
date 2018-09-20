package server;

import java.io.IOException;
import java.net.ServerSocket;

public class Server {
    private static final int PORT_NUMBER = 9898;
    ServerSocket listener = null;

    public Server() {
        Logger.log("The JavAmoba server is running!");

        try {
            this.listener = new ServerSocket(PORT_NUMBER);
            while (true) {
                new ClientThread(this.listener.accept()).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                this.listener.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
