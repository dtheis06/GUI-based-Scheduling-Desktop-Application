package util;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Customer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/** DBAppointment class */
public class DBCustomer {

    /** Gets all appointments from the database
     * @return appointments, all the appointments from the database */
    public static ObservableList<Customer> getAllCustomers() {
        ObservableList<Customer> customers = FXCollections.observableArrayList();
        try{
            String sql = "SELECT * from customers";
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                int customerID = rs.getInt("Customer_ID");
                String name = rs.getString("Customer_Name");
                String address = rs.getString("Address");
                String postalCode = rs.getString("Postal_Code");
                String phoneNumber = rs.getString("Phone");
                int divisionID = rs.getInt("Division_ID");

                Customer c = new Customer(customerID,name,address,postalCode,phoneNumber,divisionID);
                customers.add(c);
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return customers;
    }

    /** Checks to see if value matches a Customer_ID in the database
     * @param id id to check
     * @return flag, true if there is a match
     */
    public static boolean isValid(int id) {
        boolean flag = false;
        try {
            String sql = "SELECT * FROM customers WHERE Customer_ID = ? ";
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
