import JPAobjects.CategoryEntity;
import JPAobjects.TagEntity;
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
import java.util.logging.Logger;

public class TagViewController extends Controller {
    private static final Logger LOGGER = Logger.getLogger(TagViewController.class.getName());

    @FXML
    private TreeView<TreeEntityProxy> treeView;
//    @FXML
//    private Button refreshButton;
//    @FXML
//    private Button addTagButton;

    private TagRefreshService trsvc = null;
    private final ArrayList<CategoryEntity> rootCategories = new ArrayList<>();
    private ObservableList<TagEntity> unselectedTags;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        LOGGER.info("Initializing tag view controller");
        ControllerCommunicator.registerTagViewController(this);
        //init tree view
        treeView.setShowRoot(false);
        treeView.setCellFactory(CheckBoxTreeCell.<TreeEntityProxy>forTreeView());
        //init services
        trsvc = new TagRefreshService();
        trsvc.setResultList(rootCategories);
        trsvc.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
                wse -> {
                    ControllerCommunicator.unbindStatusBar();
                    ControllerCommunicator.enableDBButtons();
                    createTree();
                });
        //tree data not auto updated since its recreated everytime on refresh
        LOGGER.info("Tag view controller initialized");
    }

//    @FXML
//    private void handleRefreshButtonAction() {
//        refreshTags();
//    }

//    @FXML
//    private void handleAddTagButtonAction() {
//
//    }

    public void refreshTags() {
        ControllerCommunicator.bindStatusBar(trsvc);
        ControllerCommunicator.disableDBButtons();
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
        LOGGER.info("Creating a tag selector tree");
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
                tagItem.selectedProperty().addListener((observable, oldValue, newValue) -> {
                    if (newValue) {
                        unselectedTags.remove(tagItem.getValue().getTag());
                    } else {
                        unselectedTags.add(tagItem.getValue().getTag());
                    }
                });
            }
        } else {
            System.err.println("Null pointer at category at grow tree");
        }
        return categoryItem;
    }

    public ArrayList<CategoryEntity> getRootCategories() {
        return rootCategories;
    }

    public void setUnselectedTagsList(ObservableList<TagEntity> tagList) {
        unselectedTags = tagList;
    }

}
