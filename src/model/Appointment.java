package model;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/** Appointment Class */

public class Appointment {
    int appointmentID;
    String name;
    String description;
    String location;
    String type;
    LocalDateTime startTime;
    LocalDateTime endTime;
    Timestamp creationDate;
    String createdBy; //todo change to user
    Timestamp lastUpdate;
    String lastUpdatedBy; //todo change to user
    int customerID;
    int userID;
    int contactID;

    public Appointment () {
        //todo autogenerate appointmentId;
        name = "";
        description = "";
        location = "";
        type = "";
        //todo localdatetimes & timestamps
        //todo user createdBy & updatedBy
        customerID = -1;
        userID = -1;
        contactID = -1;
    }
}
