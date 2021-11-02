package controller.Scheduling;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import model.*;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;
import java.util.ResourceBundle;

import static BLSchedulingApplication.BLSchedulingApplication.generateAlert;
import static BLSchedulingApplication.BLSchedulingApplication.loadStage;
import static controller.Scheduling.SchedulingMainController.getSpinnerTime;
import static controller.Scheduling.SchedulingMainController.selectedAppointment;
import static model.Data.*;
/**
 * Controls update appointment screen.
 * */
public class UpdateAppointmentController implements Initializable {
    /** Disabled input field for ID.*/
    @FXML
    private TextField appointmentIdTxt;
    /** Input field for title.*/
    @FXML
    private TextField appointmentTitleTxt;
    /** Input field for location.*/
    @FXML
    private TextField appointmentLocationTxt;
    /** Input field for type.*/
    @FXML
    private TextField appointmentTypeTxt;
    /** Selection box for contact.*/
    @FXML
    private ComboBox<Contact> contactCombo;
    /** Input field for description.*/
    @FXML
    private TextField appointmentDescriptionTxt;
    /** Date selection for start date*/
    @FXML
    private DatePicker startDate;
    /** Time selection for start time hour*/
    @FXML
    private Spinner<Integer> spinnerStartHour;
    /** Time selection for start time minute.*/
    @FXML
    private Spinner<Integer> spinnerStartMinute;
    /** Date selection for end date.*/
    @FXML
    private DatePicker endDate;
    /** Time selection for end time hour.*/
    @FXML
    private Spinner<Integer> spinnerEndHour;
    /** Time selection for end time minute.*/
    @FXML
    private Spinner<Integer> spinnerEndMinute;
    /** Selection box for customer.*/
    @FXML
    private ComboBox<Customer> customerCombo;
    /** Selection box for user.*/
    @FXML
    private ComboBox<User> userCombo;
    /**
     *  Saves updated appointment. LAMBDA #2 called.
     *  <p>Lambda called from data class. Displays business hours in users system time zone.</p>
     *  <p>Lambda helps user know during what times appointment can be scheduled.</p>
     * */
    @FXML
    void onActionSaveEditedAppointment(ActionEvent event) throws IOException, SQLException {

        LocalTime appointmentStartTime = getSpinnerTime(spinnerStartHour, spinnerStartMinute);
        LocalTime appointmentEndTime = getSpinnerTime(spinnerEndHour, spinnerEndMinute);

        // Handle logical exceptions
        if((    contactCombo.getSelectionModel().getSelectedItem() == null) ||
                appointmentTitleTxt.getText().isBlank() ||
                (customerCombo.getSelectionModel().getSelectedItem() == null) ||
                (userCombo.getSelectionModel().getSelectedItem() == null) ||
                appointmentLocationTxt.getText().isBlank() ||
                appointmentTypeTxt.getText().isBlank() ||
                appointmentDescriptionTxt.getText().isBlank()||
                (startDate.getValue() == null) ||
                (endDate.getValue() == null) ||
                (endDate.getValue().isBefore(startDate.getValue())) ||
                (endDate.getValue().isAfter(startDate.getValue())) ||
                (appointmentEndTime.isBefore(appointmentStartTime))
        ){

            generateAlert(Alert.AlertType.ERROR, "Please enter valid values for all fields").show();

        }else{

            int appointmentId = selectedAppointment.getId();
            Customer customer = customerCombo.getSelectionModel().getSelectedItem();
            int customerId = customer.getId();
            String title = appointmentTitleTxt.getText();
            int userId = userCombo.getSelectionModel().getSelectedItem().getId();
            String location = appointmentLocationTxt.getText();
            String type = appointmentTypeTxt.getText();
            Contact contact = contactCombo.getSelectionModel().getSelectedItem();
            String description = appointmentDescriptionTxt.getText();
            LocalDate start = startDate.getValue();
            LocalDate end = endDate.getValue();


            Appointment appointment = new Appointment(
                    customerId,
                    title,
                    description,
                    location,
                    type,
                    LocalDateTime.of(start, appointmentStartTime),
                    LocalDateTime.of(end, appointmentEndTime)
            );
            appointment.setCreatedBy(selectedAppointment.getCreatedBy());
            appointment.setCreateDate(selectedAppointment.getCreateDate());
            appointment.setContact(contact);
            appointment.setContactName(contact.getName());
            appointment.setUserId(userId);
            appointment.setCustomer(customer);
            appointment.setId(appointmentId);

            if(isDuringBusinessHours(appointment.getStart(), appointment.getEnd())) {
                if(overlapsCustomerAppointment(appointment)){
                    generateAlert(Alert.AlertType.ERROR,customer.getName() + " already has an appointment at this time.\nChoose a different time window.").show();
                }else{
                    if (Data.updateAppointment(appointment)) {
                        Alert alert = generateAlert(Alert.AlertType.INFORMATION, "Appointment updated");
                        alert.setHeaderText("Success");
                        alert.setTitle("");
                        alert.showAndWait();
                        loadStage(event, "scheduling/SchedulingMain");
                    } else {
                        Alert alert = generateAlert(Alert.AlertType.ERROR, "Unable to update appointment");
                        alert.setTitle("");
                        alert.setHeaderText("Failed");
                        alert.show();
                    }
                }
            }else {
                Alert alert =
                        generateAlert(Alert.AlertType.ERROR, "Appointment outside of business hours.\n" +
                                "Business Hours:\n" +
                                "08:00-22:00 EST\n" +
                                convertBusHrs.out()); // LAMBDA #2
                alert.setTitle("");
                alert.setHeaderText("Unable to add appointment");
                alert.show();
            }
        }
    }
    /**
     * Returns to scheduling main menu screen.
     * */
    @FXML
    void onActionSchedulingMain(ActionEvent event) throws IOException {
        Optional<ButtonType> result = generateAlert(Alert.AlertType.CONFIRMATION, "Are you sure?\nAll changes will be lost.").showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK)
            loadStage(event, "scheduling/SchedulingMain");
    }
    /**
     *  Populates fields with data of selected customer.
     *  Initializes comboBoxes and spinners with appropriate data.
     * */
    @Override
    public void initialize(URL url, ResourceBundle rb){

        // populate fields with selected appointment info
        appointmentIdTxt.setText(String.valueOf(selectedAppointment.getId()));
        appointmentTitleTxt.setText(selectedAppointment.getTitle());
        appointmentLocationTxt.setText(selectedAppointment.getLocation());
        appointmentTypeTxt.setText(selectedAppointment.getType());
        appointmentDescriptionTxt.setText(selectedAppointment.getDescription());
        contactCombo.setItems(Data.allContacts);
        contactCombo.getSelectionModel().select(Data.getContact(selectedAppointment.getContact().getId()));
        customerCombo.setItems(Data.allCustomers);
        customerCombo.getSelectionModel().select(selectedAppointment.getCustomer());
        userCombo.setItems(Data.allUsers);
        userCombo.getSelectionModel().select(selectedAppointment.getUser());
        startDate.setValue(selectedAppointment.getStart().toLocalDate());
        endDate.setValue(selectedAppointment.getEnd().toLocalDate());

        // Start Time Spinner
        SpinnerValueFactory<Integer> startHourValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,23, selectedAppointment.getStart().getHour());
        SpinnerValueFactory<Integer> startMinuteValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,59, selectedAppointment.getStart().getMinute());
        startHourValueFactory.setWrapAround(true);
        startMinuteValueFactory.setWrapAround(true);
        spinnerStartHour.setValueFactory(startHourValueFactory);
        spinnerStartMinute.setValueFactory(startMinuteValueFactory);

        // End Time Spinner
        SpinnerValueFactory<Integer> endHourValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,23, selectedAppointment.getEnd().getHour());
        SpinnerValueFactory<Integer> endMinuteValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,59, selectedAppointment.getEnd().getMinute());
        endHourValueFactory.setWrapAround(true);
        endMinuteValueFactory.setWrapAround(true);
        spinnerEndHour.setValueFactory(endHourValueFactory);
        spinnerEndMinute.setValueFactory(endMinuteValueFactory);

    }

}
