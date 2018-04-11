package quiz.util;

import javafx.scene.paint.Color;

public class NewTeamEvent extends UserEvent{
	protected int quizID;
	protected String teamname;
	protected Color color;

	public NewTeamEvent(int quizID,String teamname,Color color) {
		super();
		this.quizID = quizID;
		this.teamname= teamname;
		this.color=color;
		this.type = "CLIENT_NEW_TEAM";
	}

	public int getQuizID() {
		return quizID;
	}

	public String getTeamName(){
		return teamname;
	}

	public Color getColor(){
		return color;
	}

	public void setTeamName(String teamname){
		this.teamname=teamname;
	}

	public void setColor(Color color){
		this.color=color;
	}
}
