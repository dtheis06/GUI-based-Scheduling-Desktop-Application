package util;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointment;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class DBAppointment {

    public static ObservableList<Appointment> getAllAppointments() {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        try{
            String sql = "SELECT * from appointments";
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                int appointmentID = rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                String description = rs.getString("Description");
                String location = rs.getString("Location");
                String type = rs.getString("Type");
                Timestamp start = rs.getTimestamp("Start");
                Timestamp end = rs.getTimestamp("End");
                int customerID = rs.getInt("Customer_ID");
                int userID = rs.getInt("User_ID");
                int contactID = rs.getInt("Contact_ID");

                Appointment c = new Appointment(appointmentID,title,description,location,type,start,end,customerID,userID,contactID);
                appointments.add(c);
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return appointments;
    }
    public static ObservableList<Appointment> getAppointmentsForWeek() {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        try{
            String sql = "SELECT * " +
                    "FROM appointments " +
                    "WHERE Start BETWEEN ? AND ?";
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            Timestamp tsNow = Timestamp.valueOf(LocalDateTime.now());
            Timestamp tsAWeekFromNow = Timestamp.valueOf(LocalDateTime.now().plusWeeks(1));
            ps.setTimestamp(1, tsNow);
            ps.setTimestamp(2, tsAWeekFromNow);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                int appointmentID = rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                String description = rs.getString("Description");
                String location = rs.getString("Location");
                String type = rs.getString("Type");
                Timestamp start = rs.getTimestamp("Start");
                Timestamp end = rs.getTimestamp("End");
                int customerID = rs.getInt("Customer_ID");
                int userID = rs.getInt("User_ID");
                int contactID = rs.getInt("Contact_ID");

                Appointment c = new Appointment(appointmentID,title,description,location,type,start,end,customerID,userID,contactID);
                appointments.add(c);
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return appointments;
    }
    public static ObservableList<Appointment> getAppointmentsForMonth() {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        try{
            String sql = "SELECT * " +
                    "FROM appointments " +
                    "WHERE Start BETWEEN ? AND ?";
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            Timestamp tsNow = Timestamp.valueOf(LocalDateTime.now());
            Timestamp tsAMonthFromNow = Timestamp.valueOf(LocalDateTime.now().plusMonths(1));
            ps.setTimestamp(1, tsNow);
            ps.setTimestamp(2, tsAMonthFromNow);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                int appointmentID = rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                String description = rs.getString("Description");
                String location = rs.getString("Location");
                String type = rs.getString("Type");
                Timestamp start = rs.getTimestamp("Start");
                Timestamp end = rs.getTimestamp("End");
                int customerID = rs.getInt("Customer_ID");
                int userID = rs.getInt("User_ID");
                int contactID = rs.getInt("Contact_ID");

                Appointment c = new Appointment(appointmentID,title,description,location,type,start,end,customerID,userID,contactID);
                appointments.add(c);
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return appointments;
    }
    public static ObservableList<Appointment> getCustomerAppointments(int custID) {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        try{
            String sql = "SELECT * " +
                    "FROM appointments " +
                    "WHERE Customer_ID = ?";
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ps.setInt(1, custID);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                int appointmentID = rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                String description = rs.getString("Description");
                String location = rs.getString("Location");
                String type = rs.getString("Type");
                Timestamp start = rs.getTimestamp("Start");
                Timestamp end = rs.getTimestamp("End");
                int customerID = rs.getInt("Customer_ID");
                int userID = rs.getInt("User_ID");
                int contactID = rs.getInt("Contact_ID");

                Appointment c = new Appointment(appointmentID,title,description,location,type,start,end,customerID,userID,contactID);
                appointments.add(c);
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return appointments;
    }
}
