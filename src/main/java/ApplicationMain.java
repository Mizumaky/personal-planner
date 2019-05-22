import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

import static java.lang.System.exit;

public class ApplicationMain extends Application {

    private Stage stage;
    private Scene connectionScene;
    private Scene mainScene;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        stage.setTitle("Taskira");
        stage.getIcons().add(new Image(getClass().getResourceAsStream( "icon.png" )));
        loadConnectionWindow();
        switchToConnectionWindow();
        stage.show();
    }

    public Stage getStage() {
        return stage;
    }

    public void loadConnectionWindow() {
        try {
            FXMLLoader connectionPaneLoader = new FXMLLoader(getClass().getResource("FXML_scene_connection.fxml"));
            Parent root = connectionPaneLoader.load(); //loads the scene graph and instantiates the associated controller
            connectionScene = new Scene(root, 300, 70);
            ControllerSceneConnection cwc = connectionPaneLoader.getController();
            cwc.setMainApp(this);
        } catch (IOException e) {
            e.printStackTrace();
            exit(1);
        }
    }
    public void switchToConnectionWindow() {
        stage.setScene(connectionScene);
    }

    public void loadMainWindow() {
        try {
            FXMLLoader mainPaneLoader = new FXMLLoader(getClass().getResource("FXML_scene_main.fxml"));
            Parent root = mainPaneLoader.load(); //also instantiates the associated controller
            mainScene = new Scene(root, 1000, 600);
            ControllerSceneMain mwc = mainPaneLoader.getController();
            mwc.setMainApp(this);
        } catch (IOException e) {
            e.printStackTrace();
            exit(1);
        }
    }
    public void switchToMainWindow() {
        stage.setScene(mainScene);
    }
}
