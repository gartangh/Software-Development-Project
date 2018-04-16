package server;

import javafx.scene.paint.Color;

public class ServerChangeTeamEvent extends ServerEvent{
	protected int quizID;
	protected int newTeamID;
	protected int oldTeamID;
	protected int userID;
	protected String userName;

	public ServerChangeTeamEvent(int quizID,int newTeamID, int oldTeamID,int userID,String userName) {
		super();
		this.quizID = quizID;
		this.newTeamID=newTeamID;
		this.oldTeamID=oldTeamID;
		this.userID=userID;
		this.userName=userName;
		this.type = "SERVER_CHANGE_TEAM";
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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}
