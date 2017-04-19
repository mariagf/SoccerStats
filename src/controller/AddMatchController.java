/**
*   <h1>AddMatchController<h1>
*   AddMatchController class that build the dashboard to add matches
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
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import models.DataDAO;

public class AddMatchController implements Initializable  {

	/**
	 * Private class variables
	 */
	private User user;
	private URL location;
	private ResourceBundle resources;
	private DataDAO dataDAO;
	private String localTeam, visitorTeam, referee, localGoals, visitorGoals, corners, fouls;

	@FXML
	private Button backBtn;

	@FXML
	private TextField localTeamText, visitorTeamText, refereeText, localGoalsText, visitorGoalsText, cornersText, foulsText;

	@FXML
	private DatePicker datePicker;

	@FXML
	private ChoiceBox<String> teamHomeBox;

	@FXML
	private ChoiceBox<String> teamAwayBox;

	@FXML
	private Hyperlink signOutLink;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.location = location;
		this.resources = resources;
		// Select teams from a choicebox
		teamHomeBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
		      @Override
		      public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
		    	  localTeam = (String) teamHomeBox.getItems().get((Integer) number2);
		      }
		});
		teamAwayBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
		      @Override
		      public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
		    	  visitorTeam = (String) teamAwayBox.getItems().get((Integer) number2);
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
	/**
	 *
	 * @throws IOException
	 */
	public void back() throws IOException{
		try {
			// Close screen
			Stage stage = (Stage) backBtn.getScene().getWindow();
			stage.close();
			// Create screen
			Stage newStage = new Stage();
			FXMLLoader loader = new FXMLLoader(AddMatchController.class.getResource("/resources/adminInitialScreen.fxml"));
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
				teamHomeBox.getItems().add(rs.getString(1));
				teamAwayBox.getItems().add(rs.getString(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@FXML
	/**
	 * addMatch method
	 */
	public void addMatch(){
		dataDAO = new DataDAO();
		try {
			referee = refereeText.getText();
			LocalDate ld = datePicker.getValue();
			localGoals = localGoalsText.getText();
			visitorGoals = visitorGoalsText.getText();
			corners = cornersText.getText();
			fouls = foulsText.getText();
			if(localTeam.isEmpty()||visitorTeam.isEmpty()||referee.isEmpty()||ld == null||localGoals.isEmpty()||visitorGoals.isEmpty()||corners.isEmpty()||fouls.isEmpty()){
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error Message");
				alert.setHeaderText("Missing Field Error");
				alert.setContentText("Please fill all the fields");
				alert.showAndWait();
			} else {
				Team teamHome = dataDAO.getTeamByName(localTeam);
				Team teamAway = dataDAO.getTeamByName(visitorTeam);
				if(teamHome == null || teamAway == null){
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Error Message");
					alert.setHeaderText("Team Error");
					alert.setContentText("The teams introduce are not in the database plese introduce them first");
					alert.showAndWait();
				} else {
					Date date = java.sql.Date.valueOf(ld);
					Stats stat = new Stats(Integer.parseInt(localGoals), Integer.parseInt(visitorGoals), Integer.parseInt(corners), Integer.parseInt(fouls));
					Match m = new Match(teamHome, teamAway, stat, date, referee);
					dataDAO.insertStat(stat);
					dataDAO.insertMatch(m);
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Information Message");
					alert.setHeaderText("Match Added Succesfully!");
					alert.setContentText("The match has been succesfully stored in the database");
					alert.showAndWait();
					back();
				}
			}
		} catch(Exception e){
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error message");
			alert.setHeaderText("There has been an error...");
			alert.setContentText("The match has not been stored in the database. Please try again later.");
			alert.showAndWait();
			System.out.println(e.getMessage());
		}
	}
}
