package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

import javafx.scene.control.Alert;
import utils.DBConnection;

import static BLSchedulingApplication.BLSchedulingApplication.generateAlert;
import static utils.DBQuery.getPreparedStatement;
import static utils.DBQuery.setPreparedStatement;
/**
 *  The class Data.java.
 *  Most manipulation of data done in the class.
 *  Most data stored in this class.
 * */
public class Data {
    /** The user currently logged in.*/
    public static User currentUser = new User();
    /** UTC timezone.*/
    public static ZoneId zoneId_UTC = ZoneId.of("UTC");
    /** EST timezone. Used to determine business hours.*/
    public static ZoneId zoneId_EST = ZoneId.of("US/Eastern");
    /** System timezone.*/
    public static ZoneId zoneId_System = ZoneId.of(TimeZone.getDefault().getID());
    /**
     * Checks if a time span is within business hours.
     * @param start the beginning of the span to check.
     * @param end the end of the span to check
     * @return whether or not the time is within the business hours (8am - 10pm EST)
     * */
    public static boolean isDuringBusinessHours(LocalDateTime start, LocalDateTime end) { // TODO: tips to verify working correctly? seems ± 1hr off
        LocalDateTime appointmentStartEST = convertTimeZone(start, zoneId_System, zoneId_EST);
        LocalDateTime appointmentEndEst = convertTimeZone(end, zoneId_System, zoneId_EST);
        return (appointmentStartEST.getHour() >= 8) && (appointmentEndEst.getHour() < 22);
    }
    /**
     *  Converts a LocalDateTime from one timezone to another.
     * @param originalLocalDateTime the date and time to convert.
     * @param currentZoneId the timezone to convert from.
     * @param targetZoneId the timezone to convert to.
     * @return the time in the desired timezone.
     * */
    public static LocalDateTime convertTimeZone(LocalDateTime originalLocalDateTime, ZoneId currentZoneId, ZoneId targetZoneId){

        LocalDate currentDate = LocalDate.of(originalLocalDateTime.getYear(),originalLocalDateTime.getMonth(),originalLocalDateTime.getDayOfMonth());
        LocalTime currentTime = LocalTime.of(originalLocalDateTime.getHour(),originalLocalDateTime.getMinute());
        ZonedDateTime currentZoneDateTime = ZonedDateTime.of(currentDate, currentTime, currentZoneId);
        ZonedDateTime targetZoneDateTime = currentZoneDateTime.withZoneSameInstant(targetZoneId);

        return targetZoneDateTime.toLocalDateTime();
    }
    /**
     *  Checks if an appointment overlaps a previous appointment of the customer.
     * @param appointmentToAdd the appointment to check.
     * @return whether or not the customer has a conflicting appointment.
     * */
    public static boolean overlapsCustomerAppointment(Appointment appointmentToAdd){ //TODO: Finish
        for(Appointment appointment : allAppointments){ // Check all appointments
            // Check if same customer | Check if same appointment (needed for update since appointment already in allAppointments)
            if((appointment.getCustomer().getId() == appointmentToAdd.getCustomer().getId()) && (appointment.getId() != appointmentToAdd.getId())){
                if(appointmentToAdd.getStart().toLocalDate().equals(appointment.getStart().toLocalDate())){ // Check if same date
                    if(                                                                                     // Check overlapping time
                            // case 1 - start1 = start2
                            appointmentToAdd.getStart().equals(appointment.getStart()) ||
                            // case 2 - start1 = end2
                            appointmentToAdd.getStart().equals(appointment.getEnd()) ||
                            // case 3 - end1 = start2
                            (appointmentToAdd.getEnd().equals(appointment.getStart())) ||
                            // case 4 - end1 = end2
                            (appointmentToAdd.getEnd().equals(appointment.getEnd())) ||
                            // case 5 - start1 between (start2 and end2)
                            (appointmentToAdd.getStart().isAfter(appointment.getStart()) && appointmentToAdd.getStart().isBefore(appointment.getEnd())) ||
                            // case 6 - end1 between (start2 and end2)
                            (appointmentToAdd.getEnd().isAfter(appointment.getStart()) && appointmentToAdd.getEnd().isBefore(appointment.getEnd())) ||
                            // case 7 - (start2 & end2) between (start1 & end1)
                            (appointment.getStart().isAfter(appointmentToAdd.getStart()) && appointment.getEnd().isBefore(appointmentToAdd.getEnd()))
                    ) return true;
                }
            }
        }
        return false;
    }
    /**
     * Generates alert regarding the users upcoming appointment.
     * @return the alert
     * */
    public static Alert getUserUpcomingAppointments(){
        for(Appointment appointment : allAppointments){
            if(currentUser.getId() == appointment.getUser().getId()){ // User appointment found
                if(appointment.getStart().toLocalDate().equals(LocalDate.now())){ // Appointment is today
                    LocalTime fifteenMinFromNow = LocalTime.now().plusMinutes(15);
                    if(appointment.getStart().toLocalTime().isAfter(LocalTime.now()) && (appointment.getStart().toLocalTime().isBefore(fifteenMinFromNow) || appointment.getStart().toLocalTime().equals(fifteenMinFromNow))){
                        Alert alert = generateAlert(Alert.AlertType.INFORMATION, "Appointment ID: " + appointment.getId() + "\nDate & Time: " + appointment.getStart().toLocalDate() + " " + appointment.getStart().toLocalTime());
                        alert.setHeaderText("You have an appointment soon.");
                        alert.setTitle("");
                        return alert;
                    }
                }
            }
        }
        Alert alert = generateAlert(Alert.AlertType.INFORMATION,"");
        alert.setHeaderText("You have no appointments within 15 minutes of now");
        alert.setTitle("");
        return alert;
    }
    /**
     *  Checks if a customer has and appointment.
     * @param customer the customer to check.
     * @return true if the customer has at least one appointment, otherwise false.
     * */
    public static boolean hasAppointments(Customer customer){
        for(Appointment appointment : allAppointments){
            if(appointment.getCustomer().getId() == customer.getId())
                return true;
        }
        return false;
    }
    /**
     *  Gets the customers name.
     * @param customerId the customers ID.
     * @return the customers name.
     * */
    public static String getCustomerName(int customerId) throws SQLException {
        initializeAllDivisions();
        initializeAllCustomers();
        for(Customer customer : allCustomers){
            if(customerId == customer.getId())
                return customer.getName();
        }
        return null;
    }
    /** List of all contacts.*/
    public  static ObservableList<Contact> allContacts = FXCollections.observableArrayList();
    /**
     *  Loads all contacts from Database into local memory.
     * */
    public static void initializeAllContacts() throws SQLException{
        ObservableList<Contact> contacts = FXCollections.observableArrayList();
        String selectStatement = "SELECT * FROM contacts";
        setPreparedStatement(DBConnection.getConn(), selectStatement);
        PreparedStatement ps = getPreparedStatement();
        ps.execute();
        ResultSet resultSet = ps.getResultSet();
        while (resultSet.next()){
            Contact contact = new Contact(
                    resultSet.getInt("Contact_ID"),
                    resultSet.getString("Contact_Name"),
                    resultSet.getString("Email")
            );
            contacts.add(contact);
        }
        allContacts.clear();
        allContacts.addAll(contacts);
    }
    /**
     * Gets a contact.
     * @param contactId the contacts ID.
     * @return the contact.
     * */
    public static Contact getContact(int contactId){
        for(Contact contact : allContacts){
            if(contact.getId() == contactId)
                return contact;
        }
        return null;
    }
    /**
     * Deletes and appointment from database.
     * @param appointment the appointment to delete.
     * @return true if the appointment was deleted, otherwise false.
     * */
    public static boolean deleteAppointment(Appointment appointment) throws SQLException{
        String deleteStatement = "DELETE FROM appointments WHERE Appointment_ID = " + appointment.getId();
        setPreparedStatement(DBConnection.getConn(), deleteStatement);
        PreparedStatement ps = getPreparedStatement();
        ps.execute();
        return ps.getUpdateCount() > 0;
    }
    /**
     * Updates an appointment in the database.
     * @param appointment the appointment to update.
     * @return true if the appointment was updated.
     * */
    public static boolean updateAppointment(Appointment appointment) throws SQLException{
        String insertStatement =
                "UPDATE appointments " +
                "SET Title = ?, " +
                "Description = ?, " +
                "Location = ?, " +
                "Type = ?, " +
                "Start = ?, " +
                "End = ?, " +
                "Create_Date = ?, " +
                "Created_By = ?, " +
                "Last_Update = ?, " +
                "Last_Updated_By = ?, " +
                "Customer_ID = ?, " +
                "User_ID = ?, " +
                "Contact_ID = ? " +
                "WHERE Appointment_ID = ?";
        setPreparedStatement(DBConnection.getConn(), insertStatement);
        PreparedStatement ps = getPreparedStatement();

        LocalDateTime   start = convertTimeZone(appointment.getStart(), zoneId_System, zoneId_UTC),
                end = convertTimeZone(appointment.getEnd(), zoneId_System, zoneId_UTC),
                now = ZonedDateTime.now().withZoneSameInstant(zoneId_UTC).toLocalDateTime(),
                createDate = convertTimeZone(appointment.getCreateDate(), zoneId_System, zoneId_UTC);

        ps.setString(1, appointment.getTitle());
        ps.setString(2, appointment.getDescription());
        ps.setString(3, appointment.getLocation());
        ps.setString(4, appointment.getType());
        ps.setTimestamp(5, Timestamp.valueOf(start)); // appointment start
        ps.setTimestamp(6, Timestamp.valueOf(end));   // appointment end
        ps.setTimestamp(7, Timestamp.valueOf(createDate));   // Create Date
        ps.setString(8, appointment.getCreatedBy());       // Created By
        ps.setTimestamp(9, Timestamp.valueOf(now));   // Last Update
        ps.setString(10, currentUser.getName());      // Last Updated By
        ps.setInt(11, appointment.customer.getId());
        ps.setInt(12, appointment.getUserId());       // user Id
        ps.setInt(13, appointment.getContact().getId());
        ps.setInt(14, appointment.getId());

        ps.execute();

        return ps.getUpdateCount() > 0;
    }
    /**
     * Adds new appointment to database.
     * @param appointment the appointment to add.
     * @return true if the appointment was added, otherwise false.
     * */
    public static boolean addAppointment(Appointment appointment) throws SQLException{

        // Check if appointment time is within business hours


            String insertStatement =
                    "INSERT INTO appointments(Title, " +
                            "Description, " +
                            "Location, Type, " +
                            "Start, End, " +
                            "Create_Date, " +
                            "Created_By, " +
                            "Last_Update, " +
                            "Last_Updated_By, " +
                            "Customer_ID, " +
                            "User_ID, " +
                            "Contact_ID) " +
                            "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";
            setPreparedStatement(DBConnection.getConn(), insertStatement);
            PreparedStatement ps = getPreparedStatement();
            // Convert times to UTC for database Storage
            LocalDateTime   start = convertTimeZone(appointment.getStart(), zoneId_System, zoneId_UTC),
                    end = convertTimeZone(appointment.getEnd(), zoneId_System, zoneId_UTC),
                    now = ZonedDateTime.now().withZoneSameInstant(zoneId_UTC).toLocalDateTime();

            ps.setString(1, appointment.getTitle());
            ps.setString(2, appointment.getDescription());
            ps.setString(3, appointment.getLocation());
            ps.setString(4, appointment.getType());
            ps.setTimestamp(5, Timestamp.valueOf(start));       // appointment start
            ps.setTimestamp(6, Timestamp.valueOf(end));         // appointment end
            ps.setTimestamp(7, Timestamp.valueOf(now));  // Create Date
            ps.setString(8, currentUser.getName());             // Created By
            ps.setTimestamp(9, Timestamp.valueOf(now));         // Last Update
            ps.setString(10, currentUser.getName());            // Last Updated By
            ps.setInt(11, appointment.customer.getId());
            ps.setInt(12, appointment.getUserId());             // user Id
            ps.setInt(13, appointment.getContact().getId());

            ps.execute();

            if(ps.getUpdateCount() > 0){
                allAppointments.add(appointment);
                return true;
            }else
                return false;

    }
    /**
     *  Gets a customer.
     * @param customerId the customers ID.
     * @return the customer.
     * */
    public static Customer getCustomer(int customerId){
        for(Customer customer : allCustomers){
            if(customerId == customer.getId())
                return customer;
        }
        return null;
    }
    /** List of all appointments in current week.*/
    public static ObservableList<Appointment> weekAppointments = FXCollections.observableArrayList();
    /**
     * Loads list of appointments in current week from list of all appointments.
     * */
    public static void initializeWeekAppointments(){
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();

        // Get date of monday of current week
        LocalDate firstDayOfWeek = null, lastDayOfWeek = null, currentDate = LocalDate.now();
        int currentDayOfWeek = LocalDate.now().getDayOfWeek().getValue();
        for(; currentDayOfWeek >= 1 ; currentDayOfWeek--){
            if(currentDayOfWeek == 1){
                firstDayOfWeek = currentDate;
            }
            currentDate = currentDate.minusDays(1);
        }
        lastDayOfWeek = firstDayOfWeek.plusDays(6);

        for(Appointment appointment : monthAppointments){
            if(     // checks if start date is in current month
                (appointment.getStart().isAfter(firstDayOfWeek.atStartOfDay()) && appointment.getStart().isBefore(lastDayOfWeek.atStartOfDay().plusHours(23).plusMinutes(59).plusSeconds(59)))
                ||  // checks if start date is in current month
                (appointment.getEnd().isAfter(firstDayOfWeek.atStartOfDay()) && appointment.getEnd().isBefore(lastDayOfWeek.atStartOfDay().plusHours(23).plusMinutes(59).plusSeconds(59)))
            ){
                appointments.add(appointment);
            }
        }
        weekAppointments.clear();
        weekAppointments.addAll(appointments);
    }
    /** List of appointments in current month.*/
    public static ObservableList<Appointment> monthAppointments = FXCollections.observableArrayList();
    /**
     *  Loads list of appointments in current month from list of all appointments.
     * */
    public static void initializeMonthAppointments(){   // TODO: ask: need to check if appointment is over a month long, also place it in filtered list?
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        for(Appointment appointment : allAppointments){
            if(     // checks if start date is in current month
                (LocalDate.now().getYear() == appointment.getStart().toLocalDate().getYear() &&
                LocalDate.now().getMonth() == appointment.getStart().toLocalDate().getMonth())
                ||  // checks if end date is in current month
                (LocalDate.now().getYear() == appointment.getEnd().toLocalDate().getYear() &&
                LocalDate.now().getMonth() == appointment.getEnd().toLocalDate().getMonth())
            ){
                appointments.add(appointment);
            }
        }
        monthAppointments.clear();
        monthAppointments.addAll(appointments);
    }
    /** List of all appointments.*/
    public static ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();
    /**
     * Loads list of all appointments from database into local memory.
     * */
    public static void initializeAllAppointments() throws SQLException{ //TODO: need to record createBy + date, lastUpdateBy + date?

        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        String selectStatement = "SELECT * FROM appointments";
        setPreparedStatement(DBConnection.getConn(), selectStatement);
        PreparedStatement ps = getPreparedStatement();
        ps.execute();
        ResultSet resultSet = ps.getResultSet();
        while (resultSet.next()){
            int appointmentId = resultSet.getInt("Appointment_ID");
            String title = resultSet.getString("Title");
            String description = resultSet.getString("Description");
            String location = resultSet.getString("Location");
            String type = resultSet.getString("Type");
            LocalDateTime start = convertTimeZone(resultSet.getTimestamp("Start").toLocalDateTime(), zoneId_UTC, zoneId_System);
            LocalDateTime end = convertTimeZone(resultSet.getTimestamp("End").toLocalDateTime(), zoneId_UTC, zoneId_System);
            LocalDateTime createDate = convertTimeZone(resultSet.getTimestamp("Create_Date").toLocalDateTime(), zoneId_UTC, zoneId_System);
            String createdBy = resultSet.getString("Created_By");
            Contact contact = getContact(resultSet.getInt("Contact_ID"));
            int customerId = resultSet.getInt("Customer_Id");
            User user = new User();
            user.setId(resultSet.getInt("User_ID"));
            user.setName(getUser(user.getId()).getName());
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm");

            Appointment appointment = new Appointment(
                    appointmentId,
                    title,
                    description,
                    location,
                    type,
                    start,
                    end,
                    createDate,
                    createdBy,
                    dtf.format(start),
                    dtf.format(end),
                    getCustomerName(customerId),
                    contact,
                    getCustomer(customerId),
                    user
            );

            appointments.add(appointment);

        }
        allAppointments.clear();
        allAppointments.addAll(appointments);
    }
    /** List of all customers.*/
    public static ObservableList<Customer> allCustomers = FXCollections.observableArrayList();
    /**
     * Loads list of all customers from database into local memory.
     * */
    public static void initializeAllCustomers() throws SQLException{
        ObservableList<Customer> customers = FXCollections.observableArrayList();

        String selectStatement = "SELECT * FROM customers";
        setPreparedStatement(DBConnection.getConn(), selectStatement);
        PreparedStatement ps = getPreparedStatement();
        ps.execute();
        ResultSet resultSet = ps.getResultSet();
        while (resultSet.next()){

            int customerDivisionId = resultSet.getInt("Division_ID");
            Division customerDivision = null;
            Country customerCountry = null;
            int customerCountryID = 0;
            for(Division division : allDivisions){
                if(division.getId() == customerDivisionId){
                    customerDivision = division;
                    customerCountryID = division.getCountryId();
                }
            }
            for(Country country : allCountries){
                if(country.getId() == customerCountryID)
                    customerCountry = country;
            }

            Customer customer = new Customer(
                    resultSet.getString("Customer_Name"),
                    resultSet.getString("Address"),
                    customerCountry,
                    customerDivision,
                    resultSet.getString("Postal_Code"),
                    resultSet.getString("Phone"),
                    resultSet.getString("Created_By"),
                    resultSet.getString("Last_Updated_By")
            );
            customer.setId(resultSet.getInt("Customer_ID"));
            customer.setCountryName(customerCountry.getName());
            customer.setDivisionName(customerDivision.getName());
            customers.add(customer);
        }
        allCustomers.clear();
        allCustomers.addAll(customers);
    }
    /** List of all countries.*/
    public static ObservableList<Country> allCountries = FXCollections.observableArrayList();
    /**
     * Loads list of all countries from database into local memory.
     * */
    public static void initializeAllCountries() throws SQLException {

        ObservableList<Country> countries = FXCollections.observableArrayList();

        String selectStatement = "SELECT * FROM countries";
        setPreparedStatement(DBConnection.getConn(), selectStatement);
        PreparedStatement ps = getPreparedStatement();
        ps.execute();
        ResultSet resultSet = ps.getResultSet();
        while (resultSet.next()){
            Country country =   new Country(resultSet.getInt("Country_ID"), resultSet.getString("Country"));
            countries.add(country);
        }
        allCountries.clear();
        allCountries.addAll(countries);
    }
    /** List of all first level divisions.*/
    public static ObservableList<Division> allDivisions = FXCollections.observableArrayList();
    /**
     *  Loads list of all first level divisions from database into local memory.
     * */
    public static void initializeAllDivisions() throws SQLException{
        ObservableList<Division> divisions = FXCollections.observableArrayList();

        String selectStatement = "SELECT * FROM first_level_divisions";
        setPreparedStatement(DBConnection.getConn(), selectStatement);
        PreparedStatement ps = getPreparedStatement();
        ps.execute();
        ResultSet resultSet = ps.getResultSet();
        while (resultSet.next()){
            Division division = new Division(
                    resultSet.getInt("Division_ID"),
                    resultSet.getInt("COUNTRY_ID"),
                    resultSet.getString("Division")
            );
            divisions.add(division);
        }
        allDivisions.clear();
        allDivisions.addAll(divisions);
    }
    /**
     * Gets all the divisions in a country.
     * @param country the country whose divisions are needed.
     * @return list of divisions in the country.
     * */
    public static ObservableList<Division> getDivisions(String country) throws SQLException{ //TODO: redo this using the observable list of division instead of SQL

        ObservableList<Division> divisions = FXCollections.observableArrayList();
        int country_ID = 0;
        for(Country country1 : allCountries) if (country.equals(country1.getName())) country_ID = country1.getId();
        String selectStatement = "SELECT * FROM first_level_divisions WHERE COUNTRY_ID = " + country_ID;
        setPreparedStatement(DBConnection.getConn(), selectStatement);
        PreparedStatement ps = getPreparedStatement();
        ps.execute();
        ResultSet resultSet = ps.getResultSet();
        while (resultSet.next()){
            Division division = new Division(resultSet.getInt("Division_ID"), resultSet.getInt("COUNTRY_ID"), resultSet.getString("Division"));
            divisions.add(division);
        }

        return divisions;
    }
    /**
     * Adds customer into the database.
     * @param customer the customer to add.
     * @return true if the customer was added, otherwise false.
     * */
    public static boolean addCustomer(Customer customer) throws SQLException{

        String insertStatement = "INSERT INTO customers(Customer_Name, Address, Postal_Code, Phone, Created_By, Last_Updated_By, Division_ID)VALUES(?,?,?,?,?,?,?)";
        setPreparedStatement(DBConnection.getConn(),insertStatement);
        PreparedStatement ps = getPreparedStatement();

        ps.setString(1, customer.getName());
        ps.setString(2, customer.getAddress());
        ps.setString(3, customer.getPostalCode());
        ps.setString(4, customer.getPhone());
        ps.setString(5, customer.getCreatedBy());
        ps.setString(6, customer.getLastUpdateBy());
        ps.setInt(7, customer.getDivision().getId());

        ps.execute();
        return ps.getUpdateCount() > 0;

    }
    /**
     * Updates customer in the database.
     * @param customer the customer to update.
     * @return true if the customer was updated, otherwise false.
     * */
    public static boolean updateCustomer(Customer customer) throws SQLException{

        String updateStatement = "UPDATE customers SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, Created_By = ?, Last_Updated_By = ?, Division_ID = ? WHERE Customer_ID = ?";
        setPreparedStatement(DBConnection.getConn(), updateStatement);
        PreparedStatement ps = getPreparedStatement();

        ps.setString(1, customer.getName());
        ps.setString(2, customer.getAddress());
        ps.setString(3, customer.getPostalCode());
        ps.setString(4, customer.getPhone());
        ps.setString(5, customer.getCreatedBy());
        ps.setString(6, customer.getLastUpdateBy());
        ps.setInt(7, customer.getDivision().getId());
        ps.setInt(8, customer.getId());

        ps.execute();
        return ps.getUpdateCount() > 0;
    }
    /**
     * Deletes customer from database.
     * @param customer the customer to delete.
     * @return true if the customer was deleted, otherwise false.
     * */
    public static boolean deleteCustomer(Customer customer) throws SQLException {
        String deleteStatement = "DELETE  FROM customers WHERE Customer_ID = " + customer.getId();
        setPreparedStatement(DBConnection.getConn(), deleteStatement);
        PreparedStatement ps = getPreparedStatement();
        ps.execute();
        return ps.getUpdateCount() > 0;
    }
    /**
     * Gets user.
     * @param userId the user id of the user to get.
     * @return the user.
     * */
    public static User getUser(int userId){
        for(User user : allUsers)
            if (userId == user.getId())
                return user;
        return null;
    }
    /** List of all users.*/
    public static ObservableList<User> allUsers = FXCollections.observableArrayList();
    /**
     * Loads all users from database into local memory.
     * */
    public static void initializeUsers() throws SQLException {
        ObservableList<User> users = FXCollections.observableArrayList();
        String selectStatement = "SELECT User_ID, User_Name FROM users";
        setPreparedStatement(DBConnection.getConn(), selectStatement);
        PreparedStatement ps = getPreparedStatement();
        ps.execute();
        ResultSet resultSet = ps.getResultSet();

        while (resultSet.next()){
            User user = new User();
            user.setId(resultSet.getInt("User_ID"));
            user.setName(resultSet.getString("User_Name"));
            users.add(user);
        }
        allUsers.clear();
        allUsers.addAll(users);
    }
    /**
     * Validates login credentials with database.
     * @param username the username entered.
     * @param password the password entered
     * @return true if access is granted, false if denied.
     * */
    public static boolean login(String username, String password) throws SQLException {

        String selectStatement = "SELECT User_ID, User_Name, Password FROM users";
        setPreparedStatement(DBConnection.getConn(), selectStatement);
        PreparedStatement ps = getPreparedStatement();
        ps.execute();
        ResultSet resultSet = ps.getResultSet();

        while(resultSet.next()){
            if(resultSet.getString("User_Name").equals(username) && resultSet.getString("Password").equals(password)) {
                currentUser.setId(resultSet.getInt("User_ID"));
                currentUser.setName(username);
                return true;
            }
            // pull out user ID for alert here or once main menu loads
        }
        return false;

    }
    /** List of Month names.*/
    public static ObservableList<String> monthNames = FXCollections.observableArrayList(
        "JANUARY",
        "FEBRUARY",
        "MARCH",
        "APRIL",
        "MAY",
        "JUNE",
        "JULY",
        "AUGUST",
        "SEPTEMBER",
        "OCTOBER",
        "NOVEMBER",
        "DECEMBER");

