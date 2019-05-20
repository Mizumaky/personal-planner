import JPAobjects.TagEntity;
import JPAobjects.TaskEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class ConnectService extends Service<Boolean> {
    @Override
    protected Task<Boolean> createTask() {
        return new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {
                try {
                    this.updateMessage("Connecting to the database...");
                    PersistenceManager.start();
                    this.updateMessage("Getting and printing tags...");
                    ObservableList<TagEntity> tags = FXCollections.observableList(PersistenceManager.getAllTags());
                    System.out.println(tags);
                    this.updateMessage("Getting and printing tasks...");
                    ObservableList<TaskEntity> tasks = FXCollections.observableList(PersistenceManager.getAllTasks());
                    System.out.println(tasks);
                } catch (Exception e) {
                    e.printStackTrace();
                    this.updateMessage("Error occured, closing connection...");
                    PersistenceManager.close();
                    return false;
                }
                return true;
            }
        };
    }


}
