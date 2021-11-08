package model;

import java.sql.Timestamp;


/** Appointment Class */
public class Appointment {
    private int appointmentID;
    private String name;
    private String description;
    private String location;
    private String type;
    private Timestamp startTime;
    private Timestamp endTime;
    private int customerID;
    private int userID;
    private int contactID;

    /** Appointment Constructor */
    public Appointment (int appointmentID, String name, String description, String location,String type,
                        Timestamp startTime,Timestamp endTime,int customerID,int userID,int contactID) {
        this.appointmentID = appointmentID;
        this.name = name;
        this.description = description;
        this.location = location;
        this.type = type;
        this.startTime = startTime;
        this.endTime = endTime;
        this.customerID = customerID;
        this.userID = userID;
        this.contactID = contactID;
    }
    /** Appointment getters & setters */

    public int getAppointmentID() {
        return appointmentID;
    }

    public void setAppointmentID(int appointmentID) {
        this.appointmentID = appointmentID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getContactID() {
        return contactID;
    }

    public void setContactID(int contactID) {
        this.contactID = contactID;
    }

}
