/**
*   <h1>SelectTamsToUpdateDeleteController<h1>
*   SelectTeamToUpdateDeleteController class that build the dashboard to select the team to update or delete
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import application.Match;
import application.Player;
import application.Stats;
import application.Team;
import application.User;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import models.DataDAO;
import views.TeamRow;

public class SelectTeamToUpdateDeleteController implements Initializable {

	/**
	 * Private class variable
	 */
	private User user;
	private URL location;
	private ResourceBundle resources;
	private DataDAO dataDAO;
	private TeamRow tRSelected;
	private List<String> selection;

	@FXML
	private TableView<TeamRow> teamsTable;

	@FXML
	private TableColumn<TeamRow, Boolean> ticksColumn;

	@FXML
	private TableColumn<TeamRow, String> teamColumn, coachColumn, cityColumn, foundationDateColumn, playersColumn, matchesColumn;

	@FXML
	private Hyperlink signOutLink;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.location = location;
		this.resources = resources;
		// Check which player is selected
		teamsTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
            	// Clear selection
                if(tRSelected != null){
                	tRSelected.setRegistered(false);
                }
                tRSelected = teamsTable.getSelectionModel().getSelectedItem();
                tRSelected.setRegistered(!teamsTable.getSelectionModel().getSelectedItem().isRegistered());
                // Add the information of the team selected into the list
                selection = new ArrayList<String>();
                selection.add(teamsTable.getItems().get(teamsTable.getSelectionModel().getSelectedIndex()).getTeam());
                selection.add(teamsTable.getItems().get(teamsTable.getSelectionModel().getSelectedIndex()).getCoach());
                selection.add(teamsTable.getItems().get(teamsTable.getSelectionModel().getSelectedIndex()).getCity());
                selection.add(teamsTable.getItems().get(teamsTable.getSelectionModel().getSelectedIndex()).getFoundationDate());
            }
        });
	}

	/**
	 * loadData method that fills the table with the information to show
	 * @param rs
	 */
	public void loadData(ResultSet rs) {
		try {
			dataDAO = new DataDAO();
			List<TeamRow> dataTeams = new ArrayList<TeamRow>();
			List<Player> dataPlayers = new ArrayList<Player>();
			List<Match> dataMatches = new ArrayList<Match>();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			while (rs.next()) {
				String teamName = rs.getString(2);
				int teamId = dataDAO.getTeamIdByName(teamName);
				ResultSet rsPlayers = dataDAO.getPlayersByTeamId(teamId);
				List<String> infoPlayers = new ArrayList<String>();
				List<String> infoMatches = new ArrayList<String>();
				while (rsPlayers.next()) {
					Player p = new Player(rsPlayers.getString(3), rsPlayers.getString(4), rsPlayers.getDate(5), rsPlayers.getInt(6));
					dataPlayers.add(p);
					infoPlayers.add(rsPlayers.getString(3)+" "+rsPlayers.getString(4));
				}
				Team t = new Team(dataPlayers, teamName, rs.getString(3), rs.getString(4), rs.getDate(5));
				ResultSet rsMatches = dataDAO.getMatchesByTeamId(teamId);
				while (rsMatches.next()) {
					Stats stats = dataDAO.getStatsByMatchId(rsMatches.getInt(1));
					Match m = new Match(dataDAO.getTeamById(rsMatches.getInt(2), rsMatches), dataDAO.getTeamById(rsMatches.getInt(3), rsMatches), stats, rsMatches.getDate(4),rsMatches.getString(5));
					dataMatches.add(m);
					infoMatches.add(rsMatches.getDate(4)+": "+dataDAO.getTeamNameById(rsMatches.getInt(2))+" vs "+dataDAO.getTeamNameById(rsMatches.getInt(3)));
				}
				String printPlayers = infoPlayers.toString().replace("[", "").replace("]", "").replace(", ", "\n").trim();
				String printMatches = infoMatches.toString().replace("[", "").replace("]", "").replace(", ", "\n").trim();

				TeamRow tR = new TeamRow(t.getName(), t.getCoach(), t.getCity(), sdf.format(t.getDateFoundation()), printPlayers, printMatches, false);
				ticksColumn.setCellFactory(CheckBoxTableCell.forTableColumn(ticksColumn) );
				ticksColumn.setCellValueFactory(param -> {
					if(param != null){
						return param.getValue().registeredProperty();
					} else {
						return null;
					}
			    });
				teamColumn.setCellValueFactory(new PropertyValueFactory<TeamRow, String>("team"));
		        coachColumn.setCellValueFactory(new PropertyValueFactory<TeamRow, String>("coach"));
		        cityColumn.setCellValueFactory(new PropertyValueFactory<TeamRow, String>("city"));
		        foundationDateColumn.setCellValueFactory(new PropertyValueFactory<TeamRow, String>("foundationDate"));
		        playersColumn.setCellValueFactory(new PropertyValueFactory<TeamRow, String>("players"));
		        matchesColumn.setCellValueFactory(new PropertyValueFactory<TeamRow, String>("matches"));
		        dataTeams.add(tR);
			}
			teamsTable.getItems().setAll(dataTeams);

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
			FXMLLoader loader = new FXMLLoader(SelectTeamToUpdateDeleteController.class.getResource("/resources/main.fxml"));
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
			Stage stage = (Stage) teamsTable.getScene().getWindow();
			stage.close();
			// Create screen
			Stage newStage = new Stage();
			FXMLLoader loader = new FXMLLoader(SelectTeamToUpdateDeleteController.class.getResource("/resources/adminInitialScreen.fxml"));
			Parent root = (Parent) loader.load();
			Scene scene = new Scene(root);
			AdminInitialScreenController iController = loader.getController();
	        iController.setUser(user);
	        newStage.setScene(scene);
			newStage.setTitle("Soccer Stats Filter");
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
	}

	/**
	 * update method to update a team
	 */
	public void update(){
		try {
			Stage stage = (Stage) teamsTable.getScene().getWindow();
			stage.close();
			Stage newStage = new Stage();
			FXMLLoader loader = new FXMLLoader(SelectTeamToUpdateDeleteController.class.getResource("/resources/updateTeam.fxml"));
			Parent root = (Parent) loader.load();
			Scene scene = new Scene(root);
			UpdateTeamController uTController = loader.getController();
	        uTController.setData(user, dataDAO.getTeamIdBySelection(selection));
	        newStage.setScene(scene);
			newStage.setTitle("Soccer Stats Update Team");
			newStage.show();
		} catch (IOException e){
			System.out.println(e.getMessage());
		}
	}

	/**
	 * delete method to delete a team
	 */
	public void delete(){
		if(dataDAO.deleteTeamById(dataDAO.getTeamIdBySelection(selection))){
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Information Message");
			alert.setHeaderText("Team Deleted Successfuly");
			alert.setContentText("The team has been erased from the database");
			alert.showAndWait();
			try {
				back();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error Message");
			alert.setHeaderText("The team has not been erased");
			alert.setContentText("There has been a problem with the database and the team has not been erased. Please try again later.");
			alert.showAndWait();
		}
	}

}
