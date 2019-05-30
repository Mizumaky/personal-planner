import JPAobjects.TagEntity;
import JPAobjects.TaskEntity;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.WorkerStateEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.System.exit;

/**
 * The type Task view controller.
 */
public class TaskViewController extends Controller {
    private static final Logger LOGGER = Logger.getLogger(TaskViewController.class.getName());
    @FXML
    private TableView<TaskEntity> tableView;
    @FXML
    private TableColumn<TaskEntity, String> nameColumn;
    @FXML
    private TableColumn<TaskEntity, Boolean> doneColumn;
    @FXML
    private TableColumn<TaskEntity, String> tagColumn;
    @FXML
    private TableColumn<TaskEntity, String> descColumn;
    @FXML
    private Button refreshButton;
    @FXML
    private Button addTaskButton;
    @FXML
    private Button editButton;
    @FXML
    private Button deleteButton;

    private final ObservableList<TaskEntity> tableData  = FXCollections.observableArrayList();
    private final ObservableList<TagEntity> filteredOutTags = FXCollections.observableArrayList();
    private TaskRefreshService trsvc;
    private TaskDeleteService tdsvc;
    private TaskEditService tesvc;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        LOGGER.info("Initializing task view controller");
        ControllerCommunicator.registerTaskViewController(this);
        //CREATE SERVICES
        trsvc = new TaskRefreshService();
        tdsvc = new TaskDeleteService();
        tesvc = new TaskEditService();

        //PREPARE AND CONNECT DATA TO SOURCE
        // Wrap the ObservableList in a FilteredList (initially display all data).
        FilteredList<TaskEntity> filteredData = new FilteredList<>(tableData, p -> true);
        // Set the filter Predicate whenever the filter changes.
        filteredOutTags.addListener(new ListChangeListener<TagEntity>() {
            @Override
            public void onChanged(Change<? extends TagEntity> c) {
                filteredData.setPredicate(taskEntity -> {
                    // If filter is empty or tag not found in task then show it
                    for (TagEntity tag : filteredOutTags) {
                        if (taskEntity.getTags().contains(tag))
                            return false; //Contains not wanted tag
                    }
                    return true;
                });
            }
        });
        // Wrap the FilteredList in a SortedList.
        SortedList<TaskEntity> sortedData = new SortedList<>(filteredData);
//        //TODO - when i change a task, the sorted list isnt notified about that, so it doesnt resort, and is kinda buggy
////        sortedData.setComparator((o1, o2) -> {
////            if (o1.getIs_done() && o2.getIs_done() || !o1.getIs_done() && !o2.getIs_done() )
////                return o1.getTitle().compareTo(o2.getTitle());
////            else if (o1.getIs_done()) //not done tasks first - return negative so that this one gets behind
////                return -1;
////            else
////                return 1;
////        });
        // Bind the SortedList comparator to the TableView comparator.
        sortedData.comparatorProperty().bind(tableView.comparatorProperty());

