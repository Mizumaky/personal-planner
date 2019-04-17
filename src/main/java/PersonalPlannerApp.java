import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.*;
import java.util.ArrayList;


public class PersonalPlannerApp extends Application {

    private Connection dbConnection = null;

    Pane paneMain = new VBox(10);
    Label labelStatus = new Label("Not connected.");
    Button btnRefreshTableTasks = new Button("Refresh");

    TableView<Task> tableTasks = new TableView<>();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        paneMain.getChildren().add(labelStatus);
        paneMain.getChildren().add(btnRefreshTableTasks);
        btnRefreshTableTasks.setOnAction(event -> {
            DBConnectivity.checkConnection(dbConnection, labelStatus);
            getTableTasks();
        });
        paneMain.getChildren().add(tableTasks);

        Scene sceneMain = new Scene(paneMain, 420, 500);
        primaryStage.setScene(sceneMain);
        primaryStage.setTitle("DB Application");
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
     *
     * @return
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
                    System.out.println("Unexpected column name detected");
                    //throw new SQLException();
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
}
