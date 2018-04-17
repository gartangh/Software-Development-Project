package user.view;

import eventbroker.EventPublisher;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import main.Context;
import main.Main;
import main.view.AlertBox;
import quiz.util.ClientCreateAccountEvent;
import user.model.User;

public class LogInController extends EventPublisher {

	@FXML
	private TextField mUsername;
	@FXML
	private PasswordField mPassword;
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

		User user = new User(0,username, password);
		Context.getContext().setUser(user);
		ClientCreateAccountEvent cCAE = new ClientCreateAccountEvent(user.getUsername(), user.getPassword());
		publishEvent(cCAE);

		main.showModeSelectorScene();
		
		/*switch (User.createAccount(username, password)) {
		case 0:
			User user = Context.getContext().getUser();
			ClientCreateAccountEvent cCAE = new ClientCreateAccountEvent(user);
			publishEvent(cCAE);

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
		}*/
	}

	@FXML
	private void handleLogIn() {
		String username = mUsername.getText();
		String password = mPassword.getText();

		/*
		 * switch (User.logIn(username, password)) { case 0: // TODO: Handle log
		 * in main.showModeSelectorScene(); break; case 1:
		 * AlertBox.display("Error", "The credentials are invalid"); break;
		 * default: AlertBox.display("Error", "Something went wrong!"); }
		 */
	}

}
