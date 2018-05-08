package quiz.model;

import javafx.beans.property.StringProperty;

public class TeamNameID {

	StringProperty teamname;
	int teamID;
	StringProperty answer;

	// Constructor
	public TeamNameID(StringProperty teamname, int teamID) {
		this.teamname = teamname;
		this.teamID = teamID;
	}

	// Getters and setters
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
