package eventbroker.clientevent;

import javafx.scene.paint.Color;

@SuppressWarnings("serial")
public class ClientCreateTeamEvent extends ClientEvent {

	public final static String EVENTTYPE = "CLIENT_NEW_TEAM";

	private int quizID;
	private String teamname;
	private int colorRed;
	private int colorGreen;
	private int colorBlue;
	private String captainname;

	// Constructor
	public ClientCreateTeamEvent(int quizID, String teamname, Color color, String captainname) {
		super.type = EVENTTYPE;
		this.quizID = quizID;
		this.teamname = teamname;
		this.colorRed = (int) (color.getRed() * 255);
		this.colorGreen = (int) (color.getGreen() * 255);
		this.colorBlue = (int) (color.getBlue() * 255);
		this.captainname = captainname;
	}

	// Getters and setters
	public int getQuizID() {
		return quizID;
	}

	public String getTeamname() {
		return teamname;
	}

	public void setTeamname(String teamname) {
		this.teamname = teamname;
	}

	public Color getColor() {
		return Color.rgb(colorRed, colorGreen, colorBlue);
	}

	public void setColor(Color color) {
		this.colorRed = (int) (color.getRed() * 255);
		this.colorGreen = (int) (color.getGreen() * 255);
		this.colorBlue = (int) (color.getBlue() * 255);
	}
	
	public String getCaptainname() {
		return captainname;
	}

}
