package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import game.GameController;

public class ClientThread extends Thread {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private ObjectOutputStream oos;

    private String uuid;
    private GameController gameController = null;
    private int player;

    private static int connectionsNumber = 0;

    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Calendar cal;

    public ClientThread(Socket socket) throws IOException {
        this.socket = socket;
        connectionsNumber++;
        this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        this.out = new PrintWriter(this.socket.getOutputStream(), true);
        this.oos = new ObjectOutputStream(socket.getOutputStream());
    }

    @Override
    public void run() {
        this.cal = Calendar.getInstance();
        Logger.log("New client connected! " + this.dateFormat.format(this.cal.getTime()) + " " + this.socket);
        Logger.log("Connections: " + connectionsNumber);

        try {
            String input = this.in.readLine();
            String code = null;

            if (input.length() >= 8) {
                code = input.substring(0, 8);
                if (input.length() > 8) {
                    this.player = Integer.parseInt(input.substring(8, 9));
                    if (input.length() > 9) {
                        this.uuid = input.substring(9);
                    }
                }
            }

            switch (code) {
                case Codes.NEWGAME:  // Start new game
                    this.uuid = PlayController.createNewGame();
                    this.out.println("New game ID: " + this.uuid);
                    startGame();
                    break;

                case Codes.OLDGAME:  // Connect to an old game
                    startGame();
                    break;

                case Codes.EXIT:     // Quit
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                this.cal = Calendar.getInstance();
                Logger.log("Client disconnected! " + this.dateFormat.format(this.cal.getTime()) + " " + this.socket);
                this.socket.close();
                connectionsNumber--;
                Logger.log("Connections: " + connectionsNumber);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void startGame() {
        this.gameController = PlayController.getOldGame(this.uuid);

        if (this.gameController == null) {
            this.out.println(Codes.INVALIDID);
            return;
        }

        int winner = 0;
        while (true) { // winner == 0
            try {
                sendBoard();
                if (this.gameController.getGame().getNextPlayer() == this.player) {
                    this.out.println(Codes.YOUPLAY);
                    Thread.sleep(2000);
                    String odds = this.in.readLine();
                    if (odds != null && !odds.equals("") && odds.length() > 3) {
                        Logger.log(odds);
                        String[] oddsParts = odds.split(",");
                        int row = Integer.parseInt(oddsParts[0]);
                        int column = Integer.parseInt(oddsParts[1]);
                        winner = this.gameController.setCell(row, column, this.player);
                    }
                } else {
                    System.out.println("kettes");
                    while (this.gameController.getGame().getNextPlayer() != this.player) {
                        this.out.println(Codes.OTHERPLAYER);
                        Thread.sleep(2000);
                    }
                }

                if (winner != 0) {
                    sendBoard();
                    PlayController.delGame(this.uuid);
                    if (winner == this.player) {
                        this.out.println(Codes.YOUWIN);
                    } else {
                        this.out.println(Codes.OTHERWIN);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void sendBoard() {
        int[][] boardArray = gameController.getGame().getBoard();
        ArrayList<Integer[]> board = new ArrayList<>();
        for (int[] x : boardArray) {
            Integer[] intArr = Arrays.stream(x).boxed().toArray(Integer[]::new);
            board.add(intArr);
        }
        try {
            this.out.println(Codes.BOARD);
            Thread.sleep(2000);
            this.oos.writeObject(board);
            System.out.println("tábla");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

}
