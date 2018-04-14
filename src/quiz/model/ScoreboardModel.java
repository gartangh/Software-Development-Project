package quiz.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ScoreboardModel {

	/*
	 * The data as an observable list of ScoreboardTeams
	 */
	private ObservableList<ScoreboardTeam> scoreboardTeams = FXCollections.observableArrayList();
	private StringProperty winnerLoser;
	
	public ScoreboardModel() {
		winnerLoser = new SimpleStringProperty("WINNER");
	}
	
	public ObservableList<ScoreboardTeam> getScoreboardTeams() {
		return scoreboardTeams;
	}

	public StringProperty getWinnerLoser() {
		return winnerLoser;
	}
	
	
}
