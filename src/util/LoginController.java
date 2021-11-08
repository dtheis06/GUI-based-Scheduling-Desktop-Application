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
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

/** LoginController class */
public class LoginController implements Initializable {
    static String user = "unknown";
    ZoneId id = ZoneId.systemDefault();
    boolean error = false;

    @FXML
    private AnchorPane bottomWindow;

    @FXML
    private TextField username;

    @FXML
    private PasswordField password;

    @FXML
    private Button login;

    @FXML
    private Button exit;

    @FXML
    private Label errorLabel;

    @FXML
    private Label zoneLabel;

    @FXML
    private Label loginLabel;

    @FXML
    private Label userNameLabel;

    @FXML
    private Label passwordLabel;

    /**
     * Override Initializable
     * This method occurs as soon as the scene is loaded
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        errorLabel.setVisible(false);
        zoneLabel.setText(zoneLabel.getText() + " " + id.toString());

        //LAMBDA #2. Easier and quicker way to create functionality for the exit button.
        exit.setOnAction(e -> System.exit(0));
    }
    /** Configures Login Button */
    public void setLogin(ActionEvent event) {
        String userName = username.getText();
        String passWord = password.getText();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM.dd.yyyy HH:mm:ss");
        LocalDateTime time = LocalDateTime.now(ZoneOffset.UTC);
        String sTime = formatter.format(time);
            try {
                String sql = "SELECT * FROM users WHERE User_Name = ? and Password = ?";
                PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
                ps.setString(1, userName);
                ps.setString(2, passWord);
                ResultSet rs = ps.executeQuery();
                if (!rs.next()) {
                    FileWriter writer = new FileWriter("login_activity", true);
                    BufferedWriter bw = new BufferedWriter(writer);
                    error = true;
                    errorLabel.setVisible(true);

                    //Writes the login attempt to login_activity.txt
                    bw.append(sTime + "        User: " + userName + "       Action: " + "Unsuccessful login attempt" + "\n");
                    bw.flush();
                    bw.close();
                } else {
                    FileWriter writer = new FileWriter("login_activity", true);
                    BufferedWriter bw = new BufferedWriter(writer);

                    //Writes the login attempt to login_activity.txt
                    bw.append(sTime + "        User: " + userName + "       Action: " + "Successful login attempt" + "\n");
                    bw.flush();
                    bw.close();
                    user = username.getText(); //assigns for future use
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
}
