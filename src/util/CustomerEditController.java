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

    @FXML
    void setCancel(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow(); //gets the stage
        stage.close(); // closes it
    }

    @FXML
    void setSaveButton(ActionEvent event) throws Exception {
        try{
            error = false;
            String user = LoginController.user;
            String name = nameText.getText();
            String address = addressText.getText();
            String postal = postalText.getText();
            String state = stateCombo.getSelectionModel().getSelectedItem();
            int stateID = DBFirstLevelDivisions.getStateID(state);
            String phone = phoneText.getText();
            LocalDateTime ldtDate = LocalDateTime.now();
            Timestamp tsDate = Timestamp.valueOf(ldtDate);
            errorLabel.setText("");
            if(name.isEmpty() || address.isEmpty() || postal.isEmpty() || state.equals(null) || phone.isEmpty() ) {
                errorLabel.setText("Missing customer information!");
                error = true;
            }
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

    @FXML
    void setStateBox(ActionEvent event) {
        String country = countryCombo.getSelectionModel().getSelectedItem();
        int countryID = DBCountries.getCountryID(country);
        stateCombo.setItems(DBFirstLevelDivisions.getStates(countryID));
    }

}

