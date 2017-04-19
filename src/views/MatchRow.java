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
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class MatchRow {

	/**
	 * private class variables
	 */
	private IntegerProperty id;
	private StringProperty localTeam;
    private StringProperty visitorTeam;
    private StringProperty localGoals;
    private StringProperty visitorGoals;
    private StringProperty corners;
    private StringProperty fouls;
    private StringProperty date;
    private StringProperty referee;
    private BooleanProperty registered;

    /**
     * Constructor method
     * @param id
     * @param localTeam
     * @param visitorTeam
     * @param localGoals
     * @param visitorGoals
     * @param corners
     * @param fouls
     * @param date
     * @param referee
     * @param registered
     */
    public MatchRow(int id, String localTeam, String visitorTeam, String localGoals, String visitorGoals, String corners, String fouls, String date, String referee, boolean registered) {
       this.id = new SimpleIntegerProperty (id);
       this.localTeam = new SimpleStringProperty (localTeam);
       this.visitorTeam = new SimpleStringProperty (visitorTeam);
       this.localGoals = new SimpleStringProperty (localGoals);
       this.visitorGoals = new SimpleStringProperty (visitorGoals);
       this.corners = new SimpleStringProperty (corners);
       this.fouls = new SimpleStringProperty (fouls);
       this.date = new SimpleStringProperty (date);
       this.referee = new SimpleStringProperty (referee);
       this.registered = new SimpleBooleanProperty (registered);
    }

    /**
     * Id getter
     * @return id
     */
    public int getId() {
        return id.get();
    }

    /**
     * Id setter
     * @param id
     */
    public void setId(int id) {
        this.id.set(id);
    }

    /**
     * Property defining method
     * @return id
     */
    public IntegerProperty idProperty() {
        return id;
    }

    /**
     * Local team getter
     * @return localTeam
     */
    public String getLocalTeam() {
        return localTeam.get();
    }

    /**
     * Local team setter
     * @param localTeam
     */
    public void setLocalTeam(String localTeam) {
        this.localTeam.set(localTeam);
    }

    /**
     * Property defining method
     * @return localTeam
     */
    public StringProperty localTeamProperty() {
        return localTeam;
    }

    /**
     * Visitor team getter
     * @return visitorTeam
     */
    public String getVisitorTeam() {
        return visitorTeam.get();
    }

    /**
     * Visitor team setter
     * @param visitorTeam
     */
    public void setVisitorTeam(String visitorTeam) {
    	this.visitorTeam.set(visitorTeam);
    }

    /**
     * Property defining method
     * @return visitorTeam
     */
    public StringProperty visitorTeamProperty() {
        return visitorTeam;
    }

    /**
     * Local goals getter
     * @return localGoals
     */
    public String getLocalGoals() {
        return localGoals.get();
    }

    /**
     * Local goals setter
     * @param localGoals
     */
    public void setLocalGoals(String localGoals) {
        this.localGoals.set(localGoals);
    }

    /**
     * Property defining method
     * @return localGoals
     */
    public StringProperty localGoalsProperty() {
        return localGoals;
    }

    /**
     * Visitor goals getter
     * @return visitorGoals
     */
    public String getVisitorGoals() {
        return visitorGoals.get();
    }

    /**
     * Visitor goals setter
     * @param visitorGoals
     */
    public void setVisitorGoals(String visitorGoals) {
        this.visitorGoals.set(visitorGoals);
    }

    /**
     * Property defining method
     * @return visitorGoals
     */
    public StringProperty visitorGoalsProperty() {
        return visitorGoals;
    }

    /**
     * Corners getter
     * @return corners
     */
    public String getCorners() {
        return corners.get();
    }

    /**
     * Corners setter
     * @param corners
     */
    public void setCorners(String corners) {
        this.corners.set(corners);
    }

    /**
     * Property defining method
     * @return corners
     */
    public StringProperty cornersProperty() {
        return corners;
    }

    /**
     * Fouls getter
     * @return fouls
     */
    public String getFouls() {
        return fouls.get();
    }

    /**
     * Fouls setter
     * @param fouls
     */
    public void setFouls(String fouls) {
        this.fouls.set(fouls);
    }

    /**
     * Property defining method
     * @return fouls
     */
    public StringProperty foulsProperty() {
        return fouls;
    }

    /**
     * Date getter
     * @return date
     */
    public String getDate() {
        return date.get();
    }

    /**
     * Date setter
     * @param date
     */
    public void setDate(String date) {
        this.date.set(date);
    }

    /**
     * Property defining method
     * @return date
     */
    public StringProperty dateProperty() {
        return date;
    }

    /**
     * Referee getter
     * @return referee
     */
    public String getReferee() {
        return referee.get();
    }

    /**
     * Referee setter
     * @param referee
     */
    public void setReferee(String referee) {
        this.referee.set(referee);
    }

    /**
     * Property defining method
     * @return referee
     */
    public StringProperty refereeProperty() {
        return referee;
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
