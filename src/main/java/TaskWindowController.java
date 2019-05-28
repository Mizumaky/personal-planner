import JPAobjects.TaskEntity;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.net.URL;
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

    private TaskEntity task = null;
    private TaskAddService tasvc = null;
    private TaskEditService tesvc = null;

    public TaskWindowController(TaskEntity task) {
        super();
        this.task = task;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tasvc = new TaskAddService();
        tesvc = new TaskEditService();
        titleTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            submitButton.setDisable(newValue.trim().isEmpty());
        });
        tasvc.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
                wse -> {
                    if (tasvc.getValue()) {
                        ControllerCommunicator.getInstance().getTaskViewData().getItems().add(tasvc.getTask());
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
                        ControllerCommunicator.getInstance().getTaskViewData().refresh();
                    } else {
                        thisStage.show();
                        submitButton.setText("Try again");
                        submitButton.setDisable(false);
                    }
                    ControllerCommunicator.getInstance().unbindStatusBar();
                    ControllerCommunicator.getInstance().enableDBButtons();
                });
        if (task != null) {
            titleTextField.setText(task.getTitle());
            descTextArea.setText(task.getDescription());
        }
    }


    @FXML
    private void handleCancelButtonAction(ActionEvent event) {
        thisStage.close();
    }
    @FXML
    private void handleSubmitButtonAction(ActionEvent event) {
        submitButton.setDisable(true);
        ControllerCommunicator.getInstance().disableDBButtons();
        if (task != null) {
            ControllerCommunicator.getInstance().bindStatusBar(tesvc);
            task.setTitle(titleTextField.getText());
            task.setDescription(descTextArea.getText());
            //task.setTags(); //TODO
            tesvc.reset();
            tesvc.setTask(task);
            tesvc.start();
        } else {
            ControllerCommunicator.getInstance().bindStatusBar(tasvc);
            task = new TaskEntity(titleTextField.getText(), descTextArea.getText(),false, new HashSet<>());
            tasvc.reset();
            tasvc.setTask(task);
            tasvc.start();
        }

        thisStage.close();
    }

//    public void setTask(TaskEntity task) {
//        this.task = task;
//    }
//
//    public TaskEntity getTask() {
//        return task;
//    }
}
