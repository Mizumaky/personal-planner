import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainWindowController extends Controller {
    @FXML //so that even if its private, the loader can access it
    private MenuItem fileQuit;
    @FXML
    private Label statusLabel;
    @FXML
    private ProgressIndicator progressIndicator;
    @FXML
    private ImageView ok;
    @FXML
    private ImageView failed;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ControllerCommunicator.getInstance().registerMainController(this);
        statusLabel.setText("Ready");
        ok.setVisible(true);
        ok.setManaged(true);
        failed.setVisible(false);
        failed.setManaged(false);
        progressIndicator.setVisible(false);
        progressIndicator.setManaged(false);
        ControllerCommunicator.getInstance().secondaryInitTaskView();
        ControllerCommunicator.getInstance().secondaryInitTagView();
    }

    @FXML
    protected void handleFileQuitMenuAction(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Quit");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure?");
        alert.getDialogPane().getStylesheets().add(getClass().getResource("CSS_my_style_1.css").toExternalForm());
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Platform.exit();
        }
    }

    public Label getStatusLabel() {
        return statusLabel;
    }

    public ProgressIndicator getProgressIndicator() {
        return progressIndicator;
    }

    public ImageView getOk() {
        return ok;
    }

    public ImageView getFailed() {
        return failed;
    }
}
