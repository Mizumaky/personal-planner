import JPAobjects.TaskEntity;
import javafx.concurrent.Service;
import javafx.scene.control.TableView;

public class ControllerCommunicator {
    private static ControllerCommunicator instance = null;
    private static MainWindowController mainWindowController = null;
    private static TaskViewController taskViewController = null;
    private static TagViewController tagViewController = null;

    //disable constructor
    private ControllerCommunicator() {
    }

    public static ControllerCommunicator getInstance() {
        if (instance == null)
            instance = new ControllerCommunicator();
        return instance;
    }

    public void registerMainController(MainWindowController ctrlr) {
        mainWindowController = ctrlr;
    }
    public void registerTaskViewController(TaskViewController ctrlr) {
        taskViewController = ctrlr;
    }
    public void registerTagViewController(TagViewController ctrlr) {
        tagViewController = ctrlr;
    }

    public void secondaryInitTaskView() {
        taskViewController.secondaryInit();
    }
    public void secondaryInitTagView() {
        tagViewController.secondaryInit();
    }

    public TableView<TaskEntity> getTaskViewData() {
        return taskViewController.getTable();
    }

    public void disableDBButtons() {
        if (taskViewController != null) {
            taskViewController.disableButtons();
        } else {
            System.err.println("Could not disable buttons, task view controller not registered.");
        }
        if (tagViewController != null) {
            tagViewController.disableButtons();
        } else {
            System.err.println("Could not disable buttons, tag view controller not registered.");
        }
    }

    public void enableDBButtons() {
        if (taskViewController != null) {
            taskViewController.enableButtons();
        } else {
            System.err.println("Could not enable buttons, task view controller not registered.");
        }
        if (tagViewController != null) {
            tagViewController.enableButtons();
        } else {
            System.err.println("Could not enable buttons, tag view controller not registered.");
        }
    }

    public void bindStatusBar(Service svc) {
        if (mainWindowController != null) {
            mainWindowController.getStatusLabel().textProperty().bind(svc.messageProperty()); //TODO null pointer
            mainWindowController.getProgressIndicator().managedProperty().bind(svc.runningProperty());
            mainWindowController.getProgressIndicator().visibleProperty().bind(svc.runningProperty());
            mainWindowController.getOk().managedProperty().bind(svc.valueProperty().isEqualTo(true));
            mainWindowController.getOk().visibleProperty().bind(svc.valueProperty().isEqualTo(true));
            mainWindowController.getFailed().managedProperty().bind(svc.valueProperty().isEqualTo(false));
            mainWindowController.getFailed().visibleProperty().bind(svc.valueProperty().isEqualTo(false));
        } else {
            System.err.println("Could not bind status bar, main controller not registered.");
        }
    }
    public void unbindStatusBar() {
        if (mainWindowController != null) {
            mainWindowController.getStatusLabel().textProperty().unbind();
            mainWindowController.getProgressIndicator().visibleProperty().unbind();
            mainWindowController.getProgressIndicator().managedProperty().unbind();
            mainWindowController.getOk().visibleProperty().unbind();
            mainWindowController.getOk().managedProperty().unbind();
            mainWindowController.getFailed().visibleProperty().unbind();
            mainWindowController.getFailed().managedProperty().unbind();
        } else {
            System.err.println("Could not unbind status bar, main controller not registered.");
        }
    }

}
