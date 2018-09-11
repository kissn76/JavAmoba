
public class Game {
    private int nextPlayer; // ő rakja a következőt (1,2)
    private int[][] board;
    private int winNumber;
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

    public int getNextPlayer() {
        return nextPlayer;
    }

    public char getFIRSTPLAYERCHAR() {
        return FIRSTPLAYERCHAR;
    }

    public char getSECONDPLAYERCHAR() {
        return SECONDPLAYERCHAR;
    }

    /**
     * @param  row
     * @param  column
     * @param  player
     * @return           Ha van győztes, akkor a száma, egyébként 0.
     * @throws Exception
     */
    public int setCell(int row, int column, int player) throws Exception {
        if (this.winnerPlayer == 0) {
            if (player == this.nextPlayer) {
                if (row >= 0 && row < this.board.length && column >= 0 && column < this.board[0].length) {
                    if (this.board[row][column] == 0) {
                        this.board[row][column] = player;
                        System.out.println(player + ": " + row + "," + column);
                        win(row, column);
                        if (this.winnerPlayer > 0) {
                            System.out.println("Winner: " + winnerPlayer);
                            return winnerPlayer;
                        }
                        nextPlayer();
                    } else {
                        throw new Exception("A cella már nem üres!");
                    }
                } else {
                    throw new Exception("A sor vagy az oszlop értéke nem megfelelő!");
                }
            } else {
                throw new Exception("Nem te jössz!");
            }
        } else {
            throw new Exception("A játék véget ért, van már nyertes!");
        }
        return 0;
    }

    public void printBoard() {
        if (this.winnerPlayer > 0) {
            System.out.println("A nyertes játékos: " + this.winnerPlayer);
        }

        // 1. sor, oszlopszámok
        System.out.print("   ");
        for (int j = 0; j < board[0].length; j++) {
            System.out.format("|%2d ", j);
        }
        System.out.println("|");

        // 2.sor
        System.out.print("   ");
        for (int j = 0; j < board[0].length; j++) {
            System.out.print("|–––");
        }
        System.out.println("|");

        // tábla
        for (int i = 0; i < this.board.length; i++) {
            System.out.format("%3d|", i);    // sorszám az elején
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
                System.out.print(" " + printChar + " |");   // bogyók a táblán
            }
            System.out.println(i);  // sorszám a végén

            // sorelválasztók
            System.out.print("   ");
            for (int j = 0; j < board[i].length; j++) {
                System.out.print("|–––");
            }
            System.out.println("|");
        }

