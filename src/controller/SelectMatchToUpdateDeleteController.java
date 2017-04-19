/**
*   <h1>SelectMatchToUpdateDeleteController<h1>
*   SelectMatchToUpdateDeleteController class that build the dashboard to select the match to update or delete
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
import views.MatchRow;

public class SelectMatchToUpdateDeleteController implements Initializable {

	/**
	 * Private class variables
	 */
	private User user;
	private URL location;
	private ResourceBundle resources;
	private DataDAO dataDAO;
	private MatchRow mRSelected;
	private List<String> selection;

	@FXML
	private TableView<MatchRow> matchesTable;

	@FXML
	private TableColumn<MatchRow, Boolean> ticksColumn;

	@FXML
	private TableColumn<MatchRow, String> localTeamColumn, visitorTeamColumn, localGoalsColumn, visitorGoalsColumn, cornersColumn, foulsColumn, dateColumn, refereeColumn;

	@FXML
	private Hyperlink signOutLink;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.location = location;
		this.resources = resources;
		// Check which match is selected
		matchesTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                // Clear selection
                if(mRSelected != null){
                	mRSelected.setRegistered(false);
                }
                mRSelected = matchesTable.getSelectionModel().getSelectedItem();
                mRSelected.setRegistered(!matchesTable.getSelectionModel().getSelectedItem().isRegistered());
                // Add the information of the match selected into the list
                selection = new ArrayList<String>();
            	selection.add(matchesTable.getItems().get(matchesTable.getSelectionModel().getSelectedIndex()).getLocalTeam());
            	selection.add(matchesTable.getItems().get(matchesTable.getSelectionModel().getSelectedIndex()).getVisitorTeam());
            	selection.add(matchesTable.getItems().get(matchesTable.getSelectionModel().getSelectedIndex()).getDate());
            	selection.add(matchesTable.getItems().get(matchesTable.getSelectionModel().getSelectedIndex()).getReferee());
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
			List<MatchRow> dataMatches = new ArrayList<MatchRow>();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			while (rs.next()) {
				Team teamHome = dataDAO.getTeamById(rs.getInt(2), rs);
				Team teamAway = dataDAO.getTeamById(rs.getInt(3), rs);
				Stats stats = dataDAO.getStatsByMatchId(rs.getInt(1));
				Match m = new Match(teamHome, teamAway, stats, rs.getDate(4),rs.getString(5));
				MatchRow mR = new MatchRow(rs.getInt(1), teamHome.getName(), teamAway.getName(), Integer.toString(stats.getGoalsHome()), Integer.toString(stats.getGoalsAway()), Integer.toString(stats.getNumberOfCorners()), Integer.toString(stats.getNumberOfFouls()), sdf.format(m.getDate()), m.getReferee(),false);
				ticksColumn.setCellFactory(CheckBoxTableCell.forTableColumn(ticksColumn) );
				ticksColumn.setCellValueFactory(param -> {
					if(param != null){
						return param.getValue().registeredProperty();
					} else {
						return null;
					}
			    });
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
			FXMLLoader loader = new FXMLLoader(SelectMatchToUpdateDeleteController.class.getResource("/resources/main.fxml"));
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
			Stage stage = (Stage) matchesTable.getScene().getWindow();
			stage.close();
			// Create screen
			Stage newStage = new Stage();
			FXMLLoader loader = new FXMLLoader(SelectMatchToUpdateDeleteController.class.getResource("/resources/adminInitialScreen.fxml"));
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
	 * update method to update a match
	 */
	public void update(){
		try {
			Stage stage = (Stage) matchesTable.getScene().getWindow();
			stage.close();
			Stage newStage = new Stage();
			FXMLLoader loader = new FXMLLoader(SelectMatchToUpdateDeleteController.class.getResource("/resources/updateMatch.fxml"));
			Parent root = (Parent) loader.load();
			Scene scene = new Scene(root);
	        UpdateMatchController uMController = loader.getController();
	        uMController.setData(user, dataDAO.getMatchIdBySelection(selection));
	        newStage.setScene(scene);
			newStage.setTitle("Soccer Stats Update Match");
			newStage.show();
		} catch (IOException e){
			System.out.println(e.getMessage());
		}
	}

	/**
	 * delete method to delete a match
	 */
	public void delete(){
		int matchId = dataDAO.getMatchIdBySelection(selection);
		if(dataDAO.deleteMatchById(matchId)){
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Information Message");
			alert.setHeaderText("Match Deleted Successfuly");
			alert.setContentText("The match has been erased from the database");
			alert.showAndWait();
			try {
				back();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error Message");
			alert.setHeaderText("The match has not been erased");
			alert.setContentText("There has been a problem with the database and the match has not been erased. Please try again later.");
			alert.showAndWait();
		}
	}
}
