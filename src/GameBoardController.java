import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

public class GameBoardController {

    @FXML
    private GridPane gameBoardPane;

    @FXML
    public void initialize() {
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
                        tf.setText("X");
                        int rowIndex = GridPane.getRowIndex(tf);
                        int columnIndex = GridPane.getColumnIndex(tf);
                        System.out.println(rowIndex + ", " + columnIndex);
                    }
                });
                this.gameBoardPane.add(tf, j, i);
            }
        }
    }

}
