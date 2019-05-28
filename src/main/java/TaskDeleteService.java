import JPAobjects.TaskEntity;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class TaskDeleteService extends Service<Boolean> {

    private TaskEntity task = null;

    public void setTask(TaskEntity task) {
        this.task = task;
    }
    public TaskEntity getTask() {
        return task;
    }

    @Override
    protected Task<Boolean> createTask() {
        return new Task<Boolean>() {
            @Override
            protected Boolean call() {
                try {
                    this.updateMessage("Deleting task from database...");
                    PersistenceManager.getInstance().remove(task);
                    this.updateMessage("Delete successful.");
                    return true;
                } catch (DBErrorException e) {
                    this.updateMessage("Deleting failed!");
                    return false;
                }
            }
        };
    }

}
