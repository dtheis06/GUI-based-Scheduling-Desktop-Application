package util;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;

public class DBAppointment {

    @FXML
    private TableView<?> appointmentTable;

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

}

