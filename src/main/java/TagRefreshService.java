import JPAobjects.CategoryEntity;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.util.ArrayList;
import java.util.List;


public class TagRefreshService extends Service<Boolean> {
    private ArrayList<CategoryEntity> resultList = null;
    public void setResultList(ArrayList<CategoryEntity> resultList) {
        this.resultList = resultList;
    }

    @Override
    protected Task<Boolean> createTask() {
        return new Task<Boolean>() {
            @Override
            protected Boolean call() {
                resultList.clear();
                this.updateMessage("Reloading tags from database...");
                try {
                    List<CategoryEntity> list = PersistenceManager.getInstance().fetchRootCategories();
                    resultList.addAll(list);
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
