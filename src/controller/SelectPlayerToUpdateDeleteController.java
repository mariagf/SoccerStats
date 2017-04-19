/**
*   <h1>SelectPlayerToUpdateDeleteController<h1>
*   SelectPlayerToUpdateDeleteController class that build the dashboard to select the player to update or delete
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

import application.Player;
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
import views.PlayerRow;

public class SelectPlayerToUpdateDeleteController implements Initializable {

	/**
	 * Private class variable
	 */
	private User user;
	private URL location;
	private ResourceBundle resources;
	private DataDAO dataDAO;
	private PlayerRow mRSelected;
	private List<String> selection;

	@FXML
	private TableView<PlayerRow> playersTable;

	@FXML
	private TableColumn<PlayerRow, Boolean> ticksColumn;

	@FXML
	private TableColumn<PlayerRow, String> nameColumn, lastnameColumn, dateOfBirthColumn, heightColumn, teamColumn;

	@FXML
	private Hyperlink signOutLink;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.location = location;
		this.resources = resources;
		// Check which player is selected
		playersTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
            	// Clear selection
                if(mRSelected != null){
                	mRSelected.setRegistered(false);
                }
                mRSelected = playersTable.getSelectionModel().getSelectedItem();
                mRSelected.setRegistered(!playersTable.getSelectionModel().getSelectedItem().isRegistered());
                // Add the information of the player selected into the list
                selection = new ArrayList<String>();
            	selection.add(playersTable.getItems().get(playersTable.getSelectionModel().getSelectedIndex()).getName());
            	selection.add(playersTable.getItems().get(playersTable.getSelectionModel().getSelectedIndex()).getLastname());
            	selection.add(playersTable.getItems().get(playersTable.getSelectionModel().getSelectedIndex()).getDateOfBirth());
            	selection.add(playersTable.getItems().get(playersTable.getSelectionModel().getSelectedIndex()).getHeight());
            	selection.add(playersTable.getItems().get(playersTable.getSelectionModel().getSelectedIndex()).getTeam());
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
			List<PlayerRow> dataPlayers = new ArrayList<PlayerRow>();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			while (rs.next()) {
				Player p = new Player(rs.getString(3), rs.getString(4), rs.getDate(5),rs.getInt(6));
				String teamName = dataDAO.getTeamNameById(rs.getInt(2));
				PlayerRow pR = new PlayerRow(p.getName(), p.getLastname(), sdf.format(p.getDateOfBirth()) , Integer.toString(p.getHeight()), teamName ,false);
				ticksColumn.setCellFactory(CheckBoxTableCell.forTableColumn(ticksColumn) );
				ticksColumn.setCellValueFactory(param -> {
					if(param != null){
						return param.getValue().registeredProperty();
					} else {
						return null;
					}
			    });
				nameColumn.setCellValueFactory(new PropertyValueFactory<PlayerRow, String>("name"));
		        lastnameColumn.setCellValueFactory(new PropertyValueFactory<PlayerRow, String>("lastname"));
		        dateOfBirthColumn.setCellValueFactory(new PropertyValueFactory<PlayerRow, String>("dateOfBirth"));
		        heightColumn.setCellValueFactory(new PropertyValueFactory<PlayerRow, String>("height"));
		        teamColumn.setCellValueFactory(new PropertyValueFactory<PlayerRow, String>("team"));
		        dataPlayers.add(pR);
			}
			playersTable.getItems().setAll(dataPlayers);
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
			FXMLLoader loader = new FXMLLoader(SelectPlayerToUpdateDeleteController.class.getResource("/resources/main.fxml"));
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
			Stage stage = (Stage) playersTable.getScene().getWindow();
			stage.close();
			// Create screen
			Stage newStage = new Stage();
			FXMLLoader loader = new FXMLLoader(SelectPlayerToUpdateDeleteController.class.getResource("/resources/adminInitialScreen.fxml"));
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
	 * update method to update a player
	 */
	public void update(){
		try {
			Stage stage = (Stage) playersTable.getScene().getWindow();
			stage.close();
			Stage newStage = new Stage();
			FXMLLoader loader = new FXMLLoader(SelectPlayerToUpdateDeleteController.class.getResource("/resources/updatePlayer.fxml"));
			Parent root = (Parent) loader.load();
			Scene scene = new Scene(root);
	        UpdatePlayerController uMController = loader.getController();
	        uMController.setData(user, dataDAO.getPlayerIdBySelection(selection));
	        newStage.setScene(scene);
			newStage.setTitle("Soccer Stats Update Player");
			newStage.show();
		} catch (IOException e){
			System.out.println(e.getMessage());
		}
	}

	/**
	 * delete method to delete a player
	 */
	public void delete(){
		if(dataDAO.deletePlayerById(dataDAO.getPlayerIdBySelection(selection))){
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Information Message");
			alert.setHeaderText("Player Deleted Successfuly");
			alert.setContentText("The player has been erased from the database");
			alert.showAndWait();
			try {
				back();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error Message");
			alert.setHeaderText("The player has not been erased");
			alert.setContentText("There has been a problem with the database and the player has not been erased. Please try again later.");
			alert.showAndWait();
		}
	}

}
