package util;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Customer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;


/** CustomerEditController class, controls EditCustomer.fxml */
public class CustomerEditController {
    private Customer customer;
    private int index;
    boolean error = false;

    @FXML
    private TextField idText;

    @FXML
    private TextField nameText;

    @FXML
    private TextField addressText;

    @FXML
    private ComboBox<String> countryCombo;

    @FXML
    private ComboBox<String> stateCombo;

    @FXML
    private TextField postalText;

    @FXML
    private TextField phoneText;

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;

    @FXML
    private Label errorLabel;

    /** Imports the parameters from the AppointmentController
     * and sets the textboxes and combos
     * @param customer - customer we're making changes to
     * @param index - index of the customer in the combobox
     */
    public void initData(Customer customer, int index) {
        this.index = index;
        this.customer = customer;
        int stateID = customer.getDivisionID();
        int countryID = DBFirstLevelDivisions.countryIDFromStateID(stateID);
        String countryName = DBCountries.getCountryName(countryID);
        String stateName = DBFirstLevelDivisions.getStateName(stateID);
        countryCombo.setItems(DBCountries.getAllCountries());

        idText.setText(String.valueOf(customer.getCustomerID()));
        nameText.setText(customer.getName());
        addressText.setText(customer.getAddress());
        stateCombo.setValue(stateName);
        countryCombo.setValue(countryName);
        postalText.setText(customer.getPostalCode());
        phoneText.setText(customer.getPhoneNumber());
    }

/** Configures cancel button */
    @FXML
    void setCancel(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow(); //gets the stage
        stage.close(); // closes it
    }
    /** Configures save button */
    @FXML
    void setSaveButton(ActionEvent event) throws Exception {
        try{
            error = false; //resets error
            errorLabel.setText(""); //resets error text

            //Customer field info for selectedCustomer
            String user = LoginController.user; //Holds username of person logged in
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
            //Updates customer information if no problems
            if(!error) {
                String sql2 = "UPDATE customers SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, " +
                        "Create_Date = ?, Created_By = ?, Last_Update = ?, Last_Updated_By = ?, Division_ID = ? " +
                        "WHERE Customer_ID = ?";
                PreparedStatement ps = JDBC.getConnection().prepareStatement(sql2);
                ps.setString(1, name);
                ps.setString(2, address);
                ps.setString(3, postal);
                ps.setString(4, phone);
                ps.setTimestamp(5, tsDate);
                ps.setString(6, user);
                ps.setTimestamp(7, tsDate);
                ps.setString(8, user);
                ps.setInt(9, stateID);
                ps.setInt(10,customer.getCustomerID());
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
            System.out.println("SQLError " + e.getStackTrace());
            errorLabel.setText("SQL Error");
        }
    }

/** Sets stateComboBox with the states/provinces of the selected country */
    @FXML
    void setStateBox(ActionEvent event) {
        String country = countryCombo.getSelectionModel().getSelectedItem();
        int countryID = DBCountries.getCountryID(country);
        stateCombo.setItems(DBFirstLevelDivisions.getStates(countryID));
    }

}

