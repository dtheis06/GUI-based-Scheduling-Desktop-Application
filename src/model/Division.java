package model;
import java.sql.Timestamp;

/** Divisions Class. Divisions divides the Country into states or provinces.*/
public class Division {
    String name;
    int countryID;


    /** Divisions Constructor */
    public Division(String name, int countryID) {
        this.name = name;
        this.countryID = countryID;

    }


    /** Getters and Setters */
    int divisionID;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCountryID() {
        return countryID;
    }

    public void setCountryID(int countryID) {
        this.countryID = countryID;
    }

}

