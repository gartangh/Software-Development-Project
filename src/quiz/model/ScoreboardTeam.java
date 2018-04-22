package quiz.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ScoreboardTeam implements Serializable {

	private final int rank;

	// You can also send the entire teamClass to all users so you can view all
	// teamMembers in scoreboard
	private final String teamName;
	private final int teamID;

	private final int score;

	public ScoreboardTeam(int rank, String teamName, int teamID, int score) {
		this.rank = rank;
		this.teamName = teamName;
		this.teamID = teamID;
		this.score = score;
	}

	public int getRank() {
		return rank;
	}

	public String getTeamName() {
		return teamName;
	}

	public int getTeamID() {
		return teamID;
	}

	public int getScore() {
		return score;
	}

}
