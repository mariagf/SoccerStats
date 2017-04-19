/**
*   <h1>AddTeamController<h1>
*   AddTeamController class that builds the dashboard to add new teams
*   @author Maria Garcia Fernandez
*   @version 1.0
*   @since 2017-04-1
*/

/**
 * Imported package and libraries
 */
package controller;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import application.Player;
import application.Team;
import application.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import models.DataDAO;

public class AddTeamController implements Initializable {

	/**
	 * Private class variables
	 */
	private User user;
	private URL location;
	private ResourceBundle resources;
	private DataDAO dataDAO;
	private String name, city, coach;

	@FXML
	private TextField nameText, cityText, coachText;

	@FXML
	private DatePicker datePicker;

	@FXML
	private Hyperlink signOutLink;

	@FXML
	private Text message;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.location = location;
		this.resources = resources;
	}

	@FXML
	/**
	 * addTeam method
	 */
	public void addTeam(){
		dataDAO = new DataDAO();
		try {
			name = nameText.getText();
			city = cityText.getText();
			coach = coachText.getText();
			LocalDate ld = datePicker.getValue();
			if(name.isEmpty()||city.isEmpty()||coach.isEmpty()|| ld == null){
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error Message");
				alert.setHeaderText("Missing Field Error");
				alert.setContentText("Please fill all the fields");
				alert.showAndWait();
			} else {
				Date date = java.sql.Date.valueOf(ld);
				List<Player> players = new ArrayList<Player>();
				Team t = new Team(players, name, coach, city, date);
				dataDAO.insertTeam(t);
				back();
			}
		} catch(Exception e){
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error message");
			alert.setHeaderText("There has been an error...");
			alert.setContentText("The team has not been stored in the database. Please try again later.");
			alert.showAndWait();
			System.out.println(e.getMessage());
		}
	}

	@FXML
	/**
	 * signOut method
	 * @throws IOException
	 */
	public void signOut() throws IOException{
		try {
			// Close screen
			Stage stage = (Stage) signOutLink.getScene().getWindow();
			stage.close();
			// Create screen
			Stage newStage = new Stage();
			FXMLLoader loader = new FXMLLoader(AddTeamController.class.getResource("/resources/main.fxml"));
			Parent root = (Parent) loader.load();
			Scene scene = new Scene(root);
	        newStage.setScene(scene);
			newStage.setTitle("Soccer Stats Login");
			newStage.show();
		} catch (IOException e){
			System.out.println(e.getMessage());
		}
	}

	@FXML
	/**
	 * back method to go one screen back
	 * @throws IOException
	 */
	private void back() throws IOException{
		// Close screen
		Stage stage = (Stage) nameText.getScene().getWindow();
		stage.close();
		// Create screen
		Stage newStage = new Stage();
		FXMLLoader loader = new FXMLLoader(AddTeamController.class.getResource("/resources/adminInitialScreen.fxml"));
		Parent root = (Parent) loader.load();
		Scene scene = new Scene(root);
		AdminInitialScreenController iController = loader.getController();
		iController.setUser(user);
		newStage.setScene(scene);
		newStage.setTitle("Soccer Stats Admin Dashboard");
		newStage.show();
	}

	/**
	 * setUser method to pass user information from one controller to another
	 * @param user
	 */
	public void setUser(User user){
		this.user = user;
		initialize(location, resources);
	}
}
