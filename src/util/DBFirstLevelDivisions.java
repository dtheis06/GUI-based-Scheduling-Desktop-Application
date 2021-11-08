package util;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/** DBFirstLevelDivisions class */
public class DBFirstLevelDivisions {

    /** Gets all states/provinces from database that match countryID
     * @param countryID (Country_ID in db)
     * @return appointments, ObservableList<String> of all states/provinces that match
     */
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
    /** Gets stateID from stateName
     * @param stateName (State in db)
     * @return id (Division_ID in db)
     */
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

    /** Gets stateName from stateId out of database
     *
     * @param stateID (Division_ID in db)
     * @return state (Division in db)
     */
    public static String getStateName(int stateID) {
        String state = "";
        try {
            String sql = "SELECT Division from first_level_divisions " +
                    "WHERE Division_ID = ?";
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ps.setInt(1, stateID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                state = rs.getString("Division");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return state;
    }

    /** Gets country from stateID out of DB
     *
     * @param stateID (Division_ID in db)
     * @return countryID (Country_ID in db
     */
    public static int countryIDFromStateID(int stateID) {
        int countryID = 0;
        try {
            String sql = "SELECT Country_ID FROM first_level_divisions " +
                    "WHERE Division_ID = ?";
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ps.setInt(1, stateID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                countryID = rs.getInt("Country_ID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return countryID;
    }
}
