import JPAobjects.CategoryEntity;
import JPAobjects.TagEntity;
import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBoxTreeItem;
import javafx.scene.control.Label;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.CheckBoxTreeCell;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class TagViewController extends Controller {
    @FXML
    private TreeView<TreeEntityProxy> treeView;
    @FXML
    private Button refreshButton;
//    @FXML
//    private Button addTagButton;

    private TagRefreshService trsvc = null;
    private ArrayList<CategoryEntity> rootCategories = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ControllerCommunicator.getInstance().registerTagViewController(this);
        //init tree view
        treeView.setShowRoot(false);
        treeView.setCellFactory(CheckBoxTreeCell.<TreeEntityProxy>forTreeView());
        //init services
        trsvc = new TagRefreshService();
        trsvc.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
                wse -> {
                    ControllerCommunicator.getInstance().unbindStatusBar();
                    ControllerCommunicator.getInstance().enableDBButtons();
                    rootCategories = trsvc.getResult();
                    createTree();
                });
        //tree data not auto updated since its recreated everytime on refresh
    }

    public void secondaryInit() {
        //refreshTags();
    }

    @FXML
    private void handleRefreshButtonAction() {
        refreshTags();
    }

//    @FXML
//    private void handleAddTagButtonAction() {
//        //TODO
//    }

    private void refreshTags() {
        ControllerCommunicator.getInstance().bindStatusBar(trsvc);
        ControllerCommunicator.getInstance().disableDBButtons();
        trsvc.reset();
        trsvc.start();
    }

    public void disableButtons() {
        refreshButton.setDisable(true);
//        addTaskButton.setDisable(true);
//        editButton.disableProperty().unbind();
//        editButton.setDisable(true);
//        deleteButton.disableProperty().unbind();
//        deleteButton.setDisable(true);
    }

    public void enableButtons() {
        refreshButton.setDisable(false);
//        addTaskButton.setDisable(false);
//        editButton.disableProperty().bind(tableView.getSelectionModel().selectedItemProperty().isNull());
//        deleteButton.disableProperty().bind(tableView.getSelectionModel().selectedItemProperty().isNull());
    }

    private void createTree(){
        CategoryEntity root = new CategoryEntity("root", null, rootCategories, new ArrayList<>());
        CheckBoxTreeItem<TreeEntityProxy> rootItem = growTree(root);
        rootItem.setExpanded(true);
        treeView.setRoot(rootItem);
    }

    private CheckBoxTreeItem<TreeEntityProxy> growTree(CategoryEntity category) {
        CheckBoxTreeItem<TreeEntityProxy> categoryItem = new CheckBoxTreeItem<>(new TreeEntityProxy(category, null));
        categoryItem.setExpanded(true);
        if (category != null) {
            for (CategoryEntity subcategory : category.getSubcategories()) {
                categoryItem.getChildren().add(growTree(subcategory));
            }
            for (TagEntity tag : category.getTags()) {
                CheckBoxTreeItem<TreeEntityProxy> tagItem = new CheckBoxTreeItem<>(new TreeEntityProxy(null, tag));
                categoryItem.getChildren().add(tagItem);
            }
        } else {
            System.err.println("Null pointer at category at grow tree");
        }
        return categoryItem;
    }


}
