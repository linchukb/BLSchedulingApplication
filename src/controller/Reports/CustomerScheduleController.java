package controller.Reports;

        import javafx.event.ActionEvent;
        import javafx.fxml.FXML;
        import javafx.fxml.Initializable;
        import javafx.scene.control.ComboBox;
        import javafx.scene.control.TableColumn;
        import javafx.scene.control.TableView;
        import javafx.scene.control.cell.PropertyValueFactory;
        import model.Appointment;
        import model.Customer;
        import model.Data;

        import java.io.IOException;
        import java.net.URL;
        import java.time.LocalDateTime;
        import java.util.ResourceBundle;

        import static BLSchedulingApplication.BLSchedulingApplication.loadStage;
        import static model.Data.customerAppointments;
        import static model.Data.initializeCustomerAppointmentsReport;
/**
 *  Controls customer schedule report.
 * */
public class CustomerScheduleController implements Initializable {
    /** Holds customers to generate report about.*/
    @FXML
    private ComboBox<Customer> customerCombo;
    /** Table of appointments for chosen customer.*/
    @FXML
    private TableView<Appointment> customersScheduleReportTableView;
    /** ID of appointment*/
    @FXML
    private TableColumn<Appointment, Integer> appointmentIdCol;
    /** Start date and time of appointment.*/
    @FXML
    private TableColumn<Appointment, LocalDateTime> appointmentStartCol;
    /** End date and time of appointment.*/
    @FXML
    private TableColumn<Appointment, LocalDateTime> appointmentEndCol;
    /**
     *  Updates table with info of chosen customer.
     * */
    @FXML
    void onActionCustomerCombo(ActionEvent event) {
        if(customerCombo.getSelectionModel().getSelectedItem() != null)
            initializeCustomerAppointmentsReport(customerCombo.getSelectionModel().getSelectedItem());
    }
    /**
     *  Returns to reports main screen.
     * */
    @FXML
    void onActionReportsMenu(ActionEvent event) throws IOException {
        loadStage(event, "reports/ReportsMenu");
    }
    /**
     *  Loads customers to choose from into comboBox.
     *  Start with first customer selected.
     * */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        customerCombo.setItems(Data.allCustomers);
        customerCombo.getSelectionModel().selectFirst();
        initializeCustomerAppointmentsReport(customerCombo.getSelectionModel().getSelectedItem());
        customersScheduleReportTableView.setItems(customerAppointments);
        appointmentIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        appointmentStartCol.setCellValueFactory(new PropertyValueFactory<>("startString"));
        appointmentEndCol.setCellValueFactory(new PropertyValueFactory<>("endString"));

    }

}