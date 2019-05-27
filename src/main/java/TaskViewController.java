import JPAobjects.TaskEntity;
import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static java.lang.System.exit;

public class TaskViewController extends Controller {
    @FXML
    private TableView<TaskEntity> tableView;
    @FXML
    private TableColumn<TaskEntity, Integer> idColumn;
    @FXML
    private TableColumn<TaskEntity, String> nameColumn;
    @FXML
    private TableColumn<TaskEntity, String> doneColumn;
    @FXML
    private TableColumn<TaskEntity, String> tagColumn;
    @FXML
    private Button refreshButton;
    @FXML
    private Button addTaskButton;
    @FXML
    private Button editButton;
    @FXML
    private Button deleteButton;

    private ObservableList<TaskEntity> tableData;
    private Stage taskStage = null;
    private RefreshService rsvc;
    private DeleteTaskService dtsvc;

    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        ControllerCommunicator.getInstance().registerTaskViewController(this);
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        doneColumn.setCellValueFactory(new PropertyValueFactory<>("is_done"));
        tagColumn.setCellValueFactory(new PropertyValueFactory<>("tags"));

        //CREATE SERVICES
        rsvc = new RefreshService();
        dtsvc = new DeleteTaskService();
        //ASSIGN ON SUCCEED METHODS
        rsvc.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
                wse -> {
                    if (rsvc.getValue()) {
                        tableView.setPlaceholder(new Label("No tasks :]"));
                    }
                    else {
                        tableView.setPlaceholder(new Label("Loading failed."));
                    }
                    ControllerCommunicator.getInstance().unbindStatusBar();
                    ControllerCommunicator.getInstance().enableDBButtons();
                });
        dtsvc.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
                wse -> {
                    ControllerCommunicator.getInstance().unbindStatusBar();
                    ControllerCommunicator.getInstance().enableDBButtons();
                    tableData.remove(dtsvc.getTask());
                });

        //ASSIGN REFRESH SERVICE RESULT
        tableData = rsvc.getResult();
        tableView.setItems(tableData); //TODO NEED TO SET ONLY ONCE?
    }

    public void manualInit() {
        refreshTasks();
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
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FXML_TaskWindow.fxml"));
            fxmlLoader.setControllerFactory(clazz -> {
                return new TaskWindowController(task);
            });
            Parent parent = fxmlLoader.load();
            Scene scene = new Scene(parent, 600, 400);
            taskStage = new Stage();
            taskStage.initModality(Modality.APPLICATION_MODAL);
            taskStage.setScene(scene);
            taskStage.getIcons().add(new Image(getClass().getResourceAsStream("icon.png")));
            TaskWindowController twc = fxmlLoader.getController();
            twc.setStageReference(taskStage);
            taskStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            exit(1);
        }
    }

    private void refreshTasks() {
        tableView.setPlaceholder(new Label("Loading..."));
        ControllerCommunicator.getInstance().bindStatusBar(rsvc);
        ControllerCommunicator.getInstance().disableDBButtons();
        rsvc.reset();
        rsvc.start();
    }

    private void deleteTasks() {
        TaskEntity task = tableView.getSelectionModel().getSelectedItem();
        if (task != null) {
            // there is a selection -> delete
            dtsvc.setTask(task);
            ControllerCommunicator.getInstance().bindStatusBar(dtsvc);
            ControllerCommunicator.getInstance().disableDBButtons();
            dtsvc.reset();
            dtsvc.start();
        }
    }

    public TableView<TaskEntity> getTable() {
        return tableView;
    }

    public void disableButtons() {
        refreshButton.setDisable(true);
        addTaskButton.setDisable(true);
        editButton.disableProperty().unbind();
        editButton.setDisable(true);
        deleteButton.disableProperty().unbind();
        deleteButton.setDisable(true);
    }

    public void enableButtons() {
        refreshButton.setDisable(false);
        addTaskButton.setDisable(false);
        editButton.disableProperty().bind(tableView.getSelectionModel().selectedItemProperty().isNull());
        deleteButton.disableProperty().bind(tableView.getSelectionModel().selectedItemProperty().isNull());
    }
}
