import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A service that calls persistence manager methods for initial connecting to the database.
 *
 * Its value is set to true if successful, false otherwise.
 */
public class ConnectService extends Service<Boolean> {
    private static final Logger LOGGER = Logger.getLogger(ConnectService.class.getName());

    @Override
    protected Task<Boolean> createTask() {
        return new Task<Boolean>() {
            @Override
            protected Boolean call() {
                try {
                    LOGGER.info("Connect service started");
                    this.updateMessage("Connecting to the database...");
                    PersistenceManager pm = PersistenceManager.getInstance();
                    pm.connect(); // Persistance managaer call, expected delay

//                  UNUSED TEST PART
//                    this.updateMessage("TEST 1:  Getting and printing tags...");
//                    List<TagEntity> tags = pm.fetchAllTags();
//                    System.out.println(tags);
//
//                    this.updateMessage("TEST 2:  Getting and printing tasks...");
//                    List<TaskEntity> tasks = pm.fetchAllTasks();
//                    System.out.println(tasks);

                    LOGGER.info("Connect service successfully ended");
                    this.updateMessage("Succesfully connected.");
                    return true;
                } catch (DBErrorException e) {
                    LOGGER.log(Level.WARNING, "Connect service failed, DB exception occured", e);
                    this.updateMessage("Connection failed!");
                    return false;
                } catch (Exception e) {
                    LOGGER.log(Level.SEVERE, "Connect service failed, unhandled exception occured", e);
                    this.updateMessage("Unspecified error!");
                    return false;
                }
            }
        };
    }


}
