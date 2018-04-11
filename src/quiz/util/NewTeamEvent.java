package quiz.util;

import javafx.scene.paint.Color;

public class NewTeamEvent extends UserEvent{
	protected int quizID;
	protected String teamname;
	protected int colorRed;
	protected int colorGreen;
	protected int colorBlue;

	public NewTeamEvent(int quizID,String teamname,Color color) {
		super();
		this.quizID = quizID;
		this.teamname= teamname;
		this.colorRed = (int) (color.getRed()*255);
		this.colorGreen = (int) (color.getGreen()*255);
		this.colorBlue = (int) (color.getBlue()*255);
		this.type = "CLIENT_NEW_TEAM";
	}

	public int getQuizID() {
		return quizID;
	}

	public String getTeamName(){
		return teamname;
	}

	public Color getColor(){
		return Color.rgb(colorRed,colorGreen,colorBlue);
	}

	public int getColorRed(){
		return colorRed;
	}

	public int getColorBlue(){
		return colorBlue;
	}

	public int getColorGreen(){
		return colorGreen;
	}


	public void setTeamName(String teamname){
		this.teamname=teamname;
	}

	public void setColor(Color color){
		this.colorRed = (int) (color.getRed()*255);
		this.colorGreen = (int) (color.getGreen()*255);
		this.colorBlue = (int) (color.getBlue()*255);
	}
}
