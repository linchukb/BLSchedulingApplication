package model;
/**
 * The class TotalOfType.
 * For use in the report: Total appointments by month and type.
 * */
public class TotalOfType {
    private String type;
    private int count = 0;
    /**
     * Constructor.
     * @param type the type of appointment.
     * */
    public TotalOfType(String type) {
        this.type = type;
    }
    /**
     * @return the type to set.
     * */
    public String getType() {
        return type;
    }
    /**
     * @param type the type of appointment.
     * */
    public void setType(String type) {
        this.type = type;
    }
    /**
     * @return the number of this type of appointments.
     * */
    public int getCount() {
        return count;
    }
    /**
     * @param count the count to set.
     * */
    public void setCount(int count) {
        this.count = count;
    }
}
