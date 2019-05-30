import javafx.fxml.Initializable;
import javafx.stage.Stage;

/**
 * A simple abstract wrapper for controllers, so that they can have a reference to the stage they're controlling.
 */
public abstract class Controller implements Initializable {
    Stage thisStage = null;

    /**
     * Empty constructor for instantiating a new Controller.
     */
    public Controller() {}

    /**
     * Set controlled stage reference
     *
     * @param stage A stage this controller controls should be passed
     */
    public void setStageReference(Stage stage) {
        this.thisStage = stage;
    }
}
