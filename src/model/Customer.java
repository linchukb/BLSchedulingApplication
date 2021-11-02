package model;
/**
 * The class Customer.java
 * */
public class Customer {

    private int id;
    private String  name,
                    address,
                    postalCode,
                    phone,
                    createdBy,
                    lastUpdateBy;
    private Country country;
    private Division division;
    private String countryName = null;
    private String divisionName = null;

    public Customer(String name, String address, Country country, Division division, String postalCode, String phone, String createdBy, String lastUpdateBy) {
        this.name = name;
        this.address = address;
        this.country = country;
        this.division = division;
        this.postalCode = postalCode;
        this.phone = phone;
        this.createdBy = createdBy;
        this.lastUpdateBy = lastUpdateBy;
    }
    /**
     * @return the name of the division.
     * */
    public String getDivisionName() {
        return divisionName;
    }
    /**
     * @param divisionName name of division to set.
     * */
    public void setDivisionName(String divisionName) {
        this.divisionName = divisionName;
    }
    /** Currently not used.*/
    public String getCountryName(){return countryName;}
    /**
     * @param countryName name of country to set.
     * */
    public void setCountryName(String countryName){this.countryName = countryName;}
    /**
     * @return the customer id.
     * */
    public int getId() {
        return id;
    }
    /**
     * @param id the customer id to set.
     * */
    public void setId(int id) { this.id = id;}
    /**
     * @return name of the customer.
     * */
    public String getName() {
        return name;
    }
    /**
     * @param name the name of the customer to set.
     * */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * @return address of the customer.
     * */
    public String getAddress() {
        return address;
    }
    /**
     * @param address  the address to set.
     * */
    public void setAddress(String address) {
        this.address = address;
    }
    /**
     * @return country the customer lives in.
     * */
    public Country getCountry() {
        return country;
    }
    /**
     * @param country the country to set.
     * */
    public void setCountry(Country country) {
        this.country = country;
    }
    /**
     * @return the division the customer lives in.
     * */
    public Division getDivision() {
        return division;
    }
    /**
     * @param division the division to set.
     * */
    public void setDivision(Division division) {
        this.division = division;
    }
    /**
     * @return customers postal code.
     * */
    public String getPostalCode() {
        return postalCode;
    }
    /** Currently not used.*/
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
    /**
     * @return customers phone number.
     * */
    public String getPhone() {
        return phone;
    }
    /**
     * @param phone the phone number to set.
     * */
    public void setPhone(String phone) {
        this.phone = phone;
    }
    /**
     * @return name of who created the customer record.
     * */
    public String getCreatedBy() {
        return createdBy;
    }
    /**
     * @param createdBy the creators name to set.
     * */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
    /**
     * @return name of who last updated the record.
     * */
    public String getLastUpdateBy() {
        return lastUpdateBy;
    }
    /** Currently not used.*/
    public void setLastUpdateBy(String lastUpdateBy) {
        this.lastUpdateBy = lastUpdateBy;
    }
    /**
     * @return name of the customer.
     * */
    @Override
    public String toString() { return name; }
}