    /** List of pairs of types of appointments and total of that type in given month.*/
    public static ObservableList<TotalOfType> totalOfTypes = FXCollections.observableArrayList();
    /**
     * Loads list totalOfTypes.
     * @param monthName the month to generate list for.
     * */
    public static void initializeTypeTotalReport(String monthName){
        ObservableList<TotalOfType> temp = FXCollections.observableArrayList();
        ObservableList<String> types = FXCollections.observableArrayList();
        // get list of distinct type names
        for(Appointment appointment : allAppointments)
            if (!(types.contains(appointment.getType()))) types.add(appointment.getType());
        // list of distinct TotalOfType objects
        for(String type : types){
            TotalOfType distinctType = new TotalOfType(type);
            temp.add(distinctType);
        }
        // Get quantity of each type in given month
        for(Appointment appointment : allAppointments){
            String aptMonthName = appointment.getStart().toLocalDate().getMonth().toString();
            if(aptMonthName.equals(monthName)){
                for(TotalOfType tempType : temp){
                    if(tempType.getType().equals(appointment.getType()))
                        tempType.setCount(tempType.getCount() + 1);
                }
            }
        }
        totalOfTypes.clear();
        totalOfTypes.addAll(temp);
    }
    /** List of all appointments for a given contact.*/
    public static ObservableList<Appointment> contactAppointments = FXCollections.observableArrayList();
    /**
     * Loads list of appointments for a given contact.
     * @param contact the contact to load list for.
     * */
    public static void initializeContactAppointmentsReport(Contact contact){
        ObservableList<Appointment> contactAppointments_Temp = FXCollections.observableArrayList();
        for(Appointment appointment : allAppointments){
            if(appointment.getContact().getId() == contact.getId()){
                contactAppointments_Temp.add(appointment);
            }
        }
        contactAppointments.clear();
        contactAppointments.addAll(contactAppointments_Temp);
    }
    /** List of appointments for given customer.*/
    public static ObservableList<Appointment> customerAppointments = FXCollections.observableArrayList();
    /**
     * Loads list a customers Appointments.
     * @param customer the customer to load list for.
     * */
    public static void initializeCustomerAppointmentsReport(Customer customer){
        ObservableList<Appointment> customerAppointmentsTemp = FXCollections.observableArrayList();
        for(Appointment appointment : allAppointments){
            if(appointment.getCustomer().getId() == customer.getId())
                customerAppointmentsTemp.add(appointment);
        }
        customerAppointments.clear();
        customerAppointments.addAll(customerAppointmentsTemp);

    }

    public interface getBusinessHoursLocalTime { String out();}
    /**
     * LAMBDA #2
     * Converts business hours from EST to local time.
     * String containing business hours and current time zone.
     * */
    public static getBusinessHoursLocalTime convertBusHrs = () -> {
        String currentZone = ZonedDateTime.now().getZone().toString();
        LocalDateTime estStart = LocalDateTime.now().withHour(8).withMinute(0);
        LocalDateTime estEnd = LocalDateTime.now().withHour(22).withMinute(0);
        LocalDateTime currZoneStart = convertTimeZone(estStart, zoneId_EST,zoneId_System);
        LocalDateTime currZoneEnd = convertTimeZone(estEnd,zoneId_EST,zoneId_System);
        String start, end;
        if(currZoneStart.getHour() < 10) start = "0" + currZoneStart.getHour() + ":00";
        else start = currZoneStart.getHour() + ":00";
        if(currZoneEnd.getHour() < 10) end = "0" + currZoneEnd.getHour() + ":00";
        else end = currZoneEnd.getHour() + ":00";
        return start + "-" + end + " " + currentZone;
    };
}
