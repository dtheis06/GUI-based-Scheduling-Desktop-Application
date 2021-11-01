package util;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Appointment;
import model.Customer;

import java.io.IOException;
import java.net.URL;
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
        public void setBackButton (ActionEvent event){
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
        public void refresh() {
                customerTable.setItems(DBCustomer.getAllCustomers());
        }
}
