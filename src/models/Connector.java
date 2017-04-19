/** 
*   <h1>Connector<h1>
*   Connector class that set up the connection with the database
*   @author Maria Garcia Fernandez
*   @version 1.0
*   @since 2017-04-01
*/
/**
 * Import package and libraries
 */
package models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connector {

	/**
	 * Class private variables
	 */
	private String url = "jdbc:mysql://www.papademas.net:3306/510labs?autoReconnect=true&useSSL=false";
	private String username = "db510";
	private String password = "510";
	private Connection c;

		/**
		 * This method sets up the connection with the database, it throws and exception if
		 * it does not find the drivers or if there is an error when accessing to the
		 * database
		 * @return Connection c if the connection has been established or null if it has not.
		 */
		public Connection getConnection(){
			try {
				// Load the MySQL driver, each DB has its own driver
				Class.forName("com.mysql.jdbc.Driver");
				// Setup the connection with the DB
				c = DriverManager.getConnection(url + "&user="+username+"&password="+password);
				c.createStatement();
				return c;
			}
			catch (SQLException e){
				System.out.println(e.getMessage());
			}
			catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			return null;
		}

}
