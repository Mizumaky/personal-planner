import JPAobjects.TaskEntity;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class DeleteTaskService extends Service<Boolean> {

    private TaskEntity task = null;

    public void setTask(TaskEntity task) {
        this.task = task;
    }

    @Override
    protected Task<Boolean> createTask() {
        return new Task<Boolean>() {
            @Override
            protected Boolean call() {
                try {
                    this.updateMessage("Deleting task from database...");
                    PersistenceManager.getInstance().remove(task);
                    return true;
                } catch (DBErrorException e) {
                    this.updateMessage("Deletion failed!");
                    return false;
                }
            }
        };
    }

}
