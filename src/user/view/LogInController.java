package user.view;

import eventbroker.EventPublisher;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import main.Context;
import main.Main;
import main.view.AlertBox;
import quiz.util.ClientCreateEvent;
import user.model.User;

public class LogInController extends EventPublisher {

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
		String username = mUsername.getText();
		String password = mPassword.getText();

		switch (User.createAccount(username, password)) {
		case 0:
			// TODO: Add User to database
			User user = Context.getContext().getUser();
			ClientCreateEvent cCE = new ClientCreateEvent(user);
			// TODO: Publish event
			
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
		String username = mUsername.getText();
		String password = mPassword.getText();

		switch (User.logIn(username, password)) {
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
