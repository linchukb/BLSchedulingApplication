package model;

import java.sql.Timestamp;
import java.time.LocalDateTime;
/**
 * The class Appointment.java.
 * */
public class Appointment {

    int id,
        customerId,
        userId;

    String  title,
            description,
            location,
            type,
            createdBy,
            contactName,
            startString,
            endString,
            customerName;

    LocalDateTime start = null,
                end = null,
                createDate = null;

    Timestamp lastUpdate;
    Contact contact;
    Customer customer;
    User user;

    /** Default Constructor*/
    public Appointment(){}
    /** Partial Constructor.*/
    public Appointment(int customerId, String title, String description, String location, String type, LocalDateTime start, LocalDateTime end) {
        this.customerId = customerId;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.start = start;
        this.end = end;
    }
    /** Full Constructor.*/
    public Appointment(
            int appointmentId,
            String title,
            String description,
            String location,
            String type,
            LocalDateTime start,
            LocalDateTime end,
            LocalDateTime createDate,
            String createdBy,
            String startString,
            String endString,
            String customerName,
            Contact contact,
            Customer customer,
            User user
    ){
        this.id = appointmentId;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.start = start;
        this.end = end;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.startString = startString;
        this.endString = endString;
        this.customerName = customerName;
        this.contact = contact;
        this.customer = customer;
        this.user = user;
    }
    /**
     * @return the user.
     * */
    public User getUser() {
        return user;
    }
    /**
     * @param user the user to set.
     * */
    public void setUser(User user) {
        this.user = user;
    }
    /**
     * @return the customer.
     * */
    public Customer getCustomer() {
        return customer;
    }
    /**
     * @param customer the customer to set.
     * */
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    /**
     * @return the customers name.
     * */
    public String getCustomerName() { return customerName; }
    /**
     * @param customerName the customers name to set.
     * */
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    /**
     * @return appointment end date and time in string form.
     * */
    public String getEndString() {
        return endString;
    }
    /**
     * @param endString appointment end date and time in string form.
     * */
    public void setEndString(String endString) {
        this.endString = endString;
    }
    /**
     * @return appointment start date and time in string form.
     * */
    public String getStartString() {
        return startString;
    }
    /**
     * @param startString appointment start date and time in string form.
     * */
    public void setStartString(String startString) {
        this.startString = startString;
    }
    /**
     * Currently unused.
     * */
    public String getContactName() {
        return contactName;
    }
    /**
     * @param contactName name of the contact.
     * */
    public void setContactName(String contactName) {
        this.contactName = contactName;
    }
    /**
     * @return the appointment ID.
     * */
    public int getId() {
        return id;
    }
    /**
     * @param id the ID to set.
     * */
    public void setId(int id) {
        this.id = id;
    }
    /**
     * @return the customer ID.
     * */
    public int getCustomerId() {
        return customerId;
    }
    /**
     * @param customerId the customer ID to set.
     * */
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }
    /**
     * @return the user ID.
     * */
    public int getUserId() {
        return userId;
    }
    /**
     * @param userId the user id to set.
     * */
    public void setUserId(int userId) {
        this.userId = userId;
    }
    /**
     * @return title of the appointment.
     * */
    public String getTitle() {
        return title;
    }
    /**
     * @param title the title to set.
     * */
    public void setTitle(String title) {
        this.title = title;
    }
    /**
     * @return description of the appointment.
     * */
    public String getDescription() {
        return description;
    }
    /**
     * @param description the description to set.
     * */
    public void setDescription(String description) {
        this.description = description;
    }
    /**
     * @return location of the appointment.
     * */
    public String getLocation() {
        return location;
    }
    /**
     * @param location the location to set.
     * */
    public void setLocation(String location) {
        this.location = location;
    }
    /**
     * @return the type of appointment.
     * */
    public String getType() {
        return type;
    }
    /**
     * @param type the type to set.
     * */
    public void setType(String type) {
        this.type = type;
    }
    /**
     * @return who created the appointment.
     * */
    public String getCreatedBy() {
        return createdBy;
    }
    /**
     * @param createdBy the creator to set.
     * */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
    /**
     * @return start date and time of the appointment.
     * */
    public LocalDateTime getStart() {
        return start;
    }
    /**
     * @param start the start date and time to set.
     * */
    public void setStart(LocalDateTime start) {
        this.start = start;
    }
    /**
     * @return the end date and time of the appointment.
     * */
    public LocalDateTime getEnd() {
        return end;
    }
    /**
     * @param end the end date and time to set.
     * */
    public void setEnd(LocalDateTime end) {
        this.end = end;
    }
    /**
     * @return string form of start
     * */
    public LocalDateTime getCreateDate() {
        return createDate;
    }
    /**
     * @param createDate string form of start to set.
     * */
    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }
    /**
     * @return time appointment was last updated.
     * */
    public Timestamp getLastUpdate() {
        return lastUpdate;
    }
    /**
     * Currently unused.
     * */
    public void setLastUpdate(Timestamp lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
    /**
     * @return the contact assigned to the appointment.
     * */
    public Contact getContact() {
        return contact;
    }
    /**
     * @param contact the contact to assign to the appointment.
     * */
    public void setContact(Contact contact) {
        this.contact = contact;
    }
}
