package client.cli;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class JavAmobaCli {

    public static void main(String[] args) throws Exception {
        String serverAddress = "localhost";
        int serverPort = 9898;

        Socket socket = new Socket(serverAddress, serverPort);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        // beolvassa az első 5 sort, amit a szerver küld
        for (int i = 0; i < 5; i++) {
            System.out.println(in.readLine());
        }

        Scanner scanner = new Scanner(System.in);

        while (true) {
            String whatToDo = scanner.nextLine();
            out.println(whatToDo);
            String massage = in.readLine();
            System.out.println(massage.substring(3));

            String code = massage.substring(0, 3);
            if (code.equals("sng")) {
                startNewGame();
            } else if (code.equals("cog")) {
                connectOldGame();
            } else if (code.equals("nvn")) {
                System.out.println("Olvass, ecsém!");
            } else if (code.equals("ean")) {
                System.out.println("Írjá mán egy számot, he!");
            } else if (code.equals("bbb")) {
                quit();
            }
        }

        // GameController gameController = new GameController();
        // Game game = gameController.getGame();
        // while (true) {
        // System.out.print("\033[H\033[2J");
        // System.out.flush();
        // System.out.println(game.getBoardView());
        // ;
        // System.out.println("\nKövetkező: " + game.getNextPlayerChar());
        // System.out.print("Sor: ");
        // int row = scanner.nextInt();
        // System.out.print("Oszlop: ");
        // int column = scanner.nextInt();
        // gameController.setCell(row, column, game.getNextPlayer());
        // }

        // scanner.close();
        // socket.close();
    }

    private static void startNewGame() {
        System.out.println("Új játék indul!");
    }

    private static void connectOldGame() {
        System.out.println("Kapcsolódás futó játékhoz");
    }

    private static void quit() {
        System.out.println("Viszlát legközelebb!");
        System.exit(0);
    }

}
