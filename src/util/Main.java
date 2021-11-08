package util;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import util.JDBC;

import java.util.Locale;
import java.util.ResourceBundle;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Locale currentLocale = Locale.getDefault();
        //Locale currentLocale = Locale.CANADA_FRENCH;
        ResourceBundle bundle = ResourceBundle.getBundle("Properties.C195", currentLocale);
        Parent parent = FXMLLoader.load(getClass().getResource("/fxml/login.fxml"), bundle);
        Scene scene = new Scene(parent);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        JDBC.makeConnection();
        launch(args);
        JDBC.closeConnection();
    }
}

