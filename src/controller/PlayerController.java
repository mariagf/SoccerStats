/**
*   <h1>PlayerController<h1>
*   PlayerController class that allows us to control players
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

import application.Player;
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
import views.PlayerRow;

public class PlayerController implements Initializable{

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
	private TableView<PlayerRow> playerTable;

	@FXML
	private TableColumn<PlayerRow, String> nameColumn, lastnameColumn, dateOfBirthColumn, heightColumn, teamColumn;

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
					loader = new FXMLLoader(PlayerController.class.getResource("/resources/adminInitialScreen.fxml"));
					Parent root = (Parent) loader.load();
					Scene scene = new Scene(root);
			        AdminInitialScreenController iController = loader.getController();
			        iController.setUser(user);
			        newStage.setScene(scene);
					newStage.setTitle("Soccer Stats Admin Dashboard");
					newStage.show();
				} else {
					loader = new FXMLLoader(PlayerController.class.getResource("/resources/initialScreen.fxml"));
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
			FXMLLoader loader = new FXMLLoader(PlayerController.class.getResource("/resources/main.fxml"));
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
	public void loadData(ResultSet rs) {
		try {
			dataDAO = new DataDAO();
			List<PlayerRow> dataPlayers = new ArrayList<PlayerRow>();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			while (rs.next()) {
				String teamName = dataDAO.getTeamNameById(rs.getInt(2));
				Player p = new Player(rs.getString(3), rs.getString(4), rs.getDate(5), rs.getInt(6));
		        PlayerRow pR = new PlayerRow(p.getName(), p.getLastname(), sdf.format(p.getDateOfBirth()), Integer.toString(p.getHeight()), teamName, false);
		        nameColumn.setCellValueFactory(new PropertyValueFactory<PlayerRow, String>("name"));
		        lastnameColumn.setCellValueFactory(new PropertyValueFactory<PlayerRow, String>("lastname"));
		        dateOfBirthColumn.setCellValueFactory(new PropertyValueFactory<PlayerRow, String>("dateOfBirth"));
		        heightColumn.setCellValueFactory(new PropertyValueFactory<PlayerRow, String>("height"));
		        teamColumn.setCellValueFactory(new PropertyValueFactory<PlayerRow, String>("team"));
		        dataPlayers.add(pR);
			}
			playerTable.getItems().setAll(dataPlayers);
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
