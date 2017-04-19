/**
*   <h1>AddPlayerController<h1>
*   AddPlayerController class that builds the dashboard to add new players
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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Date;
import java.util.ResourceBundle;

import application.Player;
import application.User;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import models.DataDAO;

public class AddPlayerController implements Initializable  {

	/**
	 * Private class variables
	 */
	private User user;
	private URL location;
	private ResourceBundle resources;
	private DataDAO dataDAO;
	private String name, lastname, height;
	private String teamName;

	@FXML
	private Button backBtn;

	@FXML
	private TextField nameText, lastnameText, heightText;

	@FXML
	private DatePicker datePicker;

	@FXML
	private ChoiceBox<String> teamsBox;

	@FXML
	private Hyperlink signOutLink;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.location = location;
		this.resources = resources;
		// Select teams from a choicebox
		teamsBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
		      @Override
		      public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
		        teamName = (String) teamsBox.getItems().get((Integer) number2);
		      }
		});
		// Allow only numbers
		heightText.textProperty().addListener(new ChangeListener<String>() {
	        @Override
	        public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
	            if (!newValue.matches("\\d*")) {
	                heightText.setText(newValue.replaceAll("[^\\d]", ""));
	            }
	        }
	    });
	}

	@FXML
	/**
	 * back method to go one screen back
	 * @throws IOException
	 */
	public void back() throws IOException{
		try {
			// Close screen
			Stage stage = (Stage) backBtn.getScene().getWindow();
			stage.close();
			// Create screen
			Stage newStage = new Stage();
			FXMLLoader loader = new FXMLLoader(AddPlayerController.class.getResource("/resources/adminInitialScreen.fxml"));
			Parent root = (Parent) loader.load();
			Scene scene = new Scene(root);
	        AdminInitialScreenController iController = loader.getController();
	        iController.setUser(user);
	        newStage.setScene(scene);
			newStage.setTitle("Soccer Stats Admin Dashboard");
			newStage.show();
		} catch (IOException e){
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
	 * addPlayer method
	 */
	public void addPlayer(){
		dataDAO = new DataDAO();
		try {
			name = nameText.getText();
			lastname = lastnameText.getText();
			height = heightText.getText();
			LocalDate ld = datePicker.getValue();
			if(name.isEmpty()||lastname.isEmpty()||height.isEmpty()||ld == null){
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error Message");
				alert.setHeaderText("Missing Field Error");
				alert.setContentText("Please fill all the fields");
				alert.showAndWait();
			} else {
				Date date = java.sql.Date.valueOf(ld);
				int idTeam = dataDAO.getTeamIdByName(teamName);
				Player p = new Player(name, lastname, date, Integer.parseInt(height));
				dataDAO.insertPlayer(p,idTeam);
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Information Message");
				alert.setHeaderText("Player Added Succesfully!");
				alert.setContentText("The player has been succesfully stored in the database");
				alert.showAndWait();
				back();
			}
		} catch(Exception e){
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error message");
			alert.setHeaderText("There has been an error...");
			alert.setContentText("The player has not been stored in the database. Please try again later.");
			alert.showAndWait();
			System.out.println(e.getMessage());
		}
	}

	/**
	 * setUser method to pass user information from one controller to another
	 * @param user
	 */
	public void setUser(User user){
		this.user = user;
		initialize(location, resources);
		dataDAO = new DataDAO();
		ResultSet rs = dataDAO.getTeamNames();
		try {
			while(rs.next()){
				teamsBox.getItems().add(rs.getString(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
