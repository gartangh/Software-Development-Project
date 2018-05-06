package quiz.model;

import java.util.ArrayList;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ScoreboardModel {

	private ObservableList<ScoreboardTeam> scoreboardTeams = FXCollections.observableArrayList();
	private StringProperty winnerLoserProperty;

	// Constructor
	public ScoreboardModel() {
		 winnerLoserProperty = new SimpleStringProperty("winnerLoser");
	}

	// Getters
	public ObservableList<ScoreboardTeam> getScoreboardTeams() {
		return scoreboardTeams;
	}

	public StringProperty getWinnerLoserProperty() {
		return winnerLoserProperty;
	}

	public void updateWinnerLoser(String winnerLoser) {
		Platform.runLater(new Runnable() {
			public void run() {
				winnerLoserProperty.setValue(winnerLoser);
			}
		});
	}

	public void addScoreboardTeam(ScoreboardTeam scoreboardTeam) {
		Platform.runLater(new Runnable() {
			public void run() {
				scoreboardTeams.add(scoreboardTeam);
			}
		});
	}

	public void addScoreboardTeams(ArrayList<ScoreboardTeam> scoreboardTeamList) {
		for (ScoreboardTeam scoreboardTeam : scoreboardTeamList)
			addScoreboardTeam(scoreboardTeam);
	}

}
