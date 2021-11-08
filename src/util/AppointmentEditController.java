package util;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Appointment;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.*;

/** AppointmentEditController Class - Controller for AppointmentEdit.fxml, the window that shows when you want to edit an appointment*/
public class AppointmentEditController{
    private Appointment selectedAppointment;
    private int selectedIndex;
    private String contactName;
    private ObservableList<LocalTime> startTimes = FXCollections.observableArrayList();
    private ObservableList<String> stringStartTimes = FXCollections.observableArrayList();
    private ObservableList<LocalTime> endTimes = FXCollections.observableArrayList();
    private ObservableList<String> stringEndTimes = FXCollections.observableArrayList();
    private ObservableList<String> types = FXCollections.observableArrayList();
    ZoneId z = ZoneId.systemDefault();
    LocalDate ld = LocalDate.now();
    ZonedDateTime zdt = ld.atStartOfDay(z);
    ZoneOffset zoneOffset = zdt.getOffset();
    int offSetInHours = zoneOffset.getTotalSeconds() / 3600;
    boolean error = false;
    private static int intCustomerID;
    private static Timestamp start;
    private static Timestamp end;
    private static int intAppointmentID;

    @FXML
    private TextField appointmentText;

    @FXML
    private TextField nameText;

    @FXML
    private TextField locationText;

    @FXML
    private ComboBox<String> typeCombo;

    @FXML
    private TextField customerText;

    @FXML
    private TextField userText;

    @FXML
    private ComboBox<String> contactNameCombo;

    @FXML
    private DatePicker datePicker;

    @FXML
    private ComboBox<String> startTimeCombo;

    @FXML
    private ComboBox<String> endTimeCombo;

    @FXML
    private TextArea descriptionText;

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;

    @FXML
    private Label errorLabel;

    /** Imports the parameters from the AppointmentController
     * and sets the textboxes and combos
     * @param appointment - appointment we're making changes to
     * @param index - index of the appointment in the combobox
     */
    public void initData(Appointment appointment, int index) {
        selectedIndex = index;
        selectedAppointment = appointment;
        populateTypeCombo();
        typeCombo.setValue(appointment.getType());
        populateStartTimes();
        populateEndTimes();
        contactNameCombo.setItems(DBContact.getContactNames());
        endTimeCombo.setItems(stringEndTimes);
        startTimeCombo.setItems(stringStartTimes);
        intAppointmentID = selectedAppointment.getAppointmentID();

        appointmentText.setText(String.valueOf(intAppointmentID));
        nameText.setText(selectedAppointment.getName());
        locationText.setText(selectedAppointment.getLocation());
        typeCombo.setValue(selectedAppointment.getType());
        customerText.setText(String.valueOf(selectedAppointment.getCustomerID()));
        userText.setText(String.valueOf(selectedAppointment.getUserID()));
        descriptionText.setText(selectedAppointment.getDescription());
        setDate();
        setContactName();
        setStartTime();
        setEndTime();
    }

