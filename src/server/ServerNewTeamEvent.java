package server;

import javafx.scene.paint.Color;
import quiz.model.Team;

public class ServerNewTeamEvent extends ServerEvent{
	protected int quizID;
	protected int teamID;
	protected String teamName;
	protected int colorRed;
	protected int colorGreen;
	protected int colorBlue;
	protected int captainID;
	protected String captainName;

	public ServerNewTeamEvent(int quizID,int teamID,String teamname,Color color,int captainID,String captainName) {
		super();
		this.quizID=quizID;
		this.teamID=teamID;
		this.teamName=teamname;
		this.colorRed = (int) (color.getRed()*255);
		this.colorGreen = (int) (color.getGreen()*255);
		this.colorBlue = (int) (color.getBlue()*255);
		this.captainID=captainID;
		this.captainName=captainName;
		this.type = "SERVER_NEW_TEAM";
	}

	public int getQuizID(){
		return quizID;
	}

	public Color getColor(){
		return Color.rgb(colorRed,colorGreen,colorBlue);
	}

	public int getTeamID() {
		return teamID;
	}

	public void setTeamID(int teamID) {
		this.teamID = teamID;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public int getCaptainID() {
		return captainID;
	}

	public void setCaptainID(int captainID) {
		this.captainID = captainID;
	}

	public String getCaptainName() {
		return captainName;
	}

	public void setCaptainName(String captainName) {
		this.captainName = captainName;
	}

	public void setQuizID(int quizID) {
		this.quizID = quizID;
	}


}
