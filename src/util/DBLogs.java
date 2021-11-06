package util;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointment;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;

public class DBLogs {

    public static int countAppointmentsForMonth(int year, int month) {
        int count = 0;
        try{
            String sql = "SELECT COUNT(*) AS total " +
                    "FROM appointments " +
                    "WHERE MONTH(Start) = ? AND YEAR(Start) = ?";
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ps.setInt(1, month);
            ps.setInt(2, year);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
               count = rs.getInt("total");
            }
        } catch(SQLException e) {
            e.printStackTrace();
         }
        return count;
    }
    public static int countAppointmentsForMonth(int year, int month, String type) {
        int count = 0;
        try{
            String sql = "SELECT COUNT(*) AS total " +
                    "FROM appointments " +
                    "WHERE MONTH(Start) = ? AND YEAR(Start) = ? AND Type = ?";
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ps.setInt(1, month);
            ps.setInt(2, year);
            ps.setString(3,type);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                count = rs.getInt("total");
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return count;
    }
    public static int countCustomersForMonth(int year,int month) {
        int count = 0;
        try{
            String sql = "SELECT COUNT(*) AS total " +
                    "FROM customers " +
                    "WHERE MONTH(Create_Date) = ? AND YEAR(Create_Date) = ?";
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ps.setInt(1, month);
            ps.setInt(2, year);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                count = rs.getInt("total");
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return count;
    }
    public static int countCustomersForYear(int year) {
        int count = 0;
        try{
            String sql = "SELECT COUNT(*) AS total " +
                    "FROM customers " +
                    "WHERE YEAR(Create_Date) = ?";
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ps.setInt(1, year);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                count = rs.getInt("total");
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return count;
    }
}
