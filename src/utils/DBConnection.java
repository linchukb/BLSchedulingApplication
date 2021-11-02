package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/**
 *  The class DBConnection.java.
 * */
public class DBConnection {

    // JDBC URL parts
    private static final String protocol = "jdbc";
    private static final String vendorName = ":mysql:";
    private static final String serverName = "//wgudb.ucertify.com/WJ08rV4";

    // JDBC URL
    private static final String jdbcURL = protocol + vendorName + serverName;

    // Driver and Connection Interface reference
    private static final String MYSQLJDBCDriver = "com.mysql.cj.jdbc.Driver"; // TODO: ask: IDE instructed to modify path to include .cj - is this correct?

    private static final String username = "U08rV4";
    private static final String password = "53689376004";

    private static Connection conn = null;
    /**
     * Starts the connection.
     * @return the connection.
     * */
    public static Connection startConnection(){
        try {

            Class.forName(MYSQLJDBCDriver);
            conn = DriverManager.getConnection(jdbcURL, username, password);

        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }
    /**
     * Closes the connection
     * */
    public static void closeConnection(){

        try {
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    /**
     * @return the connection
     * */
    public static Connection getConn(){
        return conn;
    }

}
