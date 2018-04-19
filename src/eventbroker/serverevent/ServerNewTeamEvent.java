package eventbroker.serverevent;

import javafx.scene.paint.Color;

@SuppressWarnings("serial")
public class ServerNewTeamEvent extends ServerEvent {

	public final static String EVENTTYPE = "SERVER_NEW_TEAM";

	private int quizID;
	private int teamID;
	private String teamName;
	private int colorRed;
	private int colorGreen;
	private int colorBlue;
	private int captainID;
	private String captainName;

	// Constructor
	public ServerNewTeamEvent(int quizID, int teamID, String teamname, Color color, int captainID, String captainName) {
		super.type = EVENTTYPE;
		this.quizID = quizID;
		this.teamID = teamID;
		this.teamName = teamname;
		this.colorRed = (int) (color.getRed() * 255);
		this.colorGreen = (int) (color.getGreen() * 255);
		this.colorBlue = (int) (color.getBlue() * 255);
		this.captainID = captainID;
		this.captainName = captainName;
	}

	// Getters
	public int getQuizID() {
		return quizID;
	}

	public Color getColor() {
		return Color.rgb(colorRed, colorGreen, colorBlue);
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
