import JPAobjects.TaskEntity;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

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
        if (task != null) {
            titleTextField.setText(task.getTitle());
            descTextArea.setText(task.getDescription());
        }
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

    public boolean titleLengthCheck() {
        if (titleTextField.getText().isEmpty() || titleTextField.getText().length() >= 256)
            return false;
        return true;
    }
    public boolean descriptionLengthCheck() {
        if (descTextArea.getText().length() >= 1024)
            return false;
        return true;
    }

//    public void setTask(TaskEntity task) {
//        this.task = task;
//    }
//
//    public TaskEntity getTask() {
//        return task;
//    }
}
