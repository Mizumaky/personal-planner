import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public abstract class Controller implements Initializable {
    private ApplicationMain mainApp;
    private PersistenceManager pm;

    public Controller(){
        pm = PersistenceManager.getInstance();
    }

    public void setMainApp(ApplicationMain mainApp) {
        this.mainApp = mainApp;
    }

//    private <Input, Output> Output newModalStage(Input input) {
//
//    }
}
