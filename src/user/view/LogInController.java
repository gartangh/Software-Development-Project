package user.view;

import gui.AlertBox;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import main.Main;
import user.model.User;

public class LogInController {

	@FXML
	private TextField mUsername;
	@FXML
	private TextField mPassword;
	@FXML
	private Button mLogIn;
	@FXML
	private Button mCreateAccount;

	// Reference to the main application
	private Main main;

	public void setMainApp(Main main) {
		this.main = main;
	}

	@FXML
	private void initialize() {
		// Empty initialize
	}

	@FXML
	private void handleCreateAccount() {
		switch (User.createAccount(mUsername.getText(), mPassword.getText())) {
		case 0:
			main.showModeSelectorScene();
			break;
		case 1:
			AlertBox.display("Error", "Username is invalid!");
			break;
		case 2:
			AlertBox.display("Error", "Password is invalid!");
			break;
		case 3:
			AlertBox.display("Error", "Username is not unique!");
			break;
		default:
			AlertBox.display("Error", "Something went wrong!");
		}
	}
	
	@FXML
	private void handleLogIn() {
		switch (User.logIn(mUsername.getText(), mPassword.getText())) {
		case 0:
			main.showModeSelectorScene();
			break;
		case 1:
			AlertBox.display("Error", "The credentials are invalid");
			break;
		default:
			AlertBox.display("Error", "Something went wrong!");
		}
	}

}
