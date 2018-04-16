package quiz.util;

public class ChangeTeamEvent extends UserEvent {
	protected int quizID;
	protected int newTeamID;
	protected int oldTeamID;
	protected int userID;

	public ChangeTeamEvent(int quizID,int newTeamID, int oldTeamID,int userID) {
		super();
		this.quizID = quizID;
		this.newTeamID=newTeamID;
		this.oldTeamID=oldTeamID;
		this.userID=userID;
		this.type = "CLIENT_CHANGE_TEAM";
	}

	public int getQuizID() {
		return quizID;
	}

	public void setQuizID(int quizID) {
		this.quizID = quizID;
	}

	public int getNewTeamID() {
		return newTeamID;
	}

	public void setNewTeamID(int newTeamID) {
		this.newTeamID = newTeamID;
	}

	public int getOldTeamID() {
		return oldTeamID;
	}

	public void setOldTeamID(int oldTeamID) {
		this.oldTeamID = oldTeamID;
	}

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}


}
