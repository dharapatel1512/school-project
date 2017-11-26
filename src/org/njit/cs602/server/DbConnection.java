package org.njit.cs602.server;

import java.sql.*;

public class DbConnection {

	// JDBC driver name and database URL

	/* Connect to AFS Server*/
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver"; 

	/*  Database credentials for Local Machine*/
    static final String DB_URL = "jdbc:mysql://localhost";
    static final String USER = "root";
  	static final String PASS = "dharu1512";

	/*Database credentials for AFS*/
//	static final String DB_URL ="jdbc:mysql://sql1.njit.edu/dp582:3306";
//	static final String USER = "dp582";
//	static final String PASS = "7kIDcCk6";


	public Connection connection() {
		Connection conn = null;

		try{
			//STEP 2: Register JDBC driver
			Class.forName("com.mysql.jdbc.Driver");

			//STEP 3: Open a connection
			System.out.println("Connecting to database...");
			conn = DriverManager.getConnection(DB_URL,USER,PASS);

		}catch(SQLException se){
			//Handle errors for JDBC
			se.printStackTrace();
		}catch(Exception e){
			//Handle errors for Class.forName
			e.printStackTrace();
		}
		return conn;
	}//end
}
