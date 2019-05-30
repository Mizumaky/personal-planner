import javafx.concurrent.WorkerStateEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.System.exit;

/**
 * Controller for the initial connect window, starts a connect service task and loads and switches to main window if successful.
 */
public class ConnectWindowController extends Controller {
    private static final Logger LOGGER = Logger.getLogger(ConnectWindowController.class.getName());
    //FXML references
    @FXML
    Label statusLabel;
    @FXML
    Button cancelButton;
    @FXML
    Button tryAgainButton;
    @FXML
    ProgressIndicator progressIndicator;

    private ConnectService csvc;

    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        LOGGER.info("Initializing connect window controller");
        // Assign gui event handlers
        cancelButton.setOnAction(event -> {
            cancelButton.setDisable(true);
            statusLabel.textProperty().unbind();
            statusLabel.setText("Forced exit!");
            System.exit(0);
        });
        tryAgainButton.setOnAction(event -> {
            tryAgainButton.setVisible(false);
            statusLabel.setText("Retrying...");
            statusLabel.textProperty().unbind();
            statusLabel.textProperty().bind(csvc.messageProperty());
            csvc.reset();
            csvc.start();
        });

        // Init connect service
        csvc = new ConnectService();
        // Bind visual indicators
        statusLabel.textProperty().unbind();
        statusLabel.textProperty().bind(csvc.messageProperty());
        progressIndicator.managedProperty().bind(csvc.runningProperty());
        progressIndicator.visibleProperty().bind(csvc.runningProperty());
        // Assign service completion event handler
        csvc.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
                wse -> {
                    LOGGER.info("Connect service on-succeed handler started");
                    statusLabel.textProperty().unbind();
                    boolean result = csvc.getValue();
                    // If connection was successful
                    if (result) {
                        cancelButton.setVisible(false);
                        try {
                            // Load main window
                            LOGGER.info("Loading main window");
                            FXMLLoader mainPaneLoader = new FXMLLoader(getClass().getResource("FXML_Main.fxml"));
                            Parent root = mainPaneLoader.load(); //note: also instantiates the associated controller
                            Scene mainScene = new Scene(root, 1260, 820);
                            Stage mainStage = new Stage();
                            mainStage.setTitle("Taskira");
                            mainStage.getIcons().add(new Image(getClass().getResourceAsStream("icon.png")));
                            mainStage.setScene(mainScene);
                            MainWindowController mwc = mainPaneLoader.getController();
                            mwc.setStageReference(mainStage);
                            LOGGER.info("Switching to main window");
                            thisStage.close();
                            mainStage.show();
                        } catch (IOException e) {
                            // Exit if fxml loading failed
                            LOGGER.log(Level.SEVERE, "Failed to load FXML", e);
                            exit(1);
                        }
                    } else {
                        // else show try again button
                        tryAgainButton.setVisible(true);
                    }
                });
        // Start new connection task using the service
        LOGGER.info("Starting connect service");
        csvc.start();
        LOGGER.info("Connect window controller initialized");
    }
}
