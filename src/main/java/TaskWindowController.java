import JPAobjects.TagEntity;
import JPAobjects.TaskEntity;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.HashSet;
import java.util.ResourceBundle;

public class TaskWindowController implements Initializable {
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

    private TaskEntity task = null;
    private AddTaskService atsvc;

    //private

    public TaskWindowController() {}

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        titleTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            submitButton.setDisable(newValue.trim().isEmpty());
        });
        atsvc.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
                wse -> {
                    atsvc.reset();
                    ControllerCommunicator.getInstance().unbindStatusBar();
                    ControllerCommunicator.getInstance().enableDBButtons();
                    if (atsvc.getValue()) {
                    } else {
                        submitButton.setText("Try again");
                        submitButton.setDisable(false);
                    }
                });
    }

    public void setTask(TaskEntity task) {
        this.task = task;
    }

    public TaskEntity getTask() {
        return task;
    }

    private void closeStage(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleCancelButtonAction(ActionEvent event) {
        closeStage(event);
    }
    @FXML
    private void handleSubmitButtonAction(ActionEvent event) {
        submitButton.setDisable(true);
        task = new TaskEntity(titleTextField.getText(),descTextArea.getText(),false, new HashSet<>());
        AddTaskService atsvc = new AddTaskService();
        ControllerCommunicator.getInstance().bindStatusBar(atsvc);
        ControllerCommunicator.getInstance().disableDBButtons();
        closeStage(event);
    }
}
