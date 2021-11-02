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
import static model.Data.*;
/**
 * Controls add appointment screen.
 * */
public class AddAppointmentController implements Initializable {
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
    /** Selection box for customer.*/
    @FXML
    private ComboBox<Customer> customerCombo;
    /** Selection box for user.*/
    @FXML
    private ComboBox<User> userCombo;
    /** Input field for description.*/
    @FXML
    private TextField appointmentDescriptionTxt;
    /** Date selection for start date.*/
    @FXML
    private DatePicker startDate;
    /** Date selection for end date.*/
    @FXML
    private DatePicker endDate;
    /** Time selection for start time hour.*/
    @FXML
    private Spinner<Integer> spinnerStartHour; //TODO: REDO with Spinner<LocalTime>
    /** Time selection for start time minute.*/
    @FXML
    private Spinner<Integer> spinnerStartMinute;//TODO: REDO with Spinner<LocalTime>
    /** Time selection for end time hour.*/
    @FXML
    private Spinner<Integer> spinnerEndHour;//TODO: REDO with Spinner<LocalTime>
    /** Time selection for start time minute.*/
    @FXML
    private Spinner<Integer> spinnerEndMinute;//TODO: REDO with Spinner<LocalTime>
    /**
     *  Saves new appointment. LAMBDA #2 called.
     *  <p>Lambda called from data class. Displays business hours in users system time zone.</p>
     *  <p>Lambda helps user know during what times appointment can be scheduled.</p>
     * */
    @FXML
    void onActionSaveNewAppointment(ActionEvent event) throws SQLException, IOException {

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
                (endDate.getValue().isBefore(startDate.getValue())) ||//TODO: redesign to use one picker since appointments can only span one day
                (endDate.getValue().isAfter(startDate.getValue())) ||
                (appointmentEndTime.isBefore(appointmentStartTime))
        ){

            generateAlert(Alert.AlertType.ERROR, "Please enter valid values for all fields").show();

        }else {

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
            appointment.setContact(contact);
            appointment.setContactName(contact.getName());
            appointment.setUserId(userId);
            appointment.setCustomer(customer);



            if(isDuringBusinessHours(appointment.getStart(), appointment.getEnd())) {

                if(overlapsCustomerAppointment(appointment)){
                    generateAlert(Alert.AlertType.ERROR, customer.getName() + " already has an appointment at this time.\nChoose a different time window.").show();
                }else{

                    if (Data.addAppointment(appointment)) {
                        Alert alert = generateAlert(Alert.AlertType.INFORMATION, "Appointment Added");
                        alert.setHeaderText("Success");
                        alert.setTitle("");
                        alert.showAndWait();
                        loadStage(event, "scheduling/SchedulingMain");
                    } else {
                        Alert alert = generateAlert(Alert.AlertType.ERROR, "Unable to add appointment");
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
        Optional<ButtonType> result = generateAlert(Alert.AlertType.CONFIRMATION, "Are you sure?\nAll data will be lost.").showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK)
            loadStage(event, "scheduling/SchedulingMain");
    }
    /**
     *  Initializes comboBoxes and spinners with appropriate data.
     * */
    @Override
    public void initialize(URL url, ResourceBundle rb){

        contactCombo.setItems(Data.allContacts);
        contactCombo.getSelectionModel().selectFirst();
        customerCombo.setItems(Data.allCustomers);
        customerCombo.getSelectionModel().selectFirst();
        userCombo.setItems(Data.allUsers);
        userCombo.setValue(Data.currentUser);
        startDate.setValue(LocalDate.now().plusDays(1));
        endDate.setValue(LocalDate.now().plusDays(1));
        // Initialize Start Time Spinner
        SpinnerValueFactory<Integer> startHourValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,23, 11);
        SpinnerValueFactory<Integer> startMinuteValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,59, 30);
        startHourValueFactory.setWrapAround(true);
        startMinuteValueFactory.setWrapAround(true);
        spinnerStartHour.setValueFactory(startHourValueFactory);
        spinnerStartMinute.setValueFactory(startMinuteValueFactory);

        // Initialize End Time Spinner
        SpinnerValueFactory<Integer> endHourValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,23, 12);
        SpinnerValueFactory<Integer> endMinuteValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,59, 30);
        endHourValueFactory.setWrapAround(true);
        endMinuteValueFactory.setWrapAround(true);
        spinnerEndHour.setValueFactory(endHourValueFactory);
        spinnerEndMinute.setValueFactory(endMinuteValueFactory);

    }

}
