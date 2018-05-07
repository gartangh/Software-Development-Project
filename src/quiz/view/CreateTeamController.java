package quiz.view;

import eventbroker.Event;
import eventbroker.EventBroker;
import eventbroker.EventListener;
import eventbroker.EventPublisher;
import eventbroker.clientevent.ClientCreateTeamEvent;
import eventbroker.serverevent.ServerCreateTeamFailEvent;
import eventbroker.serverevent.ServerCreateTeamSuccesEvent;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import main.Main;
import main.MainContext;
import quiz.model.Team;

public class CreateTeamController extends EventPublisher {

	@FXML
	private TextField mTeamname;
	@FXML
	private ColorPicker mColor;
	
	private CreateTeamFailHandler createTeamFailHandler = new CreateTeamFailHandler();
	private CreateTeamSuccesHandler createTeamSuccesHandler = new CreateTeamSuccesHandler();

	// Reference to the main application
	private Main main;

	public void setMain(Main main) {
		this.main = main;
	}

	// Methods
	@FXML
	private void initialize() {
		EventBroker eventBroker = EventBroker.getEventBroker();
		eventBroker.addEventListener(ServerCreateTeamFailEvent.EVENTTYPE, createTeamFailHandler);
		eventBroker.addEventListener(ServerCreateTeamSuccesEvent.EVENTTYPE, createTeamSuccesHandler);
	}

	@FXML
	private void handleCreateTeam() {
		String teamname = mTeamname.getText();
		if (teamname == null || !teamname.matches(Team.TEAMNAMEREGEX)) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("Warning");
					alert.setHeaderText("Teamname is invalid!");
					alert.setContentText("Try again with a valid teamname.");
					alert.showAndWait();
				}
			});

			return;
		}

		Color color = mColor.getValue();
		if (color == null) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("Warning");
					alert.setHeaderText("No color picked!");
					alert.setContentText("Select a color and try again.");
					alert.showAndWait();
				}

			});

			return;
		}

		// Everything is valid
		MainContext context = MainContext.getContext();
		int quizID = context.getQuiz().getQuizID();
		String captainname = context.getUser().getUsername();
		ClientCreateTeamEvent cCTE = new ClientCreateTeamEvent(quizID, teamname, color, captainname);
		publishEvent(cCTE);
	}

	@FXML
	private void handleBack() {
		EventBroker eventBroker = EventBroker.getEventBroker();
		eventBroker.removeEventListener(createTeamFailHandler);
		eventBroker.removeEventListener(createTeamSuccesHandler);
	}
	
	// Inner classes
	private class CreateTeamFailHandler implements EventListener {
		
		@Override
		public void handleEvent(Event event) {
			@SuppressWarnings("unused")
			ServerCreateTeamFailEvent sCTFE = (ServerCreateTeamFailEvent) event;
			
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Error");
					alert.setHeaderText("Team creation failed!");
					alert.setContentText("The teamname already exists.");
					alert.showAndWait();
				}
			});
		}
	}
	
	private class CreateTeamSuccesHandler implements EventListener {

		@Override
		public void handleEvent(Event event) {
			ServerCreateTeamSuccesEvent sCTSE = (ServerCreateTeamSuccesEvent) event;

			int quizID = sCTSE.getQuizID();
			int teamID = sCTSE.getTeamID();
			String teamname = sCTSE.getTeamname();
			Color color = sCTSE.getColor();
			int captainID = sCTSE.getCaptainID();
			String captainname = sCTSE.getCaptainname();
			int players = sCTSE.getPlayers();

			Team.createTeam(quizID, teamID, teamname, color, captainID, captainname, players);

			EventBroker eventBroker = EventBroker.getEventBroker();
			eventBroker.removeEventListener(createTeamFailHandler);
			eventBroker.removeEventListener(createTeamSuccesHandler);

			main.showJoinTeamScene();
		}

	}

}
