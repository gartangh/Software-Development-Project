package quiz.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ScoreboardTeam {

	private final IntegerProperty rank;
	
	// You can also send the entire teamClass to all users so you can view all teamMembers in scoreboard
	private final StringProperty teamName;
	private final IntegerProperty teamID;
	
	private final IntegerProperty score;

	public ScoreboardTeam(int rank, String teamName, int teamID, int score) {
		this.rank = new SimpleIntegerProperty(rank);
		this.teamName = new SimpleStringProperty(teamName);
		this.teamID = new SimpleIntegerProperty(teamID);
		this.score = new SimpleIntegerProperty(score);
	}
	
	public int getRank() {
		return rank.get();
	}

	public String getTeamName() {
		return teamName.get();
	}

	public int getTeamID() {
		return teamID.get();
	}

	public int getScore() {
		return score.get();
	}

}
