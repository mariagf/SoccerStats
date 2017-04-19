
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
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;
import models.DataDAO;

public class UpdatePlayerController implements Initializable  {

	private User user;
	private URL location;
	private ResourceBundle resources;
	private DataDAO dataDAO;
	private String name, lastname, height, team;
	private Player p;
	private int selection;

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
		heightText.textProperty().addListener(new ChangeListener<String>() {
	        @Override
	        public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
	            if (!newValue.matches("\\d*")) {
	                heightText.setText(newValue.replaceAll("[^\\d]", ""));
	            }
	        }
	    });

		teamsBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
		      @Override
		      public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
		        team = (String) teamsBox.getItems().get((Integer) number2);
		      }
		});

	}

	@FXML
	public void updatePlayer(){
		dataDAO = new DataDAO();
		try {
			name = nameText.getText();
			lastname = lastnameText.getText();
			height = heightText.getText();
			LocalDate ld = datePicker.getValue();
			if(name.isEmpty()||lastname.isEmpty()||height.isEmpty()
					||team == null||ld == null){
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error Message");
				alert.setHeaderText("Missing Field Error");
				alert.setContentText("Please fill all the fields");
				alert.showAndWait();
			} else {
				Date date = java.sql.Date.valueOf(ld);
				Player p = new Player(name, lastname, date, Integer.parseInt(height));
				dataDAO = new DataDAO();
				int idTeam = dataDAO.getTeamIdByName(team);
				dataDAO.updatePlayer(p, idTeam, selection);
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Information Message");
				alert.setHeaderText("Player Updated Succesfully!");
				alert.setContentText("The player has been succesfully updated in the database");
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
			FXMLLoader loader = new FXMLLoader(UpdatePlayerController.class.getResource("/resources/main.fxml"));
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
		FXMLLoader loader = new FXMLLoader(UpdatePlayerController.class.getResource("/resources/adminInitialScreen.fxml"));
		Parent root = (Parent) loader.load();
		Scene scene = new Scene(root);
		// Get the Controller from the FXMLLoader
		AdminInitialScreenController iController = loader.getController();
		iController.setUser(user);
		newStage.setScene(scene);
		newStage.setTitle("Soccer Stats Admin Dashboard");
		newStage.show();
	}

	public void setData(User user, int selection){
		this.user = user;
		this.selection  = selection;
		dataDAO = new DataDAO();
		this.p = dataDAO.getPlayerById(selection);
		if(p != null){
			nameText.setText(p.getName());
			lastnameText.setText(p.getLastname());
			heightText.setText(Integer.toString(p.getHeight()));
			datePicker.setValue(LocalDate.parse(p.getDateOfBirth().toString()));
			int teamId = dataDAO.getTeamIdByPlayerName(p.getName());
			this.team = dataDAO.getTeamNameById(teamId);
		}
		ResultSet rs = dataDAO.getTeamNames();
		try {
			while(rs.next()){
				teamsBox.getItems().add(rs.getString(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		teamsBox.getSelectionModel().select(team);
		initialize(location, resources);
	}

}
