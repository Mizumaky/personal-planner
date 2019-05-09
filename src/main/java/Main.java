import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("MainWindow.fxml")); //also instantiates the associated controller
        root.getStylesheets().add(getClass().getResource("MyStyle1.css").toExternalForm());
        primaryStage.setTitle("Taskira");
        primaryStage.setScene(new Scene(root, 400, 250));
        primaryStage.show();
    }
}
