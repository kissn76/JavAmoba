import java.util.Scanner;

public class JavAmobaCli {

    public static void main(String[] args) throws Exception {
        GameController gameController = new GameController();
        Game game = gameController.getGame();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("\033[H\033[2J");
            System.out.flush();
            System.out.println(game.getBoardView());
            ;
            System.out.println("\nKövetkező: " + game.getNextPlayerChar());
            System.out.print("Sor: ");
            int row = scanner.nextInt();
            System.out.print("Oszlop: ");
            int column = scanner.nextInt();
            gameController.setCell(row, column, game.getNextPlayer());
        }
    }

}
