package controller.Reports;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.TotalOfType;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static BLSchedulingApplication.BLSchedulingApplication.loadStage;
import static model.Data.*;
/**
 *  Controls total appointments report.
 * */
public class TotalAppointmentsController implements Initializable {
    /** Holds months to generate report about.*/
    @FXML
    private ComboBox<String> monthCombo;
    /** Table of entry pairs of class TotalOfType.*/
    @FXML
    private TableView<TotalOfType> typeTotalTableView;
    /** Type of appointment.*/
    @FXML
    private TableColumn<TotalOfType, String> typeCol;
    /** Number of appointments of corresponding type in selected month.*/
    @FXML
    private TableColumn<TotalOfType, Integer> totalCol;
    /**
     *  Updates table with info for selected month.
     * */
    @FXML
    void onActionGetReportForMonth(ActionEvent event) {
        initializeTypeTotalReport(monthCombo.getSelectionModel().getSelectedItem());
    }
    /**
     *  Returns to reports main screen.
     * */
    @FXML
    void onActionReportsMenu(ActionEvent event) throws IOException {
        loadStage(event, "reports/ReportsMenu");
    }
    /**
     *  Loads months into comboBox.
     *  Starts with January selected.
     * */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        monthCombo.setItems(monthNames);
        monthCombo.getSelectionModel().selectFirst();
        initializeTypeTotalReport(monthCombo.getSelectionModel().getSelectedItem());
        typeTotalTableView.setItems(totalOfTypes);
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        totalCol.setCellValueFactory(new PropertyValueFactory<>("count"));

    }

}
