import javafx.concurrent.Service;

public class ControllerCommunicator {
    private static ControllerCommunicator instance = null;
    private static MainController mainController = null;
    private static TaskViewController taskViewController = null;

    //disable constructor
    private ControllerCommunicator() {
    }

    public static ControllerCommunicator getInstance() {
        if (instance == null)
            instance = new ControllerCommunicator();
        return instance;
    }

    public void registerMainController(MainController ctrlr) {
        mainController = ctrlr;
    }
    public void registerTaskViewController(TaskViewController ctrlr) {
        taskViewController = ctrlr;
    }

    public void disableDBButtons() {
        taskViewController.disableButtons();
        //add more
    }

    public void enableDBButtons() {
        taskViewController.enableButtons();
        //add more
    }

    public void bindStatusBar(Service svc) {
        mainController.getStatusLabel().textProperty().bind(svc.messageProperty()); //TODO null pointer
        mainController.getProgressIndicator().visibleProperty().bind(svc.runningProperty());
        mainController.getProgressIndicator().managedProperty().bind(svc.runningProperty());
        mainController.getOk().visibleProperty().bind(svc.valueProperty().isEqualTo(true));
        mainController.getOk().managedProperty().bind(svc.valueProperty().isEqualTo(true));
        mainController.getFailed().visibleProperty().bind(svc.valueProperty().isEqualTo(false));
        mainController.getFailed().managedProperty().bind(svc.valueProperty().isEqualTo(false));
    }
    public void unbindStatusBar() {
        mainController.getStatusLabel().textProperty().unbind();
        mainController.getProgressIndicator().visibleProperty().unbind();
        mainController.getProgressIndicator().managedProperty().unbind();
        mainController.getOk().visibleProperty().unbind();
        mainController.getOk().managedProperty().unbind();
        mainController.getFailed().visibleProperty().unbind();
        mainController.getFailed().managedProperty().unbind();
    }

}
