import JPAobjects.TagEntity;
import JPAobjects.TaskEntity;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.util.List;

public class ConnectService extends Service<Boolean> {
    @Override
    protected Task<Boolean> createTask() {
        return new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {

                this.updateMessage("Connecting to the database...");
                boolean startup = PersistenceManager.start();
                if (startup) {

                    this.updateMessage("Test 1: Getting and printing tags...");
                    List<TagEntity> tags = PersistenceManager.fetchAllTags();
                    System.out.println(tags);
                    if (tags != null) {

                        this.updateMessage("Test 2: Getting and printing tasks...");
                        List<TaskEntity> tasks = PersistenceManager.fetchAllTasks();
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
