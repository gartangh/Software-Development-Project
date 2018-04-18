package quiz.view;

import eventbroker.Event;
import eventbroker.EventBroker;
import eventbroker.EventListener;
import eventbroker.EventPublisher;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import main.Context;
import main.Main;
import quiz.model.ScoreboardModel;
import quiz.model.ScoreboardTeam;
import quiz.util.ClientScoreboardDataEvent;
import quiz.util.QuizzerEvent;
import quiz.util.UserEvent;
import server.ServerScoreboardDataEvent;

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

	private ScoreboardModel scoreboardModel;
	private ScoreboardEventHandler eventHandler;

	// Reference to the main application
	private Main main;

	public void setMainApp(Main main) {
		this.main = main;
		scoreboardTable.setItems(scoreboardModel.getScoreboardTeams());
	}

	// Constructor
	public ScoreboardController() {
		this.scoreboardModel = new ScoreboardModel();
	}

	@FXML
	private void initialize() {
		eventHandler = new ScoreboardEventHandler();
		EventBroker.getEventBroker().addEventListener(eventHandler);

		winnerLoser.textProperty().bind(scoreboardModel.getWinnerLoserProperty());
		rankColumn.setCellValueFactory(cellData -> (new SimpleIntegerProperty(cellData.getValue().getRank()).asObject()));
        teamNameColumn.setCellValueFactory(cellData -> (new SimpleStringProperty(cellData.getValue().getTeamName())));
        scoreColumn.setCellValueFactory(cellData -> (new SimpleIntegerProperty(cellData.getValue().getScore()).asObject()));
        
		ClientScoreboardDataEvent cSDE = new ClientScoreboardDataEvent();
		publishEvent(cSDE);
	}

	public class ScoreboardEventHandler implements EventListener {

		@Override
		public void handleEvent(Event e) {
			switch (e.getType()) {
			case "SERVER_SCOREBOARDDATA":
				ServerScoreboardDataEvent scoreboardData = (ServerScoreboardDataEvent) e;

				scoreboardModel.addScoreboardTeams(scoreboardData.getScoreboardTeams());
				
				if (scoreboardData.getScoreboardTeams().size() > 0) {
					int curTeamID = Context.getContext().getTeamID();
					ScoreboardTeam curTeam = null;
					for(ScoreboardTeam team : scoreboardData.getScoreboardTeams()) {
						if(team.getTeamID() == curTeamID) {
							curTeam = team;
							break;
						}
					}
					if(curTeam != null) {
						if (scoreboardData.getScoreboardTeams().get(0).getTeamID() == curTeamID)
							scoreboardModel.updateWinnerLoser(curTeam.getTeamName() + ": WINNER");
						else if(Context.getContext().getQuiz().getQuizmaster() == Context.getContext().getUser().getUserID())
							scoreboardModel.updateWinnerLoser("HOST");
						else {
							scoreboardModel.updateWinnerLoser(curTeam.getTeamName() + ": LOSER");
							
						}
					}
				}

				break;
			}
		}

	}

	/**
	 * Called when the user clicks on the rematch button.
	 */
	@FXML
	private void handleRematchButton() {
		// TO DO: Clear quiz, go back to quizroom
		System.out.println("Going back to quizroom");
	}

	/**
	 * Called when the user clicks on the quit button.
	 */
	@FXML
	private void handleQuitButton() {
		// TO DO: Clear quiz, show List of Available quizzes
		System.out.println("Quitting quiz");
	}

	public ScoreboardModel getScoreboardModel() {
		return scoreboardModel;
	}

}
