package db;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DB {

	private static Connection conn = null;
	// object of connection to database of jdbc
	
	
	// method to connect to the database - OPEN CONNECTION
	public static Connection getConnection() {		
		if (conn == null) {
		// if coon is null, means there's no connection to the database
			
			try {
				Properties props = loadProperties(); // taking the properties of the connection into the db.properties file. There's a method to do this job.
				String url = props.getProperty("dburl"); // dburl is one of the properties inside of the file. We can check this out on the file itself
				conn = DriverManager.getConnection(url, props); // making the connection to the database.
				// to connect we use the "Connection". We just instantiated the Connection using the DriverManager
			}
			
			catch (SQLException e) {
				throw new DbException(e.getMessage());
				// conn = DriverManager.getConnection(url, props); this line can generate a exception
			}
		}
		
		return conn;
	}
	
	
	// method to close the connection from the database - CLOSE CONNECTION
	public static void closeConnection() {
		
		if (conn != null) {
			try {
				conn.close();
			}
			catch (SQLException e) {
				throw new DbException(e.getMessage());
			}
		}
	}
	
	
	// This method will load all the information stored in the db.properties file (created recently)
	private static Properties loadProperties() {		
		try (FileInputStream fs = new FileInputStream("db.properties")){
			
			Properties props = new Properties();
			props.load(fs);
			return props;			
			
		}
		catch (IOException e) {
			throw new DbException(e.getMessage());
			 // AIOExceltion will handle (FileNotFoundException for the FileInputStream() object) and (IOException for the load() method). 
		}
	}
	
	
	// this static method closes the Statement object in the main program
	public static void closeStatement(Statement st) {
		
		if (st != null) {
			try {
				st.close();
			} catch (SQLException e) {
				throw new DbException(e.getMessage());
			}
		}
	}
	
	
	// this static method closes the ResultSet object in the main program
	public static void closeResultSet(ResultSet rs) {
	
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				throw new DbException(e.getMessage());
			}
		}
	}
	
	
}
