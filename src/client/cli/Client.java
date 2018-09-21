package client.cli;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

import game.Game;
import server.Codes;

public class Client extends Thread {
    private final String SERVERADDRESS = "localhost";
    private final int SERVERPORT = 9898;

    private Socket socket;
    // private BufferedReader in;
    // private PrintWriter out;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private Scanner scanner;

    private static int player = 0;

    public Client() throws UnknownHostException, IOException {
        this.socket = new Socket(SERVERADDRESS, SERVERPORT);
        // this.in = new BufferedReader(new
        // InputStreamReader(this.socket.getInputStream()));
        // this.out = new PrintWriter(this.socket.getOutputStream(), true);
        this.oos = new ObjectOutputStream(socket.getOutputStream());
        this.ois = new ObjectInputStream(socket.getInputStream());
        this.scanner = new Scanner(System.in);
    }

    public void run() {
        System.out.println("1 - New game");
        System.out.println("2 - Connect game with ID");
        System.out.println("9 - Exit");
        int whatToDo = this.scanner.nextInt();
        try {
            switch (whatToDo) {
                case 1:
                    System.out.println("Player (1,2): ");
                    player = this.scanner.nextInt();
                    this.oos.writeObject(Codes.NEWGAME + player);

                    try {
                        String uuid = (String) this.ois.readObject();
                        System.out.println(uuid);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }

                    startGame();
                    break;
                case 2:
                    System.out.println("ID: ");
                    String uuid = this.scanner.next();
                    System.out.println("Player (1,2): ");
                    player = this.scanner.nextInt();
                    this.oos.writeObject(Codes.OLDGAME + player + uuid);
                    startGame();
                    break;
                case 9:
                    this.oos.writeObject(Codes.EXIT);
                    try {
                        this.socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.exit(0);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                this.socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void startGame() throws Exception {
        while (true) {
            String input = (String) this.ois.readObject();
            String code = null;
            String message = null;

            if (input.length() >= 8) {
                code = input.substring(0, 8);
                if (input.length() > 8) {
                    message = input.substring(8);
                }
            }

            switch (code) {
                case Codes.BOARD:
                    ArrayList<Integer[]> board = (ArrayList<Integer[]>) this.ois.readObject();
                    printBoard(board);
                    break;
                case Codes.YOUPLAY:
                    System.out.println("You play! row,column");
                    String odds = this.scanner.nextLine();
                    this.oos.writeObject(odds);
                    break;
                case Codes.OTHERPLAYER:
                    // System.out.println("Not you play!");
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
            Thread.sleep(100);
        }
    }

    /**
     * Kirajzolja a játéktáblát standard kimenetre.
     */
    private void printBoard(ArrayList<Integer[]> boardArrayList) {
        String retString = "";

        // 1. sor, oszlopszámok
        retString += "   ";
        for (int j = 0; j < boardArrayList.get(0).length; j++) {
            retString += String.format("|%2d ", j);
        }
        retString += "|\n";

        // 2.sor
        retString += "   ";
        for (int j = 0; j < boardArrayList.get(0).length; j++) {
            retString += "|–––";
        }
        retString += "|\n";

        // tábla
        for (int i = 0; i < boardArrayList.size(); i++) {
            retString += String.format("%3d|", i);    // sorszám az elején
            for (int j = 0; j < boardArrayList.get(i).length; j++) {
                char printChar = '-';
                switch (boardArrayList.get(i)[j]) {
                    case 0:
                        printChar = ' ';
                        break;
                    case 1:
                        printChar = Game.FIRSTPLAYERCHAR;
                        break;
                    case 2:
                        printChar = Game.SECONDPLAYERCHAR;
                        break;
                }
                retString += " " + printChar + " |";   // bogyók a táblán
            }
            retString += i + "\n";  // sorszám a végén

            // sorelválasztók
            retString += "   ";
            for (int j = 0; j < boardArrayList.get(i).length; j++) {
                retString += "|–––";
            }
            retString += "|\n";
        }

        // utolsó sor, oszlopszámok
        retString += "   ";
        for (int j = 0; j < boardArrayList.get(0).length; j++) {
            retString += String.format("|%2d ", j);
        }
        retString += "|\n";

        System.out.println(retString);
    }

}
