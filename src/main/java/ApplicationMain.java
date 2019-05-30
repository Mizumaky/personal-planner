import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.System.exit;

/**
 * Main application class.
 */
public class ApplicationMain extends Application {
    private static final Logger LOGGER = Logger.getLogger(ApplicationMain.class.getName());

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        LOGGER.info("Application started");
        primaryStage.setTitle("Taskira");
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("icon.png")));
        try {
            LOGGER.info("Loading connect window");
            FXMLLoader connectionPaneLoader = new FXMLLoader(getClass().getResource("FXML_ConnectWindow.fxml"));
            Parent root = connectionPaneLoader.load(); //loads the scene graph and instantiates the associated controller
            Scene connectionScene = new Scene(root, 300, 70);
            ConnectWindowController cwc = connectionPaneLoader.getController();
            cwc.setStageReference(primaryStage);
            primaryStage.setScene(connectionScene);
            primaryStage.show();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to load FXML", e);
            exit(1);
        }
    }
}


