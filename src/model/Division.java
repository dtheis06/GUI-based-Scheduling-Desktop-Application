package model;
import java.sql.Timestamp;

/** Divisions Class. I assume that this divides the Country into states or provinces.*/
public class Division {
    String name;
    Timestamp creationDate;
    String createdBy; //todo change to User after creating user class
    Timestamp lastUpdate;
    String lastUpdatedBy; //todo change to User
    int countryID;


    /** Divisions Constructor */
    public Division(String name, int countryID) {
        this.name = name;
        this.countryID = countryID;
        //todo autogenerate id for division id
        //todo figure out timestamps for creationDate
        this.createdBy = "notsetyet"; //todo user instead of string
        //todo figure out timestamps for lastUpdate
        this.lastUpdatedBy = "notsetyet"; //todo user instead of string
    }


    /** Getters and Setters */
    int divisionID;

    public int getDivisionID() {
        return divisionID;
    }

    public void setDivisionID(int divisionID) {
        this.divisionID = divisionID;
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

    public int getCountryID() {
        return countryID;
    }

    public void setCountryID(int countryID) {
        this.countryID = countryID;
    }

}

