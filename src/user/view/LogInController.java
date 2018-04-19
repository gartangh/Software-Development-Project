package user.view;

import eventbroker.Event;
import eventbroker.EventBroker;
import eventbroker.EventListener;
import eventbroker.EventPublisher;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import main.Context;
import main.Main;
import quiz.util.ClientCreateAccountEvent;
import server.ServerReturnUserIDEvent;
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

	private LogInEventHandler logInEventHandler;

	// Reference to the main application
	private Main main;

	public void setMainApp(Main main) {
		this.main = main;
	}
	
	public LogInController() {
		this.logInEventHandler = new LogInEventHandler();
	}

	// Getters
	public LogInEventHandler getLogInEventHandler() {
		return logInEventHandler;
	}

	public LogInEventHandler getChatModel() {
		return logInEventHandler;
	}

	@FXML
	private void initialize() {
		EventBroker.getEventBroker().addEventListener(logInEventHandler);
	}

	@FXML
	private void handleCreateAccount() {
		String username = mUsername.getText();
		String password = mPassword.getText();

		User user = new User(0, username, password);
		Context.getContext().setUser(user);
		ClientCreateAccountEvent cCAE = new ClientCreateAccountEvent(user.getUsername(), user.getPassword());
		publishEvent(cCAE);

		main.showJoinQuizScene();

		/*
		 * switch (User.createAccount(username, password)) { case 0: User user =
		 * Context.getContext().getUser(); ClientCreateAccountEvent cCAE = new
		 * ClientCreateAccountEvent(user); publishEvent(cCAE);
		 * 
		 * main.showJoinQuizScene(); break; case 1:
		 * AlertBox.display("Error", "Username is invalid!"); break; case 2:
		 * AlertBox.display("Error", "Password is invalid!"); break; case 3:
		 * AlertBox.display("Error", "Username is not unique!"); break; default:
		 * AlertBox.display("Error", "Something went wrong!"); }
		 */
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

	// Inner class
	private class LogInEventHandler implements EventListener {

		@Override
		public void handleEvent(Event event) {
			String type = event.getType();
			switch (type) {
			case "SERVER_RETURN_USERID":
				ServerReturnUserIDEvent serverCreate = (ServerReturnUserIDEvent) event;
				User user = Context.getContext().getUser();
				user.setUserID(serverCreate.getUserID());
				Context.getContext().setUser(user);
				Context.getContext().getNetwork().getUserIDConnectionIDMap().put(serverCreate.getUserID(), 0);
				System.out.println("Event received and handled: " + type);
				break;

			default:
				System.out.println("Event received but left unhandled: " + type);
			}
		}

	}

}
