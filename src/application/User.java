/**
*   <h1>User<h1>
*   User class that allows us to generate users and access to their characteristics
*   @author Maria Garcia Fernandez
*   @version 1.0
*   @since 2017-04-1
*/

/**
 * Imported package and libraries
 */
package application;

public class User {

	/**
	 * Private class variables
	 */
	private String username;
	private String password;
	private String name;
	private String lastname;
	private String email;
	private int isAdmin;

	/**
	 * constructor method
	 */
	public User(String username, String password, String name, String lastname, String email, int isAdmin){
		this.username = username;
		this.password = password;
		this.name = name;
		this.lastname = lastname;
		this.email = email;
		this.isAdmin = isAdmin;
	}

	/**
	 * Username getter method
	 * @return username for the app
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Username setter method
	 * @param username the user name for the app
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Password getter method
	 * @return password the user's password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Password setter method
	 * @param password the user's password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Name getter method
	 * @return name of the user
	 */
	public String getName() {
		return name;
	}

	/**
	 * Name setter method
	 * @param name the user's name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Lastname getter method
	 * @return lastname of the user
	 */
	public String getLastname() {
		return lastname;
	}

	/**
	 * Lastname setter method
	 * @param lastname of the user
	 */
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	/**
	 * Email getter method
	 * @return email user's email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Email setter method
	 * @param email the user's email
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * isAdmin getter method that indicates if a user is admin or not
	 * @return isAdmin int value 1 if the user is admin 0 if it is not
	 */
	public int getIsAdmin() {
		return isAdmin;
	}

	/**
	 * isAdmin setter method, that sets an user as admin or not
	 * @param isAdmin int value 1 if the user is admin 0 if it is not
	 */
	public void setIsAdmin(int isAdmin) {
		this.isAdmin = isAdmin;
	}
}
