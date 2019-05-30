import JPAobjects.TaskEntity;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A service that calls a persistence manager method for merging a task into the database.
 * It is required to first set the task to be merged berfore starting the service.
 * <p>
 * Its value is set to true if successful, false otherwise.
 */
public class TaskEditService extends Service<Boolean> {
    private static final Logger LOGGER = Logger.getLogger(TaskEditService.class.getName());

    private TaskEntity task = null;

    /**
     * Sets task to be merged into database.
     *
     * @param task an existing task reference
     */
    public void setTask(TaskEntity task) {
        this.task = task;
    }

    /**
     * Gets the task reference back if needed.
     *
     * @return the task
     */
    public TaskEntity getTask() {
        return task;
    }

    @Override
    protected Task<Boolean> createTask() {
        return new Task<Boolean>() {
            @Override
            protected Boolean call() {
                try {
                    LOGGER.info("Task edit service started");
                    this.updateMessage("Editing task in the database...");
                    PersistenceManager.getInstance().merge(task);
                    this.updateMessage("Edit successful.");
                    LOGGER.info("Task edit service successfully ended");
                    return true;
                } catch (DBErrorException e) {
                    LOGGER.log(Level.WARNING, "Task edit service failed, DB exception occured", e);
                    this.updateMessage("Database connection error! Editing task failed.");
                    return false;
                } catch (Exception e) {
                    LOGGER.log(Level.SEVERE, "Task edit service failed, unhandled exception occured", e);
                    this.updateMessage("Unspecified error! Editing task failed.");
                    return false;
                }
            }
        };
    }
}
