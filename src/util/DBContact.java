package util;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/** DBContact class */
public class DBContact {

    /** Generates a list of all Contact_Names from the database
     *
     * @return contactNames ObservableList<String> of all Contact_Names
     */
    public static ObservableList<String> getContactNames() {
        ObservableList<String> contactNames = FXCollections.observableArrayList();
        try{
            String sql = "SELECT Contact_Name from contacts";
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                String name = rs.getString("Contact_Name");
                contactNames.add(name);
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return contactNames;
    }
    /** Returns ContactID from inputted contactName
     * @param contactName that you want the contactID of
     * @return contactID of contactName
     */
    public static int getContactIDFromContactName(String contactName) {
        int contactID = 999;
        try {
            String sql = "SELECT Contact_ID from contacts WHERE Contact_Name = ?";
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ps.setString(1, contactName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                contactID = rs.getInt("Contact_ID");
            }
        } catch (SQLException e) {
            e.getStackTrace();
        }
        return contactID;
    }
}
