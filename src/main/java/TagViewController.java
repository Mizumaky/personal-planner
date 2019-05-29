import JPAobjects.CategoryEntity;
import JPAobjects.TagEntity;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBoxTreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.CheckBoxTreeCell;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class TagViewController extends Controller {
    @FXML
    private TreeView<TreeEntityProxy> treeView;
//    @FXML
//    private Button refreshButton;
//    @FXML
//    private Button addTagButton;

    private TagRefreshService trsvc = null;
    private ArrayList<CategoryEntity> rootCategories = new ArrayList<>();
    private ObservableList<TagEntity> unselectedTags;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ControllerCommunicator.getInstance().registerTagViewController(this);
        //init tree view
        treeView.setShowRoot(false);
        treeView.setCellFactory(CheckBoxTreeCell.<TreeEntityProxy>forTreeView());
        //init services
        trsvc = new TagRefreshService();
        trsvc.setResultList(rootCategories);
        trsvc.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
                wse -> {
                    ControllerCommunicator.getInstance().unbindStatusBar();
                    ControllerCommunicator.getInstance().enableDBButtons();
                    createTree();
                });
        //tree data not auto updated since its recreated everytime on refresh
    }

    @FXML
    private void handleRefreshButtonAction() {
        refreshTags();
    }

//    @FXML
//    private void handleAddTagButtonAction() {
//
//    }

    public void refreshTags() {
        ControllerCommunicator.getInstance().bindStatusBar(trsvc);
        ControllerCommunicator.getInstance().disableDBButtons();
        trsvc.reset();
        trsvc.start();
    }

    public void disableButtons() {
//        refreshButton.setDisable(true);
//        addTaskButton.setDisable(true);
//        editButton.disableProperty().unbind();
//        editButton.setDisable(true);
//        deleteButton.disableProperty().unbind();
//        deleteButton.setDisable(true);
    }

    public void enableButtons() {
//        refreshButton.setDisable(false);
//        addTaskButton.setDisable(false);
//        editButton.disableProperty().bind(tableView.getSelectionModel().selectedItemProperty().isNull());
//        deleteButton.disableProperty().bind(tableView.getSelectionModel().selectedItemProperty().isNull());
    }

    private void createTree(){
        CategoryEntity root = new CategoryEntity("root", null, rootCategories, new ArrayList<>());
        CheckBoxTreeItem<TreeEntityProxy> rootItem = growTree(root);
        rootItem.setExpanded(true);
        rootItem.setSelected(true);
        treeView.setRoot(rootItem);
    }

    private CheckBoxTreeItem<TreeEntityProxy> growTree(CategoryEntity category) {
        CheckBoxTreeItem<TreeEntityProxy> categoryItem = new CheckBoxTreeItem<>(new TreeEntityProxy(category, null), new ImageView(getClass().getResource("folder.png").toExternalForm()));
        categoryItem.setExpanded(true);
        if (category != null) {
            for (CategoryEntity subcategory : category.getSubcategories()) {
                categoryItem.getChildren().add(growTree(subcategory));
            }
            for (TagEntity tag : category.getTags()) {
                CheckBoxTreeItem<TreeEntityProxy> tagItem = new CheckBoxTreeItem<>(new TreeEntityProxy(null, tag));
                categoryItem.getChildren().add(tagItem);
                tagItem.selectedProperty().addListener(new ChangeListener<Boolean>() {
                    @Override
                    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                        if (newValue) {
                            unselectedTags.remove(tagItem.getValue().getTag());
                        } else {
                            unselectedTags.add(tagItem.getValue().getTag());
                        }
                    }
                });
            }
        } else {
            System.err.println("Null pointer at category at grow tree");
        }
        return categoryItem;
    }


    public void setUnselectedTagsList(ObservableList<TagEntity> tagList) {
        unselectedTags = tagList;
    }

}
