/**
*   <h1>AdminIntialScreenController<h1>
*   AdminIntialScreenController class that shows us the admin dashboard
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

public class AdminInitialScreenController implements Initializable  {

	/**
	 * private class variables
	 */
	private User user;
	private URL location;
	private ResourceBundle resources;
	private String groupFilter;
	private String actionFilter;
	private DataDAO dataDAO;

	@FXML
	private Text userText;

	@FXML
	private RadioButton radioMatches, radioPlayers, radioTeams, radioDisplay, radioUpdateDelete, radioAdd;

	@FXML
	private Button continueBtn;

	@FXML
	private Hyperlink signOutLink;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.location = location;
		this.resources = resources;
		// Create radio group buttons
		ToggleGroup group = new ToggleGroup();
	    radioMatches.setToggleGroup(group);
	    radioPlayers.setToggleGroup(group);
	    radioTeams.setToggleGroup(group);

	    ToggleGroup actions = new ToggleGroup();
	    radioDisplay.setToggleGroup(actions);
	    radioAdd.setToggleGroup(actions);
	    radioUpdateDelete.setToggleGroup(actions);
	    // Allow only numbers
	    group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
	        @Override
	        public void changed(ObservableValue<? extends Toggle> ov, Toggle t, Toggle t1) {
	            RadioButton chk = (RadioButton)t1.getToggleGroup().getSelectedToggle(); // Cast object to radio button
	            groupFilter = chk.getText();
	        }
	    });
	    actions.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
	        @Override
	        public void changed(ObservableValue<? extends Toggle> ov, Toggle t, Toggle t1) {
	            RadioButton chk = (RadioButton)t1.getToggleGroup().getSelectedToggle(); // Cast object to radio button
	            actionFilter = chk.getText();
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
			FXMLLoader loader = new FXMLLoader(AdminInitialScreenController.class.getResource("/resources/main.fxml"));
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
	 * cont method that shows different information depending the filter chosen
	 * @throws IOException
	 * @throws SQLException
	 */
	public void cont() throws IOException, SQLException{
		// Alert if missing fields
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error");
		if (groupFilter == null || actionFilter == null){
			alert.setHeaderText("Missing Field Error");
			alert.setContentText("Please select an action and an item");
			alert.showAndWait();
		} else {
			// Close screen
			Stage stage = (Stage) radioMatches.getScene().getWindow();
		    stage.close();
		    dataDAO = new DataDAO();
		    try {
		    	// Filter
				if(groupFilter.equals("Matches")){
					if(actionFilter.equals("Add")){
						FXMLLoader matchAddLoader = new FXMLLoader(AdminInitialScreenController.class.getResource("/resources/addMatch.fxml"));
						Parent matchAddRoot = (Parent) matchAddLoader.load();
						Scene matchAddScene = new Scene(matchAddRoot);
						AddMatchController mAddController = matchAddLoader.getController();
						mAddController.setUser(user);
						Stage matchAddStage = new Stage();
						matchAddStage.setScene(matchAddScene);
						matchAddStage.setTitle("Soccer Stats Add Matches");
						matchAddStage.show();
					} else if (actionFilter.equals("Update or Delete")){
						FXMLLoader matchUpdateDeleteLoader = new FXMLLoader(AdminInitialScreenController.class.getResource("/resources/selectMatchToEditDelete.fxml"));
						Parent matchUpdateDeleteRoot = (Parent) matchUpdateDeleteLoader.load();
						Scene matchUpdateDeleteScene = new Scene(matchUpdateDeleteRoot);
						SelectMatchToUpdateDeleteController mUpdateDeleteController = matchUpdateDeleteLoader.getController();
						mUpdateDeleteController.setUser(user);
						mUpdateDeleteController.loadData(dataDAO.getMatches());
						Stage matchUpdateDeleteStage = new Stage();
						matchUpdateDeleteStage.setScene(matchUpdateDeleteScene);
						matchUpdateDeleteStage.setTitle("Soccer Stats Select Match to Update or Delete");
						matchUpdateDeleteStage.show();
					} else { // Display
						FXMLLoader matchLoader = new FXMLLoader(AdminInitialScreenController.class.getResource("/resources/match.fxml"));
						Parent matchRoot = (Parent) matchLoader.load();
						Scene matchScene = new Scene(matchRoot);
						MatchController mController = matchLoader.getController();
						mController.setUser(user);
						mController.loadData(dataDAO.getMatches());
						Stage matchStage = new Stage();
						matchStage.setScene(matchScene);
						matchStage.setTitle("Soccer Stats Matches");
						matchStage.show();
					}
				} else if(groupFilter.equals("Teams")){
					if(actionFilter.equals("Add")){
						FXMLLoader teamAddLoader = new FXMLLoader(AdminInitialScreenController.class.getResource("/resources/addTeam.fxml"));
						Parent teamAddRoot = (Parent) teamAddLoader.load();
						Scene teamAddScene = new Scene(teamAddRoot);
						AddTeamController tAddController = teamAddLoader.getController();
						tAddController.setUser(user);
						Stage teamAddStage = new Stage();
						teamAddStage.setScene(teamAddScene);
						teamAddStage.setTitle("Soccer Stats Add Teams");
						teamAddStage.show();
					} else if (actionFilter.equals("Update or Delete")){
						FXMLLoader teamUpdateDeleteLoader = new FXMLLoader(AdminInitialScreenController.class.getResource("/resources/selectTeamToEditDelete.fxml"));
						Parent teamUpdateDeleteRoot = (Parent) teamUpdateDeleteLoader.load();
						Scene teamUpdateDeleteScene = new Scene(teamUpdateDeleteRoot);
						SelectTeamToUpdateDeleteController tUpdateDeleteController = teamUpdateDeleteLoader.getController();
						tUpdateDeleteController.setUser(user);
						tUpdateDeleteController.loadData(dataDAO.getTeams());
						Stage teamUpdateDeleteStage = new Stage();
						teamUpdateDeleteStage.setScene(teamUpdateDeleteScene);
						teamUpdateDeleteStage.setTitle("Soccer Stats Select Team to Update or Delete");
						teamUpdateDeleteStage.show();
					} else { // Display
						FXMLLoader teamLoader = new FXMLLoader(AdminInitialScreenController.class.getResource("/resources/team.fxml"));
						Parent teamRoot = (Parent) teamLoader.load();
						Scene teamScene = new Scene(teamRoot);
						TeamController tController = teamLoader.getController();
						tController.setUser(user);
						tController.loadData(dataDAO.getTeams());
						Stage teamStage = new Stage();
						teamStage.setScene(teamScene);
						teamStage.setTitle("Soccer Stats Teams");
						teamStage.show();
					}
				} else if(groupFilter.equals("Players")){
					if(actionFilter.equals("Add")){
						FXMLLoader playerAddLoader = new FXMLLoader(AdminInitialScreenController.class.getResource("/resources/addPlayer.fxml"));
						Parent playerAddRoot = (Parent) playerAddLoader.load();
						Scene playerAddScene = new Scene(playerAddRoot);
						AddPlayerController pAddController = playerAddLoader.getController();
						pAddController.setUser(user);
						Stage playerAddStage = new Stage();
						playerAddStage.setScene(playerAddScene);
						playerAddStage.setTitle("Soccer Stats Add Players");
						playerAddStage.show();
					} else if (actionFilter.equals("Update or Delete")){
						FXMLLoader playerUpdateDeleteLoader = new FXMLLoader(AdminInitialScreenController.class.getResource("/resources/selectPlayerToEditDelete.fxml"));
						Parent playerUpdateDeleteRoot = (Parent) playerUpdateDeleteLoader.load();
						Scene playerUpdateDeleteScene = new Scene(playerUpdateDeleteRoot);
						SelectPlayerToUpdateDeleteController pUpdateDeleteController = playerUpdateDeleteLoader.getController();
						pUpdateDeleteController.setUser(user);
						pUpdateDeleteController.loadData(dataDAO.getPlayers());
						Stage playerUpdateDeleteStage = new Stage();
						playerUpdateDeleteStage.setScene(playerUpdateDeleteScene);
						playerUpdateDeleteStage.setTitle("Soccer Stats Select Player to Update or Delete");
						playerUpdateDeleteStage.show();
					} else { // Display
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
					}
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
