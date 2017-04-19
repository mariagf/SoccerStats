/**
*   <h1>Player<h1>
*   Player class that allows us to generate players and access to their characteristics
*   @author Maria Garcia Fernandez
*   @version 1.0
*   @since 2017-04-1
*/

/**
 * Imported package and libraries
 */
package application;

import java.util.Date;

public class Player {
	/**
	 * Private class variables
	 */
	private String name;
	private String lastname;
	private Date dateOfBirth;
	private int height;

	/**
	 * Constructor method
	 */
	public Player(String name, String lastname, Date dateOfBirth, int height){
		this.name = name;
		this.lastname = lastname;
		this.dateOfBirth = dateOfBirth;
		this.height = height;
	}

	/**
	 * Player's name getter method
	 * @return name of the player
	 */
	public String getName() {
		return name;
	}

	/**
	 * Player's name setter method
	 * @param name the player's name
	 */
	public void setUsername(String name) {
		this.name = name;
	}

	/**
	 * Player's last name getter method
	 * @return lastname the player's last name
	 */
	public String getLastname() {
		return lastname;
	}

	/**
	 * Player's last name setter method
	 * @param lastname the player's lastname
	 */
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	/**
	 * Player's date of birth getter method
	 * @return dateOfBirth the player's date of birth
	 */
	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	/**
	 * Player's date of birth setter method
	 * @param dateOfBirth the player's date of birth
	 */
	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	/**
	 * Player's height getter method
	 * @return height of the player
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Player's height setter method
	 * @param height the player's height
	 */
	public void setHeight(int height) {
		this.height = height;
	}


}
