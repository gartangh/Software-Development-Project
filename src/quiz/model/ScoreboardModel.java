package quiz.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ScoreboardModel {

	/*
	 * The data as an observable list of ScoreboardTeams
	 */
	private ObservableList<ScoreboardTeam> scoreboardTeams = FXCollections.observableArrayList();
	
	public ObservableList<ScoreboardTeam> getScoreboardTeams() {
		return scoreboardTeams;
	}
	
	
}
