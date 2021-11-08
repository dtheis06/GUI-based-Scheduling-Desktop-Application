package model;


/** Contact Class */
public class Contact {
    private int contactID;
    private String name;
    private String email;

    /** Contact Constructor */
    public Contact(int contactID, String name, String email) {
        this.contactID = contactID;
        this.name = name;
        this.email = email;
    }
    /** Contact Getters & Setters*/

    public int getContactID() {
        return contactID;
    }

    public void setContactID(int contactID) {
        this.contactID = contactID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


}
