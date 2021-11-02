package model;
/**
 * The class Division.java
 * Holds first class division data.
 * */
public class Division {

    private int id,
                countryId;
    private String  name,
                    createDate,
                    createdBy,
                    lastUpdate,
                    lastUpdatedBy;

    public Division(int id, int countryId, String name) {
        this.id = id;
        this.countryId = countryId;
        this.name = name;
    }
    /**
     * @return the division ID
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
     * @return the id of the country the division belongs to.
     * */
    public int getCountryId() {
        return countryId;
    }
    /** Current not used. */
    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }
    /**
     * @return the name of the division.
     * */
    public String getName() {
        return name;
    }
    /**
     * @param name the name of the division.
     * */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * @return the date the division was created.
     * */
    public String getCreateDate() {
        return createDate;
    }
    /**
     * @param createDate the creation date to set.
     * */
    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }
    /**
     * @return who created the division
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
     * @return the date and time the division was last updated.
     * */
    public String getLastUpdate() {
        return lastUpdate;
    }
    /** Current not used. */
    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
    /** Current not used. */
    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }
    /** Current not used. */
    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }
    /**
     * @return the name of the division.
     * */
    @Override
    public String toString() {
        return name;
    }
}
