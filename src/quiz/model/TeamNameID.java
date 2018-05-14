package quiz.model;

import javafx.beans.property.StringProperty;

public class TeamNameID {

	StringProperty teamname;
	StringProperty captainName;

	int teamID;
	StringProperty answer;

	// Constructor
	public TeamNameID(StringProperty teamname, int teamID,StringProperty captainName) {
		this.teamname = teamname;
		this.teamID = teamID;
		this.captainName = captainName;
	}

	// Getters and setters
	public StringProperty getCaptainName() {
		return captainName;
	}

	public StringProperty getTeamname() {
		return teamname;
	}

	public void setTeamname(StringProperty teamName) {
		this.teamname = teamName;
	}

	public int getTeamID() {
		return teamID;
	}

	public void setTeamID(int teamID) {
		this.teamID = teamID;
	}

	public StringProperty getAnswer() {
		return answer;
	}

	public void setAnswer(StringProperty answer) {
		this.answer = answer;
	}

	// Method
	@Override
	public boolean equals(Object other) {
		if (other instanceof TeamNameID) {
			TeamNameID other2 = (TeamNameID) other;
			if (other2.getTeamID() == this.teamID)
				return true;
		}

		return false;
	}
}
