package controller.Main;

import javafx.scene.control.Alert;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Duration;
import model.Data;

import java.io.IOException;
import java.net.URL;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.ResourceBundle;

import static BLSchedulingApplication.BLSchedulingApplication.*;
import static model.Data.*;

/**
 * Controls login screen.
 * */
public class LoginController implements Initializable {
    /** For international definitions.*/
    ResourceBundle languages = ResourceBundle.getBundle("BLSchedulingApplication/Nat", Locale.getDefault());
    /** Username Label.*/
    @FXML
    private Label userNameLbl;
    /** Text field for input of username.*/
    @FXML
    private TextField usernameTxt;
    /** Password Label.*/
    @FXML
    private Label passwordLbl;
    /** Text field for input of password.*/
    @FXML
    private PasswordField passwordTxt;
    /** Location Label.*/
    @FXML
    private Label locationLbl;
    /** Displays users location.*/
    @FXML
    private Label userLocationLbl;
    /** Header of login screen.*/
    @FXML
    private Label header;
    /** Login button.*/
    @FXML
    private Button loginButtonLbl;
    /**
     * Processes login attempt.
     * Displays upcoming appointment info dialog.
     * Appends login attempt info to file.
     * */
    @FXML
    void onActionLogin(ActionEvent event) throws IOException, SQLException {

        // Logs usernames and passwords of each attempted login
        loginAttemptsLog.append(
                "username: " + usernameTxt.getText() + " | " +
                "password: " + passwordTxt.getText() + " | " +
                "Date/Time: " + LocalDateTime.now() + " - " + ZoneId.systemDefault().getId() + " " + ZoneId.systemDefault().getDisplayName(TextStyle.SHORT, Locale.getDefault()) + " | " +
                "login: "
        );

        if(Data.login(usernameTxt.getText(), passwordTxt.getText())) {
            loginAttemptsLog.append("successful\n");

            Data.initializeAllContacts();
            initializeAllAppointments();
            getUserUpcomingAppointments().showAndWait();

            loadStage(event, "Main/MainMenu");
        }
        else if(Locale.getDefault().getLanguage().equals("fr")) {
            loginAttemptsLog.append("failed\n");
            generateAlert(Alert.AlertType.ERROR, languages.getString("invalid.username.or.password")).show();
            //OK setText
        }
        else {
            loginAttemptsLog.append("failed\n");
            generateAlert(Alert.AlertType.ERROR, "Invalid username or password").show();
        }

    }
    /** Location of live current system time.*/
    @FXML
    private Label liveClock;

    /**
     * Translates login screen to french if needed. LAMBDA #1.
     * <p>Lambda creates and displays live local time on login screen.</p>
     * */
    @Override
    public void initialize(URL url, ResourceBundle rb){

        // Translate to french if system system in french
        if(Locale.getDefault().getLanguage().equals("fr")) {
            header.setText(languages.getString("Scheduling") + " " + languages.getString("System"));
            userNameLbl.setText(languages.getString("username"));
            passwordLbl.setText(languages.getString("password"));
            loginButtonLbl.setText(languages.getString("Login"));
            locationLbl.setText(languages.getString("Location"));
        }

        // System zone info
        ZoneId zoneId = ZoneId.systemDefault();
        String zone = zoneId.getId();
        userLocationLbl.setText(zone);

        // Live clock -  LAMBDA #1
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, event -> {
            LocalTime currentTime = LocalTime.now();
            String hour, minute, second;
            if(currentTime.getHour() < 10) hour = "0" + currentTime.getHour();
            else hour = String.valueOf(currentTime.getHour());
            if(currentTime.getMinute() < 10) minute = "0" + currentTime.getMinute();
            else minute = String.valueOf(currentTime.getMinute());
            if(currentTime.getSecond() < 10) second = "0" + currentTime.getSecond();
            else second = String.valueOf(currentTime.getSecond());
            liveClock.setText(hour + ":" + minute + ":" + second);
        }),
                new KeyFrame(Duration.seconds(1))
        );
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();

    }

}
