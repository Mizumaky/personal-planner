import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
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
        ControllerCommunicator.getInstance().bindUnselectedTagsList();
        ControllerCommunicator.getInstance().refreshTaskView();
    }

    @FXML
    private void handleFileQuitMenuAction(ActionEvent event) {
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

    @FXML
    private void handleHelpAboutAction(ActionEvent event) {
        try {
            Desktop.getDesktop().browse(new URI("https://gitlab.fel.cvut.cz/B182_B0B36PJV/mullemi5/wikis/About"));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleHelpUserManualAction(ActionEvent event) {
        try {
            Desktop.getDesktop().browse(new URI("https://gitlab.fel.cvut.cz/B182_B0B36PJV/mullemi5/wikis/User-Manual"));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
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
