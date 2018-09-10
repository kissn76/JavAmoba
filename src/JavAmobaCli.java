import java.util.Scanner;

public class JavAmobaCli {

    public static void main(String[] args) throws Exception {
        Game game = new Game();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("\033[H\033[2J");
            System.out.flush();
            game.printBoard();
            System.out.println("\nKövetkező: " + game.getNextPlayer());
            System.out.print("Sor: ");
            int row = scanner.nextInt();
            System.out.print("Oszlop: ");
            int column = scanner.nextInt();
            game.setCell(row, column, game.getNextPlayer());
        }
    }

}
