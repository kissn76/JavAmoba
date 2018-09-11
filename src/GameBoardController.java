import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

public class GameBoardController {
    GameController game;

    @FXML
    private GridPane gameBoardPane;

    @FXML
    private Label winnerLabel;

    @FXML
    public void initialize() {
        try {
            game = new GameController();
            printGameBoard();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void printGameBoard() {
        winnerLabel.setText("Kezdő játékos: " + game.getGame().getNextPlayerChar());

        // a BoardPane-hez hozzáadjuk a sorokat (i), oszlopokat (j)
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                // a cellák létrehozása
                TextField tf = new TextField("");
                tf.setEditable(false);
                tf.setCursor(Cursor.CLOSED_HAND);
                int size = 26;
                tf.setMinSize(size, size);
                tf.setPrefSize(size, size);
                tf.setMaxSize(size, size);
                // amikor rákattintunk a cellára
                tf.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        tf.setText(String.valueOf(game.getGame().getNextPlayerChar()));
                        tf.setDisable(true);
                        int rowIndex = GridPane.getRowIndex(tf);
                        int columnIndex = GridPane.getColumnIndex(tf);
                        try {
                            int winner = game.setCell(rowIndex, columnIndex, game.getGame().getNextPlayer());
                            // ha van győztes, akko már nem klikkelhetőek a cellák
                            if (winner > 0) {
                                for (Node element : gameBoardPane.getChildren()) {
                                    element.setDisable(true);
                                }
                                winnerLabel.setText("A győztes játékos: " + game.getGame().getWinnerPlayerChar());
                            } else {
                                winnerLabel.setText("Következő játékos: " + game.getGame().getNextPlayerChar());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                this.gameBoardPane.add(tf, j, i);
            }
        }
    }

}
