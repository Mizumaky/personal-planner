import JPAobjects.TagEntity;
import JPAobjects.TaskEntity;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.util.List;

public class ConnectService extends Service<Boolean> {

    @Override
    protected Task<Boolean> createTask() {
        return new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {
                PersistenceManager pm = PersistenceManager.getInstance();

                this.updateMessage("Connecting to the database...");
                boolean startup = pm.connect();
                if (startup) {

                    this.updateMessage("Test 1: Getting and printing tags...");
                    List<TagEntity> tags = pm.fetchAllTags();
                    System.out.println(tags);
                    if (tags != null) {

                        this.updateMessage("Test 2: Getting and printing tasks...");
                        List<TaskEntity> tasks = pm.fetchAllTasks();
                        System.out.println(tasks);
                        if (tasks != null)

                            return true;
                    }
                }
                //this.updateMessage("Error occured, closing connection...");
                return false;
            }
        };
    }


}
