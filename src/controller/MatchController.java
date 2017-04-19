/**
*   <h1>MatchController<h1>
*   MatchController class that allows us to control match
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
import views.MatchRow;

public class MatchController implements Initializable {

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
	private TableView<MatchRow> matchesTable;

	@FXML
	private TableColumn<MatchRow, String> localTeamColumn, visitorTeamColumn, localGoalsColumn, visitorGoalsColumn, cornersColumn, foulsColumn, dateColumn, refereeColumn;

	@FXML
	private Hyperlink signOutLink;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.location = location;
		this.resources = resources;
	}

	/**
	 * loadData method that fills the table with the information to show
	 * @param rs
	 */
	public void loadData(ResultSet rs) {
		List<MatchRow> dataMatches = new ArrayList<MatchRow>();
		try {
			dataDAO = new DataDAO();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			while (rs.next()) {
				Team teamHome = dataDAO.getTeamById(rs.getInt(2), rs);
				Team teamAway = dataDAO.getTeamById(rs.getInt(3), rs);
				Stats stats = dataDAO.getStatsByMatchId(rs.getInt(1));
				Match m = new Match(teamHome, teamAway, stats, rs.getDate(4),rs.getString(5));
				MatchRow mR = new MatchRow(rs.getInt(1), teamHome.getName(), teamAway.getName(), Integer.toString(stats.getGoalsHome()), Integer.toString(stats.getGoalsAway()), Integer.toString(stats.getNumberOfCorners()), Integer.toString(stats.getNumberOfFouls()), sdf.format(m.getDate()), m.getReferee(), false);
				localTeamColumn.setCellValueFactory(new PropertyValueFactory<MatchRow, String>("localTeam"));
				visitorTeamColumn.setCellValueFactory(new PropertyValueFactory<MatchRow, String>("visitorTeam"));
		        localGoalsColumn.setCellValueFactory(new PropertyValueFactory<MatchRow, String>("localGoals"));
		        visitorGoalsColumn.setCellValueFactory(new PropertyValueFactory<MatchRow, String>("visitorGoals"));
		        cornersColumn.setCellValueFactory(new PropertyValueFactory<MatchRow, String>("corners"));
		        foulsColumn.setCellValueFactory(new PropertyValueFactory<MatchRow, String>("fouls"));
		        dateColumn.setCellValueFactory(new PropertyValueFactory<MatchRow, String>("date"));
		        refereeColumn.setCellValueFactory(new PropertyValueFactory<MatchRow, String>("referee"));
		        dataMatches.add(mR);
			}
			matchesTable.getItems().setAll(dataMatches);
		} catch(Exception e){
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
			FXMLLoader loader = new FXMLLoader(MatchController.class.getResource("/resources/main.fxml"));
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
					loader = new FXMLLoader(MatchController.class.getResource("/resources/adminInitialScreen.fxml"));
					Parent root = (Parent) loader.load();
					Scene scene = new Scene(root);
			        AdminInitialScreenController iController = loader.getController();
			        iController.setUser(user);
			        newStage.setScene(scene);
					newStage.setTitle("Soccer Stats Admin Dashboard");
					newStage.show();
				} else {
					loader = new FXMLLoader(MatchController.class.getResource("/resources/initialScreen.fxml"));
					Parent root = (Parent) loader.load();
					Scene scene = new Scene(root);
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

	/**
	 * setUser method to pass user information from one controller to another
	 * @param user
	 */
	public void setUser(User user){
		this.user = user;
		initialize(location, resources);
	}

}
