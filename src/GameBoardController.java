import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

public class GameBoardController {
    Game game;

    @FXML
    private GridPane gameBoardPane;

    @FXML
    private Label winnerLabel;

    @FXML
    public void initialize() {
        try {
            game = new Game();
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                TextField tf = new TextField("");
                tf.setEditable(false);
                int size = 26;
                tf.setMinSize(size, size);
                tf.setPrefSize(size, size);
                tf.setMaxSize(size, size);
                tf.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        char text = game.getNextPlayer() == 1 ? game.getFIRSTPLAYERCHAR() : game.getSECONDPLAYERCHAR();
                        tf.setText(String.valueOf(text));
                        int rowIndex = GridPane.getRowIndex(tf);
                        int columnIndex = GridPane.getColumnIndex(tf);
                        try {
                            int winner = game.setCell(rowIndex, columnIndex, game.getNextPlayer());
                            // ha van győztes, akko már nem klikkelhetőek a cellák
                            if (winner > 0) {
                                for (Node element : gameBoardPane.getChildren()) {
                                    element.setOnMouseClicked(new EventHandler<MouseEvent>() {
                                        @Override
                                        public void handle(MouseEvent event) {
                                        }
                                    });
                                }
                                char winnerChar = winner == 1 ? game.getFIRSTPLAYERCHAR() : game.getSECONDPLAYERCHAR();
                                winnerLabel.setText("A győztes játékos: " + winnerChar);
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
