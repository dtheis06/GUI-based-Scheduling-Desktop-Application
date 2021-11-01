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

public class AppointmentsController implements Initializable {

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

    @FXML
    private RadioButton allRadio;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        appointmentTable.setItems(DBAppointment.getAppointmentsForWeek());
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

    public void setAddButton(ActionEvent event) throws Exception {
        FXMLLoader fxmlLoader;
        fxmlLoader = new FXMLLoader(getClass().getResource("/Fxml/AddAppointment.fxml"));
        Parent root1 = fxmlLoader.load();
        Scene scene = new Scene(root1);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.showAndWait();
        refresh();
    }

    public void setEditButton(ActionEvent event) throws Exception {
        try {
            FXMLLoader fxmlLoader;
            fxmlLoader = new FXMLLoader(getClass().getResource("/Fxml/EditAppointment.fxml"));
            Parent root1 = fxmlLoader.load();

            Appointment appointment = appointmentTable.getSelectionModel().getSelectedItem();
            System.out.println("Appointment id " + appointment.getAppointmentID());
            int index = appointmentTable.getSelectionModel().getSelectedIndex();
            AppointmentEditController editController = fxmlLoader.getController();
            editController.initData(appointment, index);
            Scene scene = new Scene(root1);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.showAndWait();
            refresh();
        } catch (NullPointerException e) {
        }
    }

    public void setDeleteButton() {
        try {
            Appointment selectedAppointment = appointmentTable.getSelectionModel().getSelectedItem();
            int id = selectedAppointment.getAppointmentID();
            Alert a = new Alert(Alert.AlertType.CONFIRMATION);
            a.setTitle("Delete appointment \"" + appointmentTable.getSelectionModel().getSelectedItem().getAppointmentID() + "\"?");
            a.setHeaderText("Delete appointment \"" + appointmentTable.getSelectionModel().getSelectedItem().getAppointmentID() + "\"?");
            a.setContentText("Are you sure you want to delete this Appointment?");
            a.showAndWait();
            if (a.getResult() == ButtonType.OK) {
                String sql = "DELETE FROM appointments WHERE Appointment_ID = ?";
                PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
                ps.setInt(1, id);
                ps.executeUpdate();
                refresh();
            } else {
                a.close();
            }
        } catch (NullPointerException e) {
        } catch (SQLException e) {
        }
    }

    @FXML
    public void setWeekRadio() {
        appointmentTable.setItems(DBAppointment.getAppointmentsForWeek());
    }

    @FXML
    public void setMonthRadio() {
        appointmentTable.setItems(DBAppointment.getAppointmentsForMonth());
    }

    @FXML
    public void setAllRadio() {
        appointmentTable.setItems(DBAppointment.getAllAppointments());
    }

    public void refresh() {
        if (weekRadio.isSelected()) {
            appointmentTable.setItems(DBAppointment.getAppointmentsForWeek());
        } else if (monthRadio.isSelected()) {
            appointmentTable.setItems(DBAppointment.getAppointmentsForMonth());
        } else if (allRadio.isSelected()) {
            appointmentTable.setItems(DBAppointment.getAllAppointments());
        }
    }

    public void setCustomerRecordsButton(ActionEvent event) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("/fxml/CustomerRecords.fxml"));
            Scene scene = new Scene(parent);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.getStackTrace();
        }
    }
}