    /** Configures the Save Button */
    public void setSaveButton() throws Exception {
        try {
            error = false; //Flag to see if an input is incompatible with database
            errorLabel.setText("");//Resets error text

            //Appointment field information from textboxes
            String user = LoginController.user;
            String title = nameText.getText();
            String description = descriptionText.getText();
            String location = locationText.getText();
            String type = typeCombo.getValue();
            String customerID = customerText.getText();
            String userID = userText.getText();
            contactName = contactNameCombo.getSelectionModel().getSelectedItem();

            //Parsed appointment field information for database
            int intUserID = Integer.parseInt(userID);
            intCustomerID = Integer.parseInt(customerID);
            int contactID = DBContact.getContactIDFromContactName(contactName);

            //Date & Time stuff
            LocalDateTime ldtDate = LocalDateTime.now();//Value for system default time of now
            Timestamp tsDate = Timestamp.valueOf(ldtDate);//Converts ldtDate to timestamp
            int startTimeIndex = startTimeCombo.getSelectionModel().getSelectedIndex();//logs the index of the selected item in the startTimeCombo
            int endTimeIndex = endTimeCombo.getSelectionModel().getSelectedIndex();//logs the index of the selected item in the endTimeCombo
            LocalDateTime ldtStart = LocalDateTime.of(datePicker.getValue(), startTimes.get(startTimeIndex));//Indexes align for the <String>combo and Observablelist<LocalTime>
            LocalDateTime ldtEnd = LocalDateTime.of(datePicker.getValue(), endTimes.get(endTimeIndex));//Combines the value of the date picker with the index to create a LocalDateTime
            start = Timestamp.valueOf(ldtStart);//Converts the ldt created in the last statement to a timestamp to log the start time in the database
            end = Timestamp.valueOf(ldtEnd);//Converts the ldt created earlier to a timestamp to log the end time in the database
            overLappingAppointmentsCheck();

            //Ensures the appointment doesn't end before or when it starts
            if(end.before(start) || end.equals(start)) {
                errorLabel.setText("The appointment has to start before it ends!");
                error = true;
            }
            //Makes sure that required combos/textboxes are not empty
            if(title.isEmpty() || location.isEmpty() || type.isEmpty() || start.equals(null) || end.equals(null) ||
                    customerID.isEmpty() || userID.isEmpty() || contactName.isEmpty()) {
                errorLabel.setText("Empty text field");
                error = true;
            }
            //Ensures that the userID input matches one in the database
            if(!DBUser.isValid(intUserID)) {
                errorLabel.setText("User ID doesn't exist");
                error = true;
            }
            //Ensures that the customerID input matches one in the database
            if(!DBCustomer.isValid(intCustomerID)) {
                errorLabel.setText("Customer ID doesn't exist");
                error = true;
            }
            //Updates appointment if no problems
            if(!error) {
                String sql2 = "UPDATE appointments SET Title = ?, Description = ?, Location = ?, " +
                        "Type = ?, Start = ?, End = ?, Create_Date = ?, Created_By = ?, Last_Update = ?, Last_Updated_By = ?, " +
                        "Customer_ID = ?, User_ID = ?, Contact_ID = ? " +
                        "WHERE Appointment_ID = ?";
                PreparedStatement ps = JDBC.getConnection().prepareStatement(sql2);
                ps.setString(1, title);
                ps.setString(2, description);
                ps.setString(3, location);
                ps.setString(4, type);
                ps.setTimestamp(5, start);
                ps.setTimestamp(6, end);
                ps.setTimestamp(7, tsDate);
                ps.setString(8, user);
                ps.setTimestamp(9, tsDate);
                ps.setString(10, user);
                ps.setInt(11, intCustomerID);
                ps.setInt(12, intUserID);
                ps.setInt(13, contactID);
                ps.setInt(14,selectedAppointment.getAppointmentID());
                ps.execute(); //Executes SQL
                Stage stage = (Stage) saveButton.getScene().getWindow(); //gets the stage
                stage.close(); // closes stage
            }
        } catch(NumberFormatException e) { //handles NumberFormatException
            errorLabel.setText("Number Format Error " + e.getMessage());
            error = true;
        } catch(IndexOutOfBoundsException e) { //handles IndexOutOfBoundsException
            errorLabel.setText("Something is missing " + e.getMessage());
            error = true;
        } catch(NullPointerException e) { //handles NullPointerException
            errorLabel.setText("NullPointerError " + e.getMessage());
            error = true;
        }
    }

