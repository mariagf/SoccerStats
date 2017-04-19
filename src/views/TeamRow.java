/**
*   <h1>Match Row<h1>
*   MatchRow class that allows us to get all the matches information as rows in the TableView
*   @author Maria Garcia Fernandez
*   @version 1.0
*   @since 2017-04-15
*/

/**
 * Imported package and libraries
 */
package views;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class TeamRow {

	/**
	 * private class variables
	 */
	private StringProperty team;
    private StringProperty coach;
    private StringProperty city;
    private StringProperty foundationDate;
    private StringProperty players;
    private StringProperty matches;
    private BooleanProperty registered;

    /**
     * Constructor method
     * @param team
     * @param coach
     * @param city
     * @param foundationDate
     * @param players
     * @param matches
     * @param registered
     */
    public TeamRow(String team, String coach, String city, String foundationDate, String players, String matches, boolean registered) {
       this.team = new SimpleStringProperty (team);
       this.coach = new SimpleStringProperty (coach);
       this.city = new SimpleStringProperty (city);
       this.foundationDate = new SimpleStringProperty (foundationDate);
       this.players = new SimpleStringProperty (players);
       this.matches = new SimpleStringProperty (matches);
       this.registered = new SimpleBooleanProperty(registered);
    }

    /**
     * Team getter
     * @return team
     */
    public String getTeam() {
        return team.get();
    }

    /**
     * Team setter
     * @param team
     */
    public void setTeam(String team) {
        this.team.set(team);
    }

    /**
     * Property defining method
     * @return team
     */
    public StringProperty teamProperty() {
        return team;
    }

    /**
     * Coach getter
     * @return coach
     */
    public String getCoach() {
        return coach.get();
    }

    /**
     * Coach setter
     * @param coach
     */
    public void setLastname(String coach) {
        this.coach.set(coach);
    }

    /**
     * Property defining method
     * @return coach
     */
    public StringProperty coachProperty() {
        return coach;
    }

    /**
     * City getter
     * @return city
     */
    public String getCity() {
        return city.get();
    }

    /**
     * City setter
     * @param city
     */
    public void setCity(String city) {
        this.city.set(city);
    }

    /**
     * Property defining method
     * @return city
     */
    public StringProperty cityProperty() {
        return city;
    }

    /**
     * Foundation date getter
     * @return foundationDate
     */
    public String getFoundationDate() {
        return foundationDate.get();
    }

    /**
     * Foundation date setter
     * @param foundationDate
     */
    public void setFoundationDate(String foundationDate) {
        this.foundationDate.set(foundationDate);
    }

    /**
     * Property defining method
     * @return foundationDate
     */
    public StringProperty foundationDateProperty() {
        return foundationDate;
    }

    /**
     * Players getter
     * @return players
     */
    public String getPlayers() {
        return players.get();
    }

    /**
     * Players setter
     * @param players
     */
    public void setPlayers(String players) {
        this.players.set(players);
    }

    /**
     * Property defining method
     * @return players
     */
    public StringProperty playersProperty() {
    	return players;
    }

    /**
     * Matches getter
     * @return matches
     */
    public String getMatches() {
        return matches.get();
    }

    /**
     * Matches setter
     * @param matches
     */
    public void setMatches(String matches) {
        this.matches.set(matches);
    }

    /**
     * Property defining method
     * @return matches
     */
    public StringProperty matchesProperty() {
        return matches;
    }

    /**
     * Registered getter
     * @return registered
     */
    public boolean isRegistered() {
    	return this.registered.get();
    }

    /**
     * Registered setter
     * @param value
     */
    public void setRegistered(boolean value) {
    	this.registered.set(value);
    }

    /**
     * Property defining method
     * @return registered
     */
    public BooleanProperty registeredProperty() {
    	return registered;
    }

}
