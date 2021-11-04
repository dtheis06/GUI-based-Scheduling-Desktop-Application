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

}
