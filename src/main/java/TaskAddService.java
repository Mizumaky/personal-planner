import JPAobjects.TaskEntity;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A service that calls a persistence manager method for persisting a task in the database.
 * It is required to first set the task to be stored berfore starting the service.
 * <p>
 * Its value is set to true if successful, false otherwise.
 */
public class TaskAddService extends Service<Boolean> {
    private static final Logger LOGGER = Logger.getLogger(TaskAddService.class.getName());

    private TaskEntity task = null;

    /**
     * Sets task to be stored in database.
     *
     * @param task a task reference
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
                    LOGGER.info("Task add service started");
                    this.updateMessage("Adding task to database...");
                    PersistenceManager.getInstance().persist(task);
                    this.updateMessage("Adding successful.");
                    LOGGER.info("Task add service successfully ended");
                    return true;
                } catch (DBErrorException e) {
                    LOGGER.log(Level.WARNING, "Task add service failed, DB exception occured", e);
                    this.updateMessage("Database connection error! Task adding failed.");
                    return false;
                } catch (Exception e) {
                    LOGGER.log(Level.SEVERE, "Task add service failed, unhandled exception occured", e);
                    this.updateMessage("Unspecified error! Task adding failed.");
                    return false;
                }
            }
        };
    }
}
