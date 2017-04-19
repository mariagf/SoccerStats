/**
*   <h1>Match<h1>
*   Match class that allows us to generate matches and access to their characteristics
*   @author Maria Garcia Fernandez
*   @version 1.0
*   @since 2017-04-1
*/

/**
 * Imported package and libraries
 */
package application;

import java.util.Date;

public class Match {

	/**
	 * Private class variables
	 */
	private Team teamHome;
	private Team teamAway;
	private Stats stats;
	private Date date;
	private String referee;

	/**
	 * Constructor method
	 */
	public Match(Team teamHome, Team teamAway, Stats stats, Date date, String referee){
		this.teamHome = teamHome;
		this.teamAway = teamAway;
		this.stats = stats;
		this.date = date;
		this.referee = referee;
	}

	/**
	 * Local or home team getter method
	 * @return teamHome the local team in the match
	 */
	public Team getTeamHome() {
		return teamHome;
	}

	/**
	 * Local or home team setter method
	 * @param teamHome the home or local team name in the match
	 */
	public void setTeamHome(Team teamHome) {
		this.teamHome = teamHome;
	}

	/**
	 * Visitor team getter method
	 * @return teamAway the visitor team in the match
	 */
	public Team getTeamAway() {
		return teamAway;
	}

	/**
	 * Visitor team setter method
	 * @param teamAway the visitor team name in the match
	 */
	public void setTeamAway(Team teamAway) {
		this.teamAway = teamAway;
	}

	/**
	 * Stats getter method
	 * @return stats the statistics of the match
	 */
	public Stats getStats() {
		return stats;
	}

	/**
	 * Stats setter method
	 * @param stats of the match
	 */
	public void setStats(Stats stats) {
		this.stats = stats;
	}

	/**
	 * Date getter method
	 * @return date of the match
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * Date setter method
	 * @param date of the match
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * Referee getter method
	 * @return referee name in the match
	 */
	public String getReferee() {
		return referee;
	}

	/**
	 * Referee setter method
	 * @param referee name in the match
	 */
	public void setReferee(String referee) {
		this.referee = referee;
	}
}
