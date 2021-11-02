package controller.Reports;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static BLSchedulingApplication.BLSchedulingApplication.loadStage;
/**
 *  Controls reports main menu.
 * */
public class ReportsController implements Initializable {
    /**
     *  Loads Total Appointments report screen.
     *  Displays totals of appointments by month and type.
     * */
    @FXML
    void onActionTotalAppointments(ActionEvent event) throws IOException {
        loadStage(event, "reports/TotalAppointments");
    }
    /**
     *  Loads Contact Schedule report screen.
     *  Generates schedule for chosen contact.
     * */
    @FXML
    void onActionContactSchedule(ActionEvent event) throws IOException {
        loadStage(event, "reports/ContactSchedule");
    }
    /**
     *  Loads Customer Schedule report screen.
     *  Generates schedule for chosen customer.
     * */
    @FXML
    void onActionCustomerSchedule(ActionEvent event) throws IOException {
        loadStage(event, "reports/CustomerSchedule");
    }
    /** Returns to main menu screen.*/
    @FXML
    void onActionMainMenu(ActionEvent event) throws IOException {
        loadStage(event, "main/MainMenu");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) { }

}