
public class Game {
    private int nextPlayer; // ő rakja a következőt (1,2)
    private int[][] board;
    private int winNumber;
    private int winnerPlayer;

    public Game() throws Exception {
        this(20, 20, 5, 1);
    }

    public Game(int boardWidth) throws Exception {
        this(boardWidth, boardWidth, 5, 1);
    }

    public Game(int boardWidth, int winNumber) throws Exception {
        this(boardWidth, boardWidth, winNumber, 1);
    }

    public Game(int boardWidth, int boardHeight, int winNumber) throws Exception {
        this(boardWidth, boardHeight, winNumber, 1);
    }

    /**
     * @param  boardWidth  A tábla szélessége.
     * @param  boardHeight A tábla magassága.
     * @param  winNumber   Ennyi egymás utáni bábu kell a nyeréshez.
     * @param  firstPlayer A kezdő játékos száma.
     * @throws Exception
     */
    public Game(int boardWidth, int boardHeight, int winNumber, int firstPlayer) throws Exception {
        if (boardWidth > 0 && boardHeight > 0) {
            if (winNumber > 0) {
                if (firstPlayer == 1 || firstPlayer == 2) {
                    this.board = new int[boardHeight][boardWidth];
                    this.winNumber = winNumber;
                    this.nextPlayer = firstPlayer;
                } else {
                    throw new Exception("A kezdő játékos száma 1-es vagy 2-es lehet!");
                }
            } else {
                throw new Exception("A győztes lerakások száma nem lehet kisebb mint 1!");
            }
        } else {
            throw new Exception("A tábla sorainak és oszlopainak száma nem lehet kisebb mint 1!");
        }
    }

    public void setCell(int row, int column, int player) throws Exception {
        if (this.winnerPlayer == 0) {
            if (player == this.nextPlayer) {
                if (row < this.board.length && column < this.board[0].length) {
                    if (this.board[row][column] == 0) {
                        this.board[row][column] = player;
                        win();
                        if (this.winnerPlayer > 0) {
                            return;
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
    }

    public void printBoard() {
        if (this.winnerPlayer > 0) {
            System.out.println("A nyertes játékos: " + this.winnerPlayer);
        }
        for (int i = 0; i < this.board.length; i++) {
            for (int j = 0; j < this.board[i].length; j++) {
                char printChar = '-';
                switch (this.board[i][j]) {
                    case 1:
                        printChar = 'X';
                        break;
                    case 2:
                        printChar = 'O';
                        break;
                }
                System.out.print(printChar + " ");
            }
            System.out.println();
        }
    }

    private void nextPlayer() {
        if (this.nextPlayer == 1) {
            this.nextPlayer = 2;
        } else {
            this.nextPlayer = 1;
        }
    }

    private void win() {
        // sorok ellenőrzése
        for (int i = 0; i < this.board.length; i++) {
            int actWinElement = this.board[i][0];
            int counter = 0;
            for (int j = 0; j < this.board[i].length; j++) {
                int actElement = this.board[i][j];
                if (actElement > 0) {
                    if (actElement == actWinElement) {
                        counter++;
                    } else {
                        actWinElement = actElement;
                        counter = 1;
                    }
                    if (counter == this.winNumber) {
                        this.winnerPlayer = actWinElement;
                        return;
                    }
                } else {
                    actWinElement = actElement;
                    counter = 0;
                }
            }
        }

        // átlók ellenőrzése
        for (int i = 0; i <= (this.board.length - this.winNumber); i++) {
            for (int j = 0; j < this.board[i].length; j++) {
                // jobbra le átlóba
                if (j <= (this.board[i].length - this.winNumber) && this.board[i][j] > 0) {
                    int actWinElement = this.board[i][j];
                    int counter = 1;
                    for (int k = 1; k < this.winNumber; k++) {
                        int actElement = this.board[i + k][j + k];
                        if (actElement == actWinElement) {
                            counter++;
                        } else {
                            break;
                        }
                    }
                    if (counter == this.winNumber) {
                        this.winnerPlayer = actWinElement;
                        return;
                    }
                }
                // balra le átlóba
                if (j >= (this.winNumber - 1) && this.board[i][j] > 0) {
                    int actWinElement = this.board[i][j];
                    int counter = 1;
                    for (int k = 1; k < this.winNumber; k++) {
                        int actElement = this.board[i + k][j - k];
                        if (actElement == actWinElement) {
                            counter++;
                        } else {
                            break;
                        }
                    }
                    if (counter == this.winNumber) {
                        this.winnerPlayer = actWinElement;
                        return;
                    }
                }
            }
        }
    }
}
