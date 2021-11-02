package controller.Main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;


import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import static BLSchedulingApplication.BLSchedulingApplication.*;
/**
 * Controls Main Menu.
 * */
public class MainMenuController implements Initializable {
    /**
     * Loads customer records main screen.
     * */
    @FXML
    void onActionCustomerRecords(ActionEvent event)throws IOException{
        loadStage(event, "customers/CustomerRecords");
    }
    /**
     * Loads scheduling main screen.
     * */
    @FXML
    void onActionScheduling(ActionEvent event)throws IOException{
        loadStage(event, "scheduling/SchedulingMain");
    }
    /**
     * Loads reports main screen.
     * */
    @FXML
    void onActionReports(ActionEvent event)throws IOException{
        loadStage(event, "reports/ReportsMenu");
    }
    /**
     * Logs out. Returns to login Screen.
     * */
    @FXML
    void onActionLogout(ActionEvent event) throws IOException {
        Optional<ButtonType> result = generateAlert(Alert.AlertType.CONFIRMATION, "Are you sure you want to logout?").showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK)
            loadStage(event, "main/Login");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb){ }

}
