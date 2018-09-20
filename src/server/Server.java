package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TreeMap;

import game.GameController;

public class Server {
    private static final int PORT_NUMBER = 9898;
    private static TreeMap<String, GameController> gameControllerArray = new TreeMap<>();

    public static void main(String[] args) {
        log("The JavAmoba server is running!");
        ServerSocket listener = null;

        try {
            listener = new ServerSocket(PORT_NUMBER);
            while (true) {
                new ClientThread(listener.accept()).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                listener.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void log(String str) {
        System.out.println(str);
    }

    private static class ClientThread extends Thread {
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;

        private static int connectionsNumber = 0;

        private final int NEWGAME_NUMBER = 1;
        private final int OLDGAME_NUMBER = 2;
        private final int EXIT_NUMBER = 9;

        public ClientThread(Socket socket) {
            try {
                this.socket = socket;
                connectionsNumber++;
                this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
                this.out = new PrintWriter(this.socket.getOutputStream(), true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            String answer = null;
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar cal = Calendar.getInstance();
            log("New client connected! " + dateFormat.format(cal.getTime()) + " " + this.socket);
            log("Connections: " + connectionsNumber);

            int inputNumber = mainMenu();

            switch (inputNumber) {
                case NEWGAME_NUMBER: // Start new game
                    String uuid = createNewGame();
                    connectOldGame(uuid);
                    break;

                case OLDGAME_NUMBER: // Connect to an old game
                    connectOldGame();
                    break;

                case EXIT_NUMBER: // Quit
                    break;
            }

            try {
                cal = Calendar.getInstance();
                log("Client disconnected! " + dateFormat.format(cal.getTime()) + " " + this.socket);
                this.socket.close();
                connectionsNumber--;
                log("Connections: " + connectionsNumber);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private int mainMenu() {
            while (true) {
                String errorMessage = null;
                int inputNumber = 0;

                // elküld 5 sor szöveget a kliensnek
                String message = "Hello gamer!" + "\n";
                message += this.NEWGAME_NUMBER + " - Start new game" + "\n";
                message += this.OLDGAME_NUMBER + " - Connect to an old game" + "\n";
                message += this.EXIT_NUMBER + " - Quit" + "\n" + "\n";
                this.out.println(message);

                try {
                    String input = this.in.readLine();
                    inputNumber = Integer.parseInt(input);

                    switch (inputNumber) {
                        case NEWGAME_NUMBER: // Start new game
                            return inputNumber;

                        case OLDGAME_NUMBER: // Connect to an old game
                            return inputNumber;

                        case EXIT_NUMBER: // Quit
                            return inputNumber;

                        default:
                            errorMessage = "nvn" + "Not a valid number";
                            break;
                    }
                } catch (NumberFormatException e) {
                    errorMessage = "ean" + "Enter a valid number!";
                } catch (IOException e) {
                    e.printStackTrace();
                }
                this.out.println(errorMessage);
            }
        }

        private String createNewGame() {
            String uuid = null;

            try {
                GameController gc = new GameController();
                uuid = gc.getUuid().toString();
                gameControllerArray.put(uuid, gc);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return uuid;
        }

        private void connectOldGame() {
            try {
                String uuid;

                do {
                    this.out.println("Enter a valid game uuid, please:");
                } while (!gameControllerArray.containsKey(uuid = this.in.readLine()));

                connectOldGame(uuid);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void connectOldGame(String uuid) {
            GameController gc = gameControllerArray.get(uuid);
            Play play = (Play) getThreadByName(gc.getUuid().toString());
            if (play != null) {
                // van üres gamer hely?
            } else {
                new Play(gc).start();
            }
        }

        private Thread getThreadByName(String threadName) {
            for (Thread t : Thread.getAllStackTraces().keySet()) {
                if (t.getName().equals(threadName)) {
                    return t;
                }
            }
            return null;
        }

    }

    private class Play extends Thread {
        GameController gameController;
        Socket gamer1;
        Socket gamer2;

        public Play(GameController gameController) {
            super(gameController.getUuid().toString());
        }

        @Override
        public void run() {
        }
    }

}
