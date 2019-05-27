import JPAobjects.TaskEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.util.List;


public class RefreshService extends Service<Boolean> {
    private ObservableList<TaskEntity> result = FXCollections.observableArrayList();

    public ObservableList<TaskEntity> getResult() { return result; }

    @Override
    protected Task<Boolean> createTask() {
        return new Task<Boolean>() {
            @Override
            protected Boolean call() {
                result.clear();
                this.updateMessage("Reloading data from database...");
                try {
                    List<TaskEntity> taskList = PersistenceManager.getInstance().fetchAllTasks();
                    result.addAll(taskList);
                    this.updateMessage("Successfully reloaded.");
                    return true;
                } catch (DBErrorException e) {
                    this.updateMessage("Database connection error!");
                    return false;
                }
            }
        };
    }
}
