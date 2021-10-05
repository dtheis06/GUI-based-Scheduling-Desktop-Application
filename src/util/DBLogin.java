package util;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Appointment;
import model.Country;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class DBLogin {

    @FXML
    private AnchorPane bottomWindow;

    @FXML
    private TextField username;

    @FXML
    private PasswordField password;

    @FXML
    private Button login;

    @FXML
    private Button cancel;

    @FXML
    private Label errorLabel;

    public void setLogin(ActionEvent event) {

        String userName = username.getText();
        String passWord = password.getText();
        try {
            String sql = "SELECT * FROM users WHERE User_Name = ? and Password = ?";
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ps.setString(1,userName);
            ps.setString(2,passWord);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                errorLabel.setText("Incorrect username/password");
            }
            else{
                Parent parent = FXMLLoader.load(getClass().getResource("/fxml/Appointments.fxml"));
                Scene scene = new Scene(parent);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.show();
            }
        } catch (SQLException e) {
            e.getStackTrace();
        } catch (IOException e) {
            e.getStackTrace();
        }
    }
    public void setCancel() {
        Stage stage = (Stage) cancel.getScene().getWindow(); //gets the stage
        stage.close(); // closes it
    }
}
