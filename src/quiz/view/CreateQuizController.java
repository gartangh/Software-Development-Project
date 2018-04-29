package quiz.view;

import eventbroker.Event;
import eventbroker.EventBroker;
import eventbroker.EventListener;
import eventbroker.EventPublisher;
import eventbroker.clientevent.ClientCreateQuizEvent;
import eventbroker.serverevent.ServerCreateQuizFailEvent;
import eventbroker.serverevent.ServerCreateQuizSuccesEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import main.Context;
import main.Main;
import main.view.AlertBox;
import quiz.model.Quiz;
import quiz.util.UserType;

public class CreateQuizController extends EventPublisher {

	@FXML
	private TextField mQuizname;
	@FXML
	private TextField mRounds;
	@FXML
	private TextField mTeams;
	@FXML
	private TextField mPlayers;

	private CreateQuizFailHandler createQuizFailHandler;
	private CreateQuizSuccesHandler createQuizSuccesHandler;

	// Reference to the main application
	private Main main;

	public void setMainApp(Main main) {
		this.main = main;
	}

	// Getter
	public CreateQuizSuccesHandler getCreateQuizFailHandler() {
		return createQuizSuccesHandler;
	}

	public CreateQuizSuccesHandler getCreateQuizSuccesHandler() {
		return createQuizSuccesHandler;
	}

	// Methods
	@FXML
	private void initialize() {
		createQuizFailHandler = new CreateQuizFailHandler();
		createQuizSuccesHandler = new CreateQuizSuccesHandler();

		EventBroker eventBroker = EventBroker.getEventBroker();
		eventBroker.addEventListener(ServerCreateQuizFailEvent.EVENTTYPE, createQuizFailHandler);
		eventBroker.addEventListener(ServerCreateQuizSuccesEvent.EVENTTYPE, createQuizSuccesHandler);
	}

	@FXML
	private void handleCreateQuiz() {
		String quizname = mQuizname.getText();
		int rounds = 0;
		int teams = 0;
		int players = 0;
		
		if (!quizname.matches(Quiz.QUIZNAMEREGEX)) {
			AlertBox.display("Error", "Quizname is invalid!");

			return;
		}
		
		try {
			rounds = Integer.parseInt(mRounds.getText());
			
			if (rounds < Quiz.MINROUNDS) {
				AlertBox.display("Error", "Minimum amount of rounds is " + Quiz.MINROUNDS + "!");

				return;
			}
			if (rounds > Quiz.MAXROUNDS) {
				AlertBox.display("Error", "Maximum amount of rounds is " + Quiz.MAXROUNDS + "!");

				return;
			}
		} catch (NumberFormatException e) {
			AlertBox.display("Error", "Amount of rounds must be a integer between " + Quiz.MINROUNDS + " and " + Quiz.MAXROUNDS + "!");
		}
		
		try {
			teams = Integer.parseInt(mTeams.getText());
			
			if (teams < Quiz.MINTEAMS) {
				AlertBox.display("Error", "Minimum amount of teams is " + Quiz.MINTEAMS + "!");

				return;
			}
			if (teams > Quiz.MAXTEAMS) {
				AlertBox.display("Error", "Maximum amount of teams is " + Quiz.MAXTEAMS + "!");

				return;
			}
		} catch (NumberFormatException e) {
			AlertBox.display("Error", "Amount of teams must be a integer between " + Quiz.MINTEAMS + " and " + Quiz.MAXTEAMS + "!");
		}
		
		try {
			players = Integer.parseInt(mPlayers.getText());
			
			if (players < Quiz.MINPLAYERS) {
				AlertBox.display("Error", "Minimum amount of players is " + Quiz.MINPLAYERS + "!");

				return;
			}
			if (players > Quiz.MAXPLAYERS) {
				AlertBox.display("Error", "Maximum amount of players is " + Quiz.MAXPLAYERS + "!");

				return;
			}
		} catch (NumberFormatException e) {
			AlertBox.display("Error", "Amount of players must be a integer between " + Quiz.MINPLAYERS + " and " + Quiz.MAXPLAYERS + "!");
		}

		// Everything is valid
		ClientCreateQuizEvent cCQE = new ClientCreateQuizEvent(quizname, teams, players, rounds,
				Context.getContext().getUser().getUsername());
		publishEvent(cCQE);
	}

	@FXML
	private void handleBack() {
		// User is now a USER
		Context.getContext().getUser().setUserType(UserType.USER);

		EventBroker eventBroker = EventBroker.getEventBroker();
		eventBroker.removeEventListener(createQuizFailHandler);
		eventBroker.removeEventListener(createQuizSuccesHandler);

		main.showJoinQuizScene();
	}

	// Inner classes
	private class CreateQuizFailHandler implements EventListener {

		@Override
		public void handleEvent(Event event) {
			@SuppressWarnings("unused")
			ServerCreateQuizFailEvent sCQSE = (ServerCreateQuizFailEvent) event;

			AlertBox.display("Error", "Create quiz failed!\nThe quizname already exists.");
		}

	}

	private class CreateQuizSuccesHandler implements EventListener {

		@Override
		public void handleEvent(Event event) {
			ServerCreateQuizSuccesEvent sCQE = (ServerCreateQuizSuccesEvent) event;

			int quizID = sCQE.getQuizID();
			String quizname = sCQE.getQuizname();
			int rounds = sCQE.getMaxAmountOfRounds();
			int teams = sCQE.getMaxAmountOfTeams();
			int players = sCQE.getMaxAmountOfPlayersPerTeam();
			int hostID = sCQE.getHostID();
			String hostname = sCQE.getHostname();

			Quiz.createQuiz(quizID, quizname, rounds, teams, players, hostID, hostname);

			EventBroker eventBroker = EventBroker.getEventBroker();
			eventBroker.removeEventListener(createQuizFailHandler);
			eventBroker.removeEventListener(createQuizSuccesHandler);

			main.showJoinTeamScene();
		}

	}

}
