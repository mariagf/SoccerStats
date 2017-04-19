/**
*   <h1>TeamController<h1>
*   TeamController class that allows us to control teams
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
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import application.Match;
import application.Player;
import application.Stats;
import application.Team;
import application.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import models.DataDAO;
import views.TeamRow;

public class TeamController implements Initializable {

	/**
	 * Private class variables
	 */
	private User user;
	private URL location;
	private ResourceBundle resources;
	private DataDAO dataDAO;

	@FXML
	private Button backBtn;

	@FXML
	private TableView<TeamRow> teamTable;

	@FXML
	private TableColumn<TeamRow, String> teamColumn, coachColumn, cityColumn, foundationDateColumn, playersColumn, matchesColumn;

	@FXML
	private Hyperlink signOutLink;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.location = location;
		this.resources = resources;
	}

	@FXML
	/**
	 * back method to come one screen back
	 * @throws IOException
	 */
	public void back() throws IOException{
		try {
			// Close screen
			Stage stage = (Stage) backBtn.getScene().getWindow();
			stage.close();
			// Create screen
			Stage newStage = new Stage();
			FXMLLoader loader;
				if(user.getIsAdmin() == 1){
					loader = new FXMLLoader(TeamController.class.getResource("/resources/adminInitialScreen.fxml"));
					Parent root = (Parent) loader.load();
					Scene scene = new Scene(root);
					// Get the Controller from the FXMLLoader
			        AdminInitialScreenController iController = loader.getController();
			        iController.setUser(user);
			        newStage.setScene(scene);
					newStage.setTitle("Soccer Stats Admin Dashboard");
					newStage.show();
				} else {
					loader = new FXMLLoader(TeamController.class.getResource("/resources/initialScreen.fxml"));
					Parent root = (Parent) loader.load();
					Scene scene = new Scene(root);
					// Get the Controller from the FXMLLoader
			        InitialScreenController iController = loader.getController();
			        iController.setUser(user);
			        newStage.setScene(scene);
					newStage.setTitle("Soccer Stats Filter");
					newStage.show();
				}
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
			FXMLLoader loader = new FXMLLoader(TeamController.class.getResource("/resources/main.fxml"));
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
	 * loadData method that fills the table with the information to show
	 * @param rs
	 */
	public void loadData(ResultSet rsTeams) {
		try {
			List<TeamRow> dataTeams = new ArrayList<TeamRow>();
			List<Player> dataPlayers = new ArrayList<Player>();
			List<Match> dataMatches = new ArrayList<Match>();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			while (rsTeams.next()) {
				int teamId = rsTeams.getInt(1);
				dataDAO = new DataDAO();
				ResultSet rsPlayers = dataDAO.getPlayersByTeamId(teamId);
				List<String> infoPlayers = new ArrayList<String>();
				List<String> infoMatches = new ArrayList<String>();
				while (rsPlayers.next()) {
					Player p = new Player(rsPlayers.getString(3), rsPlayers.getString(4), rsPlayers.getDate(5), rsPlayers.getInt(6));
					dataPlayers.add(p);
					infoPlayers.add(rsPlayers.getString(3)+" "+rsPlayers.getString(4));
				}
				ResultSet rsMatches = dataDAO.getMatchesByTeamId(teamId);
				while (rsMatches.next()) {
					Stats stats = dataDAO.getStatsByMatchId(rsMatches.getInt(1));
					Match m = new Match(dataDAO.getTeamById(rsMatches.getInt(2), rsMatches), dataDAO.getTeamById(rsMatches.getInt(3), rsMatches), stats, rsMatches.getDate(4),rsMatches.getString(5));
					dataMatches.add(m);
					infoMatches.add(rsMatches.getDate(4)+": "+dataDAO.getTeamNameById(rsMatches.getInt(2))+" vs "+dataDAO.getTeamNameById(rsMatches.getInt(3)));
				}
				String printPlayers = infoPlayers.toString().replace("[", "").replace("]", "").replace(", ", "\n").trim();
				String printMatches = infoMatches.toString().replace("[", "").replace("]", "").replace(", ", "\n").trim();
				Team t = new Team(dataPlayers, rsTeams.getString(2), rsTeams.getString(3), rsTeams.getString(4), rsTeams.getDate(5));
		        TeamRow tR = new TeamRow(t.getName(), t.getCoach(), t.getCity(), sdf.format(t.getDateFoundation()), printPlayers, printMatches, false);
		        teamColumn.setCellValueFactory(new PropertyValueFactory<TeamRow, String>("team"));
		        coachColumn.setCellValueFactory(new PropertyValueFactory<TeamRow, String>("coach"));
		        cityColumn.setCellValueFactory(new PropertyValueFactory<TeamRow, String>("city"));
		        foundationDateColumn.setCellValueFactory(new PropertyValueFactory<TeamRow, String>("foundationDate"));
		        playersColumn.setCellValueFactory(new PropertyValueFactory<TeamRow, String>("players"));
		        matchesColumn.setCellValueFactory(new PropertyValueFactory<TeamRow, String>("matches"));
		        dataTeams.add(tR);
			}
			teamTable.getItems().setAll(dataTeams);
		} catch(Exception e){
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
	}
}
