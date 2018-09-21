package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import game.GameController;

public class ClientThread extends Thread {
    private Socket socket;
    // private BufferedReader in;
    // private PrintWriter out;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;

    private String uuid;
    private GameController gameController = null;
    private int player;

    private static int connectionsNumber = 0;

    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Calendar cal;

    public ClientThread(Socket socket) throws IOException {
        this.socket = socket;
        connectionsNumber++;
        // this.in = new BufferedReader(new
        // InputStreamReader(this.socket.getInputStream()));
        // this.out = new PrintWriter(this.socket.getOutputStream(), true);
        this.oos = new ObjectOutputStream(socket.getOutputStream());
        this.ois = new ObjectInputStream(socket.getInputStream());
    }

    @Override
    public void run() {
        this.cal = Calendar.getInstance();
        Logger.log("New client connected! " + this.dateFormat.format(this.cal.getTime()) + " " + this.socket);
        Logger.log("Connections: " + connectionsNumber);

        try {
            String input = (String) this.ois.readObject();
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
                    this.oos.writeObject("New game ID: " + this.uuid);
                    startGame();
                    break;

                case Codes.OLDGAME:  // Connect to an old game
                    startGame();
                    break;

                case Codes.EXIT:     // Quit
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                this.cal = Calendar.getInstance();
                Logger.log("Client disconnected! " + this.dateFormat.format(this.cal.getTime()) + " " + this.socket);
                this.socket.close();
                connectionsNumber--;
                Logger.log("Connections: " + connectionsNumber);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private void startGame() throws Exception {
        this.gameController = PlayController.getOldGame(this.uuid);

        if (this.gameController == null) {
            this.oos.writeObject(Codes.INVALIDID);
            return;
        }

        while (!gameController.getGame().hasWinnerPlayer()) {
            sendBoard();
            if (this.gameController.getGame().getNextPlayer() == this.player) {
                this.oos.writeObject(Codes.YOUPLAY);
                String odds = (String) this.ois.readObject();
                if (odds.length() > 2) {
                    String[] oddsParts = odds.split(",");
                    int row = Integer.parseInt(oddsParts[0]);
                    int column = Integer.parseInt(oddsParts[1]);
                    this.gameController.setCell(row, column, this.player);
                }
            } else {
                while (this.gameController.getGame().getNextPlayer() != this.player && !gameController.getGame().hasWinnerPlayer()) {
                    this.oos.writeObject(Codes.OTHERPLAYER);
                    Thread.sleep(100);
                }
            }
            Thread.sleep(100);
        }

        if (gameController.getGame().hasWinnerPlayer()) {
            sendBoard();
            PlayController.delGame(this.uuid);
            if (gameController.getGame().getWinnerPlayer() == this.player) {
                this.oos.writeObject(Codes.YOUWIN);
            } else {
                this.oos.writeObject(Codes.OTHERWIN);
            }
        }
    }

    private void sendBoard() throws Exception {
        int[][] boardArray = gameController.getGame().getBoard();
        ArrayList<Integer[]> board = new ArrayList<>();
        for (int[] x : boardArray) {
            Integer[] intArr = Arrays.stream(x).boxed().toArray(Integer[]::new);
            board.add(intArr);
        }
        this.oos.writeObject(Codes.BOARD);
        this.oos.writeObject(board);
    }

}
