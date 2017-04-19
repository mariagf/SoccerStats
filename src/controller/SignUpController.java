/**
*   <h1>SignUpController<h1>
*   SignUpController class that allows us to signUp
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

public class SignUpController implements Initializable  {

	/**
	 * Private class variable
	 */
	private String name, lastname, email, username, password;
	private int isAdmin;

	@FXML
	private TextField nameText;

	@FXML
	private TextField lastnameText;

	@FXML
	private TextField emailText;

	@FXML
	private TextField usernameText;

	@FXML
	private PasswordField passwordText;

	@FXML
	private Button signup;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
	}

	@FXML
	/**
	 * back method to come one screen back
	 * @throws IOException
	 */
	private void back() throws IOException{
		// Close screen
		Stage stage = (Stage) nameText.getScene().getWindow();
		stage.close();
		// Create screen
		Stage newStage = new Stage();
		FXMLLoader loader = new FXMLLoader(SignUpController.class.getResource("/resources/main.fxml"));
		newStage.setScene(new Scene(loader.load()));
		newStage.setTitle("Soccer Stats Login");
		newStage.show();
	}

	@FXML
	/**
	 * signUP method
	 * @throws IOException
	 * @throws SQLException
	 */
	private void signUp() throws IOException, SQLException {
		name = nameText.getText();
		lastname = lastnameText.getText();
		email = emailText.getText();
		username = usernameText.getText();
		password = passwordText.getText();
		isAdmin = 0;
		// Alert if missing fields
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error");
		if (name.isEmpty() || lastname.isEmpty() || email.isEmpty() || username.isEmpty() || password.isEmpty()){
			alert.setHeaderText("Missing Field Error");
			alert.setContentText("Please fill all the fields before submiting");
			alert.showAndWait();
		} else {
			// Connect to the database
			DataDAO dataDAO = new DataDAO();
			// Create a user with all the info obtained from the username and password
			User user = new User (username, password, name, lastname, email, isAdmin);
			dataDAO.insertUser(user);
			// Close screen
			Stage stage = (Stage) nameText.getScene().getWindow();
			stage.close();
			// Create screen
			Stage newStage = new Stage();
			FXMLLoader loader = new FXMLLoader(SignUpController.class.getResource("/resources/main.fxml"));
			Parent root = (Parent) loader.load();
			Scene scene = new Scene(root);
			MainController login = loader.getController();
			login.fillData(user);
			newStage.setScene(scene);
			newStage.setTitle("Soccer Stats Login");
			newStage.show();
		}
	}
}
