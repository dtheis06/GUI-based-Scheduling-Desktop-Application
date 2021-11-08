package util;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/** DBUsers class */
public class DBUser {

    /** Checks to see if value matches a User_ID in the database
     * @param id id to check
     * @return flag, true if there is a match
     */
    public static boolean isValid(int id) {
        boolean flag = false;
        try {
            String sql = "SELECT * FROM users WHERE User_ID = ? ";
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ps.setInt(1,id);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                flag = true;
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return flag;
    }
}
