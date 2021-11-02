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
import static model.Data.addCustomer;

/**
 * Controls add customer screen.
 * */
public class AddCustomerController implements Initializable {
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
    /** Holds selected country.*/
    private Country country;
    /** Selection field for customer first level division.*/
    @FXML
    private ComboBox<Division> DivisionCombo;
    /** Holds selected first level division.*/
    private Division division;
    /**
     * Loads division comboBox with those of the selected country.
     * */
    @FXML
    void onActionCountryCombo(ActionEvent event) throws SQLException {
        // Get divisions of selected Country
        country = CountryCombo.getSelectionModel().getSelectedItem();

        if(country != null){
            DivisionCombo.setItems(Data.getDivisions(CountryCombo.getSelectionModel().getSelectedItem().toString()));
            DivisionCombo.getSelectionModel().selectFirst();
        }
    }
    /**
     * Returns to customer records screen. Discards changes
     * */
    @FXML
    void onActionCustomerRecords(ActionEvent event) throws IOException {
        Optional<ButtonType> result = generateAlert(Alert.AlertType.CONFIRMATION, "Are you sure?\nAll data will be lost.").showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK)
            loadStage(event, "Customers/CustomerRecords");
    }
    /**
     * Sets division to that selected in comboBox.
     * */
    @FXML
    void onActionDivisionCombo(ActionEvent event) {
        division = DivisionCombo.getSelectionModel().getSelectedItem();
    }
    /**
     * Saves new customer. If unable, appropriate error message displayed in dialog window.
     * */
    @FXML
    void onActionSaveNewCustomer(ActionEvent event) throws IOException, SQLException {

        Customer customer = new Customer(
                customerNameTxt.getText(),
                customerAddressTxt.getText(),
                country,
                division,
                customerZipTxt.getText(),
                customerPhoneTxt.getText(),
                Data.currentUser.getName(),
                Data.currentUser.getName()
        );

        // Input validation
        if(
                customerNameTxt.getText().equals("") ||
                customerAddressTxt.getText().equals("") || //TODO: add address prompt text by country
                customerZipTxt.getText().equals("") ||
                customerPhoneTxt.getText().equals("") ||
                (country == null) ||
                (division == null)
        ){
            generateAlert(Alert.AlertType.ERROR, "Please enter valid values for each field").show();
        }else{
            try {
                Optional<ButtonType> result = generateAlert(Alert.AlertType.CONFIRMATION, "Are you sure you want to save?").showAndWait();
                if(result.isPresent() && result.get() == ButtonType.OK)
                    if(addCustomer(customer)){
                        generateAlert(Alert.AlertType.INFORMATION, "Customer Added").showAndWait();
                        loadStage(event, "Customers/CustomerRecords");
                    }
                    else generateAlert(Alert.AlertType.ERROR, "Unable to add customer").showAndWait();
            }catch (NullPointerException e){
                generateAlert(Alert.AlertType.ERROR, "Please enter valid values for each field").show();
            }
        }

    }
    /**
     * Loads country ComboBox with list of countries.
     * */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        CountryCombo.setItems(Data.allCountries);//Throws null ptr exception
    }

}
