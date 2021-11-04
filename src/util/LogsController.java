package util;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Appointment;

import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.ResourceBundle;

public class LogsController implements Initializable {
    int year = 2021;

    @FXML
    private ComboBox<String> selectLogCombo;

    @FXML
    private ComboBox<Integer> selectYearCombo;

    @FXML
    private TextArea textArea;

    @FXML
    private Label label2;

    @FXML
    private ComboBox<String> selectContactCombo;

    @FXML
    private TableView appointmentTable;

    @FXML
    private TableColumn<Appointment, Integer> appointmentColumn;

    @FXML
    private TableColumn<Appointment, String> nameColumn;

    @FXML
    private TableColumn<Appointment, String> descriptionColumn;

    @FXML
    private TableColumn<Appointment, String> locationColumn;

    @FXML
    private TableColumn<Appointment, String> typeColumn;

    @FXML
    private TableColumn<Appointment, Integer> customerColumn;

    @FXML
    private TableColumn<Appointment, Integer> userColumn;

    @FXML
    private TableColumn<Appointment, Integer> contactColumn;

    @FXML
    private TableColumn<Appointment, Timestamp> startColumn;

    @FXML
    private TableColumn<Appointment, Timestamp> endColumn;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        populateLogCombo();
        populateYearCombo();
        selectContactCombo.setVisible(false);
        appointmentTable.setVisible(false);
    }
    private void populateLogCombo() {
        ObservableList<String> logs = FXCollections.observableArrayList();
        logs.add("Appointments");
        logs.add("Schedule");
        logs.add("Customer");
        selectLogCombo.setItems(logs);
    }
    private void populateYearCombo() {
        ObservableList<Integer> years = FXCollections.observableArrayList();
        int firstYear = 2020;
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int totalYears = currentYear - firstYear + 1;
        for (int i = 0; i < totalYears; i++) {
            years.add(firstYear+i);
        }
        selectYearCombo.setItems(years);
        selectYearCombo.setValue(currentYear);
    }
    private void populateContactCombo() {
        selectContactCombo.setItems(DBContact.getContactNames());
    }
    @FXML
    private void setLog(ActionEvent event) {
        year = selectYearCombo.getSelectionModel().getSelectedItem();
        if(selectLogCombo.getSelectionModel().getSelectedIndex() == 0) {
            textArea.setVisible(true);
            selectContactCombo.setVisible(false);
            appointmentTable.setVisible(false);
            label2.setText("Year");
            selectYearCombo.setVisible(true);
            setAppointmentsLog();
        }
        if(selectLogCombo.getSelectionModel().getSelectedIndex() == 1) {
            textArea.setVisible(false);
            label2.setText("Contact");
            selectContactCombo.setVisible(true);
            appointmentTable.setVisible(true);
            populateContactCombo();
            setScheduleLog();
        }
/*        if(selectLogCombo.getSelectionModel().getSelectedIndex() == 3) {
            setCustomerLog();*/
        /*}*/
    }
    private void setAppointmentsLog() {
        textArea.setText("Appointments By Month:" + "\n" + "\n"
                           + "January: " + DBLogs.countAppointmentsForMonth(year,1) + "\n"
                            + "February: "+ DBLogs.countAppointmentsForMonth(year,2) + "\n"
                            + "March: "+ DBLogs.countAppointmentsForMonth(year,3) + "\n"
                + "April: " + DBLogs.countAppointmentsForMonth(year,4) + "\n"
                + "May: " + DBLogs.countAppointmentsForMonth(year,5) + "\n"
                + "June: " + DBLogs.countAppointmentsForMonth(year,6) + "\n"
                + "July: " + DBLogs.countAppointmentsForMonth(year,7) + "\n"
                + "August: " + DBLogs.countAppointmentsForMonth(year,8) + "\n"
                + "September: " + DBLogs.countAppointmentsForMonth(year,9) + "\n"
                + "October: " + DBLogs.countAppointmentsForMonth(year,10) + "\n"
                + "November: " + DBLogs.countAppointmentsForMonth(year,11) + "\n"
                + "December: " + DBLogs.countAppointmentsForMonth(year,12) + "\n");

    }
    private void setScheduleLog() {
        String contact = selectContactCombo.getSelectionModel().getSelectedItem();
        int contactID = DBContact.getContactIDFromContactName(contact);
        appointmentTable.setItems(DBAppointment.getContactAppointments(contactID));
        appointmentColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        startColumn.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        endColumn.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        customerColumn.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        userColumn.setCellValueFactory(new PropertyValueFactory<>("userID"));
        contactColumn.setCellValueFactory(new PropertyValueFactory<>("contactID"));
        }
}
