package quiz.view;

import java.util.ArrayList;

import eventbroker.Event;
import eventbroker.EventBroker;
import eventbroker.EventListener;
import eventbroker.EventPublisher;
import eventbroker.clientevent.ClientScoreboardDataEvent;
import eventbroker.serverevent.ServerScoreboardDataEvent;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import main.Context;
import main.Main;
import quiz.model.ScoreboardModel;
import quiz.model.ScoreboardTeam;

public class ScoreboardController extends EventPublisher {

	@FXML
	private TableView<ScoreboardTeam> scoreboardTable;
	@FXML
	private TableColumn<ScoreboardTeam, Integer> rankColumn;
	@FXML
	private TableColumn<ScoreboardTeam, String> teamNameColumn;
	@FXML
	private TableColumn<ScoreboardTeam, Integer> scoreColumn;
	@FXML
	private Label winnerLoser;

	private ScoreboardModel scoreboardModel = new ScoreboardModel();
	private ScoreboardDataHandler scoreboardDataHandler;

	// Reference to the main application
	private Main main;

	public void setMainApp(Main main) {
		this.main = main;

		scoreboardTable.setItems(scoreboardModel.getScoreboardTeams());
	}

	// Getters
	public ScoreboardModel getScoreboardModel() {
		return scoreboardModel;
	}

	// Methods
	@FXML
	private void initialize() {
		scoreboardDataHandler = new ScoreboardDataHandler();

		EventBroker.getEventBroker().addEventListener(ServerScoreboardDataEvent.EVENTTYPE, scoreboardDataHandler);

		winnerLoser.textProperty().bind(scoreboardModel.getWinnerLoserProperty());
		rankColumn
				.setCellValueFactory(cellData -> (new SimpleIntegerProperty(cellData.getValue().getRank()).asObject()));
		teamNameColumn.setCellValueFactory(cellData -> (new SimpleStringProperty(cellData.getValue().getTeamname())));
		scoreColumn.setCellValueFactory(
				cellData -> (new SimpleIntegerProperty(cellData.getValue().getScore()).asObject()));

		ClientScoreboardDataEvent cSDE = new ClientScoreboardDataEvent();
		publishEvent(cSDE);
	}

	/**
	 * Called when the user clicks on the rematch button.
	 */
	@FXML
	private void handleRematchButton() {
		// TO DO: Clear quiz, go back to quizroom
		EventBroker.getEventBroker().removeEventListener(scoreboardDataHandler);

		main.showJoinTeamScene();
	}

	/**
	 * Called when the user clicks on the quit button.
	 */
	@FXML
	private void handleQuitButton() {
		// TODO: Clear quiz, show List of Available quizzes
		EventBroker.getEventBroker().removeEventListener(scoreboardDataHandler);
		main.showJoinQuizScene();
	}

	// Inner class
	public class ScoreboardDataHandler implements EventListener {

		@Override
		public void handleEvent(Event event) {
			ServerScoreboardDataEvent sSDE = (ServerScoreboardDataEvent) event;

			ArrayList<ScoreboardTeam> scoreboardTeams = sSDE.getScoreboardTeams();

			scoreboardModel.addScoreboardTeams(scoreboardTeams);

			Context context = Context.getContext();
			if (sSDE.getScoreboardTeams().size() > 0) {
				int curTeamID = context.getTeamID();
				ScoreboardTeam curTeam = null;
				for (ScoreboardTeam team : scoreboardTeams) {
					if (team.getTeamID() == curTeamID) {
						curTeam = team;
						break;
					}
				}

				if (context.getQuiz().getHostID() == context.getUser().getUserID())
					scoreboardModel.updateWinnerLoser("HOST");
				else if (curTeam != null) {
					if (scoreboardTeams.get(0).getTeamID() == curTeamID)
						scoreboardModel.updateWinnerLoser("WINNER WINNER CHICKEN DINNER");
					else
						scoreboardModel.updateWinnerLoser(curTeam.getTeamname() + ": LOSER");
				}
			}
		}

	}

}
