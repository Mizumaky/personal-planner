import JPAobjects.CategoryEntity;
import JPAobjects.TagEntity;
import JPAobjects.TaskEntity;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTreeCell;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.ResourceBundle;

public class TaskWindowController extends Controller {
    @FXML
    TextField titleTextField;
    @FXML
    TextArea descTextArea;
    @FXML
    Button cancelButton;
    @FXML
    Button submitButton;
    @FXML
    Label statusLabel;
    @FXML
    TreeView<TreeEntityProxy> tagTreeView;

    private TaskEntity task;
    CheckBoxTreeItem<TreeEntityProxy> rootItem;
    private ArrayList<CategoryEntity> rootTagCategories;
    private final ArrayList<TagEntity> selectedTags = new ArrayList<>();
    private TaskAddService tasvc = null;
    private TaskEditService tesvc = null;

    public TaskWindowController(TaskEntity task) {
        super();
        this.task = task;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //PREPARE FIELDS AND TREE
        titleTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            submitButton.setDisable(newValue.trim().isEmpty());
        });
        tagTreeView.setShowRoot(false);
        tagTreeView.setCellFactory(CheckBoxTreeCell.<TreeEntityProxy>forTreeView());
        rootTagCategories = ControllerCommunicator.getInstance().getRootTagCategories();
        createTagTree();
        if (task != null) {
            titleTextField.setText(task.getTitle());
            descTextArea.setText(task.getDescription());
            for (TagEntity tag : task.getTags()) {
                checkInTree(rootItem, tag);
            }
        }

        //ASSIGN HANDLERS
        tasvc = new TaskAddService();
        tesvc = new TaskEditService();
        tasvc.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
                wse -> {
                    if (tasvc.getValue()) {
                        ControllerCommunicator.getInstance().getTaskViewData().add(tasvc.getTask());
                    } else {
                        thisStage.show();
                        task = null;
                        submitButton.setText("Try again");
                        submitButton.setDisable(false);
                    }
                    ControllerCommunicator.getInstance().unbindStatusBar();
                    ControllerCommunicator.getInstance().enableDBButtons();
                });
        tesvc.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
                wse -> {
                    if (tesvc.getValue()) {
                        ControllerCommunicator.getInstance().getTaskViewTable().refresh();
                    } else {
                        thisStage.show();
                        submitButton.setText("Try again");
                        submitButton.setDisable(false);
                    }
                    ControllerCommunicator.getInstance().unbindStatusBar();
                    ControllerCommunicator.getInstance().enableDBButtons();
                });

    }


    @FXML
    public void handleCancelButtonAction(ActionEvent event) {
        thisStage.close();
    }

    @FXML
    public void handleSubmitButtonAction(ActionEvent event) {
        if (!titleLengthCheck() || !descriptionLengthCheck()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Input error");
            alert.setHeaderText(null);
            if (!titleLengthCheck())
                alert.setContentText("Title must be 1 to 255 characters!");
            else
                alert.setContentText("Description must be shorter than 1024 characters!");
            alert.getDialogPane().getStylesheets().add(getClass().getResource("CSS_my_style_1.css").toExternalForm());
            alert.showAndWait();
            return;
        }

        submitButton.setDisable(true);
        ControllerCommunicator.getInstance().disableDBButtons();
        if (task != null) {
            ControllerCommunicator.getInstance().bindStatusBar(tesvc);
            task.setTitle(titleTextField.getText());
            task.setDescription(descTextArea.getText());
            task.setTags(new HashSet<>(selectedTags));
            tesvc.reset();
            tesvc.setTask(task);
            tesvc.start();
        } else {
            ControllerCommunicator.getInstance().bindStatusBar(tasvc);
            task = new TaskEntity(titleTextField.getText(), descTextArea.getText(),false, new HashSet<>(selectedTags));
            tasvc.reset();
            tasvc.setTask(task);
            tasvc.start();
        }

        thisStage.close();
    }

    public boolean titleLengthCheck() {
        return !titleTextField.getText().isEmpty() && titleTextField.getText().length() < 256;
    }
    public boolean descriptionLengthCheck() {
        return descTextArea.getText().length() < 1024;
    }

    public void setRootTagCategories(ArrayList<CategoryEntity> rootTagCategories) {
        this.rootTagCategories = rootTagCategories;
    }

    public void createTagTree(){
        CategoryEntity root = new CategoryEntity("root", null, rootTagCategories, new ArrayList<>());
        rootItem = growTree(root);
        rootItem.setExpanded(true);
        rootItem.setSelected(false);
        tagTreeView.setRoot(rootItem);
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
                        selectedTags.add(tagItem.getValue().getTag());
                    } else {
                        selectedTags.remove(tagItem.getValue().getTag());
                    }
                });
            }
        } else {
            System.err.println("Null pointer at category at grow tree");
        }
        return categoryItem;
    }


    private void checkInTree(CheckBoxTreeItem<TreeEntityProxy> node , TagEntity tag) {
        if (node != null) {
            if (node.getValue().getType() == TreeEntityProxy.Type.TAG && node.getValue().getTag().equals(tag)) {
                node.setSelected(true);
                System.out.println(tag + " selected");
                return;
            }
            for (TreeItem<TreeEntityProxy> child : node.getChildren()) {
                checkInTree((CheckBoxTreeItem<TreeEntityProxy>) child, tag);
            }
        }
    }
}
