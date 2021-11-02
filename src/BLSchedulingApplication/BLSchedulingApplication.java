package BLSchedulingApplication;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DialogPane;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import model.Data;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Locale;
import java.util.ResourceBundle;

import static utils.DBConnection.closeConnection;
import static utils.DBConnection.startConnection;

/**
 * An appointment scheduling System.
 * Stores and retrieves data from database.
 * <p><b>Main Functions include:</b></p>
 * <p>(1) View / add / update / delete customers.</p>
 * <p>(2) View / add / update / delete appointments.</p>
 * <p>(3) Generates select reports.</p>
 * */
public class BLSchedulingApplication extends Application {

    public static FileWriter fileWriter;
    static {
        try { fileWriter = new FileWriter("src/files/login_activity.txt", true); }
        catch (IOException e) { e.getMessage(); }
    }
    /**
     * Keeps history of login attempts. Appends to existing file.
     * <p>Format of data (example): username: admin | password: admin | Date/Time: 2021-07-05T07:21:52.573142 - America/Los_Angeles PT | login: successful</p>
     * */
    public static PrintWriter loginAttemptsLog = new PrintWriter(fileWriter);

    /**
     * Generates a generic alert.
     * @param type the type of alert requested.
     * @param contentText the body text to be displayed on the alert.
     * @return an Alert object.
     * */
    public static Alert generateAlert(Alert.AlertType type, String contentText){
        Alert alert = new Alert(type);
        alert.setContentText(contentText);

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(BLSchedulingApplication.class.getResource("/styles/style.css").toExternalForm());
        dialogPane.getStyleClass().add("dialog");
        return alert;

    }


    /**
     * Loads stage called by controller.
     * @param fxml the partial path of the resource to load
     * */
    public static void loadStage(ActionEvent event, String fxml) throws IOException{
        Stage stage;
        Parent scene;

        fxml = "/view/" + fxml + ".fxml";
        stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(BLSchedulingApplication.class.getResource(fxml));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../view/main/login.fxml"));
        primaryStage.setTitle("Scheduling System");
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Starting point of application.
     * <p>Establishes and closes database connection. Closes login activity log file.</p>
     * <p>Initializes observable lists which will not change.</p>
     * @throws SQLException when accessing database to initialize lists.
     * */
    public static void main(String[] args) throws SQLException {
        // To test French login + dialog
        //Locale.setDefault(new Locale("fr"));

        // Connect to database
        Connection conn = startConnection();
        try {
            Data.initializeUsers();
            Data.initializeAllDivisions(); // Must be initialized before customers because initializeAllDBCustomers() uses division list
            Data.initializeAllCountries();
        } catch (SQLException e) { e.getMessage(); }
        launch(args);
        loginAttemptsLog.close();
        closeConnection();

    }
}