        // utolsó sor, oszlopszámok
        System.out.print("   ");
        for (int j = 0; j < board[0].length; j++) {
            System.out.format("|%2d ", j);
        }
        System.out.println("|");
    }

    private void nextPlayer() {
        if (this.nextPlayer == 1) {
            this.nextPlayer = 2;
        } else {
            this.nextPlayer = 1;
        }
    }

    /**
     * Annak ellenőrzése, hogy van-e már nyertes.
     */
    private int win(int row, int column) {
        // Az utoljára lerakott ponttól kiindulva megszámoljuk, hogy hány ugyanolyan
        // pont van jobbra-balra, le-föl, balfönt-jobblent-átlósan,
        // ballent-jobbfönt-átlósan

        final int actWinElement = this.board[row][column];

        if (actWinElement > 0) {

            // jobbra-balra
            int counterHorizental = 1;

            {   // elemtől balra
                if (column > 0) {
                    int actColumnIndex = column - 1;
                    int actElement = this.board[row][actColumnIndex];
                    while (actColumnIndex >= 0 && actElement == actWinElement) {
                        counterHorizental++;
                        actColumnIndex--;
                        actElement = actColumnIndex >= 0 ? this.board[row][actColumnIndex] : 0;
                    }
                }
            }

            {   // elemtől jobbra
                if (column < board[row].length - 1) {
                    int actColumnIndex = column + 1;
                    int actElement = this.board[row][actColumnIndex];
                    while (actColumnIndex < board[row].length && actElement == actWinElement) {
                        counterHorizental++;
                        actColumnIndex++;
                        actElement = actColumnIndex < board[row].length ? this.board[row][actColumnIndex] : 0;
                    }
                }
            }

            if (counterHorizental >= this.winNumber) {
                this.winnerPlayer = actWinElement;
                return actWinElement;
            }

            // fel-le, átlósan
            int counterVertical = 1;        // egyenesel fentről le
            int counterDiagonalTop = 1;     // bal fentről jobbra le
            int counterDiagonalBottom = 1;  // bal lentről jobbra fel

            { // elemtől fel
                if (row > 0) {
                    int actRowIndex = row - 1;
                    int actColumnIndexLeft = column - 1;    // fel-balra
                    int actColumnIndexTop = column;         // fel egyenesen
                    int actColumnIndexRight = column + 1;   // fel-jobbra
                    int actElementLeft = actRowIndex >= 0 && actColumnIndexLeft >= 0 ? board[actRowIndex][actColumnIndexLeft] : 0;
                    int actElementTop = board[actRowIndex][actColumnIndexTop];
                    int actElementRight = actRowIndex >= 0 && actColumnIndexRight < board[actRowIndex].length ? board[actRowIndex][actColumnIndexRight] : 0;
                    while (actRowIndex >= 0 && (actElementLeft == actWinElement || actElementTop == actWinElement || actElementRight == actWinElement)) {
                        actRowIndex--;
                        if (actElementLeft == actWinElement) {
                            counterDiagonalTop++;
                            actColumnIndexLeft--;
                            actElementLeft = actRowIndex >= 0 && actColumnIndexLeft >= 0 ? board[actRowIndex][actColumnIndexLeft] : 0;
                        }
                        if (actElementTop == actWinElement) {
                            counterVertical++;
                            actElementTop = actRowIndex >= 0 ? board[actRowIndex][actColumnIndexTop] : 0;
                        }
                        if (actElementRight == actWinElement) {
                            counterDiagonalBottom++;
                            actColumnIndexRight++;
                            actElementRight = actRowIndex >= 0 && actColumnIndexRight < board[actRowIndex].length ? board[actRowIndex][actColumnIndexRight] : 0;
                        }
                    }
                }
            }

            {   // elemtől le
                if (row < board.length - 1) {
                    int actRowIndex = row + 1;
                    int actColumnIndexLeft = column - 1;    // le-balra
                    int actColumnIndexTop = column;         // le egyenesen
                    int actColumnIndexRight = column + 1;   // le-jobbra
                    int actElementLeft = actRowIndex < board.length && actColumnIndexLeft >= 0 ? board[actRowIndex][actColumnIndexLeft] : 0;
                    int actElementTop = board[actRowIndex][actColumnIndexTop];
                    int actElementRight = actRowIndex < board.length && actColumnIndexRight < board[actRowIndex].length ? board[actRowIndex][actColumnIndexRight] : 0;
                    while (actRowIndex < board.length && (actElementLeft == actWinElement || actElementTop == actWinElement || actElementRight == actWinElement)) {
                        actRowIndex++;
                        if (actElementLeft == actWinElement) {
                            counterDiagonalBottom++;
                            actColumnIndexLeft--;
                            actElementLeft = actRowIndex < board.length && actColumnIndexLeft >= 0 ? board[actRowIndex][actColumnIndexLeft] : 0;
                        }
                        if (actElementTop == actWinElement) {
                            counterVertical++;
                            actElementTop = actRowIndex < board.length ? board[actRowIndex][actColumnIndexTop] : 0;
                        }
                        if (actElementRight == actWinElement) {
                            counterDiagonalTop++;
                            actColumnIndexRight++;
                            actElementRight = actRowIndex < board.length && actColumnIndexRight < board[actRowIndex].length ? board[actRowIndex][actColumnIndexRight] : 0;
                        }
                    }
                }
            }

            if (counterVertical >= this.winNumber || counterDiagonalTop >= this.winNumber || counterDiagonalBottom >= this.winNumber) {
                this.winnerPlayer = actWinElement;
                return actWinElement;
            }
        }
        return 0;

    }
}
