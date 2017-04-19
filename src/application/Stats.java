/**
*   <h1>Stats<h1>
*   Stats class that allows us to generate stats and access to their characteristics
*   @author Maria Garcia Fernandez
*   @version 1.0
*   @since 2017-04-1
*/

/**
 * Imported package and libraries
 */
package application;

public class Stats {

	/**
	 * Private class variables
	 */
	private int goalsHome;
	private int goalsAway;
	private int numberOfCorners;
	private int numberOfFouls;

	/**
	 * Constructor method
	 */
	public Stats(int goalsHome, int goalsAway, int numberOfCorners, int numberOfFouls){
		this.goalsHome = goalsHome;
		this.goalsAway = goalsAway;
		this.numberOfCorners = numberOfCorners;
		this.numberOfFouls = numberOfFouls;
	}

	/**
	 * Goals home getter method
	 * @return goalshome the number of local goals in the match
	 */
	public int getGoalsHome() {
		return goalsHome;
	}

	/**
	 * Goals home setter method
	 * @param goalshome the number of local goals in the match
	 */
	public void setGoalsHome(int goalsHome) {
		this.goalsHome = goalsHome;
	}

	/**
	 * Goals away getter method
	 * @return goalsaway the number of visitant goals in the match
	 */
	public int getGoalsAway() {
		return goalsAway;
	}

	/**
	 * Goals away setter method
	 * @param goalsaway the number of visitant goals in the match
	 */
	public void setGoalsAway(int goalsAway) {
		this.goalsAway = goalsAway;
	}

	/**
	 * Number of corners getter method
	 * @return numberOfCorners the number of corners in the match
	 */
	public int getNumberOfCorners() {
		return numberOfCorners;
	}

	/**
	 * Number of corners setter method
	 * @param numberOfCorners the number of corners in the match
	 */
	public void setNumberOfCorners(int numberOfCorners) {
		this.numberOfCorners = numberOfCorners;
	}

	/**
	 * Number of fouls getter method
	 * @return numberOfFouls the number of fouls in the match
	 */
	public int getNumberOfFouls() {
		return numberOfFouls;
	}

	/**
	 * Number of fouls setter method
	 * @param numberOfFouls the number of fouls in the match
	 */
	public void setNumberOfFouls(int numberOfFouls) {
		this.numberOfFouls = numberOfFouls;
	}
}
