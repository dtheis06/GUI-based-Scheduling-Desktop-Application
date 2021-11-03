package util;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBUser {

    public static boolean inRange(int value) {
        boolean flag = false;
        try {
            String sql = "SELECT * FROM users WHERE User_ID = ? ";
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ps.setInt(1,value);
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
