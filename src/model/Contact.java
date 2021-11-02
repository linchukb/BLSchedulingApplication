package model;
/**
 * The class Contact.java
 * */
public class Contact {

    int id;
    String name, email;
    /** Constructor.*/
    public Contact(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }
    /**
     * @return the contacts id;
     * */
    public int getId() {
        return id;
    }
    /**
     * @param id the id to set.
     * */
    public void setId(int id) {
        this.id = id;
    }
    /**
     * @return the name of the contact.
     * */
    public String getName() {
        return name;
    }
    /**
     * @param name the name to set.
     * */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * @return the email of the contact.
     * */
    public String getEmail() {
        return email;
    }
    /**
     * @param email the email to set.
     * */
    public void setEmail(String email) {
        this.email = email;
    }
    /**
     * @return the name of the contact in string form.
     * */
    @Override
    public String toString() {
        return name;
    }
}
