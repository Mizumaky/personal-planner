import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;

import java.util.Optional;

public class MainController extends Controller {
    @FXML //so that even if its private, the loader can access it
    private MenuItem fileQuit;
    @FXML
    private Label statusLabel;
    @FXML
    private ImageView ok;
    @FXML
    private ImageView error;

    @FXML
    protected void handleFileQuitMenuAction(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Quit");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure?");
        alert.getDialogPane().getStylesheets().add(getClass().getResource("CSS_my_style_1.css").toExternalForm());
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            //TODO send message to model so that it properly closes
            System.exit(0);
        }
    }


}