    /** Sets Contact Name */
    public void setContactName() {
        try {
            String sql = "SELECT Contact_Name FROM Contacts WHERE Contact_ID = ?";
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ps.setInt(1,selectedAppointment.getContactID());
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                contactNameCombo.setValue(rs.getString("Contact_Name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /** Sets the startTimeCombo */
    public void setStartTime() {
        Timestamp timestamp = selectedAppointment.getStartTime();
        LocalTime time = timestamp.toLocalDateTime().toLocalTime();
        String sTime = "";
        if (time.getHour() > 12) {
            sTime = time.minusHours(12).toString() + " PM";
        }
        if (time.getHour() == 12) {
            sTime = time + " PM";
        } else if (time.getHour() < 12) {
            sTime = time + " AM";
        }
        startTimeCombo.setValue(sTime);
    }

    /** Sets the endTimeCombo */
    public void setEndTime() {
        Timestamp timestamp = selectedAppointment.getEndTime();
        LocalTime time = timestamp.toLocalDateTime().toLocalTime();
        String sTime = "";
        if (time.getHour() > 12) {
            sTime = time.minusHours(12).toString() + " PM";
        }
        if (time.getHour() == 12) {
            sTime = time + " PM";
        } else if (time.getHour() < 12) {
            sTime = time + " AM";
        }
        endTimeCombo.setValue(sTime);
    }

    /** Generates the start times for the <String>startTimeCombo */
    private void populateStartTimes() throws NumberFormatException {
        try{
            for (int i = 0; i < 56; i++) {
                LocalTime aTime = LocalTime.of(12, 00);
                LocalTime aTime2 = aTime.plusHours(offSetInHours);
                LocalTime aTime3 = aTime2.plusMinutes(i * 15);
                String sTime3 = "";
                if (aTime3.getHour() > 12) {
                    sTime3 = aTime3.minusHours(12).toString() + " PM";
                }
                if (aTime3.getHour() == 12) {
                    sTime3 = aTime3 + " PM";
                } else if (aTime3.getHour() < 12) {
                    sTime3 = aTime3 + " AM";
                }
                startTimes.add(aTime3);
                stringStartTimes.add(sTime3);
            }
        }  catch(NumberFormatException e){
            error = true;
        }
    }

    /** Generates the end times for the <String>startTimeCombo */
    private void populateEndTimes() throws NumberFormatException {
        try {
            for (int i = 0; i < 56; i++) {
                LocalTime aTime = LocalTime.of(12, 15);
                LocalTime aTime2 = aTime.plusHours(offSetInHours);
                LocalTime aTime3 = aTime2.plusMinutes(i * 15);
                String sTime3 = "";
                if (aTime3.getHour() > 12) {
                    sTime3 = aTime3.minusHours(12) + " PM";
                }
                if (aTime3.getHour() == 12) {
                    sTime3 = aTime3 + " PM";
                } else if (aTime3.getHour() < 12) {
                    sTime3 = aTime3 + " AM";
                }
                endTimes.add(aTime3);
                stringEndTimes.add(sTime3);
            }
        }
        catch(NumberFormatException e){
            error = true;
        }
    }

    /** Sets the date for the datePicker */
    public void setDate() {
        Timestamp timestamp = selectedAppointment.getEndTime();
        LocalDate date = timestamp.toLocalDateTime().toLocalDate();
        datePicker.setValue(date);
    }

    /** Sets Cancel Button */
    public void setCancel() {
        Stage stage = (Stage) cancelButton.getScene().getWindow(); //gets the stage
        stage.close(); // closes it
    }

    /** Checks to see if the Customer would have Over Lapping Appointments with the new appointment */
    private void overLappingAppointmentsCheck() {
        try {
            String sql = "SELECT Start, End, Appointment_ID FROM appointments " +
                    "WHERE Customer_ID = ?";
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ps.setInt(1,intCustomerID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Timestamp dbStart = rs.getTimestamp("Start");
                Timestamp dbEnd = rs.getTimestamp("End");
                int appID = rs.getInt("Appointment_ID");
                if(appID != intAppointmentID) {
                    if(start.equals(dbStart) || (start.after(dbStart) && start.before(dbEnd)) || (end.after(dbStart) && end.before(dbEnd))
                            || end.equals(dbEnd)){
                        error = true;
                        errorLabel.setText("Error: Overlapping appointments");
                    }
                }
            }
        } catch (SQLException e) {
            e.getStackTrace();
        }
    }

    /** Populates the <String>typeCombo with all the different types of appointments*/
    private void populateTypeCombo() {
        types.add("De-Briefing");
        types.add("Planning Session");
        types.add("Other");
        typeCombo.setItems(types);
    }
}
