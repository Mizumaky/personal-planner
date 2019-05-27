import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

import static java.lang.System.exit;

public class ApplicationMain extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Taskira");
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("icon.png")));
        try {
            FXMLLoader connectionPaneLoader = new FXMLLoader(getClass().getResource("FXML_ConnectionWindow.fxml"));
            Parent root = connectionPaneLoader.load(); //loads the scene graph and instantiates the associated controller
            Scene connectionScene = new Scene(root, 300, 70);
            ConnectionWindowController cwc = connectionPaneLoader.getController();
            cwc.setStageReference(primaryStage);
            primaryStage.setScene(connectionScene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            exit(1);
        }
    }
}


