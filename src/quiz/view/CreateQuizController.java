package quiz.view;

import eventbroker.Event;
import eventbroker.EventBroker;
import eventbroker.EventListener;
import eventbroker.EventPublisher;
import eventbroker.clientevent.ClientCreateQuizEvent;
import eventbroker.serverevent.ServerCreateQuizFailEvent;
import eventbroker.serverevent.ServerCreateQuizSuccesEvent;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import main.Context;
import main.Main;
import quiz.model.Quiz;

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
		if (quizname == null || !quizname.matches(Quiz.QUIZNAMEREGEX)) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("Warning");
					alert.setHeaderText("Quizname is invalid!");
					alert.setContentText("Try again with a valid quizname.");
					alert.showAndWait();
				}
			});

			return;
		}

		int rounds = 0;
		try {
			rounds = Integer.parseInt(mRounds.getText());
			if (rounds < Quiz.MINROUNDS) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						Alert alert = new Alert(AlertType.WARNING);
						alert.setTitle("Warning");
						alert.setHeaderText("Amount of rounds is invalid!");
						alert.setContentText("Minimum amount of rounds is " + Quiz.MINROUNDS + ".");
						alert.showAndWait();
					}
				});

				return;
			} else if (rounds > Quiz.MAXROUNDS) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						Alert alert = new Alert(AlertType.WARNING);
						alert.setTitle("Warning");
						alert.setHeaderText("Amount of rounds is invalid!");
						alert.setContentText("Maximum amount of rounds is " + Quiz.MAXROUNDS + ".");
						alert.showAndWait();
					}
				});

				return;
			}
		} catch (NumberFormatException e) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("Warning");
					alert.setHeaderText("Amount of rounds is invalid!");
					alert.setContentText("Amount of rounds must be a integer between " + Quiz.MINROUNDS + " and "
							+ Quiz.MAXROUNDS + ".");
					alert.showAndWait();
				}
			});

			return;
		}

		int teams = 0;
		try {
			teams = Integer.parseInt(mTeams.getText());
			if (teams < Quiz.MINTEAMS) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						Alert alert = new Alert(AlertType.WARNING);
						alert.setTitle("Warning");
						alert.setHeaderText("Amount of teams is invalid!");
						alert.setContentText("Minimum amount of teams is " + Quiz.MINTEAMS + ".");
						alert.showAndWait();
					}
				});

				return;
			} else if (teams > Quiz.MAXTEAMS) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						Alert alert = new Alert(AlertType.WARNING);
						alert.setTitle("Warning");
						alert.setHeaderText("Amount of teams is invalid!");
						alert.setContentText("Maximum amount of teams is " + Quiz.MAXTEAMS + ".");
						alert.showAndWait();
					}
				});

				return;
			}
		} catch (NumberFormatException e) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("Warning");
					alert.setHeaderText("Amount of teams is invalid!");
					alert.setContentText("Amount of teams must be a integer between " + Quiz.MINTEAMS + " and "
							+ Quiz.MAXTEAMS + ".");
					alert.showAndWait();
				}
			});

			return;
		}

		int players = 0;
		try {
			players = Integer.parseInt(mPlayers.getText());
			if (players < Quiz.MINPLAYERS) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						Alert alert = new Alert(AlertType.WARNING);
						alert.setTitle("Warning");
						alert.setHeaderText("Amount of players is invalid!");
						alert.setContentText("Minimum amount of players is " + Quiz.MINPLAYERS + ".");
						alert.showAndWait();
					}
				});

				return;
			} else if (players > Quiz.MAXPLAYERS) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						Alert alert = new Alert(AlertType.WARNING);
						alert.setTitle("Warning");
						alert.setHeaderText("Amount of players is invalid!");
						alert.setContentText("Maximum amount of players is " + Quiz.MAXPLAYERS + ".");
						alert.showAndWait();
					}
				});

				return;
			}
		} catch (NumberFormatException e) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("Warning");
					alert.setHeaderText("Amount of players is invalid!");
					alert.setContentText("Amount of players must be a integer between " + Quiz.MINPLAYERS + " and "
							+ Quiz.MAXPLAYERS + ".");
					alert.showAndWait();
				}
			});

			return;
		}

		// Everything is valid
		ClientCreateQuizEvent cCQE = new ClientCreateQuizEvent(quizname, teams, players, rounds,
				Context.getContext().getUser().getUsername());
		publishEvent(cCQE);
	}

	@FXML
	private void handleBack() {
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

			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Error");
					alert.setHeaderText("Quiz creation failed!");
					alert.setContentText("The quizname already exists.");
					alert.showAndWait();
				}
			});
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
