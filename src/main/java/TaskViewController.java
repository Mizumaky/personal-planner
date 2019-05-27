import JPAobjects.TaskEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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

public class TaskViewController implements Initializable {
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
    private ApplicationMain mainApp;
    private RefreshService rsvc;
    private DeleteTaskService dtsvc;

    public void setMainApp(ApplicationMain mainApp) {
        this.mainApp = mainApp;
    }

    public TaskViewController() {}

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ControllerCommunicator.getInstance().registerTaskViewController(this);
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        doneColumn.setCellValueFactory(new PropertyValueFactory<>("is_done"));
        tagColumn.setCellValueFactory(new PropertyValueFactory<>("tags"));

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
                    //tableView.refresh(); //TODO necessary?
                    //tableView.getItems().add(rsvc.getTask()); //OR THIS?
                    rsvc.reset();
                });
        dtsvc.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
                wse -> {
                    ControllerCommunicator.getInstance().unbindStatusBar();
                    ControllerCommunicator.getInstance().enableDBButtons();
                    //tableView.refresh(); //TODO necessary?
                    //tableView.getItems().remove(dtsvc.getTask()); //OR THIS?
                    dtsvc.reset();
                });

        //INITIAL DATA LOAD

        tableView.setItems(rsvc.getResult()); //TODO NEED TO SET ONLY ONCE?
        //refreshTasks(); //TODO
    }

    public void disableButtons() {
        refreshButton.setDisable(true);
        addTaskButton.setDisable(true);
        editButton.setDisable(true);
        deleteButton.setDisable(true);
    }

    public void enableButtons() {
        refreshButton.setDisable(false);
        addTaskButton.setDisable(false);
        editButton.setDisable(false);
        deleteButton.setDisable(false);
    }

    @FXML
    private void handleRefreshButtonAction() {
        refreshTasks();
    }

    private void refreshTasks() {
        tableView.setPlaceholder(new Label("Loading..."));
        ControllerCommunicator.getInstance().bindStatusBar(rsvc);
        ControllerCommunicator.getInstance().disableDBButtons();
        rsvc.start();
    }

    @FXML
    private void handleAddTaskButtonAction() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FXML_TaskWindow.fxml"));
            Parent parent = fxmlLoader.load();
            Scene scene = new Scene(parent, 600, 400);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.getIcons().add(new Image(getClass().getResourceAsStream( "icon.png" )));
            TaskWindowController twc = fxmlLoader.getController();
            //twc.
            //stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            exit(1);
        }
    }

    @FXML
    private void handleEditButtonAction() {

    }

    @FXML
    private void handleDeleteButtonAction() {
        TaskEntity task = tableView.getSelectionModel().getSelectedItem();
        if (task != null) {
            // there is a selection -> delete
            dtsvc.setTask(task);
            ControllerCommunicator.getInstance().bindStatusBar(rsvc);
            ControllerCommunicator.getInstance().disableDBButtons();
            dtsvc.start();
        }
    }
}
