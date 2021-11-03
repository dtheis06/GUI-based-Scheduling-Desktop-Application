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
import model.Customer;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ResourceBundle;

public class CustomerRecordsController implements Initializable {
        Customer selectedCustomer;

        @FXML
        private TableColumn<Customer, Integer> customerIDColumn;

        @FXML
        private TableColumn<Customer, String> customerNameColumn;

        @FXML
        private TableColumn<Customer, String> customerAddressColumn;

        @FXML
        private TableColumn<Customer, Integer> customerPostalColumn;

        @FXML
        private TableColumn<Customer, String> customerPhoneColumn;

        @FXML
        private TableColumn<Customer, Integer> customerDivisionColumn;

        @FXML
        private Button backButton;

        @FXML
        private Button addAppointment;

        @FXML
        private Button editAppointment;

        @FXML
        private Button deleteCustomer;

        @FXML
        private TableView<Customer> customerTable;

        @FXML
        private TableView<Appointment> appointmentTable;

        @FXML
        private TableColumn<Appointment, Integer> appointmentColumn;

        @FXML
        private TableColumn<Appointment, String> nameColumn;

        @FXML
        private TableColumn<Appointment, String> locationColumn;

        @FXML
        private TableColumn<Appointment, String> typeColumn;

        @FXML
        private TableColumn<Appointment, Timestamp> startColumn;

        @FXML
        private TableColumn<Appointment, Timestamp> endColumn;

        @FXML
        private TableColumn<Appointment, String> userColumn;

        @FXML
        private Button addCustomer;

        @FXML
        private Button editCustomer;

        @FXML
        private Button deleteAppointment;

        @Override
        public void initialize(URL url, ResourceBundle rb) {
                customerIDColumn.setCellValueFactory(new PropertyValueFactory<>("customerID"));
                customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
                customerAddressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
                customerPostalColumn.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
                customerPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
                customerDivisionColumn.setCellValueFactory(new PropertyValueFactory<>("divisionID"));
                customerTable.setItems(DBCustomer.getAllCustomers());

                appointmentColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
                nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
                locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
                typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
                startColumn.setCellValueFactory(new PropertyValueFactory<>("startTime"));
                endColumn.setCellValueFactory(new PropertyValueFactory<>("endTime"));
                userColumn.setCellValueFactory(new PropertyValueFactory<>("userID"));
        }

        public void setBackButton(ActionEvent event) {
                try {
                        Parent parent = FXMLLoader.load(getClass().getResource("/fxml/Appointments.fxml"));
                        Scene scene = new Scene(parent);
                        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        stage.setScene(scene);
                        stage.show();
                } catch (IOException e) {
                        e.getStackTrace();
                }
        }

        public void onCustomerSelected() {
                selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
                appointmentTable.setItems(DBAppointment.getCustomerAppointments(selectedCustomer.getCustomerID()));
        }

        public void setAddCustomer(ActionEvent event) {
                try {
                        FXMLLoader fxmlLoader;
                        fxmlLoader = new FXMLLoader(getClass().getResource("/Fxml/AddCustomer.fxml"));
                        Parent root1 = fxmlLoader.load();
                        Scene scene = new Scene(root1);
                        Stage stage = new Stage();
                        stage.setScene(scene);
                        stage.showAndWait();
                        refresh();
                } catch (IOException e) {
                        e.getStackTrace();
                }
        }

        public void setEditCustomer(ActionEvent event) throws Exception {
                try {
                        FXMLLoader fxmlLoader;
                        fxmlLoader = new FXMLLoader(getClass().getResource("/Fxml/EditCustomer.fxml"));
                        Parent root1 = fxmlLoader.load();

                        Customer customer = customerTable.getSelectionModel().getSelectedItem();
                        int index = customerTable.getSelectionModel().getSelectedIndex();
                        CustomerEditController editController = fxmlLoader.getController();
                        editController.initData(customer, index);
                        Scene scene = new Scene(root1);
                        Stage stage = new Stage();
                        stage.setScene(scene);
                        stage.showAndWait();
                        refresh();
                } catch (NullPointerException e) {

                }
        }

        public void setDeleteCustomer() {
                try {
                        Customer selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
                        int id = selectedCustomer.getCustomerID();
                        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
                        a.setTitle("Delete customer \"" + customerTable.getSelectionModel().getSelectedItem().getCustomerID() + "\"?");
                        a.setHeaderText("Delete customer \"" + customerTable.getSelectionModel().getSelectedItem().getCustomerID() + "\"?");
                        a.setContentText("Are you sure you want to delete this Customer?");
                        a.showAndWait();
                        if (a.getResult() == ButtonType.OK && DBAppointment.hasAppointments(id)) {
                                Alert b = new Alert(Alert.AlertType.ERROR);
                                b.setContentText("Customer has appointments and therefore cannot be deleted!");
                                b.showAndWait();
                        }
                        if (a.getResult() == ButtonType.OK && !DBAppointment.hasAppointments(id)) {
                                String sql = "DELETE FROM customers WHERE Customer_ID = ?";
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

        public void setAddAppointment() throws Exception{
                FXMLLoader fxmlLoader;
                fxmlLoader = new FXMLLoader(getClass().getResource("/Fxml/AddAppointment.fxml"));
                Parent root1 = fxmlLoader.load();
                Scene scene = new Scene(root1);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.showAndWait();
                refresh();
        }

        public void setEditAppointment() throws Exception {
                try {
                        FXMLLoader fxmlLoader;
                        fxmlLoader = new FXMLLoader(getClass().getResource("/Fxml/EditAppointment.fxml"));
                        Parent root1 = fxmlLoader.load();

                        Appointment appointment = appointmentTable.getSelectionModel().getSelectedItem();
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
        public void setDeleteAppointment() throws Exception {
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
        public void refresh() {
                customerTable.setItems(DBCustomer.getAllCustomers());
                appointmentTable.setItems(DBAppointment.getCustomerAppointments(selectedCustomer.getCustomerID()));
        }
}
