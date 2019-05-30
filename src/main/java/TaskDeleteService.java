import JPAobjects.TaskEntity;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A service that calls a persistence manager method for removing a task from the database.
 * It is required to first set the task to be removed berfore starting the service.
 * <p>
 * Its value is set to true if successful, false otherwise.
 */
public class TaskDeleteService extends Service<Boolean> {
    private static final Logger LOGGER = Logger.getLogger(TaskDeleteService.class.getName());

    private TaskEntity task = null;

    /**
     * Sets task to be removed from database.
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
                    LOGGER.info("Task delete service started");
                    this.updateMessage("Deleting task from database...");
                    PersistenceManager.getInstance().remove(task);
                    this.updateMessage("Delete successful.");
                    LOGGER.info("Task delete service successfully ended");
                    return true;
                } catch (DBErrorException e) {
                    LOGGER.log(Level.WARNING, "Task delete service failed, DB exception occured", e);
                    this.updateMessage("Database connection error! Deleting task failed.");
                    return false;
                } catch (Exception e) {
                    LOGGER.log(Level.SEVERE, "Task delete service failed, unhandled exception occured", e);
                    this.updateMessage("Unspecified error! Deleting task failed.");
                    return false;
                }
            }
        };
    }

}
