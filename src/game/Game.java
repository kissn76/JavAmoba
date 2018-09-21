package game;

/**
 * POJO class
 */
public class Game {
    private int boardRowNumber;
    private int boardColumnNumber;
    private int[][] board;
    private int winNumber;

    private int nextPlayer; // ő rakja a következőt (1,2)
    private int winnerPlayer;

    public static final char FIRSTPLAYERCHAR = 'X';
    public static final char SECONDPLAYERCHAR = 'O';
    private static final int MINCOLUMN = 3;
    private static final int MINROW = 3;
    private static final int MINWINNUMBER = 3;

    public Game() throws Exception {
        this(20, 20, 5, 1);
    }

    /**
     * @param  boardRow    A tábla sorainak száma.
     * @param  boardColumn A tábla oszlopainak száma.
     * @param  winNumber   Ennyi egymás utáni bábu kell a nyeréshez.
     * @param  firstPlayer A kezdő játékos száma.
     * @throws Exception
     */
    public Game(int boardRow, int boardColumn, int winNumber, int firstPlayer) throws Exception {
        if (boardColumn >= MINCOLUMN && boardRow >= MINROW) {
            if (winNumber >= MINWINNUMBER) {
                if (firstPlayer == 1 || firstPlayer == 2) {
                    boardRowNumber = boardRow;
                    boardColumnNumber = boardColumn;
                    this.board = new int[boardRow][boardColumn];
                    this.winNumber = winNumber;
                    this.nextPlayer = firstPlayer;
                } else {
                    throw new Exception("A kezdő játékos értéke 1-es vagy 2-es lehet!");
                }
            } else {
                throw new Exception("A győztes lerakások száma nem lehet kisebb mint " + MINWINNUMBER + "!");
            }
        } else {
            throw new Exception("A tábla sorainak és oszlopainak száma nem lehet kisebb mint " + MINROW + " és " + MINCOLUMN + "!");
        }
    }

    public int getWinNumber() {
        return winNumber;
    }

    public int[][] getBoard() {
        return board;
    }

    public int getBoardRowNumber() {
        return boardRowNumber;
    }

    public int getBoardColumnNumber() {
        return boardColumnNumber;
    }

    public void setBoardCell(int row, int column, int player) {
        board[row][column] = player;
    }

    public int getBoardCell(int row, int column) {
        return board[row][column];
    }

    public void setNextPlayer(int nextPlayer) {
        this.nextPlayer = nextPlayer;
    }

    public int getNextPlayer() {
        return nextPlayer;
    }

    public char getNextPlayerChar() {
        if (nextPlayer == 1) {
            return FIRSTPLAYERCHAR;
        } else {
            return SECONDPLAYERCHAR;
        }
    }

    public void setWinnerPlayer(int winnerPlayer) {
        this.winnerPlayer = winnerPlayer;
    }

    public boolean hasWinnerPlayer() {
        if (winnerPlayer != 0) {
            return true;
        } else {
            return false;
        }
    }

    public int getWinnerPlayer() {
        return winnerPlayer;
    }

    public char getWinnerPlayerChar() {
        if (winnerPlayer == 1) {
            return FIRSTPLAYERCHAR;
        } else {
            return SECONDPLAYERCHAR;
        }
    }

}
