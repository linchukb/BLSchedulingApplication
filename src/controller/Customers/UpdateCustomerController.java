package controller.Customers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

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
import static model.Data.currentUser;
import static model.Data.updateCustomer;
/**
 * Controls update customer screen
 * */
public class UpdateCustomerController implements Initializable {
    /** Disabled Input field for customer ID.*/
    @FXML
    private TextField customerIdTxt;
    /** Input field for customer name.*/
    @FXML
    private TextField customerNameTxt;
    /** Input field for customer address.*/
    @FXML
    private TextField customerAddressTxt;
    /** Input field for customer postal code.*/
    @FXML
    private TextField customerZipTxt;
    /** Input field for customer postal code.*/
    @FXML
    private TextField customerPhoneTxt;
    /** Selection field for customer country.*/
    @FXML
    private ComboBox<Country> CountryCombo;
    /** Selection field for customer first level division.*/
    @FXML
    private ComboBox<Division> DivisionCombo;
    /** Holds selected country.*/
    private Country country= null;
    /** Holds selected first level division.*/
    private Division division = null;
    /**
     * Loads division comboBox with those of the selected country.
     * */
    @FXML
    void onActionCountryCombo(ActionEvent event) throws SQLException {
        country = CountryCombo.getSelectionModel().getSelectedItem();
        // TODO: later re-write: make a getDivisions method in Country class the call here instead of via Data class?
        if(country != null){ // Temporary workaround to fix exception. need to find why back on customerRecords even goes here in first place.
            DivisionCombo.setItems(Data.getDivisions(CountryCombo.getSelectionModel().getSelectedItem().toString()));
            DivisionCombo.getSelectionModel().selectFirst();
        }
    }
    /**
     * Sets division to that selected in comboBox.
     * */
    @FXML
    void onActionDivisionCombo(ActionEvent event) {
        division = DivisionCombo.getSelectionModel().getSelectedItem();
    }
    /**
     * Returns to customer records screen. Discards changes
     * */
    @FXML
    void onActionCustomerRecords(ActionEvent event) throws IOException {
        Optional<ButtonType> result = generateAlert(Alert.AlertType.CONFIRMATION, "Are you sure?\nAll changes will be lost.").showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK)
            loadStage(event, "Customers/CustomerRecords");
    }

    /**
     * Saves updated customer. If unable, appropriate error message displayed in dialog window.
     * */
    @FXML
    void onActionSaveUpdatedCustomer(ActionEvent event) throws IOException, SQLException {

        Optional<ButtonType> result = generateAlert(Alert.AlertType.CONFIRMATION, "Are you sure you want to save?").showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK) {
            Customer customer = new Customer(
                    customerNameTxt.getText(),
                    customerAddressTxt.getText(),
                    country,
                    division,
                    customerZipTxt.getText(),
                    customerPhoneTxt.getText(),
                    CustomersRecordsController.selectedCustomer.getCreatedBy(),
                    currentUser.getName()
            );
            customer.setId(CustomersRecordsController.selectedCustomer.getId());
            customer.setCountryName(country.getName());
            customer.setDivisionName(division.getName());


            if(
                    customerNameTxt.getText().equals("") ||
                    customerAddressTxt.getText().equals("") ||
                    customerZipTxt.getText().equals("") ||
                    customerPhoneTxt.getText().equals("")
            ){
                generateAlert(Alert.AlertType.ERROR, "Please enter valid values for each field").show();
            }else {
                try {

                    if(updateCustomer(customer)){
                        generateAlert(Alert.AlertType.INFORMATION, "Customer Updated").showAndWait();
                        loadStage(event, "Customers/CustomerRecords");
                    }
                    else generateAlert(Alert.AlertType.ERROR, "Unable to update customer").showAndWait();
                }catch (NullPointerException e){
                    generateAlert(Alert.AlertType.ERROR, "Please enter valid values for each field").show();
                }
            }
        }
    }
    /**
     * Populates all fields with info of selected customer.
     * */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        customerNameTxt.setText(CustomersRecordsController.selectedCustomer.getName());
        customerIdTxt.setText(String.valueOf(CustomersRecordsController.selectedCustomer.getId()));
        customerAddressTxt.setText(CustomersRecordsController.selectedCustomer.getAddress());
        CountryCombo.setItems(Data.allCountries);//Throws null ptr exception
        CountryCombo.getSelectionModel().select(CustomersRecordsController.selectedCustomer.getCountry());
        try { DivisionCombo.setItems(Data.getDivisions(CustomersRecordsController.selectedCustomer.getCountry().toString())); } catch (SQLException e) { e.getMessage(); }
        DivisionCombo.getSelectionModel().select(CustomersRecordsController.selectedCustomer.getDivision());
        customerZipTxt.setText(CustomersRecordsController.selectedCustomer.getPostalCode());
        customerPhoneTxt.setText(CustomersRecordsController.selectedCustomer.getPhone());
        country = CustomersRecordsController.selectedCustomer.getCountry();
        division = CustomersRecordsController.selectedCustomer.getDivision();

    }

}
