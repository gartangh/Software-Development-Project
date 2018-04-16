package quiz.model;

import java.io.Serializable;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.scene.paint.Color;

@SuppressWarnings("serial")
public class Team implements Serializable {

	private String teamname;
	private StringProperty name;
	private int teamID;
	private int amountOfPlayers = 0;
	private int maxAmountOfPlayers; // minAmountOfTeamPlayers = 1;
	private ObservableMap<Integer, String> players = FXCollections.observableHashMap();
	// To make it serializable
	private int colorRed;
	private int colorGreen;
	private int colorBlue;
	private int roundScore = 0;
	private int quizScore = 0;

	// Constructors
	public Team(String teamname, StringProperty name, Color color, String captainname) {
		this.teamname = teamname;
		this.name = name;
		this.colorRed = (int) (color.getRed() * 255);
		this.colorGreen = (int) (color.getGreen() * 255);
		this.colorBlue = (int) (color.getBlue() * 255);
		this.players.put(amountOfPlayers, captainname);
	}

	// Getters and setters
	public String getTeamname() {
		return teamname;
	}

	public StringProperty getName() {
		return name;
	}

	public int getAmountOfPlayers() {
		return amountOfPlayers;
	}

	public int getMaxAmountOfPlayers() {
		return maxAmountOfPlayers;
	}

	public void setMaxAmountOfPlayers(int maxAmountofPlayers) {
		this.maxAmountOfPlayers = maxAmountofPlayers;
	}

	public Color getColor() {
		return Color.rgb(colorRed, colorGreen, colorBlue);
	}

	public ObservableMap<Integer, String> getPlayers() {
		return players;
	}

	public int getRoundScore() {
		return roundScore;
	}

	public int getQuizScore() {
		return quizScore;
	}

	// Adders
	public boolean addPlayer(String username) {
		if (amountOfPlayers >= maxAmountOfPlayers)
			// TODO: Go back and show error
			return false;

		players.put(++amountOfPlayers, username);

		return true;
	}

	public void setName(String teamname) {
		this.name.setValue(teamname);
	}

	public void setColor(Color color) {
		this.colorRed = ((int) color.getRed() * 255);
		this.colorGreen = ((int) color.getGreen() * 255);
		this.colorBlue = ((int) color.getBlue() * 255);
	}

	// Removers
	public void removePlayer(int playerID) {
		if (players.remove(playerID) != null)
			amountOfPlayers--;
	}
	
	
	// Test
	public void setQuizScore(int quizScore) {
		this.quizScore = quizScore;
	}

	public int getTeamID() {
		return teamID;
	}

	public void setTeamID(int teamID) {
		this.teamID = teamID;
	}

}
