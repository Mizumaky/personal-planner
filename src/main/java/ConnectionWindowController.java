import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;

public class ConnectionWindowController {
    @FXML
    Label statusLabel;
    @FXML
    Button cancelButton;
    @FXML
    Button tryAgainButton;
    @FXML
    ProgressIndicator progressIndicator;

    private ConnectService connectService;

    private ApplicationMain mainApp;
    public void setMainApp(ApplicationMain mainApp) {
        this.mainApp = mainApp;
    }

    public ConnectionWindowController() {}

    @FXML
    public void initialize() {
        connectService = new ConnectService();
        statusLabel.textProperty().unbind();
        statusLabel.setText("Initializing...");
        statusLabel.textProperty().bind(connectService.messageProperty());
        progressIndicator.visibleProperty().bind(connectService.runningProperty());
        cancelButton.setOnAction(event -> {
            cancelButton.setDisable(true);
            statusLabel.textProperty().unbind();
            statusLabel.setText("Forced exit!");
            System.exit(0);
        });
        tryAgainButton.setOnAction(event -> {
            tryAgainButton.setVisible(false);
            statusLabel.textProperty().unbind();
            statusLabel.setText("Retrying...");
            Task newConnectTask = connectService.createTask();
            statusLabel.textProperty().bind(connectService.messageProperty());
            connectService.reset();
            connectService.start();
        });
        connectService.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
                wse -> {
                    boolean result = connectService.getValue();
                    statusLabel.textProperty().unbind();
                    if (result) {
                        statusLabel.setText("Succesfully connected!");
                        cancelButton.setVisible(false);
                        mainApp.loadMainWindow();
                        mainApp.switchToMainWindow();
                    } else {
                        statusLabel.setText("Failed to connect!");
                        tryAgainButton.setVisible(true);
                    }
                });

        // Start new connection task using the service
        connectService.start();
    }
}
