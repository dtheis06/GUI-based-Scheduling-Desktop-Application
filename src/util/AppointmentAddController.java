package util;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Contact;

import java.net.URL;
import java.sql.*;
import java.time.*;
import java.util.ResourceBundle;

public class AppointmentAddController implements Initializable {
    int incrementID = 0;
    private String contactName;
    private ObservableList<LocalTime> startTimes = FXCollections.observableArrayList();
    private ObservableList<String> stringStartTimes = FXCollections.observableArrayList();
    private ObservableList<LocalTime> endTimes = FXCollections.observableArrayList();
    private ObservableList<String> stringEndTimes = FXCollections.observableArrayList();
    ZoneId z = ZoneId.systemDefault();
    LocalDate ld = LocalDate.now();
    ZonedDateTime zdt = ld.atStartOfDay(z);
    ZoneOffset zoneOffset = zdt.getOffset();
    int offSetInHours = zoneOffset.getTotalSeconds() / 3600;
    boolean error = false;


    @FXML
    private Label startLabel;

    @FXML
    private Label endLabel;

    @FXML
    private TextField appointmentText;

    @FXML
    private TextField nameText;

    @FXML
    private TextField locationText;

    @FXML
    private TextField typeText;

    @FXML
    private TextField customerText;

    @FXML
    private TextField userText;

    @FXML
    private ComboBox<String> contactNameCombo;

    @FXML
    private TextArea descriptionText;

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;

    @FXML
    private ComboBox<String> startTimeCombo;

    @FXML
    private ComboBox<String> endTimeCombo;

    @FXML
    private DatePicker datePicker;

    @FXML
    private Label errorLabel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        populateStartTimes();
        populateEndTimes();
        contactNameCombo.setItems(Contact.getContactNames());
        endTimeCombo.setItems(stringEndTimes);
        startTimeCombo.setItems(stringStartTimes);
    }

    public void setSaveButton(ActionEvent event) throws Exception {
        try {
            error = false;
            String user = LoginController.user;
            String title = nameText.getText();
            String description = descriptionText.getText();
            String location = locationText.getText();
            String type = typeText.getText();
            String customerID = customerText.getText();
            String userID = userText.getText();
            int intUserID = Integer.parseInt(userID);
            int intCustomerID = Integer.parseInt(customerID);
            contactName = contactNameCombo.getSelectionModel().getSelectedItem();
            int contactID = getContactIDFromContactName();
            LocalDateTime ldtDate = LocalDateTime.now();
            Timestamp tsDate = Timestamp.valueOf(ldtDate);
            int startTimeIndex = startTimeCombo.getSelectionModel().getSelectedIndex();
            int endTimeIndex = endTimeCombo.getSelectionModel().getSelectedIndex();
            LocalDateTime ldtStart = LocalDateTime.of(datePicker.getValue(), startTimes.get(startTimeIndex));
            LocalDateTime ldtEnd = LocalDateTime.of(datePicker.getValue(), endTimes.get(endTimeIndex));
            Timestamp start = Timestamp.valueOf(ldtStart);
            Timestamp end = Timestamp.valueOf(ldtEnd);
            incrementID();
            errorLabel.setText("");
                if(end.before(start) || end.equals(start)) {
                    errorLabel.setText("The appointment has to end before it starts!");
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
                    String sql2 = "INSERT INTO appointments(Appointment_ID, Title, Description, Location, Type, Start, End, Create_Date, Created_By, Last_Update, Last_Updated_By, Customer_ID, User_ID, Contact_ID) " +
                            "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                    PreparedStatement ps = JDBC.getConnection().prepareStatement(sql2);
                    ps.setInt(1, incrementID);
                    ps.setString(2, title);
                    ps.setString(3, description);
                    ps.setString(4, location);
                    ps.setString(5, type);
                    ps.setTimestamp(6, start);
                    ps.setTimestamp(7, end);
                    ps.setTimestamp(8, tsDate);
                    ps.setString(9, user);
                    ps.setTimestamp(10, tsDate);
                    ps.setString(11, user);
                    ps.setInt(12, intCustomerID);
                    ps.setInt(13, intUserID);
                    ps.setInt(14, contactID);
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

    private int getContactIDFromContactName() {
        int contactID = 999;
        try {
            String sql = "SELECT Contact_ID from contacts WHERE Contact_Name = ?";
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ps.setString(1, contactName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                contactID = rs.getInt("Contact_ID");
            }
        } catch (SQLException e) {
            e.getStackTrace();
        }
        return contactID;
    }
    private void populateStartTimes() throws NumberFormatException {
        try{
            for (int i = 0; i < 28; i++) {
                LocalTime aTime = LocalTime.of(12, 00);
                LocalTime aTime2 = aTime.plusHours(offSetInHours);
                LocalTime aTime3 = aTime2.plusMinutes(i * 30);
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
            for (int i = 0; i < 28; i++) {
                LocalTime aTime = LocalTime.of(12, 30);
                LocalTime aTime2 = aTime.plusHours(offSetInHours);
                LocalTime aTime3 = aTime2.plusMinutes(i * 30);
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
    public void setCancel() {
        Stage stage = (Stage) cancelButton.getScene().getWindow(); //gets the stage
        stage.close(); // closes it
    }
}

