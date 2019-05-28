import JPAobjects.CategoryEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.util.ArrayList;
import java.util.List;


public class TagRefreshService extends Service<Boolean> {
    private ArrayList<CategoryEntity> result = new ArrayList<>();
    public ArrayList<CategoryEntity> getResult() { return result; }

    @Override
    protected Task<Boolean> createTask() {
        return new Task<Boolean>() {
            @Override
            protected Boolean call() {
                result.clear();
                this.updateMessage("Reloading tags from database...");
                try {
                    List<CategoryEntity> list = PersistenceManager.getInstance().fetchRootCategories();
                    result.addAll(list);
                    this.updateMessage("Tags successfully refreshed.");
                    return true;
                } catch (DBErrorException e) {
                    this.updateMessage("Database connection error!");
                    return false;
                }
            }
        };
    }
}
