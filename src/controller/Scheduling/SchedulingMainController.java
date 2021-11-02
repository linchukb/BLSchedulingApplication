package controller.Scheduling;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Appointment;
import model.Data;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.*;
import java.util.Optional;
import java.util.ResourceBundle;

import static BLSchedulingApplication.BLSchedulingApplication.generateAlert;
import static BLSchedulingApplication.BLSchedulingApplication.loadStage;
/** Controls scheduling main screen.*/
public class SchedulingMainController implements Initializable {
    /** Holds selected appointment.*/
    public static Appointment selectedAppointment = null;
    /** Tab with schedule of all appointments*/
    @FXML
    private Tab allTab;
    /** Tab with schedule of appointments only in current month*/
    @FXML
    private Tab monthTab;
    /** Tab with schedule of appointments only in current week*/
    @FXML
    private Tab weekTab;
    /** Table with all appointments.*/
    @FXML
    private TableView<Appointment> allAppointmentsTableView;
    /** Table with appointments in current month.*/
    @FXML
    private TableView<Appointment> monthAppointmentsTableView;
    /** Table with appointment in current week.*/
    @FXML
    private TableView<Appointment> weekAppointmentsTableView;
    /** ID of appointment.*/
    @FXML
    private TableColumn<Appointment, Integer> appointmentIdCol;
    /** Title of appointment.*/
    @FXML
    private TableColumn<Appointment, String> appointmentTitleCol;
    /** Description of appointment.*/
    @FXML
    private TableColumn<Appointment, String> appointmentDescriptionCol;
    /** Location of appointment.*/
    @FXML
    private TableColumn<Appointment, String> appointmentLocationCol;
    /** Contact assigned to appointment.*/
    @FXML
    private TableColumn<Appointment, String> appointmentContactCol;
    /** Type of appointment.*/
    @FXML
    private TableColumn<Appointment, String> appointmentTypeCol;
    /** Start date and time of appointment.*/
    @FXML
    private TableColumn<Appointment, LocalDateTime> appointmentStartCol;
    /** End date and time of appointment.*/
    @FXML
    private TableColumn<Appointment, LocalDateTime> appointmentEndCol;
    /** Customer assigned to appointment.*/
    @FXML
    private TableColumn<Appointment, String> appointmentCustomerCol;
    /** ID of appointment.*/
    @FXML
    private TableColumn<Appointment, Integer> monthAppointmentIdCol;
    /** Title of appointment.*/
    @FXML
    private TableColumn<Appointment, String> monthAppointmentTitleCol;
    /** Description of appointment.*/
    @FXML
    private TableColumn<Appointment, String> monthAppointmentDescriptionCol;
    /** Location of appointment.*/
    @FXML
    private TableColumn<Appointment, String> monthAppointmentLocationCol;
    /** Contact assigned to appointment.*/
    @FXML
    private TableColumn<Appointment, String> monthAppointmentContactCol;
    /** Type of appointment.*/
    @FXML
    private TableColumn<Appointment, String> monthAppointmentTypeCol;
    /** Start date and time of appointment.*/
    @FXML
    private TableColumn<Appointment, LocalDateTime> monthAppointmentStartCol;
    /** End date and time of appointment.*/
    @FXML
    private TableColumn<Appointment, LocalDateTime> monthAppointmentEndCol;
    /** Customer assigned to appointment.*/
    @FXML
    private TableColumn<Appointment, String> monthAppointmentCustomerCol;
    /** ID of appointment.*/
    @FXML
    private TableColumn<Appointment, Integer> weekAppointmentIdCol;
    /** Title of appointment.*/
    @FXML
    private TableColumn<Appointment, String> weekAppointmentTitleCol;
    /** Description of appointment.*/
    @FXML
    private TableColumn<Appointment, String> weekAppointmentDescriptionCol;
    /** Location of appointment.*/
    @FXML
    private TableColumn<Appointment, String> weekAppointmentLocationCol;
    /** Contact assigned to appointment.*/
    @FXML
    private TableColumn<Appointment, String> weekAppointmentContactCol;
    /** Type of appointment.*/
    @FXML
    private TableColumn<Appointment, String> weekAppointmentTypeCol;
    /** Start date and time of appointment.*/
    @FXML
    private TableColumn<Appointment, LocalDateTime> weekAppointmentStartCol;
    /** End date and time of appointment.*/
    @FXML
    private TableColumn<Appointment, LocalDateTime> weekAppointmentEndCol;
    /** Customer assigned to appointment.*/
    @FXML
    private TableColumn<Appointment, String> weekAppointmentCustomerCol;
    /** Loads add appointment screen.*/
    @FXML
    void onActionAddAppointment(ActionEvent event) throws IOException {
        loadStage(event, "scheduling/AddAppointment");
    }
    /**
     *  Deletes appointment.
     *  Displays appropriate message in dialog window if unable to delete.
     * */
    @FXML
    void onActionDeleteAppointment(ActionEvent event) throws SQLException {
        if(allTab.isSelected())
            selectedAppointment = allAppointmentsTableView.getSelectionModel().getSelectedItem();
        else if(monthTab.isSelected())
            selectedAppointment = monthAppointmentsTableView.getSelectionModel().getSelectedItem();
        else if(weekTab.isSelected())
            selectedAppointment = weekAppointmentsTableView.getSelectionModel().getSelectedItem();

        if(!(selectedAppointment == null)){
            String  id = String.valueOf(selectedAppointment.getId()),
                    type = selectedAppointment.getType();

            Optional<ButtonType> result = generateAlert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this appointment? ").showAndWait();
            if(result.isPresent() && result.get() == ButtonType.OK){
                if(Data.deleteAppointment(selectedAppointment)){
                    Alert alert = generateAlert(Alert.AlertType.INFORMATION, "Appointment ID: " + id + "\nAppointment type: " + type);
                    alert.setHeaderText("Deleted");
                    alert.show();
                    Data.allAppointments.remove(selectedAppointment);
                }else {
                    generateAlert(Alert.AlertType.ERROR, "Unable to delete appointment.").show();
                }
            }
        }else {
            generateAlert(Alert.AlertType.ERROR, "Select an appointment to delete.").show();
        }
    }
    /**
     * Loads update appointment Screen.
     * */
    @FXML
    void onActionUpdateAppointment(ActionEvent event) throws IOException{
        if(allTab.isSelected())
            selectedAppointment = allAppointmentsTableView.getSelectionModel().getSelectedItem();
        else if(monthTab.isSelected())
            selectedAppointment = monthAppointmentsTableView.getSelectionModel().getSelectedItem();
        else if(weekTab.isSelected())
            selectedAppointment = weekAppointmentsTableView.getSelectionModel().getSelectedItem();

        if(selectedAppointment == null){
            generateAlert(Alert.AlertType.ERROR, "Select an appointment to update").show();
        }else
            loadStage(event, "scheduling/UpdateAppointment");
    }
    /**
     * Returns to main menu screen.
     * */
    @FXML
    void onActionMainMenu(ActionEvent event) throws IOException {
        loadStage(event, "main/MainMenu");
    }
    /**
     * Gets time from spinners and converts to LocalTime format.
     * */
    public static LocalTime getSpinnerTime(Spinner<Integer> spinnerHour, Spinner<Integer> spinnerMinute){
        String hour, minute, time;
        if(spinnerHour.getValue() < 10)
            hour = "0" + spinnerHour.getValue();
        else hour = String.valueOf(spinnerHour.getValue());
        if(spinnerMinute.getValue() < 10)
            minute = "0" + spinnerMinute.getValue();
        else minute = String.valueOf(spinnerMinute.getValue());
        time = hour + ":" + minute;
        return LocalTime.parse(time);
    }
    /**
     *  Initializes all needed lists of data.
     *  Sets data into Tables.
     * */
    @Override
    public void initialize(URL url, ResourceBundle rb){

        // pull database records
        try {
            Data.initializeAllContacts();
            Data.initializeAllAppointments();
            Data.initializeMonthAppointments();
            Data.initializeWeekAppointments();
        }catch (SQLException e){ e.getMessage(); }

        allAppointmentsTableView.setItems(Data.allAppointments);
        appointmentIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        appointmentTitleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        appointmentDescriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        appointmentLocationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        appointmentContactCol.setCellValueFactory(new PropertyValueFactory<>("contactName"));
        appointmentTypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        appointmentStartCol.setCellValueFactory(new PropertyValueFactory<>("startString"));
        appointmentEndCol.setCellValueFactory(new PropertyValueFactory<>("endString"));
        appointmentCustomerCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));

        monthAppointmentsTableView.setItems(Data.monthAppointments);
        monthAppointmentIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        monthAppointmentTitleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        monthAppointmentDescriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        monthAppointmentLocationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        monthAppointmentContactCol.setCellValueFactory(new PropertyValueFactory<>("contactName"));
        monthAppointmentTypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        monthAppointmentStartCol.setCellValueFactory(new PropertyValueFactory<>("startString"));
        monthAppointmentEndCol.setCellValueFactory(new PropertyValueFactory<>("endString"));
        monthAppointmentCustomerCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));

        weekAppointmentsTableView.setItems(Data.weekAppointments);
        weekAppointmentIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        weekAppointmentTitleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        weekAppointmentDescriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        weekAppointmentLocationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        weekAppointmentContactCol.setCellValueFactory(new PropertyValueFactory<>("contactName"));
        weekAppointmentTypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        weekAppointmentStartCol.setCellValueFactory(new PropertyValueFactory<>("startString"));
        weekAppointmentEndCol.setCellValueFactory(new PropertyValueFactory<>("endString"));
        weekAppointmentCustomerCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));

    }
}
