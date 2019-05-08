import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.*;
import java.util.ArrayList;
import java.util.Optional;


public class PersonalPlannerApp extends Application {

    private Connection dbConnection = null;

    Pane paneMain = new VBox(10);
    Label labelStatus = new Label("Not connected.");
    Pane paneButtons = new HBox(6);
    Button btnRefreshTableTasks = new Button("Refresh");
    Button btnAddTask = new Button("Add a task");

    TableView<Task> tableTasks = new TableView<>();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        paneMain.getChildren().add(labelStatus);
        paneMain.getChildren().add(paneButtons);
        paneButtons.getChildren().add(btnRefreshTableTasks);
        btnRefreshTableTasks.setOnAction(event -> {
            DBConnectivity.checkConnection(dbConnection, labelStatus);
            getTableTasks();
        });
        paneButtons.getChildren().add(btnAddTask);
        btnAddTask.setOnAction(event -> {
            DBConnectivity.checkConnection(dbConnection, labelStatus);
            addTask();
        });
        paneMain.getChildren().add(tableTasks);

        Scene sceneMain = new Scene(paneMain, 420, 500);
        primaryStage.setScene(sceneMain);
        primaryStage.setTitle("Personal Planner");
        primaryStage.show();

        dbConnection = DBConnectivity.connect(labelStatus);
    }

    private void printTasks(){
        Statement stmt = null;
        try {
            stmt = dbConnection.createStatement();
            ResultSet resultSet = stmt.executeQuery( "SELECT * FROM task;" );
            while ( resultSet.next() ) {
                int task_id = resultSet.getInt("task_id");
                String title = resultSet.getString("title");
                boolean is_done  = resultSet.getBoolean("is_done");
                Timestamp due_date = resultSet.getTimestamp("due_date");
                System.out.println( "TASK_ID = " + task_id );
                System.out.println( "TITLE = " + title );
                System.out.println( "DUE_DATE = " + due_date );
                System.out.println( "IS_DONE = " + is_done );
                System.out.println();
            }
            resultSet.close();
            stmt.close();
            //dbConnection.close();
            System.out.println("Operation done successfully");
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }

    }

    /**
     * Wraps functions for getting data from database, if the app's task table doesnt have any columns then adding them
     * and then loading the rows into it.
     */
    private void getTableTasks() {
        ResultSet resultSet;
        labelStatus.setText("Querrying data from database...");
        try {
            resultSet = getData();
        } catch (SQLException e) {
            labelStatus.setText("ERROR: SQL query failed.");
            System.err.println(e.getMessage());
            e.printStackTrace();
            return; // false;
        }

        if (tableTasks.getColumns().isEmpty()) {
            labelStatus.setText("Assigning column names...");
            try {
                createTableTasksColumns(resultSet);
            } catch (SQLException e) {
                labelStatus.setText("ERROR: Assigning column names failed.");
                System.err.println(e.getMessage());;
                e.printStackTrace();
                return; // false;
            }
        }

        ObservableList dbData = null;
        labelStatus.setText("Loading and preparing rows for the table...");
        try {
            dbData = FXCollections.observableArrayList(extractTasks(resultSet));
        } catch (SQLException e) {
            labelStatus.setText("ERROR: Extracting values from data failed.");
            e.printStackTrace();
            return; // false;
        }

        labelStatus.setText("Updating the table with data...");
        tableTasks.setItems(dbData);
        labelStatus.setText("Table data update successful.");
        //return true;
    }

    private ResultSet getData() throws SQLException {
        //Extracting data from Databasee
        String selectSQL = "SELECT * FROM task;";
        PreparedStatement preparedStatement = dbConnection.prepareStatement(selectSQL);
        return preparedStatement.executeQuery();
    }

    private void createTableTasksColumns(ResultSet resultSet) throws SQLException {
        //Giving readable names to columns
        for (int i = 0 ; i < resultSet.getMetaData().getColumnCount(); i++) {
            TableColumn column = new TableColumn<>();
            String dbColumnName = resultSet.getMetaData().getColumnName(i+1);
            switch (dbColumnName) {
                case "task_id":
                    column.setText("ID #");
                    break;
                case "title":
                    column.setText("Title");
                    break;
                case "is_done":
                    column.setText("Is completed");
                    break;
                case "due_date":
                    column.setText("Due date");
                    break;
                default:
                    System.err.println("Unexpected column name detected");
                    throw new SQLException();
            }
            column.setCellValueFactory(new PropertyValueFactory<>(dbColumnName)); //Setting cell property value to correct variable from Task class.
            tableTasks.getColumns().add(column);
        }
    }

    /**
     * Extracts an Arraylist of Task objects from a ResultSet of a current connection
     * @param resultSet A ResultSet you get from executing a SELECT query
     * @return An Arraylist of Task objects extracted from the database
     * @throws SQLException if get operations from result set werent succesful
     */
    private ArrayList<Task> extractTasks(ResultSet resultSet) throws SQLException {
        ArrayList<Task> data = new ArrayList<>();
        while (resultSet.next()) {
            Task task = new Task();
            task.setTask_id(resultSet.getInt("task_id"));
            task.setTitle(resultSet.getString("title"));
            task.setIs_done(resultSet.getBoolean("is_done"));
            task.setDue_date(resultSet.getTimestamp("due_date"));
            data.add(task);
        }

        //TODO add sorting according to the settings, store in an xml?
        return data;
    }

    private void addTask() {
        Dialog<Task> dialogAddTask = new Dialog<>();
        dialogAddTask.setTitle("Create a task");
        dialogAddTask.setHeaderText("Fill in the properties of the new task");

//        // Set the icon (must be included in the project).
//        //dialogAddTask.setGraphic(new ImageView(this.getClass().getResource("login.png").toString()));
//
        // Set the button types.
        ButtonType createBtnType = new ButtonType("Create", ButtonBar.ButtonData.OK_DONE);
        dialogAddTask.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL, createBtnType);
