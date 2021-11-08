package util;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Country;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

/** CustomerAddController class, controls AddCustomer.fxml */
public class CustomerAddController implements Initializable {
    int incrementID = 0; //Default value to help generate CustomerID
    boolean error = false; //flags if there is a problem

    @FXML
    private TextField idText;

    @FXML
    private TextField nameText;

    @FXML
    private TextField addressText;

    @FXML
    private TextField postalText;

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;

    @FXML
    private Label errorLabel;

    @FXML
    private TextField phoneText;

    @FXML
    private ComboBox<String> countryCombo;

    @FXML
    private ComboBox<String> stateCombo;

    /**
     * Overrides Initializable
     * This method occurs as soon as the stage is loaded
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        countryCombo.setItems(DBCountries.getAllCountries());
    }

    /** Configures the cancel button */
    @FXML
    void setCancel(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow(); //gets the stage
        stage.close(); // closes it
    }

    /** Configures the Save Button */
    @FXML
    void setSaveButton(ActionEvent event) {
        try{
            error = false; //resets error
            errorLabel.setText(""); // resets error text
            incrementID();

            //Customer field info from textboxes/combos
            String user = LoginController.user; //Holds username of person logged in to log who created customer
            String name = nameText.getText();
            String address = addressText.getText();
            String postal = postalText.getText();
            String state = stateCombo.getSelectionModel().getSelectedItem();
            String phone = phoneText.getText();

            int stateID = DBFirstLevelDivisions.getStateID(state); //State id from state name

            //Date and Time stuff
            LocalDateTime ldtDate = LocalDateTime.now();
            Timestamp tsDate = Timestamp.valueOf(ldtDate);

            //Makes sure that required combos/textboxes are not empty
            if(name.isEmpty() || address.isEmpty() || postal.isEmpty() || state.equals(null) || phone.isEmpty() ) {
                errorLabel.setText("Missing customer information!");
                error = true;
            }
            //Creates a new customer and inserts the data into the database if no problems
            if(!error) {
                String sql2 = "INSERT INTO customers(Customer_ID, Customer_Name,Address,Postal_Code,Phone," +
                        "Create_Date,Created_By,Last_Update,Last_Updated_By,Division_ID) " +
                        "VALUES(?,?,?,?,?,?,?,?,?,?)";
                PreparedStatement ps = JDBC.getConnection().prepareStatement(sql2);
                ps.setInt(1, incrementID);
                ps.setString(2, name);
                ps.setString(3, address);
                ps.setString(4, postal);
                ps.setString(5, phone);
                ps.setTimestamp(6, tsDate);
                ps.setString(7, user);
                ps.setTimestamp(8, tsDate);
                ps.setString(9, user);
                ps.setInt(10, stateID);
                ps.execute();
                Stage stage = (Stage) saveButton.getScene().getWindow(); //gets the stage
                stage.close(); // closes it
            }
        } catch(NumberFormatException e) { //handles NumberFormatException
            errorLabel.setText("Number Format Error " + e.getMessage());
            error = true;
        } catch(IndexOutOfBoundsException e) { //handles IndexOutOfBoundsException
            errorLabel.setText("Something went wrong");
            error = true;
        } catch(NullPointerException e) { //handles NullPointerException
            errorLabel.setText("Country or State/Providence Missing");
                error = true;
        } catch(SQLException e) {
            }
        }

    /** Sets stateComboBox with the states/provinces of the selected country */
    @FXML
    void setStateBox(ActionEvent event) {
        String country = countryCombo.getSelectionModel().getSelectedItem();
        int countryID = DBCountries.getCountryID(country);
        stateCombo.setItems(DBFirstLevelDivisions.getStates(countryID));
    }

    /** Generate the CustomerID by selecting the highest value Appointment ID and adding one to it.*/
    private void incrementID() {
        try {
            String sql = "SELECT MAX(Customer_ID) FROM customers";
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                incrementID = rs.getInt("Customer_ID");
                incrementID = incrementID + 1;
            }
        } catch (SQLException e) {
            e.getStackTrace();
        }
    }

}
