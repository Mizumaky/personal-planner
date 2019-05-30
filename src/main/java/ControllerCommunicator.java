import JPAobjects.CategoryEntity;
import JPAobjects.TaskEntity;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.scene.control.TableView;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A single class with static fields and methods, holding references to interconnected controllers, providing methods for bindings and interaction between them.
 */
public class ControllerCommunicator {
    private static final Logger LOGGER = Logger.getLogger(ControllerCommunicator.class.getName());
    private static MainWindowController mainWindowController = null;
    private static TaskViewController taskViewController = null;
    private static TagViewController tagViewController = null;
//    private static TaskWindowController taskWindowController = null;

    // Disable constructor
    private ControllerCommunicator() {}

    // Registering controller references
    /**
     * Registers main window controller.
     *
     * @param ctrlr The controller reference
     */
    public static void registerMainController(MainWindowController ctrlr) {
        mainWindowController = ctrlr;
        LOGGER.info("Main window controller registered in the communicator");
    }

    /**
     * Registers task view controller.
     *
     * @param ctrlr The controller reference
     */
    public static void registerTaskViewController(TaskViewController ctrlr) {
        taskViewController = ctrlr;
        LOGGER.info("Task view controller registered in the communicator");
    }

    /**
     * Registers tag view controller.
     *
     * @param ctrlr The controller reference
     */
    public static void registerTagViewController(TagViewController ctrlr) {
        tagViewController = ctrlr;
        LOGGER.info("Tag view controller registered in the communicator");
    }

//    public static void registerTaskWindowController(TaskWindowController ctrlr) {
//        taskWindowController = ctrlr;
//    }

    // Getters
    /**
     * Gets root tag categories from tag view controller.
     *
     * @return the root categories
     */
    public static ArrayList<CategoryEntity> getRootTagCategories() {
        return tagViewController.getRootCategories();
    }

    /**
     * Gets table view from task view controller.
     *
     * @return the table view
     */
    public static TableView<TaskEntity> getTaskViewTable() {
        return taskViewController.getTable();
    }

    /**
     * Gets table data from task view controller.
     *
     * @return the table data
     */
    public static ObservableList<TaskEntity> getTaskViewData() {
        return taskViewController.getTableData();
    }

    /**
     * Gets the list of filtered out tags from task view controller and sets its reference to tag view controller.
     */
    public static void bindUnselectedTagsList() {
        tagViewController.setUnselectedTagsList(taskViewController.getUnselectedTagsList());
    }


    // Commands
    /**
     * A command to task view controller to refresh its task table view
     */
    public static void refreshTaskView() {
        taskViewController.refreshTasks();
        LOGGER.info("Calling task view controller's task refresh method");
    }

    /**
     * A command to tag view controller to refresh its tag tree view
     */
    public static void refreshTagView() {
        tagViewController.refreshTags();
        LOGGER.info("Calling tag view controller's task refresh method");
    }


    /**
     * A command to task and tag view controllers to disable any database interaction inputs
     */
    public static void disableDBButtons() {
        LOGGER.info("Calling disable database access buttons methods");
        taskViewController.disableButtons();
        tagViewController.disableButtons();
    }

    /**
     * A command to task and tag view controllers to enable all database interaction inputs
     */
    public static void enableDBButtons() {
        LOGGER.info("Calling enable database access buttons methods");
        taskViewController.enableButtons();
        tagViewController.enableButtons();
    }

    /**
     * A command to main controller to bind this service to its status bar
     *
     * @param svc the service to be bound to
     */
    public static void bindStatusBar(Service svc) {
        LOGGER.log(Level.INFO, "Calling bind main window's status bar to a service", svc);
        mainWindowController.bindStatusBar(svc);
    }

    /**
     * A command to main controller to unbind its status bar from any service
     */
    public static void unbindStatusBar() {
        LOGGER.log(Level.INFO, "Calling unbind main window's status bar");
        mainWindowController.unbindStatusBar();
    }

}
