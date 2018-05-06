package quiz.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ScoreboardTeam implements Serializable {

	private final int teamID;
	private final String teamname;
	private final int rank;
	private final int score;

	// Constructor
	public ScoreboardTeam(int teamID, String teamname, int rank, int score) {
		this.teamID = teamID;
		this.teamname = teamname;
		this.rank = rank;
		this.score = score;
	}

	// Getters
	public int getTeamID() {
		return teamID;
	}

	public String getTeamname() {
		return teamname;
	}

	public int getRank() {
		return rank;
	}

	public int getScore() {
		return score;
	}

}
