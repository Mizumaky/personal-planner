import JPAobjects.*;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

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
        Parent parent = FXMLLoader.load(getClass().getResource("ConnectionWindow.fxml")); //also instantiates the associated controller
        connectionScene = new Scene(parent, 300, 70);
        stage.setScene(connectionScene);

//        //switch to regular window
//        Parent root = FXMLLoader.load(getClass().getResource("MainWindow.fxml"));
//        mainScene = new Scene(root, 800, 400);
//        stage.setScene(mainScene);

        stage.show();
    }

    public Stage getStage() {
        return stage;
    }
}
