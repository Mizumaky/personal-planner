import JPAobjects.TaskEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import static java.lang.System.exit;

public class ControllerTaskTable implements Initializable {
    @FXML
    private TableView<TaskEntity> tableView;
    @FXML
    private TableColumn<TaskEntity, Integer> idColumn;
    @FXML
    private TableColumn<TaskEntity, String> nameColumn;
    @FXML
    private TableColumn<TaskEntity, String> doneColumn;
    @FXML
    private Button refreshButton;
    @FXML
    private Button addTaskButton;
    @FXML
    private Button editButton;
    @FXML
    private Button deleteButton;

    private ObservableList<TaskEntity> tableData = null;
    private ApplicationMain mainApp;

    public void setMainApp(ApplicationMain mainApp) {
        this.mainApp = mainApp;
    }

    public ControllerTaskTable() {}

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        doneColumn.setCellValueFactory(new PropertyValueFactory<>("is_done"));

        System.err.println("getting data...");
        updateData(); //TODO takes long time, probably do as a task
        tableView.setItems(tableData);
        System.err.println("got data:\n");
        System.out.println(tableData);
    }

    public void updateData() {
        tableView.setPlaceholder(new Label("Loading..."));
        List<TaskEntity> userList = PersistenceManager.fetchAllTasks();
        if (tableData == null) {
            tableData = FXCollections.observableArrayList(userList);
        } else {
            tableData.clear();
            tableData.addAll(userList);
        }
        tableView.setPlaceholder(new Label("No tasks :]"));
    }

    @FXML
    private void handleRefreshButtonAction() {
        updateData();
    }

    @FXML
    private void handleAddTaskButtonAction() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FXML_scene_add_task.fxml"));
            Parent parent = fxmlLoader.load();
            Scene scene = new Scene(parent, 600, 400);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.getIcons().add(new Image(getClass().getResourceAsStream( "icon.png" )));
            stage.showAndWait();
            ControllerSceneAddTask csat = fxmlLoader.getController();
            if (csat.getResult() != null) {
                updateData();
            }
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
        deleteButton.setDisable(true);
        TaskEntity task = tableView.getSelectionModel().getSelectedItem();
        if (task != null) {
            // there is a selection -> delete
            try {
                PersistenceManager.remove(task);
                //if db removal succesful
                tableView.getItems().remove(task);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        deleteButton.setDisable(false);
    }
}
