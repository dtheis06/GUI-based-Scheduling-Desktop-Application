package model;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import util.JDBC;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/** Countries Class for assigning a Country to a User */

public class Country {
    int countryID;
    String name;

    /**
     * Countries Constructor
     */
    public Country(int countryID, String name) {
        this.name = name;
        this.countryID = countryID;


        /** Getters and Setters */
    }

    public int getCountryID() {
        return countryID;
    }

    public void setCountryID(int countryID) {
        this.countryID = countryID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

