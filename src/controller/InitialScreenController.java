/**
*   <h1>IntialScreenController<h1>
*   IntialScreenController class that shows us the normal user dashboard
*   @author Maria Garcia Fernandez
*   @version 1.0
*   @since 2017-04-7
*/

/**
 * Imported package and libraries
 */
package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

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
import javafx.scene.control.Hyperlink;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import models.DataDAO;


public class InitialScreenController implements Initializable  {

	/**
	 * Private class variable
	 */
	private User user;
	private URL location;
	private ResourceBundle resources;
	private String filter;
	private DataDAO dataDAO;

	@FXML
	private Text userText;

	@FXML
	private RadioButton radioMatch, radioPlayer, radioTeam;

	@FXML
	private Button continueBtn;

	@FXML
	private Hyperlink signOutLink;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.location = location;
		this.resources = resources;
		// Create a radio group button
		ToggleGroup group = new ToggleGroup();
	    radioMatch.setToggleGroup(group);
	    radioPlayer.setToggleGroup(group);
	    radioTeam.setToggleGroup(group);
	    // Select radio button
	    group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
	        @Override
	        public void changed(ObservableValue<? extends Toggle> ov, Toggle t, Toggle t1) {
	            RadioButton chk = (RadioButton)t1.getToggleGroup().getSelectedToggle();
	            filter = chk.getText();
	        }
	    });

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
			FXMLLoader loader = new FXMLLoader(InitialScreenController.class.getResource("/resources/main.fxml"));
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
	 * filter method
	 * @throws IOException
	 * @throws SQLException
	 */
	public void filter() throws IOException, SQLException{
		// Alert if missing fields
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error");
		if (filter == null){
			alert.setHeaderText("Missing Field Error");
			alert.setContentText("Please select one filter");
			alert.showAndWait();
		} else {
			// Close screen
			Stage stage = (Stage) radioMatch.getScene().getWindow();
		    stage.close();
		    dataDAO = new DataDAO();
		    try {
		    	// Filter
				switch(filter){
					case "Match":
						FXMLLoader matchLoader = new FXMLLoader(InitialScreenController.class.getResource("/resources/match.fxml"));
						Parent matchRoot = (Parent) matchLoader.load();
						Scene matchScene = new Scene(matchRoot);
						MatchController mController = matchLoader.getController();
						mController.setUser(user);
						mController.loadData(dataDAO.getMatches());
						Stage matchStage = new Stage();
						matchStage.setScene(matchScene);
						matchStage.setTitle("Soccer Stats Matches");
						matchStage.show();
						break;
					case "Team":
						FXMLLoader teamLoader = new FXMLLoader(InitialScreenController.class.getResource("/resources/team.fxml"));
						Parent teamRoot = (Parent) teamLoader.load();
						Scene teamScene = new Scene(teamRoot);
						TeamController tController = teamLoader.getController();
						tController.setUser(user);
						tController.loadData(dataDAO.getTeams());
						Stage teamStage = new Stage();
						teamStage.setScene(teamScene);
						teamStage.setTitle("Soccer Stats Teams");
						teamStage.show();
						break;
					case "Player":
						FXMLLoader playerLoader = new FXMLLoader(getClass().getResource("/resources/player.fxml"));
						Parent playerRoot = (Parent) playerLoader.load();
						Scene playerScene = new Scene(playerRoot);
				        PlayerController pController = playerLoader.getController();
				        pController.setUser(user);
				        pController.loadData(dataDAO.getPlayers());
						Stage playerStage = new Stage();
						playerStage.setScene(playerScene);
						playerStage.setTitle("Soccer Stats Players");
						playerStage.show();
						break;
					default:
						break;
				}
		    } catch(Exception e){
		    	System.out.println(e.getMessage());
		    }
		}
	}

	/**
	 * setUser method to pass user information from one controller to another
	 * @param user
	 */
	public void setUser(User user){
		this.user = user;
		initialize(location, resources);
		userText.setText(user.getName()+"!");
	}
}
