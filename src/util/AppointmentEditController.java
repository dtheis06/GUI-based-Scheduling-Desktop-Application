package util;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Appointment;
import model.Contact;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.*;

public class AppointmentEditController{
    private Appointment selectedAppointment;
    private int selectedIndex;
    int incrementID = 0;
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

    @FXML
    public void setSaveButton(ActionEvent event) throws Exception {
        try {
            error = false;
            String user = LoginController.user;
            String title = nameText.getText();
            String description = descriptionText.getText();
            String location = locationText.getText();
            String type = typeCombo.getValue();
            String customerID = customerText.getText();
            String userID = userText.getText();
            int intUserID = Integer.parseInt(userID);
            intCustomerID = Integer.parseInt(customerID);
            contactName = contactNameCombo.getSelectionModel().getSelectedItem();
            int contactID = DBContact.getContactIDFromContactName(contactName);
            LocalDateTime ldtDate = LocalDateTime.now();
            Timestamp tsDate = Timestamp.valueOf(ldtDate);
            int startTimeIndex = startTimeCombo.getSelectionModel().getSelectedIndex();
            int endTimeIndex = endTimeCombo.getSelectionModel().getSelectedIndex();
            LocalDateTime ldtStart = LocalDateTime.of(datePicker.getValue(), startTimes.get(startTimeIndex));
            LocalDateTime ldtEnd = LocalDateTime.of(datePicker.getValue(), endTimes.get(endTimeIndex));
            start = Timestamp.valueOf(ldtStart);
            end = Timestamp.valueOf(ldtEnd);
            incrementID();
            errorLabel.setText("");
            overLappingAppointmentsCheck();
            if(end.before(start) || end.equals(start)) {
                errorLabel.setText("The appointment has to start before it ends!");
                error = true;
            }
            if(title.isEmpty() || location.isEmpty() || type.isEmpty() || start.equals(null) || end.equals(null) ||
                    customerID.isEmpty() || userID.isEmpty() || contactName.isEmpty()) {
                errorLabel.setText("Empty text field");
                error = true;
            }
            if(!DBUser.inRange(intUserID)) {
                errorLabel.setText("User ID doesn't exist");
                error = true;
            }
            if(!DBCustomer.inRange(intCustomerID)) {
                errorLabel.setText("Customer ID doesn't exist");
                error = true;
            }
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
                ps.execute();
                Stage stage = (Stage) saveButton.getScene().getWindow(); //gets the stage
                stage.close(); // closes it
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

    private void incrementID() {
        try {
            String sql = "SELECT MAX(Appointment_ID) FROM appointments";
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                incrementID = rs.getInt("Appointment_ID");
                incrementID = incrementID + 1;
            }
        } catch (SQLException e) {
            e.getStackTrace();
        }
    }
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
    public void setDate() {
        Timestamp timestamp = selectedAppointment.getEndTime();
        LocalDate date = timestamp.toLocalDateTime().toLocalDate();
        datePicker.setValue(date);
    }
    public void setCancel() {
        Stage stage = (Stage) cancelButton.getScene().getWindow(); //gets the stage
        stage.close(); // closes it
    }
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
    private void populateTypeCombo() {
        types.add("De-Briefing");
        types.add("Planning Session");
        types.add("Other");
        typeCombo.setItems(types);
    }
}
