
package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Date;
import java.util.ResourceBundle;

import application.Match;
import application.Stats;
import application.Team;
import application.User;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DatePicker;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import models.DataDAO;

public class UpdateMatchController implements Initializable  {

	private User user;
	private URL location;
	private ResourceBundle resources;
	private DataDAO dataDAO;
	private String localTeamNameString, visitorTeamNameString, cornersString, foulsString, refereeString, localGoalsString, visitorGoalsString;
	private Match m;
	private Stats s;
	private int selection;

	@FXML
	private TextField cornersText, foulsText, refereeText, localGoalsText, visitorGoalsText;

	@FXML
	private DatePicker datePicker;

	@FXML
	private Hyperlink signOutLink;

	@FXML
	private ChoiceBox<String> teamHomeBox;

	@FXML
	private ChoiceBox<String> teamAwayBox;

	@FXML
	private Text message;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.location = location;
		this.resources = resources;
		// Select teams from a choicebox
		teamHomeBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
		      @Override
		      public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
		    	  localTeamNameString = (String) teamHomeBox.getItems().get((Integer) number2);
		      }
		});
		teamAwayBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
		      @Override
		      public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
		    	  visitorTeamNameString = (String) teamAwayBox.getItems().get((Integer) number2);
		      }
		});
		// Allow only numbers
		foulsText.textProperty().addListener(new ChangeListener<String>() {
	        @Override
	        public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
	            if (!newValue.matches("\\d*")) {
	                foulsText.setText(newValue.replaceAll("[^\\d]", ""));
	            }
	        }
	    });
		localGoalsText.textProperty().addListener(new ChangeListener<String>() {
	        @Override
	        public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
	            if (!newValue.matches("\\d*")) {
	                localGoalsText.setText(newValue.replaceAll("[^\\d]", ""));
	            }
	        }
	    });
		visitorGoalsText.textProperty().addListener(new ChangeListener<String>() {
	        @Override
	        public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
	            if (!newValue.matches("\\d*")) {
	                visitorGoalsText.setText(newValue.replaceAll("[^\\d]", ""));
	            }
	        }
	    });
		cornersText.textProperty().addListener(new ChangeListener<String>() {
	        @Override
	        public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
	            if (!newValue.matches("\\d*")) {
	                cornersText.setText(newValue.replaceAll("[^\\d]", ""));
	            }
	        }
	    });
	}

	@FXML
	public void updateMatch(){
		dataDAO = new DataDAO();
		try {
			foulsString = foulsText.getText();
			localGoalsString = localGoalsText.getText();
			visitorGoalsString = visitorGoalsText.getText();
			cornersString = cornersText.getText();
			refereeString = refereeText.getText();
			LocalDate ld = datePicker.getValue();
			if(foulsString.isEmpty()||localGoalsString.isEmpty()||visitorGoalsString.isEmpty()
					||cornersString.isEmpty()||localTeamNameString.isEmpty()||visitorTeamNameString.isEmpty() ||ld == null){
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error Message");
				alert.setHeaderText("Missing Field Error");
				alert.setContentText("Please fill all the fields");
				alert.showAndWait();
			} else {
				int fouls = Integer.parseInt(foulsString);
				int goalsHome = Integer.parseInt(localGoalsString);
				int goalsAway = Integer.parseInt(visitorGoalsString);
				int corners = Integer.parseInt(cornersString);
				Stats s = new Stats(goalsHome, goalsAway, corners, fouls);
				Team teamHome = dataDAO.getTeamByName(localTeamNameString);
				Team teamAway = dataDAO.getTeamByName(visitorTeamNameString);
				Date date = java.sql.Date.valueOf(ld);
				Match m = new Match(teamHome, teamAway, s, date, refereeString);
				if(teamHome != null && teamAway != null){
					dataDAO = new DataDAO();
					dataDAO.updateStat(s, selection);
					System.out.println(selection);
					dataDAO.updateMatch(m, selection);
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Information Message");
					alert.setHeaderText("Match Updated Succesfully!");
					alert.setContentText("The match has been succesfully updated in the database");
					alert.showAndWait();
					back();
				} else {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Error message");
					alert.setHeaderText("Incorrect Team Name");
					alert.setContentText("The team names introduced are not in the database or are incorrect.");
					alert.showAndWait();
				}
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
			FXMLLoader loader = new FXMLLoader(UpdateMatchController.class.getResource("/resources/main.fxml"));
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
		Stage stage = (Stage) cornersText.getScene().getWindow();
		stage.close();
		Stage newStage = new Stage();
		FXMLLoader loader = new FXMLLoader(UpdateMatchController.class.getResource("/resources/adminInitialScreen.fxml"));
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
		this.m = dataDAO.getMatchById(selection);
		this.s = dataDAO.getStatsById(selection);
		if(m != null && s!=null){
			foulsText.setText(Integer.toString(s.getNumberOfFouls()));
			refereeText.setText(m.getReferee());
			localGoalsText.setText(Integer.toString(s.getGoalsAway()));
			visitorGoalsText.setText(Integer.toString(s.getGoalsHome()));
			cornersText.setText(Integer.toString(s.getNumberOfCorners()));
			teamHomeBox.getSelectionModel().select(m.getTeamHome().getName());
			teamAwayBox.getSelectionModel().select(m.getTeamAway().getName());
			datePicker.setValue(LocalDate.parse(m.getDate().toString()));
		}
		ResultSet rs = dataDAO.getTeamNames();
		try {
			while(rs.next()){
				teamHomeBox.getItems().add(rs.getString(1));
				teamAwayBox.getItems().add(rs.getString(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		initialize(location, resources);
	}

}
