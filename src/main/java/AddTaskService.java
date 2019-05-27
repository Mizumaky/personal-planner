import JPAobjects.TaskEntity;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class AddTaskService extends Service<Boolean> {
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
                    this.updateMessage("Adding task to database...");
                    PersistenceManager.getInstance().persist(task);
                    return true;
                } catch (DBErrorException e) {
                    this.updateMessage("Adding failed!");
                    return false;
                }
            }
        };
    }
}
