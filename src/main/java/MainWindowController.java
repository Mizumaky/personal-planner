import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;
import javafx.stage.Window;

import java.util.Optional;

public class MainWindowController {

    public MainWindowController() {
    }

    //can also have initialize method, and also a private URL location attribute, and also ResourceBundle resources attribute

    @FXML //so that even if its private, the loader can access it
    private MenuItem fileQuit;

    @FXML
    protected void handleFileQuitMenuAction(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Quit");
        alert.setHeaderText(null);
        alert.setContentText("This will kill everything, are you sure?");
        alert.getDialogPane().getStylesheets().add(getClass().getResource("MyStyle1.css").toExternalForm());
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            //TODO send message to model so that it properly closes
            System.exit(0);
        }
    }
}