//
        // Create the username and password labels and fields.
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField taskID = new TextField();
        taskID.setPromptText("number id");
        TextField taskTitle = new TextField();
        taskTitle.setPromptText("name of the task");

        grid.add(new Label("Task ID:"), 0, 0);
        grid.add(taskID, 1, 0);
        grid.add(new Label("Title:"), 0, 1);
        grid.add(taskTitle, 1, 1);

        // Enable/Disable login button depending on whether a username was entered.
        Node createBtn = dialogAddTask.getDialogPane().lookupButton(createBtnType);
        createBtn.setDisable(true);

        // Do some validation (using the Java 8 lambda syntax).
        taskID.textProperty().addListener((observable, oldValue, newValue) -> {
            boolean condition = newValue.trim().isEmpty() || taskTitle.textProperty().get().trim().isEmpty();
            createBtn.setDisable(condition);
        });
        taskTitle.textProperty().addListener((observable, oldValue, newValue) -> {
            boolean condition = newValue.trim().isEmpty() || taskID.textProperty().get().trim().isEmpty();
            createBtn.setDisable(condition);
        });

        dialogAddTask.getDialogPane().setContent(grid);
//
//        // Request focus on the username field by default.
//        Platform.runLater(() -> username.requestFocus());
//
//        // Convert the result to a username-password-pair when the login button is clicked.
//        dialogAddTask.setResultConverter(dialogButton -> {
//            if (dialogButton == loginButtonType) {
//                return new Pair<>(username.getText(), password.getText());
//            }
//            return null;
//        });
//
        Optional<Task> result = dialogAddTask.showAndWait();
//
//        result.ifPresent(usernamePassword -> {
//            System.out.println("Username=" + usernamePassword.getKey() + ", Password=" + usernamePassword.getValue());
//        });
    }
}
