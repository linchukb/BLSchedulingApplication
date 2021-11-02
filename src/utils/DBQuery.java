package utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
/**
 * The class DBQuery.java
 * */
public class DBQuery {

    private static PreparedStatement preparedStatement; // Statement reference

    /**
     *  Creates preparedStatement object.
     * @param conn the database to connect to.
     * @param sqlStatement the SQL statement to prepare.
     * */
    public static void setPreparedStatement(Connection conn, String sqlStatement) throws SQLException {
        preparedStatement = conn.prepareStatement(sqlStatement);
    }
    /**
     * @return the prepared statement.
     * */
    public static PreparedStatement getPreparedStatement(){
        return preparedStatement;
    }

}
