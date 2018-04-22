package eventbroker.clientevent;

import javafx.scene.paint.Color;

@SuppressWarnings("serial")
public class ClientNewTeamEvent extends ClientEvent {

	public final static String EVENTTYPE = "CLIENT_NEW_TEAM";

	private int quizID;
	private String teamname;
	private int colorRed;
	private int colorGreen;
	private int colorBlue;

	// Constructor
	public ClientNewTeamEvent(int quizID, String teamname, Color color) {
		super.type = EVENTTYPE;
		this.quizID = quizID;
		this.teamname = teamname;
		this.colorRed = (int) (color.getRed() * 255);
		this.colorGreen = (int) (color.getGreen() * 255);
		this.colorBlue = (int) (color.getBlue() * 255);
	}

	// Getters and setters
	public int getQuizID() {
		return quizID;
	}

	public String getTeamname() {
		return teamname;
	}

	public void setTeamName(String teamname) {
		this.teamname = teamname;
	}

	public Color getColor() {
		return Color.rgb(colorRed, colorGreen, colorBlue);
	}

	public int getColorRed() {
		return colorRed;
	}

	public int getColorBlue() {
		return colorBlue;
	}

	public int getColorGreen() {
		return colorGreen;
	}

	public void setColor(Color color) {
		this.colorRed = (int) (color.getRed() * 255);
		this.colorGreen = (int) (color.getGreen() * 255);
		this.colorBlue = (int) (color.getBlue() * 255);
	}

}
