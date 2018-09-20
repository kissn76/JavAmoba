package client.cli;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import server.Codes;

public class JavAmobaCli {
    private static BufferedReader in = null;
    private static PrintWriter out = null;
    private static Scanner scanner = new Scanner(System.in);

    private static int player = 0;

    public static void main(String[] args) throws Exception {
        String serverAddress = "localhost";
        int serverPort = 9898;

        Socket socket = new Socket(serverAddress, serverPort);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

        System.out.println("1 - New game");
        System.out.println("2 - Connect game with ID");
        System.out.println("9 - Exit");
        int whatToDo = scanner.nextInt();

        switch (whatToDo) {
            case 1:
                System.out.println("Player (1,2): ");
                player = scanner.nextInt();
                out.print(Codes.NEWGAME + player);
                startGame();
                break;
            case 2:
                System.out.println("ID: ");
                String uuid = scanner.nextLine();
                System.out.println("Player (1,2): ");
                player = scanner.nextInt();
                out.print(Codes.OLDGAME + player + uuid);
                startGame();
                break;
            case 9:
                out.println(Codes.EXIT);
                socket.close();
                System.exit(0);
                break;
        }
        socket.close();
    }

    private static void startGame() {
        while (true) {
            try {
                String input = in.readLine();
                String code = input.substring(0, 8);
                String message = null;

                if (input.length() > 8) {
                    message = input.substring(8);
                }

                switch (code) {
                    case Codes.BOARD:
                        System.out.println(message);
                        break;
                    case Codes.YOUPLAY:
                        System.out.println("You play! row,column");
                        String odds = scanner.nextLine();
                        out.println(odds);
                        break;
                    case Codes.OTHERPLAYER:
                        System.out.println("Not you play!");
                        break;
                    case Codes.INVALIDID:
                        System.out.println("Invalid ID!");
                        return;
                    case Codes.YOUWIN:
                        System.out.println("You win!");
                        return;
                    case Codes.OTHERWIN:
                        System.out.println("Not you win!");
                        return;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
