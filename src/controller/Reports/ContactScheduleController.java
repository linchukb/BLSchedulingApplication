package controller.Reports;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Appointment;
import model.Contact;
import model.Data;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

import static BLSchedulingApplication.BLSchedulingApplication.loadStage;
import static model.Data.contactAppointments;
import static model.Data.initializeContactAppointmentsReport;
/**
 * Controls contact schedule report.
 * */
public class ContactScheduleController implements Initializable {
    /** Holds contacts to generate report about.*/
    @FXML
    private ComboBox<Contact> contactCombo;
    /** Table of appointments for chosen contact.*/
    @FXML
    private TableView<Appointment> contactScheduleTableView;
    /** ID of appointment.*/
    @FXML
    private TableColumn<Appointment, Integer> appointmentIdCol;
    /** Title of appointment.*/
    @FXML
    private TableColumn<Appointment, String> appointmentTitleCol;
    /** Type of appointment.*/
    @FXML
    private TableColumn<Appointment, String> appointmentTypeCol;
    /** Description of appointment.*/
    @FXML
    private TableColumn<Appointment, String> appointmentDescriptionCol;
    /** Start date and time of appointment.*/
    @FXML
    private TableColumn<Appointment, LocalDateTime> appointmentStartCol;
    /** End date and time of appointment.*/
    @FXML
    private TableColumn<Appointment, LocalDateTime> appointmentEndCol;
    /** Customer assigned to appointment.*/
    @FXML
    private TableColumn<Appointment, String> appointmentCustomerCol;
    /**
     *  Updates table with info of chosen contact.
     * */
    @FXML
    void onActionContactCombo(ActionEvent event) {
        if(contactCombo.getSelectionModel().getSelectedItem() != null)
            initializeContactAppointmentsReport(contactCombo.getSelectionModel().getSelectedItem());
    }
    /**
     *  Goes back to reports main screen.
     * */
    @FXML
    void onActionReportsMenu(ActionEvent event) throws IOException {
        loadStage(event, "reports/ReportsMenu");
    }
    /**
     *  Loads contacts to choose from into comboBox.
     *  Starts with first contact selected.
     * */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        contactCombo.setItems(Data.allContacts);
        contactCombo.getSelectionModel().selectFirst();
        initializeContactAppointmentsReport(contactCombo.getSelectionModel().getSelectedItem());
        contactScheduleTableView.setItems(contactAppointments);
        appointmentIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        appointmentTitleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        appointmentTypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        appointmentDescriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        appointmentStartCol.setCellValueFactory(new PropertyValueFactory<>("startString"));
        appointmentEndCol.setCellValueFactory(new PropertyValueFactory<>("endString"));
        appointmentCustomerCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));
    }

}
