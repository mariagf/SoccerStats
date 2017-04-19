/**
*   <h1>MainController<h1>
*   MainController class that builds the main screen dashboard
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
import java.sql.SQLException;
import java.util.ResourceBundle;

import application.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import models.DataDAO;

public class MainController implements Initializable  {

	/**
	 * Private class variables
	 */
	private String username, password;
	private DataDAO dataDAO;

	@FXML
	private Button loginBtn;

	@FXML
	private Button signupBtn;

	@FXML
	private TextField usernameText;

	@FXML
	private PasswordField passwordText;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		dataDAO = new DataDAO();
		dataDAO.generateDB();

		loginBtn.setOnAction((event) -> {
			try {
				logIn();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});

		signupBtn.setOnAction((event) -> {
			try {
				signUp();
			} catch (IOException e) {
				e.printStackTrace();
			}
		});

	}

	@FXML
	/**
	 * logIn method
	 * @throws IOException
	 * @throws SQLException
	 */
	private void logIn() throws IOException, SQLException{
		username = usernameText.getText();
		password = passwordText.getText();
		// Alert if missing fields
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error");
		if (username.isEmpty() || password.isEmpty()){
			alert.setHeaderText("Missing Field Error");
			alert.setContentText("Please fill all the fields");
			alert.showAndWait();
		} else {
			if (!dataDAO.dbLogin(username, password)){
				alert.setHeaderText("Login Error");
				alert.setContentText("Wrong username and password");
				alert.showAndWait();
			} else {
				if(username.equals("admin")){
					// Close screen
					Stage stage = (Stage) usernameText.getScene().getWindow();
				    stage.close();
				    // Create new screen
				    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/adminInitialScreen.fxml"));
				    Parent root = (Parent)fxmlLoader.load();
				    AdminInitialScreenController adminController = fxmlLoader.<AdminInitialScreenController>getController();
				    dataDAO = new DataDAO();
				    adminController.setUser(dataDAO.getUser(username, password));
				    Scene scene = new Scene(root);
				    Stage  stage2 = new Stage();
				    stage2.setScene(scene);
					stage2.setTitle("Soccer Stats Admin Dashboard");
				    stage2.show();
				} else {
					// Close screen
					Stage stage = (Stage) usernameText.getScene().getWindow();
				    stage.close();
				    // CReate new screen
				    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/initialScreen.fxml"));
				    Parent root = (Parent)fxmlLoader.load();
				    InitialScreenController controller = fxmlLoader.<InitialScreenController>getController();
				    dataDAO = new DataDAO();
				    controller.setUser(dataDAO.getUser(username, password));
				    Scene scene = new Scene(root);
				    Stage  stage2 = new Stage();
				    stage2.setScene(scene);
					stage2.setTitle("Soccer Stats Filter");
				    stage2.show();
				}
			}
		}
	}


	@FXML
	/**
	 * signUp method
	 * @throws IOException
	 */
	private void signUp() throws IOException{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(MainController.class.getResource("/resources/signup.fxml"));
		// Close screen
		Stage stage = (Stage) usernameText.getScene().getWindow();
	    stage.close();
	    // Create screen
		Stage signUpState = new Stage();
		signUpState.setScene(new Scene(loader.load()));
		signUpState.setTitle("Soccer Stats Sign Up");
		signUpState.show();
	}

	/**
	 * fillData method to fill the text fields of the next screen with the data
	 * @param user
	 */
	public void fillData(User user) {
		usernameText.setText(user.getUsername());
		passwordText.setText(user.getPassword());
	}

}
