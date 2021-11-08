package util;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/** DBLogs class */
public class DBLogs {

    /** Gets all appointments for the month
     *
     * @param year of the search
     * @param month of the search
     * @return count of appointments
     */
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

    /** Gets all appointments for the month by appointment type
     *
     * @param year of the search
     * @param month of the search
     * @param type of appointment
     * @return count of appointments
     */
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

    /** Counts new customers by month
     *
     * @param year of search
     * @param month of search
     * @return count of customers
     */
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

    /** Counts new customers created in selected year
     *
     * @param year of search
     * @return count of customers for year
     */
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
