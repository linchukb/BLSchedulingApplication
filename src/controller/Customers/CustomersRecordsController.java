package controller.Customers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import model.Country;
import model.Customer;
import model.Data;
import model.Division;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

import static BLSchedulingApplication.BLSchedulingApplication.generateAlert;
import static BLSchedulingApplication.BLSchedulingApplication.loadStage;
/**
 * Controls customer records main screen.
 * */
public class CustomersRecordsController implements Initializable {
    /** Holds selected customer.*/
    public  static Customer selectedCustomer = null;
    /** Table of customers.*/
    @FXML
    private TableView<Customer> customersTableView;
    /** Id of the customer.*/
    @FXML
    private TableColumn<Customer, Integer> customerIdCol;
    /** Name of customer.*/
    @FXML
    private TableColumn<Customer, String> customerNameCol;
    /** Address of customer.*/
    @FXML
    private TableColumn<Customer, String> customerAddressCol;
    /** Postal code of customer.*/
    @FXML
    private TableColumn<Customer, String> customerPostalCodeCol;
    /** Country of customer.*/
    @FXML
    private TableColumn<Country, String> customerCountryCol;
    /** Division of customer.*/
    @FXML
    private TableColumn<Division, String> customerDivisionCol;
    /** Phone number of customer.*/
    @FXML
    private TableColumn<Customer, String> customerPhoneCol;
    /** Opens add customer screen.*/
    @FXML
    void onActionAddCustomer(ActionEvent event) throws IOException, SQLException {
        loadStage(event, "Customers/AddCustomer");
    }
    /**
     * Deletes selected customer upon confirmation.
     * Alerts user of:
     * <p>Customer is deleted.</p>
     * <p>Customer is not deleted</p>
     * <p>Customer is not deleted because has appointments.</p>
     * <p>No customer selected to delete.</p>
     * */
    @FXML
    void onActionDeleteCustomer(ActionEvent event) throws IOException, SQLException {
        Customer selectedCustomer = customersTableView.getSelectionModel().getSelectedItem();



        if(!(selectedCustomer == null)){

            if(!(Data.hasAppointments(selectedCustomer))){

                Optional<ButtonType> result = generateAlert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this customer? ").showAndWait();
                if(result.isPresent() && result.get() == ButtonType.OK) {
                    if(Data.deleteCustomer(selectedCustomer)){
                        Alert alert = generateAlert(Alert.AlertType.INFORMATION, "Customer Deleted");
                        alert.setHeaderText("Success");
                        alert.setTitle("");
                        alert.show();
                        Data.allCustomers.remove(selectedCustomer);
                    }else {
                        Alert alert = generateAlert(Alert.AlertType.ERROR, "Unable to delete customer");
                        alert.setTitle("");
                        alert.setHeaderText("Failed");
                        alert.show();
                    }
                }

            }else {
                generateAlert(Alert.AlertType.ERROR, "Cannot delete customer that has appointments.").show();
            }
        }else {
            generateAlert(Alert.AlertType.ERROR, "Select a customer to delete.").show();
        }
    }
    /**
     * Opens screen to update customer.
     * */
    @FXML
    void onActionUpdateCustomer(ActionEvent event) throws IOException {
        selectedCustomer = customersTableView.getSelectionModel().getSelectedItem();
        if(selectedCustomer == null)
            generateAlert(Alert.AlertType.ERROR, "Select a customer to update.").show();
        else
            loadStage(event, "Customers/UpdateCustomer");
    }
    /**
     * Opens screen to Main Menu.
     * */
    @FXML
    void onActionMainMenu(ActionEvent event) throws IOException {
        loadStage(event, "main/MainMenu");
    }
    /**
     * Initializes lists needed to view customers.
     * Loads table with customers.
     * */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        try {
            Data.initializeAllCustomers();
            Data.initializeAllContacts();
            Data.initializeAllAppointments();
        }catch (SQLException e){ e.getMessage(); }

        customersTableView.setItems(Data.allCustomers);
        customerIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        customerNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        customerAddressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        customerPostalCodeCol.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        customerCountryCol.setCellValueFactory(new PropertyValueFactory<>("countryName"));
        customerDivisionCol.setCellValueFactory(new PropertyValueFactory<>("divisionName"));
        customerPhoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));

    }
}
