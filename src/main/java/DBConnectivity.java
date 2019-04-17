import javafx.collections.ObservableList;

import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import javafx.scene.control.Label;
import java.sql.SQLException;

public class DBConnectivity {
    /**
     * Connects to a database predefined in the code.
     * @param labelStatus A label for storing current connecting status.
     * @return Returns a successful Connection, otherwise null.
     */
    public static Connection connect(Label labelStatus) {
        try {
            Connection dbConnection;
            //SQL Database connection params
            final String dbLanguagePrefix = "jdbc:postgresql://";
            final String dbHost = "slon.felk.cvut.cz";
            final String dbPort = "5432";
            final String dbName = "db19_mullemi5";
            final String connectionString = dbLanguagePrefix + dbHost + ":" + dbPort +"/" + dbName;
            final String dbUser = "db19_mullemi5";
            final String dbPassword = "Px7DA4";

            //Registering the driver by dynamically loading its class file into memory
            Class.forName("org.postgresql.Driver");

            //Connecting to Database
            labelStatus.setText("Connecting to database...");
            dbConnection = DriverManager.getConnection(connectionString, dbUser, dbPassword);
            labelStatus.setText("Succesfully connected.");
            return dbConnection;

        } catch (ClassNotFoundException e) {
            labelStatus.setText("ERROR: PostgreSQL JDBC driver not found.");
            e.printStackTrace();
        } catch (SQLException e) {
            labelStatus.setText("ERROR: Database connection failure.");
            e.printStackTrace();
        }
        return null;
    }
    /**
     * Checks if the specified connection exists/is valid, if not, tries to connect again, and if still not, then returns false.
     * @param con A connection to check.
     * @param labelStatus A label for storing current connecting status.
     * @return Returns true on successful connection.
     */
    public static boolean checkConnection(Connection con, Label labelStatus) {
        if (con == null) {
            con = connect(labelStatus);
            if (con == null)
                return false;
        } else {
            try {
                if (!con.isValid(1)) {
                    con = connect(labelStatus);
                    if (con == null)
                        return false;
                }
            } catch (SQLException e) {
                System.out.println("WARNING: function isValid timeout argument is less than zero");
            }
        }
        return true;
    }

}

