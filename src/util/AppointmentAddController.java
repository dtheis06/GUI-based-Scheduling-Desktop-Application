package util;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.*;
import java.time.*;
import java.util.ResourceBundle;

/** AppointmentAddController Class - Controller for AppointmentAdd.fxml, the window that shows when you want to add an appointment*/
public class AppointmentAddController implements Initializable {
    int incrementID = 0; //default value for the Appointment ID generator
    private String contactName;
    private ObservableList<LocalTime> startTimes = FXCollections.observableArrayList(); //HOlds the different possible startTimes for appointments
    private ObservableList<String> stringStartTimes = FXCollections.observableArrayList(); //String form of startTimes
    private ObservableList<LocalTime> endTimes = FXCollections.observableArrayList(); //HOlds the different possible endTimes for appointments
    private ObservableList<String> stringEndTimes = FXCollections.observableArrayList();//String form of endTimes
    private ObservableList<String> types = FXCollections.observableArrayList(); //Holds the different types of appointments
    private ZoneId z = ZoneId.systemDefault(); //Default zone id for user's operating system
    private LocalDate ld = LocalDate.now();
    private ZonedDateTime zdt = ld.atStartOfDay(z);
    private ZoneOffset zoneOffset = zdt.getOffset(); //Gets the offset of the user's operating system from UTC
    private int offSetInHours = zoneOffset.getTotalSeconds() / 3600; //Converts that offset into hours
    private boolean error = false; //flag to see if there is anything input incompatible
    private static int intCustomerID;
    private static Timestamp start;
    private static Timestamp end;

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
    private ComboBox<String> typeCombo;

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

    /**
     * Overrides Initializable
     * This method occurs as soon as the scene is loaded
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        populateStartTimes();
        populateEndTimes();
        populateTypeCombo();
        contactNameCombo.setItems(DBContact.getContactNames());
        endTimeCombo.setItems(stringEndTimes);
        startTimeCombo.setItems(stringStartTimes);

        //LAMBDA- easier way to set the cancelbutton. With this way, you don't have to create a method and select that method in scenebuilder.
        cancelButton.setOnAction(e -> {
            Stage stage = (Stage) cancelButton.getScene().getWindow();
            stage.close();
        });
    }
    /** Configures the Save Button */
    public void setSaveButton() throws Exception {
        try {
            error = false; //resets error
            incrementID(); //Appointment ID is auto incremented/generated
            errorLabel.setText(""); //Resets error text

            //Appointment field information from textboxes/combos
            String user = LoginController.user; //Holds the username of the logged in individual who made the appointment
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
            LocalDateTime ldtDate = LocalDateTime.now(); //Value for system default time of now
            Timestamp tsDate = Timestamp.valueOf(ldtDate);//Converts ldtDate to timestamp
            int startTimeIndex = startTimeCombo.getSelectionModel().getSelectedIndex();//logs the index of the selected item in the startTimeCombo
            int endTimeIndex = endTimeCombo.getSelectionModel().getSelectedIndex();//logs the index of the selected item in the endTimeCombo
            LocalDateTime ldtStart = LocalDateTime.of(datePicker.getValue(), startTimes.get(startTimeIndex));//Indexes align for the <String>combo and Observablelist<LocalTime>
            LocalDateTime ldtEnd = LocalDateTime.of(datePicker.getValue(), endTimes.get(endTimeIndex)); //Combines the value of the date picker with the index to create a LocalDateTime
            start = Timestamp.valueOf(ldtStart);//Converts the ldt created in the last statement to a timestamp to log the start time in the database
            end = Timestamp.valueOf(ldtEnd);//Converts the ldt created earlier to a timestamp to log the end time in the database
            overLappingAppointmentsCheck();

            //Ensures the appointment doesn't end before or when it starts
            if(end.before(start) || end.equals(start)) {
                errorLabel.setText("The appointment has to end before it starts!");
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
            //Creates a new appointment and inserts it into the database if no problems
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

    /** Generate the Appointment ID by selecting the highest value Appointment ID and adding one to it.*/
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

    /** Checks to see if the Customer would have Over Lapping Appointments with the new appointment */
    private void overLappingAppointmentsCheck() {
        try {
            String sql = "SELECT Start, End FROM appointments " +
                    "WHERE Customer_ID = ?";
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ps.setInt(1,intCustomerID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
               Timestamp dbStart = rs.getTimestamp("Start");
               Timestamp dbEnd = rs.getTimestamp("End");
                if(start.equals(dbStart) || (start.after(dbStart) && start.before(dbEnd)) || (end.after(dbStart) && end.before(dbEnd))
                || end.equals(dbEnd)){
                    error = true;
                    errorLabel.setText("Error: Overlapping appointments");
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

