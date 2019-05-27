import javafx.fxml.Initializable;
import javafx.stage.Stage;

public abstract class Controller implements Initializable {
    Stage thisStage = null;

    public Controller() {}

    public void setStageReference(Stage stage) {
        this.thisStage = stage;
    }

//    private <Input, Output> Output newModalStage(Input input) {
//
//    }
}
