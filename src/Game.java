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

    private final char FIRSTPLAYERCHAR = 'X';
    private final char SECONDPLAYERCHAR = 'O';
    private final int MINCOLUMN = 3;
    private final int MINROW = 3;
    private final int MINWINNUMBER = 3;

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

    /**
     * Kirajzolja a játéktáblát standard kimenetre.
     */
    public String getBoardView() {
        String retString = "";
        if (this.winnerPlayer > 0) {
            retString += "A nyertes játékos: " + this.winnerPlayer + "\n";
        }

        // 1. sor, oszlopszámok
        retString += "   ";
        for (int j = 0; j < board[0].length; j++) {
            retString += String.format("|%2d ", j);
        }
        retString += "|\n";

        // 2.sor
        retString += "   ";
        for (int j = 0; j < board[0].length; j++) {
            retString += "|–––";
        }
        retString += "|\n";

        // tábla
        for (int i = 0; i < this.board.length; i++) {
            retString += String.format("%3d|", i);    // sorszám az elején
            for (int j = 0; j < this.board[i].length; j++) {
                char printChar = '-';
                switch (this.board[i][j]) {
                    case 0:
                        printChar = ' ';
                        break;
                    case 1:
                        printChar = FIRSTPLAYERCHAR;
                        break;
                    case 2:
                        printChar = SECONDPLAYERCHAR;
                        break;
                }
                retString += " " + printChar + " |";   // bogyók a táblán
            }
            retString += i + "\n";  // sorszám a végén

            // sorelválasztók
            retString += "   ";
            for (int j = 0; j < board[i].length; j++) {
                retString += "|–––";
            }
            retString += "|\n";
        }

        // utolsó sor, oszlopszámok
        retString += "   ";
        for (int j = 0; j < board[0].length; j++) {
            retString += String.format("|%2d ", j);
        }
        retString += "|\n";

        return retString;
    }

}
