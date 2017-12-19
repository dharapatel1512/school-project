package server;

import java.sql.*;

/**
 * @author Dharaben Patel, Nidhi Patel
 */
public class DbConnection {
    // JDBC driver
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";

	/*  Database credentials for Local Machine
	static final String DB_URL = "jdbc:mysql://localhost";
	static final String USER = "root";
	static final String PASS = "dharu1512"; */

    /*Database credentials for AFS */
    static final String DB_URL ="jdbc:mysql://sql2.njit.edu/np397";
    static final String USER = "np397";
    static final String PASS = "Ae61AQfCj";

    public Connection connection() {
        Connection conn = null;
        try {
            //Register JDBC driver
            Class.forName("com.mysql.jdbc.Driver");
            //Open a connection
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("Error while connecting to database");
        }

        return conn;
    }
}