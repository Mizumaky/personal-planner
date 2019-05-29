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

import static java.lang.System.exit;

public class ConnectWindowController extends Controller {
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
        //INIT CONNECT SERVICE
        csvc = new ConnectService();
        statusLabel.setText("Initializing...");
        statusLabel.textProperty().unbind();
        statusLabel.textProperty().bind(csvc.messageProperty());
        progressIndicator.managedProperty().bind(csvc.runningProperty());
        progressIndicator.visibleProperty().bind(csvc.runningProperty());

        //ASSIGN EVENT HANDLERS
        csvc.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
                wse -> {
                    boolean result = csvc.getValue();
                    statusLabel.textProperty().unbind();
                    if (result) {
                        cancelButton.setVisible(false);
                        try {
                            FXMLLoader mainPaneLoader = new FXMLLoader(getClass().getResource("FXML_Main.fxml"));
                            Parent root = mainPaneLoader.load(); //also instantiates the associated controller
                            Scene mainScene = new Scene(root, 1260, 820);
                            Stage mainStage = new Stage();
                            mainStage.setTitle("Taskira");
                            mainStage.getIcons().add(new Image(getClass().getResourceAsStream("icon.png")));
                            mainStage.setScene(mainScene);
                            MainWindowController mwc = mainPaneLoader.getController();
                            mwc.setStageReference(mainStage);
                            thisStage.close();
                            mainStage.show();
                        } catch (IOException e) {
                            e.printStackTrace();
                            exit(1);
                        }
                    } else {
                        tryAgainButton.setVisible(true);
                    }
                });
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

        // Start new connection task using the service
        csvc.start();
    }
}
