package model;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import util.DBConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/** Countries Class for assigning a Country to a User */

public class Country {
    int countryID;
    String name;
    Timestamp creationDate;
    String createdBy; //todo change to User after creating user class
    Timestamp lastUpdate;
    String lastUpdatedBy; //todo change to User


    /** Countries Constructor */
    public Country(int countryID, String name) {
        //todo autogenerateIdMethod this.countryID = ...
        this.name = name;
        this.countryID = countryID;
        //this.creationDate = LocalDateTime.now();
        this.createdBy = "notsetyet"; //todo user instead of string
        //this.lastUpdate = LocalDateTime.now();
        this.lastUpdatedBy = "notsetyet"; //todo user instead of string


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

    public Timestamp getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Timestamp getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Timestamp lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }
}
