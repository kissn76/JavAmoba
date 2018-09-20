package game;

public class GameController {
    private Game game;

    public GameController() throws Exception {
        this.game = new Game();
    }

    public Game getGame() {
        return this.game;
    }

    /**
     * Beállítja a cellát a játék táblán. Ez vezérli az egész játékot.
     *
     * @param  row       Az utolsó tét sora
     * @param  column    Az utolsó tét oszlopa
     * @param  player    A játékos száma, aki tette a tétet
     * @return           Ha van győztes, akkor a száma (1 vagy 2), egyébként 0.
     * @throws Exception
     */
    public int setCell(int row, int column, int player) throws Exception {
        if (this.game.getWinnerPlayer() == 0) {
            if (player == this.game.getNextPlayer()) {
                if (row >= 0 && row < this.game.getBoardRowNumber() && column >= 0 && column < this.game.getBoardColumnNumber()) {
                    if (this.game.getBoardCell(row, column) == 0) {
                        this.game.setBoardCell(row, column, player);
                        this.game.setWinnerPlayer(win(row, column));
                        if (this.game.getWinnerPlayer() > 0) {
                            return this.game.getWinnerPlayer();
                        }
                        setNextPlayer();
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

    private void setNextPlayer() {
        if (this.game.getNextPlayer() == 1) {
            this.game.setNextPlayer(2);
        } else {
            this.game.setNextPlayer(1);
        }
    }

    /**
     * Annak ellenőrzése, hogy van-e már nyertes. Paraméternek megkapja a referencia
     * cellát, annak az elemét vizsgálja (1 vagy 2). A referencia cellától minden
     * irányba összeszámolja, hogy mennyi ugyanolyan elem van letéve.
     *
     * @param  row    Az utolsó tét sora
     * @param  column Az utolsó tét oszlopa
     * @return        A gyöztes játékos száma, vagy 0
     */
    private int win(int row, int column) {
        // Az utoljára lerakott ponttól kiindulva megszámoljuk, hogy hány ugyanolyan
        // pont van jobbra-balra, le-föl, balfönt-jobblent-átlósan,
        // ballent-jobbfönt-átlósan

        final int actWinElement = this.game.getBoardCell(row, column);   // ehhez a téthez képest vizsgálunk

        if (actWinElement > 0) {

            // jobbra-balra
            int counterHorizental = 1;

            {   // elemtől balra
                if (column > 0) {
                    int actColumnIndex = column - 1;
                    int actElement = this.game.getBoardCell(row, actColumnIndex);
                    while (actColumnIndex >= 0 && actElement == actWinElement) {
                        counterHorizental++;
                        actColumnIndex--;
                        actElement = actColumnIndex >= 0 ? this.game.getBoardCell(row, actColumnIndex) : 0;
                    }
                }
            }

            {   // elemtől jobbra
                if (column < this.game.getBoardColumnNumber() - 1) {
                    int actColumnIndex = column + 1;
                    int actElement = this.game.getBoardCell(row, actColumnIndex);
                    while (actColumnIndex < this.game.getBoardColumnNumber() && actElement == actWinElement) {
                        counterHorizental++;
                        actColumnIndex++;
                        actElement = actColumnIndex < this.game.getBoardColumnNumber() ? this.game.getBoardCell(row, actColumnIndex) : 0;
                    }
                }
            }

            if (counterHorizental >= this.game.getWinNumber()) {
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
                    int actElementLeft = actRowIndex >= 0 && actColumnIndexLeft >= 0 ? this.game.getBoardCell(actRowIndex, actColumnIndexLeft) : 0;
                    int actElementTop = this.game.getBoardCell(actRowIndex, actColumnIndexTop);
                    int actElementRight = actRowIndex >= 0 && actColumnIndexRight < this.game.getBoardColumnNumber() ? this.game.getBoardCell(actRowIndex, actColumnIndexRight) : 0;
                    while (actRowIndex >= 0 && (actElementLeft == actWinElement || actElementTop == actWinElement || actElementRight == actWinElement)) {
                        actRowIndex--;
                        if (actElementLeft == actWinElement) {
                            counterDiagonalTop++;
                            actColumnIndexLeft--;
                            actElementLeft = actRowIndex >= 0 && actColumnIndexLeft >= 0 ? this.game.getBoardCell(actRowIndex, actColumnIndexLeft) : 0;
                        }
                        if (actElementTop == actWinElement) {
                            counterVertical++;
                            actElementTop = actRowIndex >= 0 ? this.game.getBoardCell(actRowIndex, actColumnIndexTop) : 0;
                        }
                        if (actElementRight == actWinElement) {
                            counterDiagonalBottom++;
                            actColumnIndexRight++;
                            actElementRight = actRowIndex >= 0 && actColumnIndexRight < this.game.getBoardColumnNumber() ? this.game.getBoardCell(actRowIndex, actColumnIndexRight) : 0;
                        }
                    }
                }
            }

            {   // elemtől le
                if (row < this.game.getBoardRowNumber() - 1) {
                    int actRowIndex = row + 1;
                    int actColumnIndexLeft = column - 1;    // le-balra
                    int actColumnIndexTop = column;         // le egyenesen
                    int actColumnIndexRight = column + 1;   // le-jobbra
                    int actElementLeft = actRowIndex < this.game.getBoardRowNumber() && actColumnIndexLeft >= 0 ? this.game.getBoardCell(actRowIndex, actColumnIndexLeft) : 0;
                    int actElementTop = this.game.getBoardCell(actRowIndex, actColumnIndexTop);
                    int actElementRight = actRowIndex < this.game.getBoardRowNumber() && actColumnIndexRight < this.game.getBoardColumnNumber() ? this.game.getBoardCell(actRowIndex, actColumnIndexRight) : 0;
                    while (actRowIndex < this.game.getBoardRowNumber() && (actElementLeft == actWinElement || actElementTop == actWinElement || actElementRight == actWinElement)) {
                        actRowIndex++;
                        if (actElementLeft == actWinElement) {
                            counterDiagonalBottom++;
                            actColumnIndexLeft--;
                            actElementLeft = actRowIndex < this.game.getBoardRowNumber() && actColumnIndexLeft >= 0 ? this.game.getBoardCell(actRowIndex, actColumnIndexLeft) : 0;
                        }
                        if (actElementTop == actWinElement) {
                            counterVertical++;
                            actElementTop = actRowIndex < this.game.getBoardRowNumber() ? this.game.getBoardCell(actRowIndex, actColumnIndexTop) : 0;
                        }
                        if (actElementRight == actWinElement) {
                            counterDiagonalTop++;
                            actColumnIndexRight++;
                            actElementRight = actRowIndex < this.game.getBoardRowNumber() && actColumnIndexRight < this.game.getBoardColumnNumber() ? this.game.getBoardCell(actRowIndex, actColumnIndexRight) : 0;
                        }
                    }
                }
            }

            if (counterVertical >= this.game.getWinNumber() || counterDiagonalTop >= this.game.getWinNumber() || counterDiagonalBottom >= this.game.getWinNumber()) {
                return actWinElement;
            }
        }
        return 0;
    }

}
