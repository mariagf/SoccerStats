/**
*   <h1>Team<h1>
*   Team class that allows us to generate team and access to their characteristics
*   @author Maria Garcia Fernandez
*   @version 1.0
*   @since 2017-04-1
*/

/**
 * Imported package and libraries
 */
package application;

import java.util.Date;
import java.util.List;

public class Team {

	/**
	 * Private class variables
	 */
	private List<Player> players;
	private String name;
	private String coach;
	private String city;
	private Date dateFoundation;

	/**
	 * Constructor method
	 */
	public Team(List<Player> players, String name, String coach, String city, Date dateFoundation){
		this.players = players;
		this.name = name;
		this.coach = coach;
		this.city = city;
		this.dateFoundation = dateFoundation;
	}

	/**
	 * Team players list getter method
	 * @return players of the team
	 */
	public List<Player> getTeamPlayers() {
		return players;
	}

	/**
	 * Team players setter method
	 * @param players of the team
	 */
	public void setTeamPlayers(List<Player> players) {
		this.players = players;
	}

	/**
	 * Team name getter method
	 * @return name of the team
	 */
	public String getName() {
		return name;
	}

	/**
	 * Team name setter method
	 * @param name of the team
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Team coach getter method
	 * @return coach of the team
	 */
	public String getCoach() {
		return coach;
	}

	/**
	 * Team coach setter method
	 * @param coach of the team
	 */
	public void setCoach(String coach) {
		this.coach = coach;
	}

	/**
	 * Team city getter method
	 * @return city of the team
	 */
	public String getCity() {
		return city;
	}

	/**
	 * Team city setter method
	 * @param city of the team
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * Team date of foundation getter method
	 * @return dateFoundation of the team
	 */
	public Date getDateFoundation() {
		return dateFoundation;
	}

	/**
	 * Team date of foundation setter method
	 * @param dateFundation of the team
	 */
	public void setDateFoundation(Date dateFoundation) {
		this.dateFoundation = dateFoundation;
	}
}
