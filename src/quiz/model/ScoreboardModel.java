package quiz.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ScoreboardModel {

	/*
	 * The data as an observable list of ScoreboardTeams
	 */
	private ObservableList<ScoreboardTeam> scoreboardTeams;
	private StringProperty winnerLoserProperty;
	
	public ScoreboardModel() {
		 scoreboardTeams = FXCollections.observableArrayList();
		 winnerLoserProperty = new SimpleStringProperty("winnerLoser");
	}
	
	public ObservableList<ScoreboardTeam> getScoreboardTeams() {
		return scoreboardTeams;
	}

	public StringProperty getWinnerLoserProperty() {
		return winnerLoserProperty;
	}
	
	public void updateWinnerLoser(String winnerLoser) {
		this.winnerLoserProperty.setValue(winnerLoser);
	}
	
	
}
