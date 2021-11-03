package util;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Country;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBCountries {

    public static ObservableList<String> getAllCountries() {
        ObservableList<String> countries = FXCollections.observableArrayList();
        try {
            String sql = "SELECT Country from countries";
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                String countryName = rs.getString("Country");
                countries.add(countryName);
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return countries;
    }
    public static int getCountryID(String countryName) {
        int id = 0;
        try {
            String sql = "SELECT Country_ID from countries " +
                    "WHERE Country = ?";
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ps.setString(1, countryName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                id = rs.getInt("Country_ID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }
    public static String getCountryName(int countryID) {
        String name = "";
        try {
            String sql = "SELECT Country from countries " +
                    "WHERE Country_ID = ?";
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ps.setInt(1, countryID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                name = rs.getString("Country");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return name;
    }
}
