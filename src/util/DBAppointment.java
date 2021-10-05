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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Appointment;
import model.Country;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ResourceBundle;

public class DBAppointment implements Initializable {

    @FXML
    private TableView<Appointment> appointmentTable;

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

    @FXML
    private Button addButton;

    @FXML
    private Button editButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button customerRecordsButton;

    @FXML
    private Button logoutButton;

    @FXML
    private RadioButton monthRadio;

    @FXML
    private ToggleGroup filterToggle;

    @FXML
    private RadioButton weekRadio;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        appointmentTable.setItems(Appointment.getAllAppointments());
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


    public void setLogoutButton(ActionEvent event) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("/fxml/login.fxml"));
            Scene scene = new Scene(parent);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.getStackTrace();
        }
    }
}

