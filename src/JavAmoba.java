
public class JavAmoba {

    public static void main(String[] args) throws Exception {
        Game game = new Game();
        game.setCell(5, 6, 1);
        game.printBoard();
        game.setCell(3, 5, 2);
        game.printBoard();
        game.setCell(6, 5, 1);
        game.printBoard();
        game.setCell(4, 5, 2);
        game.printBoard();
        game.setCell(7, 4, 1);
        game.printBoard();
        game.setCell(2, 4, 2);
        game.printBoard();
        game.setCell(1, 4, 1);
        game.printBoard();
        game.setCell(2, 5, 2);
        game.printBoard();
        game.setCell(8, 3, 1);
        game.printBoard();
        game.setCell(2, 6, 2);
        game.printBoard();
        game.setCell(9, 2, 1);
        game.printBoard();
    }

}
