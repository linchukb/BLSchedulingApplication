package model;
/**
 *  The class User.java
 * */
public class User {

    int id;
    String  name;
    /** Default Constructor */
    public User(){}
    /** Constructor*/
    public User(int id, String name) {
        this.id = id;
        this.name = name;
    }
    /**
     * @return this users ID
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
     * @param name the name to set.
     * */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * @return the name of this user.
     * */
    public String getName() {
        return name;
    }
    /**
     * @return the users name.
     * */
    @Override
    public String toString() { return name; }
}
