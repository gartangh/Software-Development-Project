package quiz.view;

import eventbroker.Event;
import eventbroker.EventBroker;
import eventbroker.EventListener;
import eventbroker.EventPublisher;
import eventbroker.clientevent.ClientCreateAccountEvent;
import eventbroker.clientevent.ClientLogInEvent;
import eventbroker.serverevent.ServerAlreadyLoggedInEvent;
import eventbroker.serverevent.ServerCreateAccountFailEvent;
import eventbroker.serverevent.ServerCreateAccountSuccesEvent;
import eventbroker.serverevent.ServerLogInFailEvent;
import eventbroker.serverevent.ServerLogInSuccesEvent;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.media.MediaView;
import main.Main;
import quiz.model.User;

public class LogInController extends EventPublisher {

	@FXML
	private TextField mUsername;
	@FXML
	private PasswordField mPassword;
	@FXML
	private Button mLogIn;
	@FXML
	private Button mCreateAccount;
	@FXML
	private MediaView mediaView;
	
	private CreateAccountFailHandler createAccountFailHandler = new CreateAccountFailHandler();
	private CreateAccountSuccesHandler createAccountSuccesHandler = new CreateAccountSuccesHandler();
	private LogInFailHandler logInFailHandler = new LogInFailHandler();
	private AlreadyLoggedInHandler alreadyLoggedInHandler = new AlreadyLoggedInHandler();
	private LogInSuccesHandler logInSuccesHandler = new LogInSuccesHandler();
	
	// Reference to the main application
	private Main main;

	public void setMainApp(Main main) {
		this.main = main;
	}

	// Methods
	@FXML
	private void initialize() {
		EventBroker eventBroker = EventBroker.getEventBroker();
		eventBroker.addEventListener(ServerCreateAccountFailEvent.EVENTTYPE, createAccountFailHandler);
		eventBroker.addEventListener(ServerCreateAccountSuccesEvent.EVENTTYPE, createAccountSuccesHandler);
		eventBroker.addEventListener(ServerLogInFailEvent.EVENTTYPE, logInFailHandler);
		eventBroker.addEventListener(ServerAlreadyLoggedInEvent.EVENTTYPE, alreadyLoggedInHandler);
		eventBroker.addEventListener(ServerLogInSuccesEvent.EVENTTYPE, logInSuccesHandler);		
	}

	@FXML
	private void handleCreateAccount() {
		String username = mUsername.getText();
		if (username == null || !username.matches(User.USERNAMEREGEX)) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("Warning");
					alert.setHeaderText("Username is invalid!");
					alert.setContentText("Try again with a valid username.");
					alert.showAndWait();
				}
			});

			return;
		}

		String password = mPassword.getText();
		if (password == null || !password.matches(User.PASSWORDREGEX)) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("Warning");
					alert.setHeaderText("Password is invalid!");
					alert.setContentText("Try again with a valid password.");
					alert.showAndWait();
				}
			});

			return;
		}

		// Everything is valid
		ClientCreateAccountEvent cCAE = new ClientCreateAccountEvent(username, password);
		publishEvent(cCAE);
	}

	@FXML
	private void handleLogIn() {
		String username = mUsername.getText();
		if (username == null || !username.matches(User.USERNAMEREGEX)) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("Warning");
					alert.setHeaderText("Username is invalid!");
					alert.setContentText("Try again with a valid username.");
					alert.showAndWait();
				}
			});

			return;
		}

		String password = mPassword.getText();
		if (password == null || !password.matches(User.PASSWORDREGEX)) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("Warning");
					alert.setHeaderText("Password is invalid!");
					alert.setContentText("Try again with a valid password.");
					alert.showAndWait();
				}
			});

			return;
		}

		// Everything is valid
		ClientLogInEvent cLIE = new ClientLogInEvent(username, password);
		publishEvent(cLIE);
	}

	// Inner classes
	private class CreateAccountFailHandler implements EventListener {

		@Override
		public void handleEvent(Event event) {
			@SuppressWarnings("unused")
			ServerCreateAccountFailEvent sCAFE = (ServerCreateAccountFailEvent) event;
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Error");
					alert.setHeaderText("Account creation failed!");
					alert.setContentText("The username already exists.");
					alert.showAndWait();
				}
			});
		}

	}

	private class CreateAccountSuccesHandler implements EventListener {

		@Override
		public void handleEvent(Event event) {
			ServerCreateAccountSuccesEvent sCASE = (ServerCreateAccountSuccesEvent) event;

			int userID = sCASE.getUserID();
			String username = sCASE.getUsername();
			String password = sCASE.getPassword();

			User.createAccount(userID, username, password);

			EventBroker eventBroker = EventBroker.getEventBroker();
			eventBroker.removeEventListener(createAccountFailHandler);
			eventBroker.removeEventListener(createAccountSuccesHandler);
			eventBroker.removeEventListener(logInFailHandler);
			eventBroker.removeEventListener(alreadyLoggedInHandler);
			eventBroker.removeEventListener(logInSuccesHandler);

			main.showJoinQuizScene();
		}

	}

	private class LogInFailHandler implements EventListener {

		@Override
		public void handleEvent(Event event) {
			@SuppressWarnings("unused")
			ServerLogInFailEvent sLIFE = (ServerLogInFailEvent) event;

			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Error");
					alert.setHeaderText("Logging in failed!");
					alert.setContentText("The combination of this username and password does not exist.");
					alert.showAndWait();
				}
			});
		}

	}
	
	private class AlreadyLoggedInHandler implements EventListener {

		@Override
		public void handleEvent(Event event) {
			@SuppressWarnings("unused")
			ServerAlreadyLoggedInEvent sALIE = (ServerAlreadyLoggedInEvent) event;
			
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Error");
					alert.setHeaderText("Logging in failed!");
					alert.setContentText("The user with this username is already logged in.");
					alert.showAndWait();
				}
			});
		}
		
	}

	private class LogInSuccesHandler implements EventListener {

		@Override
		public void handleEvent(Event event) {
			ServerLogInSuccesEvent sLISE = (ServerLogInSuccesEvent) event;

			int userID = sLISE.getUserID();
			String username = sLISE.getUsername();
			String password = sLISE.getPassword();
			int level = sLISE.getLevel();
			long xp = sLISE.getXp();

			User.logIn(userID, username, password, level, xp);

			EventBroker eventBroker = EventBroker.getEventBroker();
			eventBroker.removeEventListener(createAccountFailHandler);
			eventBroker.removeEventListener(createAccountSuccesHandler);
			eventBroker.removeEventListener(logInFailHandler);
			eventBroker.removeEventListener(alreadyLoggedInHandler);
			eventBroker.removeEventListener(logInSuccesHandler);

			main.showJoinQuizScene();
		}

	}

}
