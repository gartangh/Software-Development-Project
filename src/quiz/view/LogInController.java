package quiz.view;

import eventbroker.Event;
import eventbroker.EventBroker;
import eventbroker.EventListener;
import eventbroker.EventPublisher;
import eventbroker.clientevent.ClientCreateAccountEvent;
import eventbroker.clientevent.ClientLogInEvent;
import eventbroker.serverevent.ServerCreateAccountFailEvent;
import eventbroker.serverevent.ServerCreateAccountSuccesEvent;
import eventbroker.serverevent.ServerLogInFailEvent;
import eventbroker.serverevent.ServerLogInSuccesEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import main.Main;
import main.view.AlertBox;
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

	private CreateAccountFailHandler createAccountFailHandler;
	private CreateAccountSuccesHandler createAccountSuccesHandler;
	private LogInFailHandler logInFailHandler;
	private LogInSuccesHandler logInSuccesHandler;

	// Reference to the main application
	private Main main;

	public void setMainApp(Main main) {
		this.main = main;
	}

	// Getters
	public CreateAccountFailHandler getCreateAccountFailHandler() {
		return createAccountFailHandler;
	}

	public CreateAccountSuccesHandler getCreateAccountSuccesHandler() {
		return createAccountSuccesHandler;
	}

	public LogInFailHandler getLogInFailHandler() {
		return logInFailHandler;
	}

	public LogInSuccesHandler getLogInSuccesHandler() {
		return logInSuccesHandler;
	}

	// Methods
	@FXML
	private void initialize() {
		createAccountFailHandler = new CreateAccountFailHandler();
		createAccountSuccesHandler = new CreateAccountSuccesHandler();
		logInFailHandler = new LogInFailHandler();
		logInSuccesHandler = new LogInSuccesHandler();

		EventBroker eventBroker = EventBroker.getEventBroker();
		eventBroker.addEventListener(ServerCreateAccountFailEvent.EVENTTYPE, createAccountFailHandler);
		eventBroker.addEventListener(ServerCreateAccountSuccesEvent.EVENTTYPE, createAccountSuccesHandler);
		eventBroker.addEventListener(ServerLogInFailEvent.EVENTTYPE, logInFailHandler);
		eventBroker.addEventListener(ServerLogInSuccesEvent.EVENTTYPE, logInSuccesHandler);
	}

	@FXML
	private void handleCreateAccount() {
		String username = mUsername.getText();
		String password = mPassword.getText();

		if (!username.matches(User.USERNAMEREGEX)) {
			AlertBox.display("Error", "Username is invalid!");

			return;
		}
		if (!password.matches(User.PASSWORDREGEX)) {
			AlertBox.display("Error", "Password is invalid!");

			return;
		}

		// Everything is valid
		ClientCreateAccountEvent cCAE = new ClientCreateAccountEvent(username, password);
		publishEvent(cCAE);
	}

	@FXML
	private void handleLogIn() {
		String username = mUsername.getText();
		String password = mPassword.getText();

		if (!username.matches(User.USERNAMEREGEX)) {
			AlertBox.display("Error", "Username is invalid!");

			return;
		}
		if (!password.matches(User.PASSWORDREGEX)) {
			AlertBox.display("Error", "Password is invalid!");

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

			AlertBox.display("Error", "Create account failed!\nThe username already exists.");
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
			eventBroker.removeEventListener(logInSuccesHandler);

			main.showJoinQuizScene();
		}

	}

	private class LogInFailHandler implements EventListener {

		@Override
		public void handleEvent(Event event) {
			@SuppressWarnings("unused")
			ServerLogInFailEvent sLIFE = (ServerLogInFailEvent) event;

			AlertBox.display("Error", "Log in failed!\nThe combination of this username and password does not exist.");
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
			eventBroker.removeEventListener(logInSuccesHandler);

			main.showJoinQuizScene();
		}

	}

}