        //SET UP TABLE
        doneColumn.setCellValueFactory(param -> {
            TaskEntity task = param.getValue();
            return new SimpleBooleanProperty(task.getIs_done());
        });
        doneColumn.setCellFactory(CheckBoxTableCell.forTableColumn(new Callback<Integer, ObservableValue<Boolean>>() {
            @Override
            public ObservableValue<Boolean> call(Integer index) {
                ObservableValue<Boolean> value = doneColumn.getCellObservableValue(index);
                value.addListener((obs, wasSelected, isSelected) -> {
                    LOGGER.info("Checkbox tick event handler started");
                    TaskEntity task = sortedData.get(index);
                    task.setIs_done(isSelected);
                    tesvc.setTask(task);
                    tesvc.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
                            wse -> {
                                LOGGER.info("Task edit service on-succeed handler started");
                                if (!tesvc.getValue()) {
                                    //REVERT CHANGES
                                    task.setIs_done(wasSelected);
                                }
                                ControllerCommunicator.unbindStatusBar();
                                ControllerCommunicator.enableDBButtons();
                                tableView.refresh();
                            });
                    ControllerCommunicator.bindStatusBar(tesvc);
                    ControllerCommunicator.disableDBButtons();
                    tesvc.reset();
                    LOGGER.info("Starting task edit service");
                    tesvc.start();
                });
                return value;
            }
        }));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        tagColumn.setCellValueFactory(new PropertyValueFactory<>("tags"));
        descColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        tableView.setEditable(true);

        // ADD DATA TO TABLE
        tableView.setItems(sortedData);

        //ASSIGN SERVICE COMPLETION HANDLERS
        trsvc.setResultList(tableData);
        trsvc.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
                wse -> {
                    if (trsvc.getValue()) {
                        tableView.setPlaceholder(new Label("No tasks :]"));
                    }
                    else {
                        tableView.setPlaceholder(new Label("Loading failed!"));
                    }
                    ControllerCommunicator.unbindStatusBar();
                    ControllerCommunicator.enableDBButtons();
                    ControllerCommunicator.refreshTagView();
                    tableView.refresh();
                });
        tdsvc.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
                wse -> {
                    ControllerCommunicator.unbindStatusBar();
                    ControllerCommunicator.enableDBButtons();
                    tableData.remove(tdsvc.getTask());
                    tableView.refresh();
                });
        LOGGER.info("Task view controller initialized");
    }

    @FXML
    private void handleAddTaskButtonAction() {
        createTaskWindow(null);
    }

    @FXML
    private void handleEditButtonAction() {
        TaskEntity task = tableView.getSelectionModel().getSelectedItem();
        if (task != null) {
            createTaskWindow(task);
        }
    }

    @FXML
    private void handleRefreshButtonAction() {
        refreshTasks();
    }

    @FXML
    private void handleDeleteButtonAction() {
        deleteTasks();
    }

    private void createTaskWindow(TaskEntity task) {
        try {
            LOGGER.info("Loading task window");
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FXML_TaskWindow.fxml"));
            fxmlLoader.setControllerFactory(clazz -> new TaskWindowController(task));
            Parent parent = fxmlLoader.load();
            Scene scene = new Scene(parent, 600, 700);
            Stage taskStage = new Stage();
            taskStage.initModality(Modality.APPLICATION_MODAL);
            taskStage.setScene(scene);
            taskStage.getIcons().add(new Image(getClass().getResourceAsStream("icon.png")));
            TaskWindowController twc = fxmlLoader.getController();
            twc.setStageReference(taskStage);
            taskStage.show();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to load FXML", e);
            exit(1);
        }
    }

    private void deleteTasks() {
        TaskEntity task = tableView.getSelectionModel().getSelectedItem();
        if (task != null) {
            // there is a selection -> delete
            tdsvc.setTask(task);
            ControllerCommunicator.bindStatusBar(tdsvc);
            ControllerCommunicator.disableDBButtons();
            tdsvc.reset();
            LOGGER.info("Starting task delete service");
            tdsvc.start();
        }
    }

    /**
     * Refresh tasks.
     */
    public void refreshTasks() {
        tableView.setPlaceholder(new Label("Loading..."));
        ControllerCommunicator.bindStatusBar(trsvc);
        ControllerCommunicator.disableDBButtons();
        trsvc.reset();
        LOGGER.info("Starting task refresh service");
        trsvc.start();
    }

    /**
     * Gets the task table view.
     *
     * @return the table
     */
    public TableView<TaskEntity> getTable() {
        return tableView;
    }

    /**
     * Gets task table data list.
     *
     * @return the table data
     */
    public ObservableList<TaskEntity> getTableData() {
        return tableData;
    }

    /**
     * Disable database acess buttons.
     */
    public void disableButtons() {
        refreshButton.setDisable(true);
        addTaskButton.setDisable(true);
        editButton.disableProperty().unbind();
        editButton.setDisable(true);
        deleteButton.disableProperty().unbind();
        deleteButton.setDisable(true);
        tableView.setEditable(false);
    }

    /**
     * Enable database acess  buttons.
     */
    public void enableButtons() {
        refreshButton.setDisable(false);
        addTaskButton.setDisable(false);
        editButton.disableProperty().bind(tableView.getSelectionModel().selectedItemProperty().isNull());
        deleteButton.disableProperty().bind(tableView.getSelectionModel().selectedItemProperty().isNull());
        tableView.setEditable(true);
    }

    /**
     * Gets unselected tags list.
     *
     * @return the unselected tags list
     */
    public ObservableList<TagEntity> getUnselectedTagsList() {
        return filteredOutTags;
    }
}
