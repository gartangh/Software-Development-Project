package eventbroker.serverevent;

import javafx.scene.paint.Color;

@SuppressWarnings("serial")
public class ServerCreateTeamSuccesEvent extends ServerEvent {

	public final static String EVENTTYPE = "SERVER_CREATE_TEAM_SUCCES";

	private int quizID;
	private int teamID;
	private String teamname;
	// To make color serializable
	private int colorRed;
	private int colorGreen;
	private int colorBlue;
	private int captainID;
	private String captainname;
	private int players;

	// Constructor
	public ServerCreateTeamSuccesEvent(int quizID, int teamID, String teamname, Color color, int captainID,
			String captainname, int players) {
		super.type = EVENTTYPE;
		this.quizID = quizID;
		this.teamID = teamID;
		this.teamname = teamname;
		this.colorRed = (int) (color.getRed() * 255);
		this.colorGreen = (int) (color.getGreen() * 255);
		this.colorBlue = (int) (color.getBlue() * 255);
		this.captainID = captainID;
		this.captainname = captainname;
		this.players = players;
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

	public String getTeamname() {
		return teamname;
	}

	public int getCaptainID() {
		return captainID;
	}

	public String getCaptainname() {
		return captainname;
	}

	public int getPlayers() {
		return players;
	}

}
