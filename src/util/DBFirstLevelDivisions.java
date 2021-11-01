package util;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBFirstLevelDivisions {


    public static ObservableList<String> getStates(int countryID) {
        ObservableList<String> states = FXCollections.observableArrayList();
        try {
            String sql = "SELECT Division from first_level_divisions WHERE " +
                    "Country_ID = ?";
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ps.setInt(1,countryID);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                String stateName = rs.getString("Division");
                states.add(stateName);
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return states;
    }
    public static int getStateID(String stateName) {
        int id = 0;
        try {
            String sql = "SELECT Division_ID from first_level_divisions " +
                    "WHERE Division = ?";
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ps.setString(1, stateName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                id = rs.getInt("Division_ID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }
}
