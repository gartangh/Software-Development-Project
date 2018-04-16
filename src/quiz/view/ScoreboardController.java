package quiz.view;

import eventbroker.Event;
import eventbroker.EventBroker;
import eventbroker.EventListener;
import eventbroker.EventPublisher;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import main.Context;
import network.Client;
import quiz.model.ScoreboardModel;
import quiz.model.ScoreboardTeam;
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
	
	public ScoreboardController() {
		scoreboardModel = new ScoreboardModel();
	}
	
	@FXML
	private void initialize() {

		eventHandler = new ScoreboardEventHandler();
		EventBroker.getEventBroker().addEventListener(eventHandler);
		
		QuizzerEvent askForScoreboardDataEvent = new QuizzerEvent();
		askForScoreboardDataEvent.setType("CLIENT_SCOREBOARDDATA");
		publishEvent(askForScoreboardDataEvent);
	}
	
	public class ScoreboardEventHandler implements EventListener {

		@Override
		public void handleEvent(Event e) {
			switch(e.getType()) {
				case "SERVER_SCOREBOARDDATA":
					ServerScoreboardDataEvent scoreboardData = (ServerScoreboardDataEvent) e;

					rankColumn.setCellValueFactory(new PropertyValueFactory<>("rank"));
					teamNameColumn.setCellValueFactory(new PropertyValueFactory<>("teamName"));
					scoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));
					scoreboardTable.setItems(FXCollections.observableArrayList(scoreboardData.getScoreboardTeams()));
					
					if(scoreboardData.getScoreboardTeams().size() > 0) {
						int curTeamID = Context.getContext().getTeamID();
						if(scoreboardData.getScoreboardTeams().get(0).getTeamID() == curTeamID) {
							winnerLoser.textProperty().set(scoreboardData.getScoreboardTeams().get(curTeamID).getTeamName() + ": WINNER");
						}
						else {
							winnerLoser.textProperty().set(scoreboardData.getScoreboardTeams().get(curTeamID).getTeamName() + ": LOSER");
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
