import JPAobjects.TaskEntity;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.util.List;


public class TaskRefreshService extends Service<Boolean> {
    private ObservableList<TaskEntity> resultList = null;

    public void setResultList(ObservableList<TaskEntity> resultList) {
        this.resultList = resultList;
    }

    @Override
    protected Task<Boolean> createTask() {
        return new Task<Boolean>() {
            @Override
            protected Boolean call() {
                if (resultList != null) {
                    resultList.clear();
                    this.updateMessage("Reloading tasks from database...");
                    try {
                        List<TaskEntity> taskList = PersistenceManager.getInstance().fetchAllTasks();
                        resultList.addAll(taskList);
                        this.updateMessage("Tasks successfully refreshed.");
                        return true;
                    } catch (DBErrorException e) {
                        this.updateMessage("Database connection error!");
                        return false;
                    }
                } else {
                    System.err.println("Error: Task refresh service result list not set!");
                    return false;
                }
            }
        };
    }
}
