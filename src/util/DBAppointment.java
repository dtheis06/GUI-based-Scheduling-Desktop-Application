package util;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointment;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.*;

/** DBAppointment class */
public class DBAppointment {

    /** Gets all appointments from the database
     * @return appointments, all the appointments from the database
     */
    public static ObservableList<Appointment> getAllAppointments() {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        try {
            String sql = "SELECT * from appointments";
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
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

                Appointment c = new Appointment(appointmentID, title, description, location, type, start, end, customerID, userID, contactID);
                appointments.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointments;
    }

    /** Gets all appointments within the next seven days from the database
     * @return appointments, all the appointments within the next seven days
     */
    public static ObservableList<Appointment> getAppointmentsForWeek() {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        try {
            String sql = "SELECT * " +
                    "FROM appointments " +
                    "WHERE Start BETWEEN ? AND ?";
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            Timestamp tsNow = Timestamp.valueOf(LocalDateTime.now());
            Timestamp tsAWeekFromNow = Timestamp.valueOf(LocalDateTime.now().plusWeeks(1));
            ps.setTimestamp(1, tsNow);
            ps.setTimestamp(2, tsAWeekFromNow);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
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

                Appointment c = new Appointment(appointmentID, title, description, location, type, start, end, customerID, userID, contactID);
                appointments.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointments;
    }

    /** Gets all appointments within the next month from the database
     * @return appointments, all the appointments within the next month
     */
    public static ObservableList<Appointment> getAppointmentsForMonth() {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        try {
            String sql = "SELECT * " +
                    "FROM appointments " +
                    "WHERE Start BETWEEN ? AND ?";
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            Timestamp tsNow = Timestamp.valueOf(LocalDateTime.now());
            Timestamp tsAMonthFromNow = Timestamp.valueOf(LocalDateTime.now().plusMonths(1));
            ps.setTimestamp(1, tsNow);
            ps.setTimestamp(2, tsAMonthFromNow);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
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

                Appointment c = new Appointment(appointmentID, title, description, location, type, start, end, customerID, userID, contactID);
                appointments.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointments;
    }

    /** Gets all appointments that contain the specified customerID
     * @return appointments, all the appointments with customerID
     * @param custID, the customerID of the appointments we want
     */
    public static ObservableList<Appointment> getCustomerAppointments(int custID) {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        try {
            String sql = "SELECT * " +
                    "FROM appointments " +
                    "WHERE Customer_ID = ?";
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ps.setInt(1, custID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
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

                Appointment c = new Appointment(appointmentID, title, description, location, type, start, end, customerID, userID, contactID);
                appointments.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointments;
    }
    /** Checks to see if any appointment has the customerID
     * @return hasAps, true if appointments are found
     * @param customerID, customerID we want to check that has appointments
     */
    public static boolean hasAppointments(int customerID) {
        boolean hasAps = false;
        try {
            String sql = "SELECT * " +
                    "FROM appointments " +
                    "WHERE Customer_ID = ?";
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ps.setInt(1, customerID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                hasAps = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return hasAps;
    }

    /** Returns all appointments that have the contactID
     * @return appointments, all appointments that match
     * @param contactID of the appointments we want
     */
    public static ObservableList<Appointment> getContactAppointments(int contactID) {
        ObservableList<Appointment> appointments =  FXCollections.observableArrayList();
        try {
            String sql = "SELECT * " +
                    "FROM appointments " +
                    "WHERE Contact_ID = ? ORDER BY Start";
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ps.setInt(1, contactID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int appointmentID = rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                String description = rs.getString("Description");
                String location = rs.getString("Location");
                String type = rs.getString("Type");
                Timestamp start = rs.getTimestamp("Start");
                Timestamp end = rs.getTimestamp("End");
                int customerID = rs.getInt("Customer_ID");
                int userID = rs.getInt("User_ID");
                Appointment c = new Appointment(appointmentID, title, description, location, type, start, end, customerID, userID, contactID);
                appointments.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointments;
    }
}
