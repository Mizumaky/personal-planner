import JPAobjects.CategoryEntity;
import JPAobjects.TaskEntity;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.scene.control.TableView;

import java.util.ArrayList;

public class ControllerCommunicator {
    private static ControllerCommunicator instance = null;
    private static MainWindowController mainWindowController = null;
    private static TaskViewController taskViewController = null;
//    private static TaskWindowController taskWindowController = null;
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
//    public void registerTaskWindowController(TaskWindowController ctrlr) {
//        taskWindowController = ctrlr;
//    }
    public void registerTagViewController(TagViewController ctrlr) {
        tagViewController = ctrlr;
    }

    public void refreshTaskView() {
        taskViewController.refreshTasks();
    }

    public void refreshTagView() {
        tagViewController.refreshTags();
    }

    public ArrayList<CategoryEntity> getRootTagCategories() {
        return tagViewController.getRootCategories();
    }

    public TableView<TaskEntity> getTaskViewTable() {
        return taskViewController.getTable();
    }

    public ObservableList<TaskEntity> getTaskViewData() {
        return taskViewController.getTableData();
    }

    public void bindUnselectedTagsList() {
        tagViewController.setUnselectedTagsList(taskViewController.getUnselectedTagsList());
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
            mainWindowController.getStatusLabel().textProperty().bind(svc.messageProperty());
            mainWindowController.getProgressIndicator().managedProperty().bind(svc.valueProperty().isNull());
            mainWindowController.getProgressIndicator().visibleProperty().bind(svc.valueProperty().isNull());
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
