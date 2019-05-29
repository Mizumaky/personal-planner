import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class ConnectService extends Service<Boolean> {

    @Override
    protected Task<Boolean> createTask() {
        return new Task<Boolean>() {
            @Override
            protected Boolean call() {
                try {
                    PersistenceManager pm = PersistenceManager.getInstance();
                    this.updateMessage("Connecting to the database...");
                    pm.connect();

//                    this.updateMessage("TEST 1:  Getting and printing tags...");
//                    List<TagEntity> tags = pm.fetchAllTags();
//                    System.out.println(tags);
//
//                    this.updateMessage("TEST 2:  Getting and printing tasks...");
//                    List<TaskEntity> tasks = pm.fetchAllTasks();
//                    System.out.println(tasks);

                    this.updateMessage("Succesfully connected.");
                    return true;

                } catch (DBErrorException e) {
                    this.updateMessage("Connection failed!");
                    return false;
                }
            }
        };
    }


}
