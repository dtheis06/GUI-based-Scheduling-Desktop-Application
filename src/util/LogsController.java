package util;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Appointment;

import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.ResourceBundle;

/** LogsController class - controller for Logs.fxml*/
public class LogsController implements Initializable {
    LocalDate current_date = LocalDate.now();
    int year = current_date.getYear();

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

    /**
     * Override Initializable
     * This method occurs as soon as the scene is loaded
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
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
        populateLogCombo();
        populateYearCombo();
        populateContactCombo();
        selectLogCombo.getSelectionModel().selectFirst();
        selectContactCombo.getSelectionModel().selectFirst();
        setLog();
        selectContactCombo.setVisible(false);
        appointmentTable.setVisible(false);
    }
    /** Populates the selectLogCombo Box */
    private void populateLogCombo() {
        ObservableList<String> logs = FXCollections.observableArrayList();
        logs.add("Appointments");
        logs.add("Schedule");
        logs.add("Customer");
        selectLogCombo.setItems(logs);
    }
    /** Populates the selectYearCombo Box */
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
    /** Populates the selectContactCombo box */
    private void populateContactCombo() {
        selectContactCombo.setItems(DBContact.getContactNames());
    }

    /** Functionality to show a log based on what is selected in the
     * comboboxes
     */
    @FXML
    private void setLog(){
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
            selectYearCombo.setVisible(false);
            selectContactCombo.setVisible(true);
            appointmentTable.setVisible(true);
            setScheduleLog();
        }
          if(selectLogCombo.getSelectionModel().getSelectedIndex() == 2) {
              textArea.setVisible(true);
              selectContactCombo.setVisible(false);
              appointmentTable.setVisible(false);
              label2.setText("Year");
              selectYearCombo.setVisible(true);
              setCustomerLog();
          }
    }
    /** Log for the Appointments selection */
    private void setAppointmentsLog()  {
        textArea.setText("Appointments By Month:" + "\n" + "\n"
                + "January    Total: " + DBLogs.countAppointmentsForMonth(year,1) + "    " + "Planning Sessions: " + DBLogs.countAppointmentsForMonth(year,1,"Planning Session") + "    "
                +  "De-Briefings: " +  DBLogs.countAppointmentsForMonth(year,1,"De-Briefing")+"    " + "Other: "  +  DBLogs.countAppointmentsForMonth(year,1,"Other") +"\n"
                + "February    Total: "+ DBLogs.countAppointmentsForMonth(year,2) + "    " + "Planning Sessions: " + DBLogs.countAppointmentsForMonth(year,2,"Planning Session") + "    "
                +  "De-Briefings: "  +  DBLogs.countAppointmentsForMonth(year,2,"De-Briefing")+"    " + "Other: "  +  DBLogs.countAppointmentsForMonth(year,2,"Other") +"\n"
                + "March    Total: "+ DBLogs.countAppointmentsForMonth(year,3) + "    " + "Planning Sessions: " + DBLogs.countAppointmentsForMonth(year,3,"Planning Session") + "    "
                +  "De-Briefings: "  +  DBLogs.countAppointmentsForMonth(year,3,"De-Briefing")+"    " + "Other: "  +  DBLogs.countAppointmentsForMonth(year,3,"Other") +"\n"
                + "April    Total: " + DBLogs.countAppointmentsForMonth(year,4) +  "    " + "Planning Sessions: " + DBLogs.countAppointmentsForMonth(year,4,"Planning Session") + "    "
                +  "De-Briefings: "  +  DBLogs.countAppointmentsForMonth(year,4,"De-Briefing")+"    " + "Other: "  +  DBLogs.countAppointmentsForMonth(year,4,"Other") +"\n"
                + "May    Total: " + DBLogs.countAppointmentsForMonth(year,5) +  "    " + "Planning Sessions: " + DBLogs.countAppointmentsForMonth(year,5,"Planning Session") + "     "
                +  "De-Briefings: "  +  DBLogs.countAppointmentsForMonth(year,5,"De-Briefing")+"    " + "Other: "  +  DBLogs.countAppointmentsForMonth(year,5,"Other") +"\n"
                + "June    Total: " + DBLogs.countAppointmentsForMonth(year,6) +   "    " + "Planning Sessions: " + DBLogs.countAppointmentsForMonth(year,6,"Planning Session") + "   "
                +  "De-Briefings: "  +  DBLogs.countAppointmentsForMonth(year,6,"De-Briefing")+"    " + "Other: "  +  DBLogs.countAppointmentsForMonth(year,6,"Other") +"\n"
                + "July    Total: " + DBLogs.countAppointmentsForMonth(year,7) + "   " + "Planning Sessions: " + DBLogs.countAppointmentsForMonth(year,7,"Planning Session") + "    "
                +  "De-Briefings: "  +  DBLogs.countAppointmentsForMonth(year,7,"De-Briefing")+"    " + "Other: "  +  DBLogs.countAppointmentsForMonth(year,7,"Other") +"\n"
                + "August    Total: " + DBLogs.countAppointmentsForMonth(year,8) + "     " + "Planning Sessions: " + DBLogs.countAppointmentsForMonth(year,8,"Planning Session") + "    "
                +  "De-Briefings: "  +  DBLogs.countAppointmentsForMonth(year,8,"De-Briefing")+"    " + "Other: "  +  DBLogs.countAppointmentsForMonth(year,8,"Other") +"\n"
                + "September    Total: " + DBLogs.countAppointmentsForMonth(year,9) + "    " + "Planning Sessions: " + DBLogs.countAppointmentsForMonth(year,9,"Planning Session") + "    "
                +  "De-Briefings: "  +  DBLogs.countAppointmentsForMonth(year,9,"De-Briefing")+"    " + "Other: "  +  DBLogs.countAppointmentsForMonth(year,9,"Other") +"\n"
                + "October    Total: " + DBLogs.countAppointmentsForMonth(year,10) + "    " + "Planning Sessions: " + DBLogs.countAppointmentsForMonth(year,10,"Planning Session") + "     "
                +  "De-Briefings: "  +  DBLogs.countAppointmentsForMonth(year,10,"De-Briefing")+"    " + "Other: "  +  DBLogs.countAppointmentsForMonth(year,10,"Other") +"\n"
                + "November    Total: " + DBLogs.countAppointmentsForMonth(year,11) +  "    " + "Planning Sessions: " + DBLogs.countAppointmentsForMonth(year,11,"Planning Session") + "   "
                +  "De-Briefings: "  +  DBLogs.countAppointmentsForMonth(year,11,"De-Briefing")+"    " + "Other: "  +  DBLogs.countAppointmentsForMonth(year,11,"Other") +"\n"
                + "December    Total: " + DBLogs.countAppointmentsForMonth(year,12) + "    " + "Planning Sessions: " + DBLogs.countAppointmentsForMonth(year,12,"Planning Session") + "    "
                +  "De-Briefings: "  +  DBLogs.countAppointmentsForMonth(year,12,"De-Briefing")+"    " + "Other: "  +  DBLogs.countAppointmentsForMonth(year,12,"Other") +"\n");


    }
    /** Log for the Schedule selection */
    private void setScheduleLog()  {
        String contact = selectContactCombo.getSelectionModel().getSelectedItem();
        int contactID = DBContact.getContactIDFromContactName(contact);
        appointmentTable.setItems(DBAppointment.getContactAppointments(contactID));
        }

    /** Log for the Customers selection */
    private void setCustomerLog(){
        textArea.setText("New Customers by Month" + "\n" + "\n" +
                "January: " + DBLogs.countCustomersForMonth(year, 1) + "\n" +
                "February: " + DBLogs.countCustomersForMonth(year, 2) + "\n" +
                "March: " + DBLogs.countCustomersForMonth(year, 3) + "\n" +
                "April: " + DBLogs.countCustomersForMonth(year, 4) + "\n" +
                "May: " + DBLogs.countCustomersForMonth(year, 5) + "\n" +
                "June: " + DBLogs.countCustomersForMonth(year, 6) + "\n" +
                "July: " + DBLogs.countCustomersForMonth(year, 7) + "\n" +
                "August: " + DBLogs.countCustomersForMonth(year, 8) + "\n" +
                "September: " + DBLogs.countCustomersForMonth(year, 9) + "\n" +
                "October: " + DBLogs.countCustomersForMonth(year, 10) + "\n" +
                "November: " + DBLogs.countCustomersForMonth(year, 11) + "\n" +
                "December: " + DBLogs.countCustomersForMonth(year, 12) + "\n" +
                "Total: " + DBLogs.countCustomersForYear(year));
        }
}

