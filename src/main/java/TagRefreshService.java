import JPAobjects.CategoryEntity;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * A service that calls a persistence manager method for loading tags from the database.
 * It is required to first set an array list into which the data should be loaded berfore starting the service.
 * <p>
 * Its value is set to true if successful, false otherwise.
 */
public class TagRefreshService extends Service<Boolean> {
    private static final Logger LOGGER = Logger.getLogger(TagRefreshService.class.getName());

    private ArrayList<CategoryEntity> resultList = null;

    /**
     * Sets the array list for storing result data.
     *
     * @param resultList an existing list reference
     */
    public void setResultList(ArrayList<CategoryEntity> resultList) {
        this.resultList = resultList;
    }

    @Override
    protected Task<Boolean> createTask() {
        return new Task<Boolean>() {
            @Override
            protected Boolean call() {
                try {
                    LOGGER.info("Tag refresh service started");
                    resultList.clear();
                    this.updateMessage("Reloading tags from database...");
                    List<CategoryEntity> list = PersistenceManager.getInstance().fetchRootCategories();
                    resultList.addAll(list);
                    this.updateMessage("Tags successfully refreshed.");
                    LOGGER.info("Tag refresh service successfully ended");
                    return true;
                } catch (DBErrorException e) {
                    LOGGER.log(Level.WARNING, "Tag refresh service failed, DB exception occured", e);
                    this.updateMessage("Database connection error! Task reloading failed.");
                    return false;
                } catch (Exception e) {
                    LOGGER.log(Level.SEVERE, "Tag refresh service failed, unhandled exception occured", e);
                    this.updateMessage("Unspecified error! Task reloading failed.");
                    return false;
                }
            }
        };
    }
}
