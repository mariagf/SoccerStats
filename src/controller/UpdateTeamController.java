
package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;
import models.DataDAO;

public class UpdateTeamController implements Initializable  {

	private User user;
	private URL location;
	private ResourceBundle resources;
	private DataDAO dataDAO;
	private String name, city, coach;
	private Team t;
	private int selection;

	@FXML
	private TextField nameText, cityText, coachText;

	@FXML
	private DatePicker datePicker;

	@FXML
	private Hyperlink signOutLink;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.location = location;
		this.resources = resources;
	}

	@FXML
	public void updateTeam(){
		dataDAO = new DataDAO();
		try {
			name = nameText.getText();
			city = cityText.getText();
			coach = coachText.getText();
			LocalDate ld = datePicker.getValue();

			if(name.isEmpty()||city.isEmpty()||coach.isEmpty()){
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error Message");
				alert.setHeaderText("Missing Field Error");
				alert.setContentText("Please fill all the fields");
				alert.showAndWait();
			} else {
				Date date = java.sql.Date.valueOf(ld);
				List<Player> players = new ArrayList<Player>();
				Team team = new Team(players, name, coach, city, date);
				dataDAO.updateTeam(team, selection);
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Information Message");
				alert.setHeaderText("Team Added Succesfully!");
				alert.setContentText("The team has been succesfully stored in the database");
				alert.showAndWait();
				back();
			}
		} catch(Exception e){
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error message");
			alert.setHeaderText("There has been an error...");
			alert.setContentText("The match has not been updated in the database. Please try again later.");
			alert.showAndWait();
			System.out.println(e.getMessage());
		}
	}

	@FXML
	public void signOut() throws IOException{
		try {
			// Close the CreateAccountScreen
			Stage stage = (Stage) signOutLink.getScene().getWindow();
			stage.close();
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
	private void back() throws IOException{
		// Close the CreateAccountScreen
		Stage stage = (Stage) nameText.getScene().getWindow();
		stage.close();
		Stage newStage = new Stage();
		FXMLLoader loader = new FXMLLoader(UpdateTeamController.class.getResource("/resources/adminInitialScreen.fxml"));
		Parent root = (Parent) loader.load();
		Scene scene = new Scene(root);
		// Get the Controller from the FXMLLoader
		AdminInitialScreenController iController = loader.getController();
		iController.setUser(user);
		newStage.setScene(scene);
		newStage.setTitle("Soccer Stats Admin Dashboard");
		newStage.show();
	}

	public void setUser(User user){
		this.user = user;
		initialize(location, resources);
	}

	public void setData(User user, int selection){
		this.user = user;
		dataDAO = new DataDAO();
		ResultSet rsMatches = null;
		try {
			rsMatches = dataDAO.getMatchesByTeamId(selection);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.t = dataDAO.getTeamById(selection, rsMatches);
		this.selection = selection;
		if(t != null){
			nameText.setText(t.getName());
			cityText.setText(t.getCity());
			coachText.setText(t.getCoach());
			datePicker.setValue(LocalDate.parse(t.getDateFoundation().toString()));
		}
		initialize(location, resources);
	}

}
