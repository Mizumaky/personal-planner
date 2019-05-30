import JPAobjects.TaskEntity;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A service that calls a persistence manager method for loading tasks from the database.
 * It is required to first set an observable list into which the data should be loaded berfore starting the service.
 * <p>
 * Its value is set to true if successful, false otherwise.
 */
public class TaskRefreshService extends Service<Boolean> {
    private static final Logger LOGGER = Logger.getLogger(TaskRefreshService.class.getName());

    private ObservableList<TaskEntity> resultList = null;

    /**
     * Sets the observable list for storing result data.
     *
     * @param resultList an existing list reference
     */
    public void setResultList(ObservableList<TaskEntity> resultList) {
        this.resultList = resultList;
    }

    @Override
    protected Task<Boolean> createTask() {
        return new Task<Boolean>() {
            @Override
            protected Boolean call() {
                try {
                    LOGGER.info("Tag refresh service started");
                    resultList.clear();
                    this.updateMessage("Reloading tasks from database...");
                    List<TaskEntity> taskList = PersistenceManager.getInstance().fetchAllTasks();
                    resultList.addAll(taskList);
                    this.updateMessage("Tasks successfully refreshed.");
                    LOGGER.info("Task refresh service successfully ended");
                    return true;
                } catch (DBErrorException e) {
                    LOGGER.log(Level.WARNING, "Task refresh service failed, DB exception occured", e);
                    this.updateMessage("Database connection error! Tag reloading failed.");
                    return false;
                } catch (Exception e) {
                    LOGGER.log(Level.SEVERE, "Task refresh service failed, unhandled exception occured", e);
                    this.updateMessage("Unspecified error! Tag reloading failed.");
                    return false;
                }
            }
        };
    }
}
