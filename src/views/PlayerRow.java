/**
*   <h1>Player Row<h1>
*   PlayerRow class that allows us to get all the players information as rows in the TableView
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

public class PlayerRow {

	/**
	 * private class variables
	 */
	private StringProperty name;
    private StringProperty lastname;
    private StringProperty dateOfBirth;
    private StringProperty height;
    private StringProperty team;
    private BooleanProperty registered;

    /**
     * Constructor method
     * @param name
     * @param lastname
     * @param dateOfBirth
     * @param height
     * @param team
     * @param registered
     */
    public PlayerRow(String name, String lastname, String dateOfBirth, String height, String team, boolean registered) {
       this.name = new SimpleStringProperty (name);
       this.lastname = new SimpleStringProperty (lastname);
       this.dateOfBirth = new SimpleStringProperty (dateOfBirth);
       this.height = new SimpleStringProperty (height);
       this.team = new SimpleStringProperty (team);
       this.registered = new SimpleBooleanProperty(registered);
    }

    /**
     * Name getter
     * @return name
     */
    public String getName() {
        return name.get();
    }

    /**
     * Name setter
     * @param name
     */
    public void setName(String name) {
        this.name.set(name);
    }

    /**
     * Property defining method
     * @return name
     */
    public StringProperty nameProperty() {
        return name;
    }

    /**
     * Lastname getter
     * @return lastname
     */
    public String getLastname() {
        return lastname.get();
    }

    /**
     * Lastname setter
     * @param lastname
     */
    public void setLastname(String lastname) {
        this.lastname.set(lastname);
    }

    /**
     * Property defining method
     * @return lastname
     */
    public StringProperty lastnameProperty() {
        return lastname;
    }

    /**
     * Date of birth getter
     * @return dateOfBirth
     */
    public String getDateOfBirth() {
        return dateOfBirth.get();
    }

    /**
     * Date of birth setter
     * @param dateOfBirth
     */
    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth.set(dateOfBirth);
    }

    /**
     * Property defining method
     * @return dateOfBirth
     */
    public StringProperty dateOfBirthProperty() {
        return dateOfBirth;
    }

    /**
     * Height getter
     * @return height
     */
    public String getHeight() {
        return height.get();
    }

    /**
     * Height setter
     * @param height
     */
    public void setHeight(String height) {
        this.height.set(height);
    }

    /**
     * Property defining method
     * @return height
     */
    public StringProperty heightProperty() {
        return height;
    }

    /**
     * Team name getter
     * @return
     */
    public String getTeam() {
        return team.get();
    }

    /**
     * Team name setter
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
