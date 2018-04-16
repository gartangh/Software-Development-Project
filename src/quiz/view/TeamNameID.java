package quiz.view;

import javafx.beans.property.StringProperty;

public class TeamNameID {
	StringProperty teamName;
	int teamID;

	public TeamNameID(StringProperty teamName,int teamID){
		this.teamName=teamName;
		this.teamID=teamID;
	}

	public StringProperty getTeamName() {
		return teamName;
	}

	public void setTeamName(StringProperty teamName) {
		this.teamName = teamName;
	}

	public int getTeamID() {
		return teamID;
	}

	public void setTeamID(int teamID) {
		this.teamID = teamID;
	}

	@Override
	public boolean equals(Object other){
		if (other instanceof TeamNameID){
			TeamNameID other2=(TeamNameID) other;
			if (other2.getTeamID()==this.teamID) return true;
		}
		return false;
	}
}
