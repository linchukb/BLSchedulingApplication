package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
/**
 * The class Country.java.
 * */
public class Country {


    private int id;
    private String  name,
            createDate,
            createdBy,
            lastUpdate,
            lastUpdatedBy;
    /** List of first level divisions in the country.*/
    private ObservableList<Division> divisions = FXCollections.observableArrayList();
    /** Constructor*/
    public Country(int id, String name) {
        this.id = id;
        this.name = name;
    }
    /**
     * @return the country id
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
     * @return the name of the country.
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
     * @return the date the country was created.
     * */
    public String getCreateDate() {
        return createDate;
    }
    /**
     * @param createDate the date to set.
     * */
    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }
    /**
     * @return who created the country.
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
     * @return last date and time the country was updated.
     * */
    public String getLastUpdate() {
        return lastUpdate;
    }
    /** Currently not used.*/
    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
    /** Currently not used.*/
    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }
    /** Currently not used.*/
    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }
    /**
     * @return list of first level division in the country;
     * */
    public ObservableList<Division> getDivisions() {
        return divisions;
    }
    /**
     * @param divisions the division to set.
     * */
    public void setDivisions(ObservableList<Division> divisions) {
        this.divisions = divisions;
    }
    /**
     * @return the name of the country.
     * */
    @Override
    public String toString() {
        return name;
    }
}
