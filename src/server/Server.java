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
        private static int connectionsNumber = 0;

        public ClientThread(Socket socket) {
            this.socket = socket;
            connectionsNumber++;
        }

        public void run() {
            final int EXIT_NUMBER = 9;
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar cal;
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

                cal = Calendar.getInstance();
                log("New client connected! " + dateFormat.format(cal.getTime()) + " " + socket);
                log("Connections: " + connectionsNumber);

                // elküld 5 sor szöveget a kliensnek
                out.println("Hello gamer!");
                out.println("1 - Start new game");
                out.println("2 - Connect to an old game");
                out.println(EXIT_NUMBER + " - Quit");
                out.println("");

                while (true) {
                    String input = in.readLine();
                    String answer = null;
                    int inputNumber = 0;
                    String uuid = null;

                    try {
                        inputNumber = Integer.parseInt(input);

                        switch (inputNumber) {
                            case 1: // Start new game
                                answer = "sng" + "Start new game";
                                uuid = createNewGame();
                                connectOldGame(uuid);
                                break;

                            case 2: // Connect to an old game
                                answer = "cog" + "Connect to an old game" + "\nPlease enter the UUID of game:";
                                uuid = in.readLine();
                                connectOldGame(uuid);
                                break;

                            case EXIT_NUMBER: // Quit
                                answer = "bbb" + "Bye bye!";
                                break;

                            default:
                                answer = "nvn" + "Not a valid number";
                                break;
                        }
                    } catch (NumberFormatException e) {
                        answer = "ean" + "Enter a number!";
                    }
                    out.println(answer);

                    if (inputNumber == EXIT_NUMBER) {
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    cal = Calendar.getInstance();
                    log("Client disconnected! " + dateFormat.format(cal.getTime()) + " " + socket);
                    socket.close();
                    connectionsNumber--;
                    log("Connections: " + connectionsNumber);
                } catch (IOException e) {
                    e.printStackTrace();
                }
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

        private void connectOldGame(String uuid) {
            log(uuid);
        }

    }

}
